package com.jelly.shiroMySQLDemo.shiro;

import com.jelly.shiroMySQLDemo.model.TPermission;
import com.jelly.shiroMySQLDemo.model.TResource;
import com.jelly.shiroMySQLDemo.model.TRole;
import com.jelly.shiroMySQLDemo.model.TUser;
import com.jelly.shiroMySQLDemo.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jelly-liu on 2016/10/15.
 * Realm提供了安全的访问应用的相关实体类，比如用户、角色、权限，对其中的访问应用相应的认证或者授权操作。
 * 其提供的主要的方法为AuthenticationInfo#getAuthenticationInfo，涉及的内容是关于信息的认证，这主要由AuthencatingRealm类实现
 */
public class MyAuthorizingRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(MyAuthorizingRealm.class);

    @Resource
    ShiroService shiroService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;

        //only do check out user is exit or not, do not need do password matching
        TUser user = shiroService.getUserByUsername(token.getUsername());
        if(user == null){
            return null;
        }

        //pick out stored password and salt to AuthenticationInfo
        //数据库的密文密码为abc123,防破解标识为abc,当前台输入密码123后,会拿123+abc进行加密成密文去和数据的密码匹配  如果相等则通过 
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getId(),//at here, set an unique principal
        		//user.getUsername(),
                user.getPassword(),
                new SimpleByteSource(user.getSalt()),//user.getSalt()为防破解标识
                this.getName());
        return authenticationInfo;
    }

    //校验身份通过后  获取用户角色及权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String userId = (String)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();

        //load roles and permissions
        List<TRole> roles = shiroService.getRoleOfUser(userId);
        List<TPermission> perms = shiroService.getPermissionsOfUser(userId);

        if(roles != null && roles.size() > 0){
            for(TRole role : roles){
                simpleAuthorInfo.addRole(StringUtils.trim(role.getRoName()));
            }
        }
        if(perms != null && perms.size() > 0){
            for(TPermission perm : perms){
                simpleAuthorInfo.addStringPermission(StringUtils.trim(perm.getPeName()));
            }
        }

        return simpleAuthorInfo;
    }

    //参考 https://blog.csdn.net/mar5342/article/details/79677289
    //SimpleHash(String algorithmName, Object source, Object salt, int hashIterations)
    //algorithmName:算法类型    source:加密前的值  salt:干扰数据 盐 防破解标识   hashIterations:hash的次数
    public static void main(String[] args) {
    	
        SimpleHash simpleHash = new SimpleHash("MD5", "123", "abc", 1);//本例子中1 代表用户id值
        System.out.println(simpleHash.toString());
        System.out.println((System.currentTimeMillis() + "").length());
        //md5 在线 解密 https://www.cmd5.com/
    }
}
