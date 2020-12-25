package com.zz.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.zz.util.JedisUtil;

@Component
public class RedisCache<K, V> implements Cache<K, V>{

	@Autowired
	private JedisUtil jedisUtil;
	
	private final String CACHE_TAG = "cache_zzj";
	
	private byte[] getKey(K key) {
		if(key instanceof String) {
			return (CACHE_TAG + key).getBytes();
		}
		return SerializationUtils.serialize(key);
	}
	
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public V get(K arg0) throws CacheException {
		byte[] vaule = jedisUtil.get(getKey(arg0));
		if(vaule != null) {
			return (V) SerializationUtils.deserialize(vaule);
		}
		return null;
	}

	public Set<K> keys() {
		
		return null;
	}

	public V put(K arg0, V arg1) throws CacheException {
		
		jedisUtil.setex(getKey(arg0), SerializationUtils.serialize(arg1), 600);
		return arg1;
	}

	public V remove(K arg0) throws CacheException {
		byte[] key = getKey(arg0);
		V v = get(arg0);
		jedisUtil.del(key);
		if(v != null) {
			return v;
		}
		return null;
	}

	public int size() {
		Set<byte[]> keys = jedisUtil.keys(CACHE_TAG.getBytes());
		if(keys != null) {
			return keys.size();
		}
		return 0;
	}

	public Collection<V> values() {
		Set<byte[]> keys = jedisUtil.keys(CACHE_TAG.getBytes());
		Set<V> values = new HashSet<V>();
		if(keys != null) {
			for (byte[] key : keys) {
				byte[] value = jedisUtil.get(key);
				values.add((V)SerializationUtils.deserialize(value));
			}
		}
		return values;
	}

}
