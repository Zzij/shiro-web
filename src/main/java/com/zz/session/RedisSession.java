package com.zz.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import com.zz.util.JedisUtil;

public class RedisSession extends AbstractSessionDAO{

	@Autowired
	private JedisUtil jedisUtil;
	
	private final String SHIRO_SESSSION_PREFIX = "zzj_shiro";
	
	private byte[] getKey(String key) {
		return (SHIRO_SESSSION_PREFIX + key).getBytes();
	}
	public void update(Session session) throws UnknownSessionException {
		System.out.println("update");
		saveSession(session);
	}

	public void delete(Session session) {
		if(session == null || session.getId() == null) {
			return;
		}
		byte[] key = getKey(session.getId().toString());
		jedisUtil.del(key);
	}

	public Collection<Session> getActiveSessions() {
		System.out.println("getactive");
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSSION_PREFIX.getBytes());
		Set<Session> set = new HashSet<Session>();
		if(CollectionUtils.isEmpty(keys)) {
			return set;
		}
		for (byte[] key : keys) {
			set.add((Session)SerializationUtils.deserialize(key));
		}
		return set;
	}

	@Override
	protected Serializable doCreate(Session session) {
		System.out.println("create");
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("read");
		if(sessionId == null) {
			return null;
		}
		byte[] key = getKey(sessionId.toString());
		byte[] value = jedisUtil.get(key);
		return (Session) SerializationUtils.deserialize(value);
	}

	
	private void saveSession(Session session) {
		if(session == null || session.getId() == null) {
			return;
		}
		byte[] key = getKey(session.getId().toString());
		byte[] value = SerializationUtils.serialize(session);
		jedisUtil.setex(key, value, 600);
	}
}
