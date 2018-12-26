var isShowExitBox=false;
document.getElementsByClassName('headerRight')[0].onclick = function () {
    var dom = document.getElementsByClassName('userSetting')[0];
    var state = dom.style.display;
    if (state == "" || state == "block") {
        dom.style.display = "none";
    } else {
        dom.style.display = "block";
    }
    if(isShowExitBox){
    	isShowExitBox=false;
    }
}

document.getElementById('body').onclick = function () {
    var dom = document.getElementsByClassName('userSetting')[0];
    var state = dom.style.display;
    
    if(isShowExitBox){
    	 dom.style.display = "none";
    	 isShowExitBox=false;
    }else{
    	 isShowExitBox=true;
    }
      
}

var menuApp = angular.module('menu', []);
String.prototype.parseURL = function () {
    var url = this.toString()
    var a = document.createElement('a');
    a.href = url;
    return {
        source: url,
        protocol: a.protocol.replace(':', ''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
        hash: a.hash.replace('#', ''),
        path: a.pathname.replace(/^([^\/])/, '/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
        segments: a.pathname.replace(/^\//, '').split('/'),
        params: (function () {
            var ret = {};
            var seg = a.search.replace(/^\?/, '').split('&').filter(function (v, i) {
                if (v !== '' && v.indexOf('=')) {
                    return true;
                }
            });
            seg.forEach(function (element, index) {
                var idx = element.indexOf('=');
                var key = element.substring(0, idx);
                var val = element.substring(idx + 1);
                ret[key] = val;
            });
            return ret;
        })()
    };
}
menuApp.run(['$rootScope', '$location', '$sce', '$log', function ($rootScope, $location, $sce, $log) {
    $rootScope.menuObj = [
        {
            title: "数据统计",
            icon: "data",
            url: "/index",
            second: [
                {
                    title: "数据统计",
                    url: "/index"
                }
            ]
        },
        
        {
            title: "订单管理",
            icon: "order",
            url: "/order/list",
            second: [
                {
                    title: "订单管理",
                    url: "/order/list"
                }
            ]
        },
        
        {
            title: "商品管理",
            icon: "integral2",
            url: "/goods/list",
            second: [
                {
                    title: "商品管理",
                    url: "/goods/list"
                }
            ]
        },
        {
            title: "套餐列表",
            icon: "Theoldmember",
            url: "/setMeal/list",
            second: [
                {
                    title: "套餐列表",
                    url: "/setMeal/list"
                }
            ]
        },
        
        {
            title: "无匹配订单",
            icon: "zanwuyituichudingdan",
            url: "/matching/list",
            second: [
                {
                    title: "无匹配订单",
                    url: "/matching/list"
                }
            ]
        },
        
        
        {
            title: "消费明细",
            icon: "remainingbalance",
            url: "/rechargeUserDetailed/list",
            second: [
                {
                    title: "消费明细",
                    url: "/rechargeUserDetailed/list"
                }
            ]
        },
        
        {
            title: "资金变动明细",
            icon: "finance",
            url: "/changeDetail/list",
            second: [
                {
                    title: "资金变动明细",
                    url: "/changeDetail/list"
                }
            ]
        },
        
        {
            title: "账号设置",
            icon: "tubiao01",
            url: "/setting/baseInfo",
            second: [
                {
                    title: "基本信息",
                    url: "/setting/baseInfo"
                },
                {
                    title: "修改密码",
                    url: "/setting/modifyPassword"
                },{
                    title: "API接口参数",
                    url: "/setting/APIParam"
                },{
                    title: "API接口信息",
                    url: "/setting/APIInfo"
                },{
                    title: "账号信息",
                    url: "/setting/account"
                }
            ]
        }
    ];

    $rootScope.secondObj = [];
    $rootScope.secondObj = $rootScope.menuObj[0].second;
    $rootScope.titleSetSecond = function (title) {
        for (var i = 0; i < $rootScope.menuObj.length; i++) {
            $rootScope.menuObj[i].select = false;
            if ($rootScope.menuObj[i].title == title) {
                $rootScope.secondObj = $rootScope.menuObj[i].second;
                $rootScope.menuObj[i].select = true;
            }
        }
    }
    $rootScope.$on('$locationChangeSuccess', function (evt, current, previous) {
        var nowRoute = window.location.hash;
        var flag = false;
        function r() {
            for (var i = 0; i < $rootScope.menuObj.length; i++) {
                var root = $rootScope.menuObj[i];
                $rootScope.menuObj[i].select = false;
                for (var j = 0; j < root.second.length; j++) {
                    var second = root.second[j];
                    var url = "#" + second.url;
                    $rootScope.menuObj[i].second[j].select = false;
                    if (nowRoute == url) {
                        $rootScope.menuObj[i].select = true;
                        $rootScope.menuObj[i].second[j].select = true;
                        $rootScope.secondObj = root.second;
                        localStorage.setItem("menuTempMenuObj", JSON.stringify($rootScope.menuObj));
                        localStorage.setItem("menuTempMenusecondObj", JSON.stringify($rootScope.secondObj));
                        flag = true;
                    }
                }
            }
        };
        r();
        if (!flag) {
            nowRoute = '#' + arguments[2].parseURL().hash;
            r();
            if (!flag) {
                $rootScope.menuObj = JSON.parse(localStorage.getItem("menuTempMenuObj"));
                $rootScope.secondObj = JSON.parse(localStorage.getItem("menuTempMenusecondObj"));
            }
        }
    });
}]);

var app = angular.module('app', ['ngRoute']);
app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    
    	//首页
        .when('/index', {
            templateUrl: "/static/main/merchant/template/index/index.html",
            controller: "index"
        })
        //账号设置-基本信息
        .when('/setting/baseInfo', {
            templateUrl: "/static/main/merchant/template/setting/baseInfo.html",
            controller: "settingBaseInfo"
        })
        //账号设置-修改密码
        .when('/setting/modifyPassword', {
            templateUrl: "/static/main/merchant/template/setting/modifyPassword.html",
            controller: "settingModifyPassword"
        })
         //账号设置-API接口参数
        .when('/setting/APIParam', {
            templateUrl: "/static/main/merchant/template/setting/APIParam.html",
            controller: "settingAPIParam"
        })
         //账号设置-API接口信息
        .when('/setting/APIInfo', {
            templateUrl: "/static/main/merchant/template/setting/APIInfo.html",
            controller: "settingAPIInfo"
        })
         //账号设置-账号信息
        .when('/setting/account', {
            templateUrl: "/static/main/merchant/template/setting/account.html",
            controller: "settingAccount"
        })
        //商品管理-商品列表
        .when('/goods/list', {
            templateUrl: "/static/main/merchant/template/goods/list.html",
            controller: "goodsList"
        })
        //订单管理-订单列表
        .when('/order/list', {
            templateUrl: "/static/main/merchant/template/order/list.html",
            controller: "orderList"
        })
        //资金变动明细-列表
        .when('/changeDetail/list', {
            templateUrl: "/static/main/merchant/template/changeDetail/list.html",
            controller: "changeDetailList"
        })
        //无匹配订单-列表
        .when('/matching/list', {
            templateUrl: "/static/main/merchant/template/matching/list.html",
            controller: "matchingList"
        })
        //套餐列表-列表
        .when('/setMeal/list', {
            templateUrl: "/static/main/merchant/template/setMeal/list.html",
            controller: "setMealList"
        })
        //套餐列表-详情
        .when('/setMeal/detail/:id', {
            templateUrl: "/static/main/merchant/template/setMeal/detail.html",
            controller: "setMealDetail"
        })
        //消费明细-列表
        .when('/rechargeUserDetailed/list', {
            templateUrl: "/static/main/merchant/template/rechargeUserDetailed/list.html",
            controller: "rechargeUserDetailedList"
        })
         //商品管理-二维码列表
        .when('/goods/qrCodeList/:id', {
            templateUrl: "/static/main/merchant/template/goods/qrCodeList.html",
            controller: "goodsQrCodeList"
        })
        .otherwise({ redirectTo: '/index' })
}]);

