package com.jelly.shiroMySQLDemo.shiro;

import java.net.URL;

import org.apache.shiro.authc.credential.CredentialsMatcher;

public class ClassPath {
    public static void main(String args[]) {
            Class targetclass = CredentialsMatcher.class;
            String className = targetclass.getName();
            className = className.replace('.', '/');
            String resource = "/" + className + ".class";
            URL url = targetclass.getResource(resource);
            System.out.println(url.getFile());
            // 输出结果：
            // file:/D:/devtools/IBMJSDK141/jre/lib/core.jar!/java/lang/String.class
    }

}