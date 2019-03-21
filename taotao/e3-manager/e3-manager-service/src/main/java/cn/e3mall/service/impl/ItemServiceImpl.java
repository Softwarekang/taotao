package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.untils.IDUtils;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * <p>
 * Title: ItemServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.cn
 * </p>
 * 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private JedisClient jedisClient;
	@Resource
	// 默认根据id查找，没有则查找类型
	private Destination topicDestination;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CAHE_TIME}")
	private Integer ITEM_CAHE_TIME;

	@Override
	public TbItem getItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据主键查询
		// TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andIdEqualTo(itemId);
		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 添加缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CAHE_TIME);
				return list.get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(tbItemExample);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		Long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(final TbItem tbItem, String desc) {
		// 生成商品id
		System.out.println(desc);
		long itemId = IDUtils.genItemId();
		// 补全属性
		tbItem.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		Date date = new Date();
		tbItem.setCreated(date);
		tbItem.setUpdated(date);
		// 插入商品信息
		itemMapper.insert(tbItem);
		// 创建商品描述对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		// 插入商品描述信息
		tbItemDescMapper.insert(tbItemDesc);
		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(tbItem.getId().toString());
			}
		});
		return E3Result.ok();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.e3mall.service.ItemService#deleteItem(java.util.List) 删除商品
	 */
	@Override
	public E3Result deleteItem(List<Long> ids) {
		// 利用id删除每一个商品
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		for (Long id : ids) {
			// 设置条件
			criteria.andIdEqualTo(id);
			// 删除商品
			itemMapper.deleteByExample(tbItemExample);
		}
		return E3Result.ok();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.e3mall.service.ItemService#soldOutItem(java.util.List) 商品下架
	 */
	@Override
	public E3Result soldOutItem(List<Long> ids) {
		// 根据商品的id将商品的status=3
		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 2);
		for (Long id : ids) {
			// 设置查询条件
			TbItemExample tbItemExample = new TbItemExample();
			Criteria criteria = tbItemExample.createCriteria();
			criteria.andIdEqualTo(id);
			// 进行更信param:实体信息，若属性为null则不更新,param2:查询条件
			itemMapper.updateByExampleSelective(tbItem, tbItemExample);
		}
		return E3Result.ok();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.e3mall.service.ItemService#putAway(java.util.List) 上架商品
	 */
	@Override
	public E3Result putAway(List<Long> ids) {
		// 根据id为条件将商品的status设置为1
		TbItem tbItem = new TbItem();
		tbItem.setStatus((byte) 1);
		for (Long id : ids) {
			// 设置查询条件
			TbItemExample tbItemExample = new TbItemExample();
			Criteria criteria = tbItemExample.createCriteria();
			criteria.andIdEqualTo(id);
			// 进行数据的更新
			itemMapper.updateByExampleSelective(tbItem, tbItemExample);
		}
		return E3Result.ok();
	}

	/*
	 * 获取商品详情信息 (non-Javadoc)
	 * 
	 * @see cn.e3mall.service.ItemService#geTbItemDescById(java.lang.Long)
	 */
	@Override
	public TbItemDesc geTbItemDescById(Long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		// 添加缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CAHE_TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
