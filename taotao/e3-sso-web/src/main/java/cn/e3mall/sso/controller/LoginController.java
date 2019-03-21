package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.CookieUtils;
import cn.e3mall.sso.service.LoginService;

/*
 * 	用户登录处理
 */
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@RequestMapping("/page/login")
	public String showlogin(String redirect, Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){
		E3Result result = loginService.userLogin(username, password);
		// 判断是否登录成功
		if(200 == result.getStatus()){
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		return result;
	}
}
