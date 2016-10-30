var url='/';
var curJobPage=1;
var jobSize=30;//job页条目数
var curSource='';
var curJobType='ALL';
var curAllCount=0;//中条目数
var isKeyFilter=0;//是否开启过滤
var allKeys;
var hasNext=1;
var jol=0;
var shareCount=-1;
var isLogin=0;
var orderBy=1;//time 
var timeTo=7;
var uid=-1;
var userQueryLog;
var subInfo=new Object();
var myScrol;
var hd="<div class=\"noti-hd\"><img src=\"img/loading.gif\"></div>";
var er="<div class=\"noti-er\">Error...</div>";
 $.ajaxSetup({ cache: false }); 
function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}


function initLoginInfo(){
	$.ajax({

		  url: url+"rest/user/getUserInfo",
		  success: function(info){
				//success
			  	if(info.uid){
			  		isLogin=1;
			  		uid=info.uid;
			  		username=info.username;
			  		searchkey=info.searchkey;
			  		headurl=info.headurl;
	
			  		$('.login-info').css('display','');
			  		$('.login-info .login-info-img img').attr('src',headurl);
			  		$('.login-info .login-info-name').text(username);
			  		
			  		$('.login-btn').css('display','none');
			  		if(isLogin==1){// get query log
						var turl=url+"solrapi/querylog";
						$.ajax({
						  	type: 'POST',
							url: turl,
							data:  JSON.stringify({ 'type':+'0', 'query': '','uid':uid }),
						  	success: function(querylog){
						  		userQueryLog=querylog;
						  		$('#query-input').autocomplete({
								    lookup: userQueryLog
								});
							  },
						  	error:function(XMLHttpRequest, textStatus, errorThrown){ 
							  },
							contentType: "application/json",
						  	dataType: "json"
						  
						});
					}

					$.ajax({
					  url: "/solrapi/Tag?uid="+uid+"&size=10",
					  success: function(info){
					  		subInfo.recTags=new Array();
					  		$.each(info, function(i, tag){
					  			subInfo.recTags[i]=tag;
					  		});
						  },
					  error:function(XMLHttpRequest, textStatus, errorThrown){
						},
					  dataType: "json"
					});

				}else{
					isLogin=0;
					$('.login-info').css('display','none');
			  		$('.login-btn').css('display','');
			  		
				}
			  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
			  	isLogin=0;
				$('.login-info').css('display','none');
		  		$('.login-btn').css('display','');
		  		
			  },
		  dataType: "json"
		});

		

}
var curJobId;
var curJobSource;
function changeJobPostFav(){
	if(isLogin==0){
		$('#myModalLogin').modal('show');
		return 0;
	}

	$('.modal-fav a').attr('onclick','');
	
	var isS=$('.modal-fav').attr('isSave');
	if(isS=='Y'){
		var curl=url+"rest/user/removeJobFav";
	}else{
		var curl=url+"rest/user/addJobFav";
	}
	var id=curJobId;
	$.ajax({
		  type: 'POST',
		  url: curl,
		  data:  JSON.stringify({ 'id': ''+id, 'type': 'job' }),
		  success: function(info){
			  statues=info.statues;
			  if(statues=="1"){//success
			  	if(isS=='Y'){
			  		//updata post
			  		$('.modal-fav .modal-fav-t').text('添加到收藏');
				  	$('.modal-fav a').attr('onclick','changeJobPostFav()');
				  	$('.modal-fav').attr('isSave','N');
				  	//updata list
				  	$(".job-tr-"+id).removeClass('job-mark-Y');
			  		$(".job-tr-"+id).addClass('job-mark-N');
			  		$(".job-tr-"+id).attr('isSave','N');
			  	}else{

				  	$('.modal-fav .modal-fav-t').text('取消收藏');
				  	$('.modal-fav a').attr('onclick','changeJobPostFav()');
				  	$('.modal-fav').attr('isSave','Y');
				  	//
				  	$(".job-tr-"+id).removeClass('job-mark-N');
			  		$(".job-tr-"+id).addClass('job-mark-Y');
			  		$(".job-tr-"+id).attr('isSave','Y');
				 
				}
			  }else if(statues=="0"){
				  alert('操作失败');
				  $('.modal-fav a').attr('onclick','changeJobPostFav()');
			  }else{
				  $('#myModalLogin').modal('show');
				  $('.modal-fav a').attr('onclick','changeJobPostFav()');
			  }
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
			  $('.modal-fav a').attr('onclick','changeJobPostFav()');
			  },
		  contentType: "application/json",
		  dataType: "json"
		});
}
function changeJobListFav(id){
	//alert(id);
	if(isLogin==0){
		$('#myModalLogin').modal('show');
		return 0;
	}
	var isS=$(".job-tr-"+id).attr('isSave');
	//alert(isS);
	$(".job-tr-"+id+' .t-mark a').attr('onclick','');
	if(isS=='Y'){
		var curl=url+"rest/user/removeJobFav";
	}else{
		var curl=url+"rest/user/addJobFav";
	}
	$.ajax({
		  type: 'POST',
		  url: curl,
		  data:  JSON.stringify({ 'id': ''+id, 'type': 'job' }),
		  success: function(info){
			  statues=info.statues;

			  if(statues=="1"){//success

			  		if(isS=='Y'){
			  			$(".job-tr-"+id).removeClass('job-mark-Y');
			  			$(".job-tr-"+id).addClass('job-mark-N');
			  			$(".job-tr-"+id).attr('isSave','N');
			  		}else{
			  			$(".job-tr-"+id).removeClass('job-mark-N');
			  			$(".job-tr-"+id).addClass('job-mark-Y');
			  			$(".job-tr-"+id).attr('isSave','Y');
			  		}
				  
			  }else if(statues=="0"){
				  alert('操作失败');
			
			  }else{
				  $('#myModalLogin').modal('show');
			  }
			  $(".job-tr-"+id+' .t-mark a').attr('onclick','changeJobListFav(\"'+id+'\")');
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
		  		alert('连接超时');
			  	$(".job-tr-"+id+' .t-mark a').attr('onclick','changeJobListFav(\"'+id+'\")');
			  },
		  contentType: "application/json",
		  dataType: "json"
		});
}
function initFavList(){
	isKeyFilter=2;
	firstPage();
	$('.login-info .login-fav-box').css('background','#fff');
	$('.login-info .login-fav-box').css('border-top','1px solid');
	$('.login-info .login-fav-box').css('border-bottom','1px solid');
	$('.login-info .login-fav-box').css('border-left','1px solid');
	$('.login-info .login-fav-box').css('box-shadow','5px 0 #fff');
}

