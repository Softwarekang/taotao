package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.CookieUtils;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/*
 *  购物车处理Controller
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${COOKIES_NAME}")
	private String COOKIES_NAME;
	@Value("${COOKIES_TIME}")
	private Integer COOKIES_TIME;
	@Autowired
	private CartService cartService;
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request, HttpServletResponse response){
		// 判断用户是否登录
		TbUser user = (TbUser)request.getAttribute("user");
		if(null != user){
			// 保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		// 从Cookie中取购物车列表
		List<TbItem> cartList = GetCartListFromCookies(request);
		// 如果存在数量相加
		Boolean find = false;
		for(TbItem tbItem:cartList){
			if(tbItem.getId() == itemId.longValue()){
				find = true;
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}
		// 如果不存在，根据商品id查询商品信息
		if(!find){
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			String image = item.getImage();
			if(!StringUtils.isBlank(image)){
				item.setImage(image.split(",")[0]);
			}
			// 把商品添加到商品列表，写入cookie，返回添加成功页面
			cartList.add(item);
		}
		CookieUtils.setCookie(request, response, COOKIES_NAME, JsonUtils.objectToJson(cartList), COOKIES_TIME ,  true);
		return "cartSuccess";
	}
	
	/*
	 *  从Cookies中取商品列表
	 */
	private List<TbItem> GetCartListFromCookies(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, COOKIES_NAME , true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<>();
		}
		// 不为空json->list<tbItem>
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/*
	 * 返回购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, HttpServletResponse response){
		// 从cookie取cartList
		List<TbItem> cartList = GetCartListFromCookies(request); 
		// 判断用户是否为登录状态
		TbUser user = (TbUser)request.getAttribute("user");
		// 如果登录从服务端获得购物车列表和cookies中的够物车合并,删除cookies的购物车
		if(null != user){
			cartService.mergeCartList(user.getId(), cartList);
			CookieUtils.deleteCookie(request, response, COOKIES_NAME);
			cartList = cartService.getCartList(user.getId());
		}
		// 未登录
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	/*
	 *  更新购物车数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response){
		TbUser user = (TbUser)request.getAttribute("user");
		if(null != user){
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		List<TbItem> cartList = GetCartListFromCookies(request);
		for(TbItem item:cartList){
			if(item.getId() == itemId.longValue()){
				item.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, COOKIES_NAME, JsonUtils.objectToJson(cartList), COOKIES_TIME ,  true);
		return E3Result.ok();
	}
	
	/*
	 *  删除购物车商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response){
		TbUser user = (TbUser)request.getAttribute("user");
		if(null != user){
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// 从Cookies中获得CartList
		List<TbItem> cartList = GetCartListFromCookies(request);
		for(TbItem item:cartList){
			if(item.getId() == itemId.longValue()){
				cartList.remove(item);
			}
			break;
		}
		CookieUtils.setCookie(request, response, COOKIES_NAME, JsonUtils.objectToJson(cartList), COOKIES_TIME ,  true);
		return "redirect:/cart/cart.html";
	}
}
