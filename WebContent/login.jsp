<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登陆</title>
</head>
<body>
 <h3>Login with Username and Password</h3>
        <form name='f' action='/rest/auth/test' method='POST'>
            <table>
                <tr>
                    <td>User:</td>
                    <td>
                        <input type='text' name='username' value=''>
                        </td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td>
                            <input type='password' name='password'/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan='2'>
                            <input name="submit" type="submit" value="Login"/>
                        </td>
                    </tr>
                </table>
                 <p>如果你没有网站帐号，你还可以用人人网帐号登录本网站：</p>
  				 <a href="https://graph.renren.com/oauth/authorize?client_id=${appId}&response_type=code&redirect_uri=${redirectUri}&display=page"><img style="border:0px" src="img/login.png"/></a>
            </form>
</body>
</body>
</html>