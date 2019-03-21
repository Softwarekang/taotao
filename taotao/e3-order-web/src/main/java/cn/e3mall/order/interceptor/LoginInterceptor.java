package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.CookieUtils;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/*
 *  用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 判断用户是否登录
		// 从Cookies中取token.
		String token = CookieUtils.getCookieValue(request, "token");
		if (StringUtils.isBlank(token)) {
			// 如果不存在,调用soo登录服务取登录页面
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		// 如果存在
		E3Result result = tokenService.getUserByToken(token);
		if (200 != result.getStatus()) {
			// 登录过期,调用soo登录服务取登录页面
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		// 如果没有过期
		TbUser user = (TbUser)result.getData();
		request.setAttribute("user", user);
		// 判断cookie中是否有cartlist
		String cartList = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(cartList)){
			// 合并购物车
			cartService.mergeCartList(user.getId(), JsonUtils.jsonToList(cartList, TbItem.class));
		}
		return true;
	}

}