function openJob(id,source){
	$('#myModal').modal('show');
	if(isLogin==1){
		$('.modal-fav').css('display','');
		var isS=$(".job-tr-"+id).attr('isSave');
		if(isS=='Y'){
			$('.modal-fav').attr('isSave','Y');
			$('.modal-fav .modal-fav-t').text('取消收藏');
		}else{
			$('.modal-fav').attr('isSave','N');
			$('.modal-fav .modal-fav-t').text('添加到收藏');
		}
		curJobId=id;
		curJobSource=source;
	}else{
		$('.modal-fav').attr('isSave','N');
		$('.modal-fav .modal-fav-t').text('添加到收藏');
		$('.modal-fav').css('display','');
	}
	
	getJob(id,source);
}
function getJob(id,source){

$('#myModal .modal-body').html(hd);

$.ajax({

	  url: url+"rest/jobs/getcontent?id="+id+"&source="+source,
	  success: function(job){
			//success
			content=job.content;
			time=job.time;
			title=job.title;
			purl='http://www.4jobs.me/jobpost.html?source='+source+'&id='+id;
			if(source=='SJ'||source=='PKU')
				content=content.replace(/\n/g,'<br/>');
			$('#myModal .modal-header h4').text(title);
			$('#myModal .modal-body').html(content);
			$('#myModal .modal-right-header-t').text(time);
			$('#myModal .modal-right-header-s').text(sourceToStr(source));
			$('#myModal .modal-right-header-img').css('background',"url('"+getSourceImg(source)+"')");
			$('#myModal .modal-right-url a').attr('href',url+'jobpost.html?source='+source+'&id='+id);
			$('#myModal .modal-right-url a').text(purl);
			
			summary=$('#myModal .modal-body').text().substring(0,100);
			
			setShare('[www.4jobs.me]'+title,purl,summary);
			
		  },
	  error:function(XMLHttpRequest, textStatus, errorThrown){
			  $('#myModal .modal-body').html(er);
		  },
	  dataType: "json"
	  
	});

}
function setShare(title,url,summary){
	
	//bShare.init();
	bShare.addEntry({
		title: title,
		url: url,
		summary: summary
		//pic: "指定分享的图片的链接，目前支持新浪微博，QQ空间，搜狐微博，网易微博，人人网， 嘀咕，腾讯微博和做啥。"
	});
	shareCount=shareCount+1;
}
function share(tar){
	bShare.share(event, tar, shareCount);
}
function nextPage(){
//$('.nav_pages .pre').removeClass('disabled');
	if(hasNext==1){
		getJobs(1,curSource);
	}
}
function prePage(){
	if(curJobPage!=1){
		getJobs(-1,curSource);
	}

}
function freshPage(){
	getJobs(0,curSource);
}
function firstPage(){
getJobs(1-curJobPage,curSource);
}
function initJobs(){
	if(isKeyFilter==2){//cancel fav 
		isKeyFilter=0;
		$('.login-info .login-fav-box').css('background','#ddd');
		$('.login-info .login-fav-box').css('border','0');
		$('.login-info .login-fav-box').css('box-shadow','0 0 0');
	}
	firstPage();
}
function getJobs(page,sou){
	
	tpage=curJobPage+page;
	var turl='';
	$('.top_nav').css('display','');
	$('.top_nav_right').css('display','none');
	if(isKeyFilter==1){
		
		// if(sou==''){
		// 	//$('.top_nav .label-s').text('全部 :');
		// 	turl=url+"rest/jobs/search?keywordlist="+allKeys+"&start="+((tpage-1)*jobSize+0)+"&end="+(tpage*jobSize-1);
		// }else{
		// 	//$('.top_nav .label-s').text(sourceToStr(sou)+' :');
		// 	turl=url+"rest/jobs/search?keywordlist="+allKeys+"&start="+((tpage-1)*jobSize+0)+"&end="+(tpage*jobSize-1)+"&source="+sou;
		// }
	}else if(isKeyFilter==0){
		//$('.top_nav').css('display','none');
		if(sou==''){
			turl=url+"rest/jobs/getlist?start="+((tpage-1)*jobSize+0)+"&end="+(tpage*jobSize-1)+"&jobtype="+curJobType;
		}else{
			turl=url+"rest/jobs/getlist?start="+((tpage-1)*jobSize+0)+"&end="+(tpage*jobSize-1)+"&source="+sou+"&jobtype="+curJobType;
		}
	}else if(isKeyFilter==2){
		//$('.top_nav').css('display','');
		turl=url+'rest/user/getJobFavList?start='+((tpage-1)*jobSize+0)+'&end='+(tpage*jobSize-1);
	}else if(isKeyFilter==3){
		if(sou!='')
			turl=url+'rest/jobs/search?page='+(tpage-1)+'&pageSize='+jobSize+'&key='+allKeys+'&source='+sou+'&orderBy='+orderBy+'&timeto='+timeTo+"&jobtype="+curJobType;
		else
			turl=url+'rest/jobs/search?page='+(tpage-1)+'&pageSize='+jobSize+'&key='+allKeys+'&orderBy='+orderBy+'&timeto='+timeTo+"&jobtype="+curJobType;
		
	}
	$('.job_table table').html(hd);
	turl=encodeURI(turl);
	turl=encodeURI(turl); //两次编码
	$.ajax({

		  url: turl,
		  success: function(jobs){
				//success
			  $('.job_table table').html('');
			  //alert(jobs.cnt);
			  if(isKeyFilter==3)
			  	curAllCount=parseInt(jobs.numFound);
			  else
			  	curAllCount=parseInt(jobs.cnt);
			  	
			  var joblist;
			  if(isKeyFilter==1){
				  joblist=jobs.result;
				  	if(sou==''){	
				  		$('.top_nav .top_nav_left .label-s').text('Search in 全部 ( '+curAllCount+' ) :');
					}else{
						$('.top_nav  .top_nav_left .label-s').text('Search in '+sourceToStr(sou)+' ( '+curAllCount+' ) :');
					}
			  }else if(isKeyFilter==0){
				  joblist=jobs.joblist;
				  	$('.top_nav .top_nav_left').html('');
  					$('.top_nav .top_nav_left').append('<span class=\"label-s\"></span>');
					if(sou==''){	
				  		$('.top_nav .top_nav_left .label-s').text('全部 ( '+curAllCount+' ) :');
					}else{
						$('.top_nav .top_nav_left .label-s').text(sourceToStr(sou)+' ( '+curAllCount+' ) :');
					}
					//$('.top_nav').append('<a class=\"top_nav_back \" onclick=\"closeKeyFilter()\" href=\"javascript:void(0)\"><span class=\"label label-danger\">返回</span></a>');

			  }else if(isKeyFilter==2){
				  	var statues=jobs.statues;

					if(statues=='-1'){
						alert('请登录');
						return;
					}else if(statues=='1'){
						joblist=jobs.favlist;
						$('.top_nav .top_nav_left').html('');
  						$('.top_nav .top_nav_left').append('<span class=\"label-s\">Search in 全部 :</span>');
						$('.top_nav .top_nav_left .label-s').text('我的收藏 ( '+curAllCount+' ) :');
						$('.top_nav .top_nav_left').append('<a class=\"top_nav_back \" onclick=\"closeKeyFilter()\" href=\"javascript:void(0)\"><span class=\"label label-danger\">返回</span></a>');

				  	}
			  }else if(isKeyFilter==3){
				  	joblist=jobs.jobs;
				  	if(sou==''){	
				  		$('.top_nav .top_nav_left .label-s').text('Search in 全部 ( '+curAllCount+' ) :');
					}else{
						$('.top_nav .top_nav_left .label-s').text('Search in '+sourceToStr(sou)+' ( '+curAllCount+' ) :');
					}
					$('.top_nav_right').css('display','');
			  }
			    $.each(joblist, function(i, job){
			      
				  var id=job.id;
				  var time=job.time;
				  var title=job.title;
				  var source=job.source;
				  var isSave=job.save;
				  if(isKeyFilter==2){
				  	isSave='Y';
				  }
				  sourceStr='';

				  if(isLogin==0)
				  	isSave='N';
				  
				  
				  var html='<tr isSave=\"'+isSave+'\" class=\"job-tr-'+id+' job-mark-'+isSave+' job-tr-'+i+'\">';
					html+='<td class=\"t-logo\"><img src=\"img/'+source+'.jpg\"></td>';
					html+='<td class=\"t-title\"><a onclick=\"openJob('+id+',\''+source+'\')\" href=\"javascript:void(0)\">'+title+'</a></td>';
					html+='<td class=\"t-desc\">';
					html+='<span>'+sourceToStr(source)+'</span>';
					html+='<span>'+time+'</span>';
					html+='</td><td class=\"t-mark\">';
					html+='<a onclick=\"changeJobListFav('+id+')\" href=\"javascript:void(0)\"></a></td></tr>';
				  
				  $('.job_table table').append(html);
			    });
				curJobPage=curJobPage+page;
				if(curJobPage==1){
					$('.nav_pages .pre').addClass('disabled');
				}else if(curJobPage>1){
					$('.nav_pages .pre').removeClass('disabled');
				}
				if(curJobPage*jobSize>=curAllCount){
					$('.nav_pages .next').addClass('disabled');
					hasNext=0;
				}else{
					$('.nav_pages .next').removeClass('disabled');
					hasNext=1;
				}
				
				$('html, body').animate({scrollTop:0}, 'fast');
			  },
			  error:function(XMLHttpRequest, textStatus, errorThrown){
				  $('.job_table table').html(er);
			  },
		  dataType: "json"
		});
	
}
function sourceToStr(s){
	if(s=='FDU'){
		return '复旦日月光华';
	}else if(s=='SJ'){
		return '上交饮水思源';
	}else if(s=='QINGHUA'){
		return '清华水木社区';
	}else if(s=='PKU'){
		return '北大未名社区';
	}else if(s=='NJU'){
		return '南大小百合';
	}else if(s=='BYR'){
		return '北邮人';
	}else if(s=='ECNU'){
		return '爱在华师';
	}else if(s=='ECUST'){
		return '华理梅陇客栈';
	}else if(s=='BJTU'){
		return '北交知行';
	}
}
function getSourceImg(s){
	if(s=='FDU'){
		return 'img/FDU.jpg';
	}else if(s=='SJ'){
		return 'img/SJ.jpg';
	}else if(s=='QINGHUA'){
		return 'img/QINGHUA.jpg';
	}else if(s=='PKU'){
		return 'img/PKU.jpg';
	}else if(s=='NJU'){
		return 'img/NJU.jpg';
	}else if(s=='BYR'){
		return 'img/BYR.jpg';
	}else if(s=='ECNU'){
		return 'img/ECNU.jpg';
	}else if(s=='ECUST'){
		return 'img/ECUST.jpg';
	}else if(s=='BJTU'){
		return 'img/BJTU.jpg';
	}
}