app.run(['$rootScope', '$location', '$sce', function ($rootScope, $location, $sce) {
    $rootScope.$on('$routeChangeStart', function (evt, next, current) {
    	showTips.showShadeBg(false);
    	showTips.showLoading(false);
        showTips.showLoading(true);        
    });
    $rootScope.$on('$routeChangeSuccess', function (evt, current, previous) {
        showTips.showLoading(false);
        showTips.showShadeBg(false);
    });
}]);


/** 暂无权限 */
//app.controller('withoutPermission', ['$scope', '$rootScope', function ($scope, $rootScope) {
//
//
//}]);

/** 商品管理-二维码列表 */
app.controller('goodsQrCodeList', ['$scope', '$rootScope','$routeParams', function ($scope, $rootScope,$routeParams) {

	
	var id = $routeParams.id;
    if (id == '' || id == undefined || id == null) {
        return;
    }
	
		
	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/user/commodity/findByQrcodeList", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data == undefined || data.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data;
                $scope.totalPage = 1;
                $scope.currentPage = 1;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};
    $scope.postDataObj.pageNum = $scope.currentPage;
    $scope.postDataObj.commodityID=id;
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    
    
    /* 删除 */
    $scope.delete = function (item) {

        showTips.showAlert('确定删除该二维码吗?', function () {
            gatewayHttpRequest("/user/commodity/deleteQrcode", { commodityID: item.id }, function (res) {
                if (res.resultCode) {
                    showTips.showTopToast('success', '删除成功,页面即将刷新...', 2000);
                    setTimeout(function(){
                     window.location.reload();
       				 return;
                    },1500);       
                }

            });
        }, function () {

        });  
    }
       	
}]);






/** 消费明细-列表 */
app.controller('rechargeUserDetailedList', ['$scope', '$rootScope','$filter', function ($scope, $rootScope,$filter) {

	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/user/recharge/user/detailed", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data.list == undefined || data.list.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data.list;
                $scope.totalPage = data.totalPage;
                $scope.currentPage = data.pageNum;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};

    $scope.postDataObj.pageNum = $scope.currentPage;
    $scope.postDataObj.fee = '0';
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    
    
    /*金额 检查*/
    $scope.checkFee=function(obj){
    	 var value= checkFeeInputValidFunc(obj);
    	 
    	 if(value.length>7){
    		 showTips.showTopToast('error', '金额过大', 1500);
	         return;
    	 } 	 
         $scope.searchKey =value; 
    }
    
    
    $scope.startTime;
    $scope.endTime;

    /* 开始时间 */
    $scope.onStartTimeChange = function () {

        if ($scope.startTime == undefined || $scope.startTime == null || $scope.startTime == '') {
            return;
        }
        var startTime = $filter('date')($scope.startTime, 'yyyy-MM-dd');

        $scope.postDataObj.startDate=startTime;
    }

    /* 结束时间 */
    $scope.onEndTimeChange = function () {

        if ($scope.endTime == undefined || $scope.endTime == null || $scope.endTime == '') {
            return;
        }
        var endTime = $filter('date')($scope.endTime, 'yyyy-MM-dd');     
        $scope.postDataObj.endDate=endTime;
    }
    
    
 // 类型选择数组
    $scope.changeTypeList = [
    	
    	{
            'name': '全部',
            'state': 'ALL'
        },

        {
            'name': '充值',
            'state': '1'
        },
        {
            'name': '消费',
            'state': '2'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange = false;
    // 是否显示类型下拉框
    $scope.showTypechange = function () {
        $scope.isShowTypeChange = !$scope.isShowTypeChange;
        if(showClick){
        	showClick=false;
        } 
        
    }
    
    /* 关闭所有弹框 */
    $scope.closeAllBox=function(){
    	
    	if(showClick){
    		$scope.isShowTypeChange=false;
    		showClick = false;
    	}else{
    		showClick = true;
    	} 	
 	
    }
      
    // 类型
    $scope.typeChangeObj;

    /* 类型选择 */
    $scope.typeChoise = function (item) {
        $scope.typeChangeObj = item;
        $scope.isShowTypeChange = false;
        
        if($scope.typeChangeObj.state=='ALL'){
       	
        	if ($scope.postDataObj.state != undefined) {
                delete $scope.postDataObj.state;
            }         
        	
        }else{       	
        	$scope.postDataObj.state=$scope.typeChangeObj.state;       	
        }           
    }
    
    // 搜索
    $scope.searchKey;

    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
    	// 搜索关键词
//        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
//            if ($scope.postDataObj.fee != undefined) {
//                delete $scope.postDataObj.fee;
//            }
//            $scope.postDataObj.fee = '0';
//        } else {
//            $scope.currentPage = 1;
//            $scope.postDataObj.fee = $scope.searchKey*100 + '';         
//        }
    
        getDataListForCondition($scope.postDataObj);
    }
    	
}]);




/**
 * 套餐列表-详情
 */
