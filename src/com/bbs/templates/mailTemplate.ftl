<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>你在4jobs.me订阅的职位信息</title>
<style type="text/css">
body{
	width: 650px;
	margin: auto;
	padding: 10px;
}
.top{
	background: #428bca;
	padding: 10px;
}
.mylogo{
    overflow: auto;
}
.mylogo p{
    font-size: 36px;
  	margin: 0;
  	padding: 0;
    text-shadow: 0 0 2px #000;
    float: left;
    margin-left: 10px;
}
.mylogo p .mylogo-p-1{
    font-size: 38px;
    color: #fff;
}
.mylogo p .mylogo-p-2{
    font-size: 36px;
    color: #fff;
}
.mylogo p .mylogo-p-3{
    font-size: 36px;
    color: #fff;
}
.intro{
	font-size: 14px;
	color: #333;
	padding: 10px 20px;
}
.jobs{
	list-style: none;
	margin: 0;
	padding: 10px 20px;
}
.jobs .title{
	float: left;
	line-height: 35px;
	color: #428bca;
	font-size: 16px;
	width: 100%;
}
.jobs .from{
	float: left;
	line-height: 25px;
	font-size: 12px;
	color: #999;
	margin: 0;
}
.jobs .detail{
	height: 60px;
	width: 60px;
	line-height: 60px;
	text-align: center;
	background: #eee;
	float: right;
	color: #666;
	text-decoration: none;
}
.jobs .detail:hover{
	color: #fff;
	background: #428bca;
}
.more{
	background: #eee;
	width: 610px;
	height: 35px;
	display: block;
	margin: 0 20px;
	border: 1px solid #ccc;
	text-align: center;
	line-height: 35px;
	color: #428bca;
}
.more:hover{
	box-shadow: 0 0 5px 1px #aaa;
}
.bottom{
border-top: 1px solid #ccc;
margin-top: 10px;
}
.bottom p{
	font-size: 12px;
	text-align: center;
}
</style>
</head>
<body>

<div align="center">
	<table align="center" border="0" cellpadding="0">
		<thead>
			<tr class="top">
				<td colspan=2>
					<div class="mylogo">
						<a href="http://www.4jobs.me">
							<p class="logo"><span class="mylogo-p-1">4</span><span class="mylogo-p-2">jobs</span>.<span class="mylogo-p-3">me</span></p>
						</a>
					</div>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><p>亲爱的用户，你好！</p></td>
			</tr>
			<tr>
				<td><p>你在<a href="http://www.4jobs.me">4jobs.me</a>上订阅了职位信息，以下是我们为你推荐的职位，希望对你有所帮助！</p></td>
			</tr>
			${joblist}
			<tr>
				<td colspan=2>
					<p>&nbsp;</p>
					<a href="http://www.4jobs.me" style="background: #eee;width: 610px;height: 35px;display: block;margin: 0 20px;border: 1px solid #ccc;text-align: center;line-height: 35px;color: #428bca;" target="_blank">More...</a>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="bottom">
	<p>请勿回复本邮件！</p>
	<p>如果你不想再收到本邮件，请点击${unsubscribe}</p>
</div>
</body>
</html>