package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {
	E3Result addCart(Long userId, Long itemId, int num);
	E3Result mergeCartList(Long userId, List<TbItem> itemList);
	List<TbItem> getCartList(Long userId);
	E3Result updateCartNum(Long userId, Long itemId, int num);
	E3Result deleteCartItem(Long userId, Long itemId);
	E3Result deleteAllCartList(Long userId);
}
