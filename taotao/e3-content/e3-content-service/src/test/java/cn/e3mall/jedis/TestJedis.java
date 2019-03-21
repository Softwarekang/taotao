package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	// 单机操作
	@Test
	public void testJedis() throws Exception {
		// 创建jedis对象，参数:host.port
		Jedis jedis = new Jedis("112.74.166.230",6379);
		// 直接使用jedis操作redis。所有jedis的命令都对应一个方法
		jedis.set("test", "value");
		System.out.println(jedis.get("test"));
		// 关闭连接
		jedis.close();
	}
	// 单机操作
	@Test
	public void testJedisPool() throws Exception{
		// 创建一个连接池对象
		JedisPool jedisPool = new JedisPool("112.74.166.230", 6379);
		// 从连接池获得连接
		Jedis jedis = jedisPool.getResource();
		// 使用jedis操作redis
		System.out.println(jedis.get("test"));
		jedis.close();
		jedisPool.close();
	}
	
	// 进行集群的操作
	@Test
	public void testJedisCluster() throws Exception{
		//  创建JedisCluster对象，有一个参数nodes是set类型，set包含若干addeess和port
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("112.74.166.230", 7001));
		nodes.add(new HostAndPort("112.74.166.230", 7002));
		nodes.add(new HostAndPort("112.74.166.230", 7003));
		nodes.add(new HostAndPort("112.74.166.230", 7004));
		nodes.add(new HostAndPort("112.74.166.230", 7005));
		nodes.add(new HostAndPort("112.74.166.230", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		// 使用JedisCluster操作redis
		jedisCluster.set("an", "666");
		System.out.println(jedisCluster.get("an"));
		jedisCluster.close();
		
	}
}