app.controller('setMealDetail', ['$scope', '$rootScope','$routeParams', '$interval',function ($scope, $rootScope,$routeParams,$interval) {

	var id = $routeParams.id;
    if (id == '' || id == undefined || id == null) {
        return;
    }
    
    
 // 类型选择数组
    $scope.changeTypeList = [

    	{
			name:'日',
			type:'1'
		},
		{
			name:'月',
			type:'2'
		},
		{
			name:'年',
			type:'3'
		}
    ];
    
    gatewayHttpRequest("/user/setMeal/info", {id:id}, function (res) {
		var data=res.data;
		$scope.detailObj=data;
		
		for(var j =0;j< $scope.changeTypeList.length;j++){
    		if($scope.detailObj.type==$scope.changeTypeList[j].type){
    			$scope.detailObj.timeStr=$scope.detailObj.number+' '+$scope.changeTypeList[j].name;
        	}
    	} 	
		
        $scope.$apply();
    });
    
    var orderQuery; 
	$scope.isShowRechargeBox=false;
	$scope.closeRechargeBox=function(){
		showTips.showShadeBg(false);	
		$scope.isShowRechargeBox=false;
		//$scope.isShowQrCode=false;
		$scope.appObj={};
		$scope.isNeedToRechangeFee='';
		$scope.payTypeText='';
		$interval.cancel(orderQuery);
	}
	
	/*充值弹框*/
	$scope.showRechargeBox=function(){
		
		 if (id == undefined ||id == null || id == '') {
	   		  showTips.showTopToast('error', '套餐id不能为空', 1500);
	            return;
	     }
		 
		 
		 showTips.showAlert('是否使用账户余额支付?', function () {
			 
	            gatewayHttpRequest("/user/setMealPurchase/paySetMeat", { setMeatID: id }, function (res) {
	                if (res.resultCode) {
	                    showTips.showTopToast('success', '支付成功,页面即将跳转', 1500);
	                    
	                    setTimeout(function(){
	                    window.location.hash='/setting/account';
	       				 return;
	                    },1500);       
	                }

	            });
	            
	        }, function () {

	        	gatewayHttpRequest("/user/setMealPurchase/pay", {setMealID:id}, function (res) {
	 			   
	 			   var data=res.data;
	 			   console.log(data);
	 			   $scope.isNeedToRechangeFee=data.fee/100;
	 			   
	 			   if(data.payType=='wechat'){
	 				   $scope.payTypeText='微信';
	 			   }else{
	 				   $scope.payTypeText='支付宝';
	 			   }
	 			   
	 			  //$scope.qrcodeUrl=data.qrcodeUrl;
	 			   
	 			  document.getElementById("qrCode").innerHTML = "";
	            var qrcode = new QRCode(document.getElementById("qrCode"), {
	                width: 200,
	                height: 200
	            });
	            
	                qrcode.makeCode(data.qrcodeUrl);
	 			   
	 			   //$scope.isShowQrCode=true;
	 			   $scope.isShowRechargeBox=true;
	 			   showTips.showShadeBg(true);
	 			   
	 	           $scope.$apply();
	 	            
	 	           $interval.cancel(orderQuery);

	             orderQuery = $interval(function () {

	                 gatewayHttpRequest("/user/setMealPurchase/query", { outTradeNo: data.outTradeNo }, function (res) {
//	                 	console.log('查单中。。。');
//	                     console.log(res.data);
	                     if (res.data) {
	                     	//console.log('支付成功了');
	                         showTips.showTopToast('success', '支付成功，页面即将跳转',1500);
	                         $scope.closeRechargeBox();
	                         $scope.$apply();
	                         setTimeout(function(){
//	                         	window.location.reload();
	                         	window.location.hash='/setting/account';
	                         	return;
	                         },1500);
	                     }

	                 }, function () {

	                 }, function () {

	                 }, false);
	             }, 3000);
	 	            
	 	        });
	        });  
		
	}
	
	 //页面切换时，取消定时器
   $rootScope.$on('$routeChangeSuccess', function (evt, current, previous) {
   	$interval.cancel(orderQuery);
   	orderQuery=undefined;  	
   });
 
}]);

/**
 * 套餐列表-列表
 */
app.controller('setMealList', ['$scope', '$rootScope', function ($scope, $rootScope) {

	// 类型选择数组
    $scope.changeTypeList = [

    	{
			name:'日',
			type:'1'
		},
		{
			name:'月',
			type:'2'
		},
		{
			name:'年',
			type:'3'
		}
    ];
	
	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;

    /* 请求方法 */
    var getDataListForCondition = function (postData) {
        gatewayHttpRequest('/user/setMeal/list', postData, function (res) {
            var data = res.data;

            console.log(data);
            if (data[0] == undefined || data.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data;
                $scope.totalPage = 1;
                $scope.currentPage = 1;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
                
                for(var i =0;i<$scope.rowList.length;i++){
                	for(var j =0;j< $scope.changeTypeList.length;j++){
                		if($scope.rowList[i].type==$scope.changeTypeList[j].type){
                			$scope.rowList[i].timeStr=$scope.rowList[i].number+' '+$scope.changeTypeList[j].name;
                    	}
                	} 	
                }
            }
            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj={};
    $scope.postDataObj.pageNum = $scope.currentPage;
    getDataListForCondition($scope.postDataObj);


    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);
        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 跳转模板
    $scope.skipToModule=function(item){
    	window.location.hash='/setMeal/detail/'+item.id;
    	return;
    }

}]);


/** 无匹配订单-列表 */
app.controller('matchingList', ['$scope', '$rootScope','$filter', function ($scope, $rootScope,$filter) {

	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/user/orderList/five/matching", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data.list == undefined || data.list.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data.list;
                $scope.totalPage = data.totalPage;
                $scope.currentPage = data.pageNum;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};

    $scope.postDataObj.pageNum = $scope.currentPage;
    $scope.postDataObj.fee = '0';
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    
    
    /*金额 检查*/
    $scope.checkFee=function(obj){
    	 var value= checkFeeInputValidFunc(obj);
    	 
    	 if(value.length>7){
    		 showTips.showTopToast('error', '金额过大', 1500);
	         return;
    	 } 	 
         $scope.searchKey =value; 
    }
    
    
    $scope.startTime;
    $scope.endTime;

    /* 开始时间 */
    $scope.onStartTimeChange = function () {

        if ($scope.startTime == undefined || $scope.startTime == null || $scope.startTime == '') {
            return;
        }
        var startTime = $filter('date')($scope.startTime, 'yyyy-MM-dd');

        $scope.postDataObj.startDate=startTime;
    }

    /* 结束时间 */
    $scope.onEndTimeChange = function () {

        if ($scope.endTime == undefined || $scope.endTime == null || $scope.endTime == '') {
            return;
        }
        var endTime = $filter('date')($scope.endTime, 'yyyy-MM-dd');     
        $scope.postDataObj.endDate=endTime;
    }
    
    
 // 类型选择数组
    $scope.changeTypeList = [

        {
            'name': '支付宝+微信',
            'type': 'ALL'
        },
        {
            'name': '支付宝',
            'type': 'alipay'
        },
        {
            'name': '微信',
            'type': 'wechat'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange = false;
    // 是否显示类型下拉框
    $scope.showTypechange = function () {
        $scope.isShowTypeChange = !$scope.isShowTypeChange;
        if(showClick){
        	showClick=false;
        } 
        
    }
    
    /* 关闭所有弹框 */
    $scope.closeAllBox=function(){
    	
    	if(showClick){
    		$scope.isShowTypeChange=false;
    		showClick = false;
    	}else{
    		showClick = true;
    	} 	
 	
    }
      
    // 类型
    $scope.typeChangeObj;

    /* 类型选择 */
    $scope.typeChoise = function (item) {
        $scope.typeChangeObj = item;
        $scope.isShowTypeChange = false;
        
        if($scope.typeChangeObj.type=='ALL'){
       	
        	if ($scope.postDataObj.type != undefined) {
                delete $scope.postDataObj.type;
            }         
        	
        }else{       	
        	$scope.postDataObj.type=$scope.typeChangeObj.type;       	
        }           
    }
    
    // 搜索
    $scope.searchKey;

    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
    	// 搜索关键词
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.fee != undefined) {
                delete $scope.postDataObj.fee;
            }
            $scope.postDataObj.fee = '0';
        } else {
            $scope.currentPage = 1;
            $scope.postDataObj.fee = $scope.searchKey*100 + '';         
        }
    
        getDataListForCondition($scope.postDataObj);
    }
    	
}]);