function removeSTag(){
	$(".btn-s-all").removeClass('active');
	$(".btn-s-fd").removeClass('active');
	$(".btn-s-sj").removeClass('active');
	$(".btn-s-qh").removeClass('active');
	$(".btn-s-bd").removeClass('active');
	$(".btn-s-nju").removeClass('active');
	$(".btn-s-byr").removeClass('active');
	$(".btn-s-ecnu").removeClass('active');
	$(".btn-s-ecust").removeClass('active');
	$(".btn-s-bjtu").removeClass('active');
	
	$(".c_left_source table button span").css('display','none');
}
function removeJTag(){
	$(".btn-j-all").removeClass('active');
	$(".btn-j-full").removeClass('active');
	$(".btn-j-part").removeClass('active');
	
	$(".c_left_jobtype table button span").css('display','none');
}
function closeKeyFilter(){
	if(isKeyFilter==2){
		$('.login-info .login-fav-box').css('background','#ddd');
		$('.login-info .login-fav-box').css('border','0');
		$('.login-info .login-fav-box').css('box-shadow','0 0 0');
	}
	isKeyFilter=0;
	firstPage();
}
function jobSearch(){
	if(isLogin==0){
		$('#myModalLogin').modal('show');
		return 0;
	}
	var keys='';
	var keyStr=$('.search-val').val();
  	keyStr=trimStr(keyStr);
  	if(keyStr==''){
  		return;
  	}
  	$('.top_nav .top_nav_left').html('');

  	$('.top_nav .top_nav_left').append('<span class=\"label-s\">全部 :</span>');
	var ks=keyStr.split(' ');
  			for(var i=0;i<ks.length;i++){
  				if(trimStr(ks[i])!=''){
  					keys=keys+trimStr(ks[i])+',';
  					$('.top_nav .top_nav_left').append('<span class=\"top_nav_key label label-primary\">'+trimStr(ks[i])+'</span>');
  				}
  			}
  	$('.top_nav .top_nav_left').append('<a class=\"top_nav_back \" onclick=\"closeKeyFilter()\" href=\"javascript:void(0)\"><span class=\"label label-danger\">返回</span></a>');


	if(isKeyFilter==2){//fav
		$('.login-info .login-fav-box').css('background','#ddd');
		$('.login-info .login-fav-box').css('border','0');
		$('.login-info .login-fav-box').css('box-shadow','0 0 0');
	}
	isKeyFilter=3;
	allKeys=keyStr;
	addQueryLog(keyStr);
	firstPage();
}
function queryOrder(d,val){
	orderBy=d;
	$('.top_nav_right_order .btn').html(val+"<span class=\"caret\"></span>");
	//alert(val);
	freshPage();

}
function queryTimeTo(t,val){
	timeTo=t;
	$('.top_nav_right_timeto .btn').html(val+"<span class=\"caret\"></span>");
	freshPage();
}
function addQueryLog(query){
	if(uid==-1)
		return;
	var turl=url+"solrapi/querylog";
	$.ajax({
	  	type: 'POST',
		url: turl,
		data:  JSON.stringify({ 'type':+'1', 'query': query,'uid':uid }),
	  	success: function(querylog){
	  		userQueryLog=querylog;
	  		$('#query-input').autocomplete({
			    lookup: userQueryLog
			});
		  },
	  	error:function(XMLHttpRequest, textStatus, errorThrown){ 
		  },
		contentType: "application/json",
	  	dataType: "json"
	  
	});
}
function openUserQueryLog(){
	if(isLogin==1&&userQueryLog!=null){
		$('.autocomplete-suggestions').html('');
		$.each(userQueryLog, function(i, log){
			//<div class="autocomplete-suggestion" data-index="0"></div>
			$('.autocomplete-suggestions').append("<div class=\"autocomplete-suggestion\" data-index=\""+i+"\">"+log.value+"</div>");
		});
		$('.autocomplete-suggestions').css('display','');
	}
}
function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]); return null; //返回参数值
} 
function openLoginModal(){
	$('#myModalLogin').modal('show');
}
function openSubModal(){

	if(isLogin!=1){
		$('#myModalLogin').modal('show');
		return 0;
	}
	$("#wizard").scrollable().begin();

	$('#myModalSub').modal('show');
	$('#wizard .subRead').html(hd);
    $('#wizard .subRead').show(200);
	$('#wizard .subEdit').hide();

    $.ajax({
		  type: 'GET',
		  url: '/rest/subscribe/getsubscribes',
		  success: function(info){

		  		$('#wizard .subRead').hide();
				$('#wizard .subEdit').show(200);
				$("#myTags").tagit({
					tagLimit:8,
				});

		  		if(info.statues==1&&info.result!=null){
		  			subInfo.has='Y';

		  			subInfo.email=info.result.email;
		  			tmp=info.result.keylist.split(' ');
		  			subInfo.subTags=new Array();
		  			for(ti=0;ti<tmp.length;ti++){
		  				subInfo.subTags[ti]=tmp[ti];
		  			}

		  			tmp=info.result.source.split(' ');
		  			subInfo.sources=new Array();
		  			for(ti=0;ti<tmp.length;ti++){
		  				subInfo.sources[ti]=tmp[ti];
		  			}
		  			subInfo.verify=info.result.verify;
		  			subInfo.enable=info.result.enable;
		  			

				    initSubModal(subInfo);

		  		}else{
		  			subInfo.has='N';
		  			subInfo.verify='N';
		  		}
		  		
		  		
			  	
		  },
		  error:function(XMLHttpRequest, textStatus, errorThrown){
		  		
		},
		  dataType: "json"
		});

    

}
function initSubModal(subinfo){
	$('.step-1-email').val(subinfo.email);
	for(ti=0;ti<subinfo.subTags.length;ti++){
		$("#myTags").tagit("createTag", subinfo.subTags[ti]);
	}
	$("#wizard .page-3 .sub-source").prop("checked",false);
	for(ti=0;ti<subinfo.sources.length;ti++){
		//alert(subinfo.sources[ti]);
		if(subinfo.sources[ti]=="ALL"){
			$('#wizard .page-3 .sub-source').prop('checked',true);
			break;
		}
		$('#wizard .page-3 .sub-source-'+subinfo.sources[ti]).prop('checked',true);
	}
	$('.step-2-keylist').html('');
	$('.step-2-keylist').html('<span>推荐：</span>');
	for(ti=0;ti<subinfo.recTags.length;ti++){
		//<a class="sub-key" href="javascript:void(0)">java</a>
		$('.step-2-keylist').append("<a class=\"sub-key\" onclick=\"addRecTag('"+subInfo.recTags[ti]+"')\" href=\"javascript:void(0)\">"+subInfo.recTags[ti]+"</a>");
	}
}
function addRecTag(val){
  		$("#myTags").tagit("createTag", val);
}
function init(a1){
	jol=a1;
	if(jol==0){
		initLoginInfo();
		initJobs();
		var jobid=getUrlParam('jobid');
		if(jobid!=null){
			//
		}
	}
}

