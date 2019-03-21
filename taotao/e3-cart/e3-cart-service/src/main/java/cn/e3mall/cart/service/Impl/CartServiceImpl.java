package cn.e3mall.cart.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
/*
 *  购物车处理服务
 */
@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper tbItemMapper; 
	@Override
	public E3Result addCart(Long userId, Long itemId, int num) {
		// redis中添加购物车hash key:用户id filed:商品id  value:商品信息
		// 判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+userId, itemId + "");
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE+userId, itemId + "");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum() + num);
			jedisClient.hset(REDIS_CART_PRE+userId, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		// 如果不存在
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		// 设置购物车数量
		item.setNum(num);
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			item.setImage(image.split(",")[0]);
		}
		jedisClient.hset(REDIS_CART_PRE+userId, itemId + "", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}
	
	/*
	 * 合并购物车
	 * (non-Javadoc)
	 * @see cn.e3mall.cart.service.CartService#mergeCartList(java.lang.Long, java.util.List)
	 */
	@Override
	public E3Result mergeCartList(Long userId, List<TbItem> itemList) {
		// 遍历商品列表,把列表添加进购物车
		// 如果有数量相加，如果没有则添加商品
		for(TbItem item:itemList){
			addCart(userId, item.getId(), item.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		// 根据id获得所有值
		List<String> listJson = jedisClient.hvals(REDIS_CART_PRE+userId);
		List<TbItem> list = new ArrayList<>();
		for(String item:listJson){
			TbItem tbItem = JsonUtils.jsonToPojo(item, TbItem.class);
			list.add(tbItem);
		}
		return list;
	}

	@Override
	public E3Result updateCartNum(Long userId, Long itemId, int num) {
		String json = jedisClient.hget(REDIS_CART_PRE + userId, itemId+"");
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(Long userId, Long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + userId, itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result deleteAllCartList(Long userId) {
		jedisClient.del(REDIS_CART_PRE + userId);
		return E3Result.ok();
	}

}