/** 资金变动明细-列表 */
app.controller('changeDetailList', ['$scope', '$rootScope','$filter', function ($scope, $rootScope,$filter) {

	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/user/orderList/changeDetailList", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data.list == undefined || data.list.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data.list;
                $scope.totalPage = data.totalPage;
                $scope.currentPage = data.pageNum;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};

    $scope.postDataObj.pageNum = $scope.currentPage;
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    
    
    $scope.startTime;
    $scope.endTime;

    /* 开始时间 */
    $scope.onStartTimeChange = function () {

        if ($scope.startTime == undefined || $scope.startTime == null || $scope.startTime == '') {
            return;
        }
        var startTime = $filter('date')($scope.startTime, 'yyyy-MM-dd');

        $scope.postDataObj.startDate=startTime;
    }

    /* 结束时间 */
    $scope.onEndTimeChange = function () {

        if ($scope.endTime == undefined || $scope.endTime == null || $scope.endTime == '') {
            return;
        }
        var endTime = $filter('date')($scope.endTime, 'yyyy-MM-dd');     
        $scope.postDataObj.endDate=endTime;
    }
    
    
 

    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;	
        getDataListForCondition($scope.postDataObj);
    }
    	
}]);




/** 订单管理-订单列表 */
app.controller('orderList', ['$scope', '$rootScope','$filter', function ($scope, $rootScope,$filter) {

	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {
    	if($scope.systemIDKey != undefined){
    		conditionObj.systemID = $scope.systemIDKey;
    	}
        gatewayHttpRequest("/user/orderList/list", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data.list == undefined || data.list.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data.list;
                $scope.totalPage = data.totalPage;
                $scope.currentPage = data.pageNum;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};

    $scope.postDataObj.pageNum = $scope.currentPage;
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    
    
    $scope.startTime;
    $scope.endTime;

    /* 开始时间 */
    $scope.onStartTimeChange = function () {

        if ($scope.startTime == undefined || $scope.startTime == null || $scope.startTime == '') {
            return;
        }
        var startTime = $filter('date')($scope.startTime, 'yyyy-MM-dd');

        $scope.postDataObj.startDate=startTime;
    }

    /* 结束时间 */
    $scope.onEndTimeChange = function () {

        if ($scope.endTime == undefined || $scope.endTime == null || $scope.endTime == '') {
            return;
        }
        var endTime = $filter('date')($scope.endTime, 'yyyy-MM-dd');     
        $scope.postDataObj.endDate=endTime;
    }
    
    
 // 类型选择数组
    $scope.changeTypeList = [

        {
            'name': '支付宝+微信',
            'type': 'ALL'
        },
        {
            'name': '支付宝',
            'type': 'alipay'
        },
        {
            'name': '微信',
            'type': 'wechat'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange = false;
    // 是否显示类型下拉框
    $scope.showTypechange = function () {
        $scope.isShowTypeChange = !$scope.isShowTypeChange;
        if(showClick){
        	showClick=false;
        } 
        
    }
    
    /* 关闭所有弹框 */
    $scope.closeAllBox=function(){
    	
    	if(showClick){
    		$scope.isShowTypeChange=false;
    		showClick = false;
    	}else{
    		showClick = true;
    	} 	
 	
    }
      
    // 类型
    $scope.typeChangeObj;

    /* 类型选择 */
    $scope.typeChoise = function (item) {
        $scope.typeChangeObj = item;
        $scope.isShowTypeChange = false;
        
        if($scope.typeChangeObj.type=='ALL'){
       	
        	if ($scope.postDataObj.type != undefined) {
                delete $scope.postDataObj.type;
            }         
        	
        }else{       	
        	$scope.postDataObj.type=$scope.typeChangeObj.type;       	
        }           
    }
    
    // 搜索
    $scope.searchKey;

    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
    	// 搜索关键词
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.outTradeNo != undefined) {
                delete $scope.postDataObj.outTradeNo;
            }         
        } else {
            $scope.currentPage = 1;
            $scope.postDataObj.outTradeNo = $scope.searchKey + '';         
        }
    
        getDataListForCondition($scope.postDataObj);
    }
    
    
    
    /* 删除 */
    $scope.retransmission = function (item) {
    	
    	
    	 gatewayHttpRequest("/user/orderList/retransmission", { orderID: item.id }, function (res) {
           if (res.resultCode) {
               showTips.showTopToast('success', '发送成功,页面即将刷新...', 1500);
               setTimeout(function(){
                   window.location.reload();
            	   //getDataListForCondition($scope.postDataObj);
  				 return;
               },1500);       
           }

       });
    	
    	
    	

//        showTips.showAlert('确定删除 ' + item.name + ' 的吗?', function () {
//            gatewayHttpRequest("/user/commodity/delete", { id: item.id }, function (res) {
//                if (res.resultCode) {
//                    showTips.showTopToast('success', '删除成功,页面即将刷新...', 2000);
//                    setTimeout(function(){
//                     window.location.reload();
//       				 return;
//                    },1500);       
//                }
//
//            });
//        }, function () {
//
//        });  
    }
    
    
    
    	
}]);




/** 首页*/
app.controller('index', ['$scope', '$rootScope', function ($scope, $rootScope) {

	gatewayHttpRequest("/user/orderList/index/data", {}, function (res) {
        var data = res.data;
        $scope.statistics=data.statistics;
        $scope.list=data.list;
        $scope.$apply();
    });
		
	/*详情*/
	gatewayHttpRequest("/user/recharge/consumptionDetail", {}, function (res) {
		 
		 var data=res.data;
		 console.log(data);
		 if(data.expireDate!=undefined&&data.expireDate!=null&&data.expireDate!='null'){
			 $scope.expireDate=data.expireDate;
		 }
		 if(data.procedures!=undefined&&data.procedures!=null&&data.procedures!='null'){
			 $scope.procedures=data.procedures;
		 }	 
		 if(data.fee!=undefined&&data.fee!=null&&data.fee!='null'){
			 $scope.fee=data.fee;
		 }	
		 
		 if(!data.minFee){
			 showTips.showAlert('该账号余额已达到后台设定临界值，请及时充值！', function () {
		           
		        }, function () {

		        });  
		 }
		 
        $scope.$apply();
    });
	
	
	
}]);

