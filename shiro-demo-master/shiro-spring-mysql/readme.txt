编译后 没有找到spring-shiro.xml等文件  导致对象没有spring ioc创建

登陆校验原理
https://blog.csdn.net/hnbcjzj/article/details/84909671

jetty 启动会报错  但不影响访问
	oeja.AnnotationParser:Problem processing jar entry sun/applet/AppletPanel.class
	java.lang.ArrayIndexOutOfBoundsException: 30305
启动 
	1 jetty:run  访问 http://127.0.0.1/toLogin
	2 tomcat7:run 访问 http://127.0.0.1:8088/toLogin
用户名密码  adminSuper/123
//数据库的密文密码为abc123,防破解标识为abc,当前台输入密码123后,会拿123+abc进行加密成密文去和数据的密码匹配  如果相等则通过 	
md5 加密  https://blog.csdn.net/mar5342/article/details/79677289