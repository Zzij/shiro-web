package com.zz.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisUtil {

	@Autowired
	private JedisPool jedisPool;

	private final String NX="NX";
	
	private final String XX="XX";
	
	private final String EX="EX";
	
	private final String PX="PX";
	
	private Jedis getResource() {
		return jedisPool.getResource();
	}
	
	/**
	 * setex = set + expire
	 * @param key
	 * @param value
	 * @param time
	 */
	public void setex(byte[] key, byte[] value, int time) {
		Jedis jedis = getResource();
		try {
			String set = jedis.setex(key, time, value);
			System.out.println(set);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedis.close();
		}
	}

	public byte[] get(byte[] key) {
		Jedis jedis = getResource();
		try {
			return jedis.get(key);
		} finally {
			jedis.close();
		}
	}

	public void del(byte[] key) {
		Jedis jedis = getResource();
		try {
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedis.close();
		}
	}

	public Set<byte[]> keys(byte[] patten) {
		Jedis jedis = getResource();
		try {
			Set<byte[]> keys = jedis.keys(patten);
			return keys;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jedis.close();
		}
		return null;
	}
	
	
}