/** 商品管理-商品列表*/
app.controller('goodsList', ['$scope', '$rootScope', function ($scope, $rootScope) {

	 // 当前页
    $scope.currentPage = 1;
    // 总页数
    $scope.totalPage;
    // 页数列表
    $scope.totalPageList;
    // 页码输入
    $scope.pageInput;
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/user/commodity/page", conditionObj, function (res) {
            var data = res.data;

            console.log(data);
            if (data.list == undefined || data.list.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data.list;
                $scope.totalPage = data.totalPage;
                $scope.currentPage = data.pageNum;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }

            $scope.$apply();
        });
    }

    // 第一次加载数据
    $scope.postDataObj = {};

    $scope.postDataObj.pageNum = $scope.currentPage;
    getDataListForCondition($scope.postDataObj);

    // 选择页数
    $scope.onPageClick = function (page) {

        $scope.currentPage = page;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 首页
    $scope.indexPage = function () {
        $scope.currentPage = 1;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);

    }
    // 尾页
    $scope.lastPage = function () {
        $scope.currentPage = $scope.totalPage;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }
    // 上一页
    $scope.prePage = function () {

        if ($scope.currentPage > 1) {
            $scope.currentPage = $scope.currentPage - 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是第一页了', 1500);
        }


    }
    // 下一页
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.totalPage) {
            $scope.currentPage = $scope.currentPage + 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);

        } else {
            showTips.showTopToast('error', '已经是最后一页了', 1500);
        }
    }

    // 跳转
    $scope.skipPage = function () {

        if ($scope.pageInput == undefined || $scope.pageInput == null) {
            showTips.showTopToast('error', '请输入页码', 1500);
            return;
        }
        if ($scope.pageInput < 1) {
            showTips.showTopToast('error', '最小页码为1页', 1500);
            return;
        }

        if ($scope.pageInput > $scope.totalPage) {
            showTips.showTopToast('error', '最大页码为' + $scope.totalPage + '页', 1500);
            return;
        }
        $scope.currentPage = $scope.pageInput;
        $scope.postDataObj.pageNum = $scope.currentPage;
        getDataListForCondition($scope.postDataObj);
    }

    // 搜索
    $scope.searchKey;

    /* 搜索 */
    $scope.doSearch = function () {

    	// 搜索关键词
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.name != undefined) {
                delete $scope.postDataObj.name;
            }

            $scope.currentPage = 1;
            $scope.postDataObj.pageNum = $scope.currentPage;
            getDataListForCondition($scope.postDataObj);
        } else {

            $scope.currentPage = 1;
            $scope.postDataObj.name = $scope.searchKey + '';
            getDataListForCondition($scope.postDataObj);
        }

    }
    
    
    $scope.isShowAddGoodsItem=false;
    $scope.showAddGoodsItem=function(){
    	$scope.isShowAddGoodsItem=true;
    }
    
    $scope.closeAddGoodsItem=function(){
    	$scope.isShowAddGoodsItem=false;
    	$scope.addName='';
    	$scope.addFee='';
    }
    
    
    $scope.addGoodsFunc=function(){
    	
    	if($scope.addName==undefined||$scope.addName==null||$scope.addName==''){
  		 	 showTips.showTopToast('error', '请输入商品名称', 1500);
 		     return;
   	    }
    	
    	if($scope.addFee==undefined||$scope.addFee==null||$scope.addFee==''){
 		 	 showTips.showTopToast('error', '请输入商品价格', 1500);
		     return;
  	    }
    	
    	var name=$scope.addName;
    	var fee=$scope.addFee*100+'';
    	
 	   gatewayHttpRequest("/user/commodity/save", {name:name,fee:fee}, function (res) {
 		   
 		   if(res.resultCode){
 			  showTips.showTopToast('success', '添加成功，页面即将刷新。。。', 1500);
 			  
 			  setTimeout(function(){
 				 $scope.closeAddGoodsItem();
 				 window.location.reload();
 			  },1500);			  
 		   }
            
            $scope.$apply();
        },function(){
        	$scope.closeAddGoodsItem();
        });	
	 
    }
    
    $scope.checkAddFee=function(obj){
    	var value= checkFeeInputValidFunc(obj);
        $scope.addFee =value;
    }
    
    
    
    $scope.isShowUpLoadQrCodeBox=false;
    
    $scope.closeUpLoadQrCodeBox=function(){
    	$scope.qrCodePicUrl='';
    	$scope.qrCodeObj={};
    	$scope.itemClickObj={};
    	showTips.showShadeBg(false);
    	$scope.isShowUpLoadQrCodeBox=false;
    	$scope.qrCodePicUrlList=[];
    }
    
    /*金额 检查*/
    $scope.checkFee=function(obj){
    	 var value= checkFeeInputValidFunc(obj);
         $scope.fee =value; 
    }
    
    var getObjectURL = function(file){
        var url = null ;
        if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
        } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
         } else if (window.webkitURL!=undefined) { // webkit or chrome
         url = window.webkitURL.createObjectURL(file) ;
        }
         return url ;
       }  
    
    $scope.qrCodeObj={};        
    $scope.qrCodePicUrlList=[];    
    $scope.upLoadFailureCount=0;
    $scope.upLoadFile=0;
    
    
//    $scope.changeImgReturn = function(httpRequestObj){
//    	
//    	if (httpRequestObj.readyState === 4 && httpRequestObj.status === 200) {
//            var data = httpRequestObj.responseText;
//            var res = JSON.parse(data)
//            if (!res.resultCode) {
//                showTips.showLoading(false);
//                showTips.showTopToast('error', '图片上传失败，' + res.errCode + ' ' + res.errCodeDes);
//                return;
//            }
//        
//    	}    	
//    	var data = JSON.parse(res.data);
//    	if(!data.state){
//          	 $scope.upLoadFailureCount++;
//         }else{
//          	 $scope.qrCodePicUrlList.push(data);
//         }
//         if($scope.upLoadFile == ($scope.qrCodePicUrlList.length+$scope.upLoadFailureCount)){
//        	 showTips.showTopToast('success', '总共上传图片' + $scope.qrCodePicUrlList.length + ' 张，其中上传失败' + $scope.upLoadFailureCount + '张');
//        	 //showTips.showLoading(false);
//         }
//         
//         $scope.$apply(); 
//  	
//    }
    
    
    $scope.deletePicItem=function(index){
    	 $scope.qrCodePicUrlList.splice(index, 1);
    }
    
    
    $scope.choisePicFunc=function(obj){
  	
    	if (obj.value.length <= 0) {
            return;
        }
    	$scope.qrCodePicUrlList=[];
	  	$scope.upLoadFile = obj.files.length;
	  	showTips.showLoading(true);
	  	showTips.showShadeBg(true);
    	$scope.isShowUpLoadQrCodeBox=true;
    	$scope.$apply(); 
    	
    	for(var i=0;i<obj.files.length;i++){
    		var file=obj.files[i];
	  		var fileName=file.name;
	  		var suffixIndex=fileName.lastIndexOf(".");  
	  	    var suffix=fileName.substring(suffixIndex+1).toUpperCase(); 
	  	    
	  	    if(suffix!="BMP"&&suffix!="JPG"&&suffix!="JPEG"&&suffix!="PNG"&&suffix!="GIF"){  
		  	    showTips.showTopToast('error', '请上传图片（格式BMP、JPG、JPEG、PNG、GIF等）!');
		  	    showTips.showLoading(false);
			  	showTips.showShadeBg(false);
		    	$scope.isShowUpLoadQrCodeBox=false;
		    	$scope.$apply(); 
		  	    return;
	  	     }    		
    	}
    		
	  	for(var i=0;i<obj.files.length;i++){	
	  		var file=obj.files[i];	
	  		upLoadFile(file,'/user/file/upload',function(res){
	  			console.log(res);
	  			if (!res.resultCode) {
	                showTips.showLoading(false);
	                showTips.showTopToast('error', '图片上传失败，' + res.errCode + ' ' + res.errCodeDes);
	                showTips.showLoading(false);
	                return;
	            }  		
	  			var data = JSON.parse(res.data);
	  	    	if(!data.state){
	  	          	 $scope.upLoadFailureCount++;
	  	         }else{
	  	          	 $scope.qrCodePicUrlList.push(data);
	  	         }	  	    	
	  	    	 if($scope.upLoadFile == ($scope.qrCodePicUrlList.length+$scope.upLoadFailureCount)){
	  	        	 showTips.showTopToast('success', '总共上传图片' + $scope.qrCodePicUrlList.length + ' 张，其中上传失败' + $scope.upLoadFailureCount + '张');
	  	        	 showTips.showLoading(false);
	  	         }
 	
	  			$scope.$apply(); 	  			
	  		});
	    }
  	
    	/*if (obj.value.length <= 0) {
              return;
        }
    	$scope.qrCodePicUrlList=[];
    	//showTips.showLoading(true);
		showTips.showShadeBg(true);
    	$scope.isShowUpLoadQrCodeBox=true;
    	//$scope.$apply(); 
    	$scope.upLoadFile = obj.files.length;
    	for(var i=0;i<obj.files.length;i++){	
    		var file=obj.files[i];
    		var dataForm = new FormData();
    		dataForm.append('file', file);

    	    var httpRequestObj = new XMLHttpRequest();    
    	    httpRequestObj.open("POST", '/user/file/upload',false);
    	    httpRequestObj.send(dataForm);
    	    httpRequestObj.onreadystatechange = $scope.changeImgReturn(httpRequestObj);
    		
        }*/
   	
    }
    	//-------------------------
    	
    	/* 图片上传 */
