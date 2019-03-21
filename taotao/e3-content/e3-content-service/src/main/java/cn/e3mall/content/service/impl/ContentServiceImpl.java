package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/*
 * 内容管理
 */
@Service
public class ContentServiceImpl implements ContentService{
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	/*
	 * 添加商品内容
	 * (non-Javadoc)
	 * @see cn.e3mall.content.service.ContentService#addContent(cn.e3mall.pojo.TbContent)
	 */
	@Override
	public E3Result addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insertSelective(tbContent);
		// 同步缓存
		jedisClient.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	/*
	 * 返回商品内容列表
	 * (non-Javadoc)
	 * @see cn.e3mall.content.service.ContentService#getListContentByCid(java.lang.Long)
	 */
	@Override
	public List<TbContent> getListContentByCid(Long cid) {
		// 在redis缓存中查询
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			if(StringUtils.isNoneBlank(json)){
				return JsonUtils.jsonToList(json, TbContent.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TbContentExample tbContentExample = new TbContentExample();
		// 设置查询条件
		Criteria criteria = tbContentExample.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
		// 如果redis中没有则存入redis
		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	
}
