<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>后台管理</title>
<link rel="stylesheet" href="/static/style/css/public.css">
<!-- <link rel="icon" href="https://static.zixu.hk/zixuapp.com/app/cms/file/20180928/151607/b0bcde76da8c41be80c9c36f72eda2a8.ico" mce_href="https://static.zixu.hk/zixuapp.com/app/cms/file/20180928/151607/b0bcde76da8c41be80c9c36f72eda2a8.ico" type="image/x-icon" />
 --></head>

<body id="body">
	<header>
		<div class="headerLogo mg-t-16">
			<!--  <img src="/static/style/images/zixuwanglogo.svg" alt=""> -->
			<!-- <img src="/static/merchant/images/haotian-logo.png" alt="">  -->
			<!-- <img src="/static/user/login/images/zhifuba.png" alt=""> -->
		</div>
		<div class="gobackIndex fl">
			<!-- <a href="/main/merchant">
                <i class="iconfont icon-fanhui1 text-sm"></i>
                返回主首页
            </a> -->
		</div>
		
		
		<div class="headerRight fr">
				<!-- <div class="fl  loadingApk">

					<a href="/app/app-debug.apk">下载apk</a>

				</div>  -->

			<div class="userBox fl">

				<!-- <div>

					<a href="/app/app-debug.apk">下载apk</a>

				</div> -->

				<div class="userName ">
					<span>您好:</span> <span>${userInfo.name}</span> <i
						class="iconfont icon-pulldown"></i>
				</div>


				<div class="userSetting" style="display: none">
					<ul>
						<li class=""><a href="/user/loginPage">安全退出</a></li>
					</ul>
				</div>
			</div>

		</div>
		<div class="fr  loadingApk">

					<a href="/app/WMpayer.apk">下载APP</a>

				</div> 
	</header>
	<nav ng-app="menu" ng-view>
		<div class="navLeft fl">
			<ul>
				<li ng-repeat="item in menuObj"
					class="{{(item.select) ? 'selected' : ''}}"
					ng-click="titleSetSecond(item.title)"><a
					ng-href="{{ (item.url != null) ? '#'+item.url : 'javascript:;' }}">
						<i class="iconfont icon-{{item.icon}}"></i> <span>{{item.title}}</span>
				</a></li>
			</ul>

		</div>

		<div class="navRight fl">

			<ul>
				<li ng-repeat="item in secondObj"
					class="{{(item.select) ? 'selected' : ''}}"><a
					href="{{ (item.url != null) ? '#'+item.url : 'javascript:;' }}">{{item.title}}</a>
				</li>
			</ul>

		</div>
	</nav>

	<main id="app" ng-app="app" ng-view> </main>

</body>

</html>
<script src="https://static.zixu.hk/angular/1.4.6_angular.js"></script>
<script src="https://static.zixu.hk/angular/1.3.13_angular-route.js"></script>
<script src="/static/common/common.js"></script>
 <script src="/static/common/qrcode.js"></script>
<script src="/static/common/llqrcode.js"></script>
<script src="/static/main/merchant/template/index.js"></script>