//        showTips.showLoading(true);
//        upLoadFile(obj, '/file/upload', function (res) {
//            if (!res.resultCode) {
//                showTips.showLoading(false);
//                showTips.showTopToast('error', '图片上传失败，' + res.errCode + ' ' + res.errCodeDes);
//                return;
//            }
//            showTips.showLoading(false);
//            alert(1111111111);
//        });
    	
    	//----------------------------
    	/*
        return;
    	
    	var file=obj;
    	var url=getObjectURL(file.files[0]);
    	console.log('url: '+url);
        qrcode.decode(url);
        
        qrcode.callback = function(imgMsg){
        	$scope.qrCodePicUrl='';
        	$scope.qrCodeObj={};   	
            console.log("imgMsg",imgMsg);            
            if(imgMsg=='error decoding QR Code'){
            	$scope.itemClickObj={};
            	showTips.showTopToast('error', '二维码有误，请上传正确的二维码', 1500);
	 		     return;
            }            
            if(imgMsg.indexOf("ALIPAY") != -1){
            	$scope.qrCodeObj.type='alipay';
            }else if(imgMsg.indexOf("wxp") != -1){
            	$scope.qrCodeObj.type='wechat';
            }else {
            	 $scope.qrCodeObj.type='other';
            	 showTips.showTopToast('error', '二维码有误，请上传微信或者支付宝收款码', 1500);
	 		     return;
            }
            
            $scope.qrCodeObj.msg=imgMsg;
            $scope.qrCodePicUrl=url;
            $scope.qrCodeText=imgMsg;
            showTips.showShadeBg(true);
        	$scope.isShowUpLoadQrCodeBox=true;
        	$scope.$apply();          
        } 	
    }*/
    
    $scope.itemClickObj={};  
    $scope.onItmeClick=function(item){
    	 $scope.itemClickObj={};
    	 $scope.itemClickObj=item; 	  	 
    }
        
	   $scope.cannelUpLoadFunc=function(){
	      $scope.closeUpLoadQrCodeBox();
	   } 
    
    
	   $scope.comfirmUpLoadFunc=function(){
		   		   
		   
		   if($scope.itemClickObj.id==undefined||$scope.itemClickObj.id==null||$scope.itemClickObj.id==''){
			     showTips.showTopToast('error', '请先选择商品', 1500);
	 		     return;
		   }
		   
		   
		   if($scope.qrCodePicUrlList.length<1){
			   showTips.showTopToast('error', '请上传二维码', 1500);
	 		   return;
		   }
		   
		   var dataJson=[];
		   
		   for(var i =0;i<$scope.qrCodePicUrlList.length;i++){
			   
			   var obj={};
//			   obj.fee=$scope.qrCodePicUrlList[i].price*100+'';
			   //var fee=$scope.qrCodePicUrlList[i].price;
			   //fee=fee.toFixed(2);
			   //obj.fee=Math.ceil(fee*100)+'';
			   obj.fee=Math.round($scope.qrCodePicUrlList[i].price*100)+'';
			   obj.qrcodeUrl=$scope.qrCodePicUrlList[i].qrCodeUrl;
			   obj.type=$scope.qrCodePicUrlList[i].type;
			   obj.commodityID=$scope.itemClickObj.id;
			   dataJson.push(obj);
		   }
		   
		   dataJson=JSON.stringify(dataJson);
		   
		   //console.log(dataJson);
		   
		  
		   gatewayHttpRequest("/user/commodity/saveQrcode", {dataJson:encodeURI(dataJson)}, function (res) {
               console.log(res);
               
               if(res.resultCode){
      			  showTips.showTopToast('success', '上传二维码成功，页面即将刷新。。。', 1500);
      			  setTimeout(function(){
      				 window.location.reload();
      			  },1500);			  
      		   }
             
               $scope.$apply();
	        },function(){
	        	 //showTips.showTopToast('success', '上传二维码失败，页面即将刷新。。。', 1500);
     			  setTimeout(function(){
     				 window.location.reload();
     			  },1500);
	        });
		   
		  		   
//		   if($scope.qrCodeObj.type==undefined||$scope.qrCodeObj.type==null||$scope.qrCodeObj.type==''){
//	  		 	showTips.showTopToast('error', '请上传二维码', 1500);
//	 		     return;
//	   	   }
//		   
//		   if($scope.qrCodeObj.type=='other'){
//	  		 	showTips.showTopToast('error', '二维码有误', 1500);
//	 		     return;
//	   	   }
//   
//		   if($scope.fee==undefined||$scope.fee==null||$scope.fee==''){
//	  		 	 showTips.showTopToast('error', '请输入金额', 1500);
//	 		     return;
//	   	   }
//		   
//		   var commodityID=$scope.itemClickObj.id;
//		   var type=$scope.qrCodeObj.type;
//		   var msg=$scope.qrCodeObj.msg;
//		   var fee=$scope.fee*100+'';
		   
		   
//		   gatewayHttpRequest("/user/commodity/findByFee", {dataJson:dataJson}, function (res) {
//               console.log(res);
//
//               if(res.data=='false'){
//            	   document.getElementsByClassName("shadeBg")[0].style.zIndex = "100";
//            	   
//            	   showTips.showAlert('该金额已存在，确定覆盖吗?', function () {
//            		   $scope.upLoadPicFunc(fee,msg,type,commodityID);
//                   }, function () {
//                	   document.getElementsByClassName("shadeBg")[0].style.zIndex = "21";
//                	   $scope.cannelUpLoadFunc();
//                	   
//                	   window.location.reload();
//                   });
//            	   return;
//               }
//               
//                $scope.upLoadPicFunc(fee,msg,type,commodityID);
//	   
//	            $scope.$apply();
//	        });	   		   
	   } 
	   
	   
