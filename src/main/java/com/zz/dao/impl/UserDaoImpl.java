package com.zz.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zz.dao.UserDao;
import com.zz.dao.UserWrapp;
import com.zz.vo.User;

@Component
public class UserDaoImpl implements UserDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User getUserByUserName(String username) {
		String sql = "select username,password from users where username=?";
		User user = jdbcTemplate.queryForObject(sql, new UserWrapp(), username);
		return user;
	}

}
