package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/*
 * 用户注册处理
 */
@Service
public class RegisterServiceImpl implements RegisterService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Override
	public E3Result checkData(String parm, int type) {
		// 根据不同type生成不同的查询条件
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		// 1:用户名 2:手机 3:邮箱
		if(1 == type){
			criteria.andUsernameEqualTo(parm);
		} else if(2 == type){
			criteria.andPhoneEqualTo(parm);
		} else if(3 == type){
			criteria.andEmailEqualTo(parm);
		} else{
			return E3Result.build(400, "数据类型错误");
		}
		// 执行查询
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(null != list && list.size() > 0){
			return E3Result.ok(false);
		} else{
			return E3Result.ok(true);
		}
	}
	
	@Override
	public E3Result register(TbUser user) {
		// 数据有效性校验
		// 1、使用TbUser接收提交的请求。
		if (StringUtils.isBlank(user.getUsername())) {
			return E3Result.build(400, "用户名不能为空");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			return E3Result.build(400, "密码不能为空");
		}
		//校验数据是否可用
		E3Result result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "此用户名已经被使用");
		}
		//校验电话是否可以
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此手机号已经被使用");
			}
		}
		//校验email是否可用
		if (StringUtils.isNotBlank(user.getEmail())) {
			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此邮件地址已经被使用");
			}
		}
		// 补全属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 对密码进行md5加密
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		// 用户数据插入
		int selective = tbUserMapper.insertSelective(user);
		// 返回添加信息
		if(selective > 0){
			return E3Result.ok();
		} else {
			return E3Result.build(500, "服务器处理错误", null);
		}
	}

}
