package com.zz.realm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zz.dao.UserDao;
import com.zz.vo.User;
/**
 * 自定义realm
 * 继承AuthorizingRealm实体2个方法 一个授权一个认证
 * @author zzj
 *
 */
@Service
public class CustomerRealm extends AuthorizingRealm{

	@Autowired
	private UserDao userDao;
	
	Map<String, String> userMap = new HashMap<String, String>();
	Map<String, String> roleMap = new HashMap<String, String>();
	Map<String, String> permissionMap = new HashMap<String, String>();
	
	{
		userMap.put("zzj", "cd5508665d1d747749c531e502ea27df");
		userMap.put("mark", "123456");
		roleMap.put("zzj", "admin");
		permissionMap.put("admin", "user:delete");
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//授权
		//1.获取用户名
		String userName = (String) principals.getPrimaryPrincipal();
		//2.查表查出用户的角色
		
		Set<String> roles = getRolesByUserName(userName);
		Set<String> permissions = getPermissionsByUserName(roles);
		if(roles == null) {
			return null;
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(roles);
		authorizationInfo.setStringPermissions(permissions);
		return authorizationInfo;
	}

	private Set<String> getPermissionsByUserName(Set<String> roles) {
		
		Set<String> sets = new HashSet<String>();
		for (String role : roles) {
			if(permissionMap.containsKey(role)) {
				sets.add(permissionMap.get(role));
			}
		}
		if(sets.size() > 0) {
			return sets;
		}
		return null;
	}

	private Set<String> getRolesByUserName(String userName) {
		
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < roleMap.size(); i++) {
			if(roleMap.containsKey(userName)) {
				set.add(roleMap.get(userName));
			}
		}
		if(set.size() > 0) {
			return set;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//认证
		//1.从主体传过来的认证信息中获取用户名
		String userName = (String) token.getPrincipal();
		
		//通过用户名到数据库中获取凭证
		//String password = getPasswordByUserName(userName);
		User user = userDao.getUserByUserName(userName);
		if(user == null) {
			return null;
		}
		
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), "customeRealm");
		//在这里设置加盐
		simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zzj"));
		return simpleAuthenticationInfo;
	}

	private String getPasswordByUserName(String userName) {
		
		//模拟数据库
		return userMap.get(userName);
	}	
	
	public static void main(String[] args) {
		//加盐 
		Md5Hash md5 = new Md5Hash("123", "zzj");
		System.out.println(md5.toString());
	}


}
