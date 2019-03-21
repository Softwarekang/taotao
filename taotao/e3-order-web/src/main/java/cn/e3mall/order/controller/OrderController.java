package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

/*
 *  购物车管理Controller
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		TbUser user = (TbUser)request.getAttribute("user");
		List<TbItem> cartList = cartService.getCartList(user.getId());
		request.setAttribute("cartList", cartList);
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo info, HttpServletRequest request){
		// 取用户信息
		TbUser user = (TbUser)request.getAttribute("user");
		// 把用户信息写入订单信息中
		info.setUserId(user.getId());
		info.setBuyerNick(user.getUsername());
		// 调用服务生成订单
		E3Result e3Result = orderService.createOrder(info);
		if(200 == e3Result.getStatus()){
			// 清空购物车
			cartService.deleteAllCartList(user.getId());
		}
		request.setAttribute("orderId", e3Result.getData());
		request.setAttribute("payment", info.getPayment());
		return "success";
	}
}
