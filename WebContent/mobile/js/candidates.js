$(function(){
	$(window).scroll(function(){
		if($(this).scrollTop()>=100){
			$("body").addClass("p-scrolling");
			$("#back-to-top").fadeIn(100);
		}
		else{
			$("body").removeClass("p-scrolling");
			$("#back-to-top").fadeOut(100);
		}
	});
	//当点击跳转链接后，回到页面顶部位置
		$("#back-to-top").click(function() {
			/* $('body,html').animate({
				scrollTop: 0
			}, 100); */
			$('#bt').on('click', function(e) {
			  $.scrollTo({
				endY: 0,
				duration: 200,
				callback: function() {
				  
				}
			  });
			});
		});
	$('#job-detail').scroll(function(){
		if($(this).scrollTop()>=100){
			$(".hidejob").addClass("dark");$("#detail-title").addClass("dark");$("#job-detail-header").addClass("transp");
		}
		else{
			$(".hidejob").removeClass("dark");$("#detail-title").removeClass("dark");$("#job-detail-header").removeClass("transp");
		}
	});
	//菜单的下拉与收回
	$('.menu-btn').on('click',function(){
		$('.mm-menu').css('top','0%');
		$('.mm-top').css('top','0%');
		$('.mm-front').css('top','0%');
		$('.mm-next').css('top','0%');
		$('#header').css('display','none');
		$('.bannerPane').css('display','none');
		$('#menu').css('display','block');
	});
	$('.menu-btn2').on('click',function(){
		$('.mm-menu').css('top','-80%');
		$('.mm-top').css('top','-80%');
		$('.mm-front').css('top','-80%');
		$('.mm-next').css('top','-80%');
		$('#header').css('display','block');
		$('.bannerPane').css('display','block');
		$('#menu').css('display','none');
	});

	$.getJSON(domain+'/rest/jobs/getlist?start=0&end='+pageIndex*pageSize,function(data){
		$.each(data.joblist,function(index,item){
			for(attr in item){
				if(item[attr]==0)
					continue;
				if(item[attr]==null || item[attr]==undefined || item[attr]=="")
					delete item[attr];
			}
			item.parent='.o-team';
			$.profile(item);
		});
	});
	pageIndex++;
	//加载列表
	// $.profile({parent:'.o-team'});
	// $.profile({parent:'.o-team'});
	// $.profile({parent:'.o-team'});
	// $.profile({parent:'.o-team'});
	//关闭工作详情
	$('.hidejob').on('click',function(){
		$('#job-detail').css('right','0');$('#job-detail').css('width','0%');$('.hidejob').css('display','none');
	});
	//点击兼职类型按钮
	$('#part-load').on('click',function(){
		$('.o-team').html('');
		$(this).addClass("b-bigger");$("#all-load").removeClass("b-bigger");$("#full-load").removeClass("b-bigger");
		pageIndex=1;
		curJobType='PART';
		$.getJSON(domain+'/rest/jobs/getlist?start='+(pageIndex-1)*pageSize+'&end='+pageIndex*pageSize+'&jobtype='+curJobType+'&source='+curSource, function (data, state) {
			$.each(data.joblist,function(index,item){
					for(attr in item){
						if(item[attr]==0)
							continue;
						if(item[attr]==null || item[attr]==undefined || item[attr]=="")
							delete item[attr];
					}
					item.parent='.o-team';
					$.profile(item);
				});
				pageIndex++;
				myScroll.refresh();
		});
	});
	//点击全职类型按钮
	$('#full-load').on('click',function(){
		$('.o-team').html('');
		$(this).addClass("b-bigger");$("#all-load").removeClass("b-bigger");$("#part-load").removeClass("b-bigger");
		pageIndex=1;
		curJobType='FULL';
		$.getJSON(domain+'/rest/jobs/getlist?start='+(pageIndex-1)*pageSize+'&end='+pageIndex*pageSize+'&jobtype='+curJobType+'&source='+curSource, function (data, state) {
			$.each(data.joblist,function(index,item){
					for(attr in item){
						if(item[attr]==0)
							continue;
						if(item[attr]==null || item[attr]==undefined || item[attr]=="")
							delete item[attr];
					}
					item.parent='.o-team';
					$.profile(item);
				});
				pageIndex++;
				myScroll.refresh();
		});
	});
	//点击所有类型按钮
	$('#all-load').on('click',function(){
		$('.o-team').html('');
		$(this).addClass("b-bigger");$("#part-load").removeClass("b-bigger");$("#full-load").removeClass("b-bigger");
		pageIndex=1;
		curJobType='ALL';
		$.getJSON(domain+'/rest/jobs/getlist?start='+(pageIndex-1)*pageSize+'&end='+pageIndex*pageSize+'&jobtype='+curJobType+'&source='+curSource, function (data, state) {
			$.each(data.joblist,function(index,item){
					for(attr in item){
						if(item[attr]==0)
							continue;
						if(item[attr]==null || item[attr]==undefined || item[attr]=="")
							delete item[attr];
					}
					item.parent='.o-team';
					$.profile(item);
				});
				pageIndex++;
				myScroll.refresh();
		});
	});
});
//上拉加载参数
var data,
myScroll,
pullUpEl, pullUpOffset,
generatedCount = 0;

//获取服务器数据参数
var domain = "http://localhost:8080/bbs_ws";
var curSource='';
var curJobType='ALL';
var pageSize=10;
var pageIndex=1;

function pullUpAction () {

$.getJSON(domain+'/rest/jobs/getlist?start='+(pageIndex-1)*pageSize+'&end='+pageIndex*pageSize+'&jobtype='+curJobType+'&source='+curSource, function (data, state) {
	setTimeout(function () {
		$.each(data.joblist,function(index,item){
			for(attr in item){
				if(item[attr]==0)
					continue;
				if(item[attr]==null || item[attr]==undefined || item[attr]=="")
					delete item[attr];
			}
			item.parent='.o-team';
			$.profile(item);
		});
		pageIndex++;
		if(pageIndex%2!=0){
			$('.o-team').append('<div style="text-align:center;font-size:2px;color:rgba(29, 20, 20, 0.46);">关注公众号"聚点一族", 新工作、好工作尽在掌握</div>');
		}
		myScroll.refresh();
/* 		$.profile({parent:'.o-team'});
		$.profile({parent:'.o-team'});
		$.profile({parent:'.o-team'});
		$.profile({parent:'.o-team'});
		myScroll.refresh(); */
	}, 600);
	/* if (data && data.state == 1 && state == 'success') {
		//本地测试，为了看到加载中效果故加上定时器
		setTimeout(function () {
			$('#news-lists').append(data.data);
			myScroll.refresh();
		}, 600);
	} */
});
}

//去除浏览器默认事件
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
//domready后绑定初始化事件
document.addEventListener('DOMContentLoaded', loaded, false);

function loaded() {
pullUpEl = document.getElementById('pullUp');	
pullUpOffset = pullUpEl.offsetHeight;

/**
 * 初始化iScroll控件
 */
myScroll = new iScroll('o-person-details-pane', {
	vScrollbar : false,
	onRefresh : function () {
		if (pullUpEl.className.match('loading')) {
			pullUpEl.className = '';
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
		}
	},
	onScrollMove: function () {
		if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
			pullUpEl.className = 'flip';
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
		}
	},
	onScrollEnd: function () {
		if (pullUpEl.className.match('flip')) {
			pullUpEl.className = 'loading';
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
			pullUpAction();
		}
	}
});
}
