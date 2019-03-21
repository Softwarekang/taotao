package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public E3Result userLogin(String userName, String userPassword) {
		// 业务分析
		// 判断用户名密码是否正确
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(userName);
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(null == list && list.size() == 0){
			// 返回登录错误
			return E3Result.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(userPassword.getBytes()).equals(user.getPassword())){
			// 返回密码错误
			return E3Result.build(400, "密码错误");
		}
		// 生成token
		String token = UUID.randomUUID().toString();
		// 把用户信息写入redis key:token value=用户信息
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		// 设置Session过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		// 表现层:处理cokie 返回token
		return E3Result.ok(token);
	}

}