//	  $scope.upLoadPicFunc=function(fee,qrcodeUrl,type,commodityID){
//		  
//		   gatewayHttpRequest("/user/commodity/saveQrcode", {fee:fee,qrcodeUrl:qrcodeUrl,type:type,commodityID:commodityID}, function (res) {
//               console.log(res);
//               
//               if(res.resultCode){
//      			  showTips.showTopToast('success', '上传二维码成功，页面即将刷新。。。', 1500);
//      			  setTimeout(function(){
//      				 window.location.reload();
//      			  },1500);			  
//      		   }
//             
//               $scope.$apply();
//	        },function(){
//	        	 //showTips.showTopToast('success', '上传二维码失败，页面即将刷新。。。', 1500);
//     			  setTimeout(function(){
//     				 window.location.reload();
//     			  },1500);
//	        });
//	  }
	  
	  /* 删除 */
	    $scope.delete = function (item) {

	        showTips.showAlert('确定删除 ' + item.name + ' 的吗?', function () {
	            gatewayHttpRequest("/user/commodity/delete", { id: item.id }, function (res) {
	                if (res.resultCode) {
	                    showTips.showTopToast('success', '删除成功,页面即将刷新...', 2000);
	                    setTimeout(function(){
	                     window.location.reload();
	       				 return;
	                    },1500);       
	                }

	            });
	        }, function () {

	        });  
	    }
	    
		  /* 清空二维码 */
	    $scope.deleteAllQrCode = function (item) {

	        showTips.showAlert('确定清空 ' + item.name + ' 的二维码吗?', function () {
	            gatewayHttpRequest("/user/clear/qrcode", { id: item.id }, function (res) {
	                if (res.resultCode) {
	                    showTips.showTopToast('success', '清空成功,页面即将刷新...', 2000);
	                    setTimeout(function(){
	                     window.location.reload();
	       				 return;
	                    },1500);       
	                }

	            });
	        }, function () {

	        });  
	    }
	

}]);


/** 账号设置-账号信息 */
app.controller('settingAccount', ['$scope', '$rootScope','$interval', function ($scope, $rootScope,$interval) {

	/*详情*/
	gatewayHttpRequest("/user/recharge/consumptionDetail", {}, function (res) {
		 
		 var data=res.data;
		 console.log(data);
		 if(data.expireDate!=undefined&&data.expireDate!=null&&data.expireDate!='null'){
			 $scope.expireDate=data.expireDate;
		 }
		 if(data.procedures!=undefined&&data.procedures!=null&&data.procedures!='null'){
			 $scope.procedures=data.procedures;
		 }	 
		 if(data.fee!=undefined&&data.fee!=null&&data.fee!='null'){
			 $scope.fee=data.fee;
		 }	 
        $scope.$apply();
    });
	
	
	var showClick = false;
	
	 // 是否显示应用弹框
	  $scope.isShowAppBox=false;	  
	  // 应用弹框
	  $scope.showAppBox=function(){
		 $scope.isShowAppBox = !$scope.isShowAppBox;
		 
		 if($scope.isShowAppBox){
			  gatewayHttpRequest("/user/recharge/list", {}, function (res) {
				    if(res.data[0]==undefined||res.data.length<1){
				    	 $scope.appList = [];
				    }else{
				    	 $scope.appList = res.data;
				    }
		            $scope.$apply();
		        });
		 }
		 
		 if(showClick){
	        	showClick=false;
	        }
	}
	// 应用选中对象
	$scope.appObj={};
	// 应用选中
	$scope.appChoise=function(item){
			 $scope.appObj={};
			 $scope.appObj = item;
			 $scope.appObj.price=$scope.appObj.price/100;
		     $scope.isShowAppBox = false;
	}

	    /* 关闭所有弹框 */
	    $scope.closeAllBox=function(){  	
	    	if(showClick){
	    		$scope.isShowAppBox = false;
	    		showClick = false;
	    	}else{
	    		showClick = true;
	    	} 
	
	 	
	    }
   
	
	$scope.isShowRechargeBox=false;
	$scope.closeRechargeBox=function(){
		showTips.showShadeBg(false);	
		$scope.isShowRechargeBox=false;
		$scope.isShowQrCode=false;
		$scope.appObj={};
		$scope.isNeedToRechangeFee='';
		$scope.payTypeText='';
		$interval.cancel(orderQuery);
	}
	
	/*充值弹框*/
	$scope.showRechargeBox=function(){
		$scope.isShowRechargeBox=true;
		showTips.showShadeBg(true);	
	}
	
	 //页面切换时，取消定时器
    $rootScope.$on('$routeChangeSuccess', function (evt, current, previous) {
    	$interval.cancel(orderQuery);
    	orderQuery=undefined;  	
    });
	
	var orderQuery; 
	 
	$scope.isShowQrCode=false;
	
	$scope.rechargeCommit=function(){
		
		 if ($scope.appObj.id == undefined || $scope.appObj.id == null || $scope.appObj.id == '') {
	   		  showTips.showTopToast('error', '请选择金额', 1500);
	            return;
	     }
		 
		 gatewayHttpRequest("/user/recharge/pay", {rechargeID:$scope.appObj.id}, function (res) {
			   
			   var data=res.data;
			   $scope.isNeedToRechangeFee=data.fee/100;
			   
			   if(data.payType=='wechat'){
				   $scope.payTypeText='微信';
			   }else{
				   $scope.payTypeText='支付宝';
			   }
			   
			  //$scope.qrcodeUrl=data.qrcodeUrl;
			   
			   document.getElementById("qrCode").innerHTML = "";
               var qrcode = new QRCode(document.getElementById("qrCode"), {
                   width: 200,
                   height: 200
               });
               
               qrcode.makeCode(data.qrcodeUrl);
			   
			   $scope.isShowQrCode=true;
			   
	            $scope.$apply();
	            
	            $interval.cancel(orderQuery);

                orderQuery = $interval(function () {

                    gatewayHttpRequest("/user/setMealPurchase/query", { outTradeNo: data.outTradeNo }, function (res) {
//                    	console.log('查单中。。。');
//                        console.log(res.data);
                        if (res.data) {
//                        	console.log('支付成功了');
                            showTips.showTopToast('success', '支付成功，页面即将刷新',1500);
                            $scope.closeRechargeBox();
                            $scope.$apply();
                            setTimeout(function(){
                            	window.location.reload();
                            },1500);
                        }

                    }, function () {

                    }, function () {

                    }, false);
                }, 3000);
	            
	        });
	}
	 

}]);



/** 账号设置-API接口信息 */
app.controller('settingAPIInfo', ['$scope', '$rootScope', function ($scope, $rootScope) {

	/*详情*/
	gatewayHttpRequest("/user/userInfo", {}, function (res) {
		 
		 var data=res.data;
		 
		 if(data.uid!=undefined&&data.uid!=null&&data.uid!='null'){
			 $scope.uuid=data.uid;
		 }
		 if(data.token!=undefined&&data.token!=null&&data.token!='null'){
			 $scope.token=data.token;
		 }	 
        $scope.$apply();
    });
	
	/*重置token*/
	$scope.resetToken=function(){
		showTips.showAlert('确定要重置token吗?', function () {
            gatewayHttpRequest("/user/resetToken", {}, function (res) {
                if (res.resultCode) {
                    showTips.showTopToast('success', '重置成功,页面即将刷新...', 2000);
                    setTimeout(function(){
                     window.location.reload();
       				 return;
                    },1500);       
                }

            });
        }, function () {

        });
	}
	 

}]);

