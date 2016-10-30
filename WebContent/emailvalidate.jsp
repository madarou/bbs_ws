<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.*,com.bbs.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>E-mail verification</title>
	<style>
		body {
			font: 14px/1.2 arial,Helvetica, sans-serif;
			background: url(../../img/tile2.jpg) repeat;
			margin: 0;
		}
		h1 {
			padding-left: 20px;
			font-size: 30px;
			background: #BA5D1D;
			color: #ffddcc;
			line-height: 2;
			margin-top: 0;
		}
		.wrapper {			
			box-shadow: 0 1px 2px #CCC;
			width: 750px;
			margin: 0 auto;
			margin-top: 100px;
			background: #fff;
			padding: 20px 20px 30px 20px;
			height: 100px;
			font-size: 18px;
			white-space: pre-line;
			line-height: 1;
			color: #BA5D1D;
			
		}
	</style>
</head>
<body>

	<h1>E-mail validate</h1>
	<div class='wrapper'>
		
			<%
			Object statues=request.getAttribute("statues");
			if(statues==null){
				out.print("Error! Please try again!");
			}
			else{
				int statues_int=Integer.parseInt(statues.toString()); 
				if(statues_int==Constants.URL_INVALID){
					out.print("Sorry! Your link is invalid.Please try again!");
				}
				else if(statues_int==Constants.URL_SUCCESS){
					out.print("Congratulations!\n\nYour Email is validate!");
				}
				else{
					out.print("Sorry! Your link is out of time!");
				}
			}
			%>
			
		
	</div>
</body>
</html>