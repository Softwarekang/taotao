package cn.e3mall.cart.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.CookieUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 完成处理，返回modelAndvalue之后
		// 可以处理异常
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// handler执行之后，返回ModelandValue之前
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 前处理，执行handler之前执行此方法
		// 返回true,放行 false:拦截
		// 从cookie取到token 
		String token = CookieUtils.getCookieValue(request, "token");
		// 没有token直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		// 获得进行调用SSO系统的服务
		E3Result e3Result = tokenService.getUserByToken(token);
		// 没有油用户信息，登录过期，直接放行
		if(e3Result.getStatus() != 200){
			return true;
		}
		// 获得用户信息，登录状态
		TbUser user = (TbUser)e3Result.getData();
		// 把用户信息放到request中，只需要在Controller判断request信息
		request.setAttribute("user", user);
		return true;
	}
	
}
