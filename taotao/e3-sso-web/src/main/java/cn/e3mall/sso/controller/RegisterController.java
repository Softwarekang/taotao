package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

/*
 * 用户注册Controller
 */
@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{parm}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String parm, @PathVariable Integer type){
		return registerService.checkData(parm, type);
	}
	
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result userRegister(TbUser user){
		return registerService.register(user);
	}
}