/** 账号设置-API接口参数 */
app.controller('settingAPIParam', ['$scope', '$rootScope', function ($scope, $rootScope) {
	
	
		$scope.overdueTimeObj={
				isCheck:false,
		}
	

		 gatewayHttpRequest("/user/config/info", {}, function (res) {
				 
				 var data=res.data;
				 
				 if(data==null||data=='null'){
					 $scope.overdueTime='';
					 $scope.overdueTimeObj.isCheck=false;
					 $scope.minFee='';
				 }else{
					 
					 //data=JSON.parse(data);
					 
					 if(data.overdueTime!=undefined&&data.overdueTime!=null&&data.overdueTime!='null'){
						 $scope.overdueTime=data.overdueTime;
					 }
					
					 if(data.minFee!=undefined&&data.minFee!=null&&data.minFee!='null'){
						 $scope.minFee=data.minFee/100;
					 }
					 
					 if(data.immediately!=undefined&&data.immediately!=null&&data.immediately!='null'){
						 
						 if(data.immediately=='0'){
							 $scope.overdueTimeObj.isCheck=false;
						 }else{
							 $scope.overdueTimeObj.isCheck=true; 
						 }				
					 }
				 }
				 
		         $scope.$apply();
		     });
		
		
		$scope.checkFee=function(obj){
			var value=checkFeeInputValidFunc(obj);
			$scope.minFee=value;
		}
		
		
		
		
		//提交
		 $scope.commit=function(){

			 if($scope.overdueTime==undefined||$scope.overdueTime==null||$scope.overdueTime==''){
		  		 	showTips.showTopToast('error', '请输入二维码过期时间', 1500);
		 		     return;
		   	 }
			 
			 if (!/^[1-9]\d*$/.test($scope.overdueTime)) {
		            showTips.showTopToast('error', '过期时间只能是大于0的正整数', 1500);
		            return;
		     }
			 
			 if($scope.minFee==undefined||$scope.minFee==null||$scope.minFee==''){
		  		 	showTips.showTopToast('error', '请输入余额不足提醒金额', 1500);
		 		     return;
		   	 }
			 
			 if($scope.overdueTime<120||$scope.overdueTime>600){
				   showTips.showTopToast('error', '过期时间只能是120-600秒之间的值', 1500);
		            return;
			 }
			 
			 var postDataObj={};
			 postDataObj.overdueTime=$scope.overdueTime+'';
			 if($scope.overdueTimeObj.isCheck){
				 postDataObj.immediately='1';
			 }else{
				 postDataObj.immediately='0'; 
			 }
 
			 postDataObj.minFee=$scope.minFee*100;
			 gatewayHttpRequest("/user/config/save", postDataObj, function (res) {

		            if (res.resultCode) {
		                showTips.showTopToast('success', '提交成功', 2000);
		            }

		            setTimeout(function () {
		                window.location.reload();
		                return;
		            }, 2000);

		            $scope.$apply();
		        });
			 
		 }
		
		
			

}]);

/** 账号设置-修改密码 */
app.controller('settingModifyPassword', ['$scope', '$rootScope', function ($scope, $rootScope) {

	    $scope.oldPassword;
	    $scope.newPassword;
	    $scope.newPasswordAg;

	    $scope.commit = function () {

	        if ($scope.oldPassword == undefined || $scope.oldPassword == null || $scope.oldPassword == '') {
	            showTips.showTopToast('error', '旧密码不能为空')
	            return;
	        }
	        if ($scope.newPassword == undefined || $scope.newPassword == null || $scope.newPassword == '') {
	            showTips.showTopToast('error', '新密码不能为空')
	            return;
	        }
	        if ($scope.newPasswordAg == undefined || $scope.newPasswordAg == null || $scope.newPasswordAg == '') {
	            showTips.showTopToast('error', '确认密码不能为空')
	            return;
	        }

	        if ($scope.newPassword != $scope.newPasswordAg) {
	            showTips.showTopToast('error', '新密码与确认密码不一致')
	            return;
	        }


	        gatewayHttpRequest("/user/updatePassword", { oldPassword: $scope.oldPassword, newPassword: $scope.newPassword }, function (res) {

	            if (res.resultCode) {
	                showTips.showTopToast('success', '修改密码成功,即将跳转到登陆页面...', 3000);
	            }

	            setTimeout(function () {
	                window.location.href = "/user/loginPage";
	                return;
	            }, 3000);

	            $scope.$apply();
	        });
	    }


}]);


/** 账号设置-基本信息 */
app.controller('settingBaseInfo', ['$scope', '$rootScope', function ($scope, $rootScope) {

	 gatewayHttpRequest("/user/userInfo", {}, function (res) {
		 
		 var data=res.data;		 
		 if(data.account!=undefined&&data.account!=null&&data.account!='null'){
			 $scope.account=data.account;
		 }
		 if(data.mobile!=undefined&&data.mobile!=null&&data.mobile!='null'){
			 $scope.mobile=data.mobile;
		 }
		 if(data.wechat!=undefined&&data.wechat!=null&&data.wechat!='null'){
			 $scope.wechat=data.wechat;
		 }
		 if(data.qqNumber!=undefined&&data.qqNumber!=null&&data.qqNumber!='null'){
			 $scope.qqNumber=data.qqNumber;
		 }
		 
         $scope.$apply();
     });
	 
	 //提交
	 $scope.commit=function(){
		 
		 if($scope.account==undefined||$scope.account==null||$scope.account==''){
	  		 	showTips.showTopToast('error', '请输入真实姓名', 1500);
	 		     return;
	   	 }
		 if($scope.mobile==undefined||$scope.mobile==null||$scope.mobile==''){
	  		 	showTips.showTopToast('error', '请输入电话号码或者手机号码', 1500);
	 		     return;
	   	 }
		 if (!/^1[34578]\d{9}$/.test($scope.mobile) && !/^0\d{2,3}-?\d{7,8}$/.test($scope.mobile)) {
	            showTips.showTopToast('error', '电话号码或者手机号码格式不正确');
	            return;
	     }
		 
		 if($scope.wechat==undefined||$scope.wechat==null||$scope.wechat==''){
	  		 	showTips.showTopToast('error', '请输入微信号', 1500);
	 		     return;
	   	 }
		 if($scope.qqNumber==undefined||$scope.qqNumber==null||$scope.qqNumber==''){
	  		 	showTips.showTopToast('error', '请输入qq号码', 1500);
	 		     return;
	   	 }
		 
		 var postDataObj={};
		 
		 postDataObj.account=$scope.account;
		 postDataObj.mobile=$scope.mobile;
		 postDataObj.wechat=$scope.wechat;
		 postDataObj.qqNumber=$scope.qqNumber;
		 
		 gatewayHttpRequest("/user/update", postDataObj, function (res) {

	            if (res.resultCode) {
	                showTips.showTopToast('success', '提交成功', 2000);
	            }

	            setTimeout(function () {
	                window.location.reload();
	                return;
	            }, 2000);

	            $scope.$apply();
	        });
		 
	 }

}]);


angular.bootstrap(document.getElementById("app"), ['app']);