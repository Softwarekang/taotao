package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.untils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		// 根据Token到redis取用户信息
		String json = jedisClient.get("SESSION:"+token);
		// 没有信息，返回登录过期
		if(StringUtils.isBlank(json)){
			return E3Result.build(201, "用户登录过期");
		}
		// 获得用户信息更新token信息
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		// 返回E3result含tbuser对象
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}