$(document).ready(function(){
	  $(".btn-s-all").click(function(){ 
		  if(curSource!=''){
			  removeSTag();
			  $(".btn-s-all").addClass('active');
			  if(jol==0){
			  	curSource='';
			  	initJobs();
			  }else{
			  initLoves('');
			  }
			  
		  }  
	  });
	  $(".btn-s-fd").click(function(){ 
		  if(curSource!='FDU'){
			  removeSTag();
			  $(".btn-s-fd").addClass('active');
			  $(".btn-s-fd span").css('display','');
			  if(jol==0){
			  	curSource='FDU';
			  	initJobs();
			  }else{
			  initLoves('FDU');
			  }
		  }  
	  });
	  $(".btn-s-qh").click(function(){ 
		  if(curSource!='QINGHUA'){
			  removeSTag();
			  $(".btn-s-qh").addClass('active');
			  $(".btn-s-qh span").css('display','');
			  if(jol==0){
			  	curSource='QINGHUA';
			  	initJobs();
			  }else{
			  initLoves('QINGHUA');
			  }
		  }  
	  });
	  $(".btn-s-sj").click(function(){ 
		  if(curSource!='SJ'){
			  removeSTag();
			  $(".btn-s-sj").addClass('active');
			  $(".btn-s-sj span").css('display','');
			  if(jol==0){
			  	curSource='SJ';
			  	initJobs();
			  }else{
			  initLoves('SJ');
			  }
		  }  
	  });
	  $(".btn-s-bd").click(function(){ 
		  if(curSource!='PKU'){
			  removeSTag();
			  $(".btn-s-bd").addClass('active');
			  $(".btn-s-bd span").css('display','');
			  if(jol==0){
			  	curSource='PKU';
			  	initJobs();
			  }else{
			  initLoves('PKU');
			  }
		  }  
	  });

	  $(".btn-s-nju").click(function(){ 
		  if(curSource!='NJU'){
			  removeSTag();
			  $(".btn-s-nju").addClass('active');
			  $(".btn-s-nju span").css('display','');
			  if(jol==0){
			  	curSource='NJU';
			  	initJobs();
			  }else{
			  initLoves('NJU');
			  }
		  }  
	  });

	  $(".btn-s-byr").click(function(){ 
		  if(curSource!='BYR'){
			  removeSTag();
			  $(".btn-s-byr").addClass('active');
			  $(".btn-s-byr span").css('display','');
			  if(jol==0){
			  	curSource='BYR';
			  	initJobs();
			  }else{
			  initLoves('BYR');
			  }
		  }  
	  });

	  $(".btn-s-ecnu").click(function(){ 
		  if(curSource!='ECNU'){
			  removeSTag();
			  $(".btn-s-ecnu").addClass('active');
			  $(".btn-s-ecnu span").css('display','');
			  if(jol==0){
			  	curSource='ECNU';
			  	initJobs();
			  }else{
			  initLoves('ECNU');
			  }
		  }  
	  });

	  $(".btn-s-ecust").click(function(){ 
		  if(curSource!='ECUST'){
			  removeSTag();
			  $(".btn-s-ecust").addClass('active');
			  $(".btn-s-ecust span").css('display','');
			  if(jol==0){
			  	curSource='ECUST';
			  	initJobs();
			  }else{
			  initLoves('ECUST');
			  }
		  }  
	  });

	  $(".btn-s-bjtu").click(function(){ 
		  if(curSource!='BJTU'){
			  removeSTag();
			  $(".btn-s-bjtu").addClass('active');
			  $(".btn-s-bjtu span").css('display','');
			  if(jol==0){
			  	curSource='BJTU';
			  	initJobs();
			  }else{
			  initLoves('BJTU');
			  }
		  }  
	  });

	  $(".btn-j-all").click(function(){ 
		  if(curJobType!='ALL'){
			  removeJTag();
			  $(".btn-j-all").addClass('active');
			  $(".btn-j-all span").css('display','');
			  	curJobType='ALL';
			  	initJobs();
		  }  
	  });
	  $(".btn-j-full").click(function(){ 
		  if(curJobType!='FULL'){
			  removeJTag();
			  $(".btn-j-full").addClass('active');
			  $(".btn-j-full span").css('display','');
			  	curJobType='FULL';
			  	initJobs();
		  }  
	  });

	  $(".btn-j-part").click(function(){ 
		  if(curJobType!='PART'){
			  removeJTag();
			  $(".btn-j-part").addClass('active');
			  $(".btn-j-part span").css('display','');
			  	curJobType='PART';
			  	initJobs();
		  }  
	  });

	  $('.search-val').bind('keypress',function(event){
            if(event.keyCode == "13")    
            {
                jobSearch();
            }
       });

	$(".search-val").on('input',function(e){  
		   if($('.search-val').val().length==0){
		   	//open log
		   //	openUserQueryLog();
		   }
	}); 

	$(".search-val").click(function(){
  		if($('.search-val').val().length==0){
		   	//open log
		   //	openUserQueryLog();
		}
  	});

  	$("#wizard .page-2 .removeTags").click(function(){
  		$("#myTags").tagit("removeAll");
  	});

  	$("#wizard .page-3 .selectAll").click(function(){
  		$("#wizard .page-3 .sub-source").prop("checked",true);
  	});

	$("#wizard").scrollable({
      onSeek: function(event,i){
         $("#status li").removeClass("active").eq(i).addClass("active");
      },
      onBeforeSeek:function(event,i){
         if(i==1){
            var email_val = trimStr($("#wizard .step-1-email").val());
            var email_str = /^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/;

			 if(!email_str.test(email_val)){

			 	$('#wizard .step-1-email').focus();
			 	$('#wizard .step-1-email').parent().addClass("has-error");
			 	return false;
			 }else{
			 	$('#wizard .step-1-email').parent().removeClass("has-error");
			 }
			 if(email_val!=subInfo.email){//修改了email
			 	subInfo.verify='N';
			 }
			 subInfo.email=email_val;
         }
         if(i==2){

         	var subTags=$("#myTags").tagit("assignedTags");

         	if(subTags.length==0)
         		return false;

         	$('.step-4-keylist').html('');
         	var tags_str = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/;
         	strKeyList='';
         	for(ti=0;ti<subTags.length;ti++){

         		if(subTags[ti].length>10){
         			$('#wizard .page-2 .btn_nav p').text("Warning:单个Tag字数不超过10");
         			$('#wizard .page-2 .btn_nav p').show(200);
         			//$('#wizard .page-2 .btn_nav p').hide();
         			return false;
         		}
         		if(!tags_str.test(subTags[ti])){
         			$('#wizard .page-2 .btn_nav p').text("Warning:不能有特殊字符");
         			$('#wizard .page-2 .btn_nav p').show(200);
         			//$('#wizard .page-2 .btn_nav p').hide();
         			return false;
         		}
         		strKeyList=strKeyList+' '+subTags[ti];
         		$('#wizard .page-2 .btn_nav p').text("");
         		$('#wizard .page-2 .btn_nav p').hide();
         		//$('.step-4-keylist').append("<span class=\"label label-primary\">"+subTags[ti]+"</span>");
         	}
         	strKeyList=trimStr(strKeyList);
         	subInfo.subTags=subTags;
         }
         if(i==3){
         		subInfo.sources = new Array();
         		si=0;
         		strSources='';
         	  	$("#wizard .page-3 .sub-source").each(function(){
         	  		
			  		if($(this).prop('checked')==true){
			  			subInfo.sources[si]=$(this).val();
			  			strSources=strSources+' '+$(this).val();
			  			//alert($(this).val());
			  			si=si+1;
			  		}
			  	});
			  	strSources=trimStr(strSources);
			  	if(subInfo.sources.length==8)
			  		strSources='ALL';
			  	if(si==0)
			  		return false;
			  	//init page 4
			  	$("#wizard .page-4 .page-body div").hide();
			  	$.ajax({
				  type: 'POST',
				  url: 'rest/subscribe/addsubscribe',
				  data: JSON.stringify({"timeto":7,"source":strSources,"keylist":strKeyList,"email":subInfo.email}),
				  success: function(info){//success
				  	if(info.statues==1){
				  		if(subInfo.verify=='N'){
							$("#wizard .page-4 .verify_email").css('display','');
							$("#wizard .page-4 .verify_email strong").text(subInfo.email);
							$("#wizard .page-4 .verify_email .mail_host").text('mail.'+subInfo.email.split('@')[1]);
							$("#wizard .page-4 .verify_email .mail_host").attr('href','http://mail.'+subInfo.email.split('@')[1]);
					  	}else{
					  		$("#wizard .page-4 .all_done").css('display','');
					  	}
				  	}else{
				  		$("#wizard .page-4 .sub_error").css('display','');	
				  	}
				  },
				  error:function(XMLHttpRequest, textStatus, errorThrown){
				  		$(".sub_error").show();
				  },
				  contentType: "application/json",
				  dataType: "json"
				});
         }
      }
   });

});

var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?cf84d378683b71b3f30b09bb1c2d8143";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
