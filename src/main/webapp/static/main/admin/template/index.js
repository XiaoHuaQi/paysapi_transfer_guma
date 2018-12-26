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

document.getElementById("updateBut").onclick = function () {
    updateFun.showDiv();
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
            title: "套餐管理",
            icon: "Theoldmember",
            url: "/setMeal/list",
            second: [
                {
                    title: "套餐管理",
                    url: "/setMeal/list"
                }
            ]
        },
        {
            title: "商户管理",
            icon: "membership",
            url: "/merchant/list",
            second: [
                {
                    title: "商户管理",
                    url: "/merchant/list"
                }
            ]
        },
        {
            title: "充值管理",
            icon: "finance",
            url: "/recharge/list",
            second: [
                {
                    title: "充值管理",
                    url: "/recharge/list"
                }
            ]
        },
        {
            title: "订单管理",
            icon: "finance",
            url: "/order/list",
            second: [
                {
                    title: "订单管理",
                    url: "/order/list"
                }
            ]
        },
        
        {
            title: "商户订单",
            icon: "finance",
            url: "/merchant/order",
            second: [
                {
                    title: "商户订单",
                    url: "/merchant/order"
                }
            ]
        },
        
        {
            title: "用户购买详情",
            icon: "integral2",
            url: "/setMealPurchase/list",
            second: [
                {
                    title: "用户购买详情",
                    url: "/setMealPurchase/list"
                }
            ]
        },
        
        {
            title: "注册码管理",
            icon: "finance",
            url: "/registerCode/list",
            second: [
                {
                    title: "注册码管理",
                    url: "/registerCode/list"
                }
            ]
        },
        
        {
            title: "测试码管理",
            icon: "finance",
            url: "/testQrCode/list",
            second: [
                {
                    title: "测试码管理",
                    url: "/testQrCode/list"
                }
            ]
        },
        
        
        {
            title: "系统配置",
            icon: "tubiao01",
            url: "/setting/config",
            second: [
                {
                    title: "系统配置",
                    url: "/setting/config"
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
            templateUrl: "/static/main/admin/template/index/index.html",
            controller: "index"
        })
        //套餐管理-列表
        .when('/setMeal/list', {
            templateUrl: "/static/main/admin/template/setMeal/list.html",
            controller: "setMealList"
        })
        //套餐管理-新增
        .when('/setMeal/add', {
            templateUrl: "/static/main/admin/template/setMeal/add.html",
            controller: "setMealAdd"
        })
          //套餐管理-编辑
        .when('/setMeal/edit/:id', {
            templateUrl: "/static/main/admin/template/setMeal/edit.html",
            controller: "setMealEdit"
        })
        
         //商户管理-列表
        .when('/merchant/list', {
            templateUrl: "/static/main/admin/template/merchant/list.html",
            controller: "merchantList"
        })
         //商户管理-新增
        .when('/merchant/add', {
            templateUrl: "/static/main/admin/template/merchant/add.html",
            controller: "merchantAdd"
        })
         //商户管理-编辑
        .when('/merchant/edit/:id', {
            templateUrl: "/static/main/admin/template/merchant/edit.html",
            controller: "merchantEdit"
        })
         //充值管理-列表
        .when('/recharge/list', {
            templateUrl: "/static/main/admin/template/recharge/list.html",
            controller: "rechargeList"
        })
         //用户购买详情-列表
        .when('/setMealPurchase/list', {
            templateUrl: "/static/main/admin/template/setMealPurchase/list.html",
            controller: "setMealPurchaseList"
        })
          //用户购买详情-新增
        .when('/setMealPurchase/add', {
            templateUrl: "/static/main/admin/template/setMealPurchase/add.html",
            controller: "setMealPurchaseAdd"
        })
         //系统配置
        .when('/setting/config', {
            templateUrl: "/static/main/admin/template/setting/config.html",
            controller: "settingConfig"
        })
          //订单管理-列表
        .when('/order/list', {
            templateUrl: "/static/main/admin/template/order/list.html",
            controller: "orderList"
        })
          //商户订单
        .when('/merchant/order', {
            templateUrl: "/static/main/admin/template/merchantOrder/order.html",
            controller: "merchantOrder"
        })
        
          //注册码管理-列表
        .when('/registerCode/list', {
            templateUrl: "/static/main/admin/template/registerCode/list.html",
            controller: "registerCodeList"
        })
        
         //套餐管理-二维码列表
        .when('/setMeal/qrCodeList/:id', {
            templateUrl: "/static/main/admin/template/setMeal/qrCodeList.html",
            controller: "setMealQrCodeList"
        })
        //充值管理-二维码列表
        .when('/recharge/qrCodeList/:id', {
            templateUrl: "/static/main/admin/template/recharge/qrCodeList.html",
            controller: "rechargeQrCodeList"
        })
        
          //测试码管理-列表
        .when('/testQrCode/list', {
            templateUrl: "/static/main/admin/template/testQrCode/list.html",
            controller: "testQrCodeList"
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


/** 测试码管理-列表*/
app.controller('testQrCodeList', ['$scope', '$rootScope', function ($scope, $rootScope) {

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

        gatewayHttpRequest("/admin/test/findByPage", conditionObj, function (res) {
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
	  		upLoadFile(file,'/admin/test/upload',function(res){
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
  	
    }

    
    $scope.itemClickObj={};  
    $scope.onItmeClick=function(item){
    	 $scope.itemClickObj={};
    	 $scope.itemClickObj=item; 	  	 
    }
        
	   $scope.cannelUpLoadFunc=function(){
	      $scope.closeUpLoadQrCodeBox();
	   } 
    
    
	   $scope.comfirmUpLoadFunc=function(){
		   		   
	   
		   if($scope.qrCodePicUrlList.length<1){
			   showTips.showTopToast('error', '请上传二维码', 1500);
	 		   return;
		   }
		   
		   var dataJson=[];
		   
		   for(var i =0;i<$scope.qrCodePicUrlList.length;i++){
			   
			   var obj={};
//			   obj.fee=$scope.qrCodePicUrlList[i].price*100+'';
			   obj.fee=Math.round($scope.qrCodePicUrlList[i].price*100)+'';
			   obj.qrcodeUrl=$scope.qrCodePicUrlList[i].qrCodeUrl;
			   obj.type=$scope.qrCodePicUrlList[i].type;
			   //obj.rechargeID =$scope.itemClickObj.id;
			   dataJson.push(obj);
		   }
		   
		   dataJson=JSON.stringify(dataJson);
		   console.log(dataJson);
		   gatewayHttpRequest("/admin/test/saveQrcode", {dataJson:encodeURI(dataJson)}, function (res) {
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
		   
	   } 
	   

	  
	  /* 删除 */
	    $scope.delete = function (item) {

	        showTips.showAlert('确定删除 ' + item.price/100 + ' 的吗?', function () {
	            gatewayHttpRequest("/admin/test/deleteQrcode", { id: item.id }, function (res) {
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



/** 充值管理-二维码列表 */
app.controller('rechargeQrCodeList', ['$scope', '$rootScope','$routeParams', function ($scope, $rootScope,$routeParams) {

	
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

        gatewayHttpRequest("/admin/recharge/findByQrcodeList", conditionObj, function (res) {
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
    $scope.postDataObj.rechargeID=id;
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
            gatewayHttpRequest("/admin/recharge/deleteQrcode", { rechargeID: item.id }, function (res) {
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





/** 套餐管理-二维码列表 */
app.controller('setMealQrCodeList', ['$scope', '$rootScope','$routeParams', function ($scope, $rootScope,$routeParams) {

	
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

        gatewayHttpRequest("/admin/setMeat/findByQrcodeList", conditionObj, function (res) {
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
    $scope.postDataObj.setMealID=id;
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
            gatewayHttpRequest("/admin/setMeat/deleteQrcode", { setMealID: item.id }, function (res) {
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




/** 注册码管理-列表 */
app.controller('registerCodeList', ['$scope', '$rootScope', function ($scope, $rootScope) {

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

        gatewayHttpRequest("/admin/registerCode/list", conditionObj, function (res) {
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
  
    // 类型选择数组
    $scope.changeTypeList = [

        {
            'name': '全部',
            'state': 'ALL'
        },
        {
            'name': '未注册',
            'state': '0'
        },
        {
            'name': '已注册',
            'state': '1'
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
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.userName != undefined) {
                delete $scope.postDataObj.userName;
            }         
        } else {
            $scope.currentPage = 1;
            $scope.postDataObj.userName = $scope.searchKey + '';         
        }
    
        getDataListForCondition($scope.postDataObj);
    }
    
    
    $scope.createNewCode=function(){
    	
        gatewayHttpRequest("/admin/registerCode/save", {}, function (res) {
            var data = res.data;
            console.log(data);
            
            showTips.showAlert('注册码为: <br/><br/>'+data.code, function () {
            	 window.location.reload();
            }, function () {
            	 window.location.reload();
            });  
            
            $scope.$apply();
        });
    	
    }
    
    /* 删除 */
    $scope.delete = function (item) {

        showTips.showAlert('确定删除该注册码吗?', function () {
            gatewayHttpRequest("/admin/registerCode/delete", { id: item.id }, function (res) {
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


/** 商户订单 */
app.controller('merchantOrder', ['$scope', '$rootScope','$filter', function ($scope, $rootScope,$filter) {
	
	
	 gatewayHttpRequest("/admin/merchant/order/statistics", {}, function (res) {
		    var data=res.data;
		    console.log(data);
		    $scope.statistics=data;
           $scope.$apply();
     });
	

	  $scope.isShowWechatBox=false;	  
	  $scope.showWechatBox=function(){
		 $scope.isShowWechatBox = !$scope.isShowWechatBox;
		 $scope.isShowTypeChange=false;
		 $scope.isShowAppBox = false;
		 if($scope.isShowWechatBox){
			  gatewayHttpRequest("/admin/merchant/list", {}, function (res) {
				    if(res.data[0]==undefined||res.data.length<1){
				    	 $scope.authorizerList = [];
				    }else{
				    	 $scope.authorizerList = res.data;
				    }
		            $scope.$apply();
		        });
		 }
		 
		 if(showClick){
	        	showClick=false;
	        }
	}
	$scope.wechatObj={};
	$scope.weChatChoise=function(item){
			$scope.wechatObj={};
			$scope.wechatObj = item;
		    $scope.isShowWechatBox = false;
		    
		    gatewayHttpRequest("/admin/merchant/order/statistics", {userID:item.id}, function (res) {
			    var data=res.data;
			    console.log(data);
			    $scope.statistics=data;
	            $scope.$apply();
	        });
		    $scope.postDataObj.userID=$scope.wechatObj.id;
		    getDataListForCondition($scope.postDataObj);
	}
	
	
	$scope.userClickAll=function(){
		window.location.reload();
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

    	if($scope.systemIDKey != undefined){
    		conditionObj.systemID = $scope.systemIDKey;
    	}
    	if($scope.orderIDKey != undefined){
    		conditionObj.outTradeNo = $scope.orderIDKey;
    	}
    	
        gatewayHttpRequest("/admin/merchant/order/dataList", conditionObj, function (res) {
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

    if($scope.wechatObj.id!=undefined){
    	 $scope.postDataObj.userID=$scope.wechatObj.id;
    }   
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
            'name': '全部',
            'type': 'ALL'
        },
        {
            'name': '未支付',
            'type': '0'
        },
        {
            'name': '已支付',
            'type': '1'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange = false;
    // 是否显示类型下拉框
    $scope.showTypechange = function () {
    	$scope.isShowTypeChange1=false;
        $scope.isShowTypeChange = !$scope.isShowTypeChange;
        if(showClick){
        	showClick=false;
        } 
        
    }
    
    /* 关闭所有弹框 */
    $scope.closeAllBox=function(){
    	
    	if(showClick){
    		$scope.isShowTypeChange=false;
    		$scope.isShowTypeChange1=false;
    		showClick = false;
    	}else{
    		showClick = true;
    	} 	
 	
    }
      
    // 类型 -支付状态
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
    
  
    
    // 类型选择数组
    $scope.changeType1List = [

        {
            'name': '全部',
            'payType': 'ALL'
        },
        {
            'name': '支付宝',
            'payType': 'alipay'
        },
        {
            'name': '微信',
            'payType': 'wechat'
        }
       
    ];
    var showClick = false;
    $scope.isShowTypeChange1 = false;
    // 是否显示类型下拉框
    $scope.showTypechange1 = function () {
    	$scope.isShowTypeChange=false;
        $scope.isShowTypeChange1 = !$scope.isShowTypeChange1;
        if(showClick){
        	showClick=false;
        } 
        
    }
     
    // 类型 -支付状态
    $scope.typeChangeObj1;
    /* 类型选择 */
    $scope.typeChoise1 = function (item) {
        $scope.typeChangeObj1 = item;
        $scope.isShowTypeChange1 = false;
        if($scope.typeChangeObj1.payType=='ALL'){
        	if ($scope.postDataObj.payType != undefined) {
                delete $scope.postDataObj.payType;
            }
        }else{      
        	
        	$scope.postDataObj.payType=$scope.typeChangeObj1.payType;       	
        }           
    }
    

  
    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
         getDataListForCondition($scope.postDataObj);
    }
    
	
	
    $scope.retransmission = function (item) {
    	
    	
   	 gatewayHttpRequest("/admin/merchant/order/retransmission", { orderID: item.id }, function (res) {
          if (res.resultCode) {
              showTips.showTopToast('success', '发送成功,页面即将刷新...', 1500);
              setTimeout(function(){
                  window.location.reload();
           	   //getDataListForCondition($scope.postDataObj);
 				 return;
              },1500);       
          }

      });
    }

}]);

/** 订单管理-列表 */
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

        gatewayHttpRequest("/admin/setMealOrder/list", conditionObj, function (res) {
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
            'name': '全部',
            'type': 'ALL'
        },
        {
            'name': '未支付',
            'type': '0'
        },
        {
            'name': '已支付',
            'type': '1'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange = false;
    // 是否显示类型下拉框
    $scope.showTypechange = function () {
    	$scope.isShowTypeChange1=false;
        $scope.isShowTypeChange = !$scope.isShowTypeChange;
        if(showClick){
        	showClick=false;
        } 
        
    }
    
    /* 关闭所有弹框 */
    $scope.closeAllBox=function(){
    	
    	if(showClick){
    		$scope.isShowTypeChange=false;
    		$scope.isShowTypeChange1=false;
    		showClick = false;
    	}else{
    		showClick = true;
    	} 	
 	
    }
      
    // 类型 -支付状态
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
    
  
    
    // 类型选择数组
    $scope.changeType1List = [

        {
            'name': '全部',
            'orderType': 'ALL'
        },
        {
            'name': '购买套餐',
            'orderType': '1'
        },
        {
            'name': '充值余额',
            'orderType': '2'
        },
        {
            'name': '商户下单',
            'orderType': '0'
        }
    ];
    var showClick = false;
    $scope.isShowTypeChange1 = false;
    // 是否显示类型下拉框
    $scope.showTypechange1 = function () {
    	$scope.isShowTypeChange=false;
        $scope.isShowTypeChange1 = !$scope.isShowTypeChange1;
        if(showClick){
        	showClick=false;
        } 
        
    }
     
    // 类型 -支付状态
    $scope.typeChangeObj1;
    /* 类型选择 */
    $scope.typeChoise1 = function (item) {
        $scope.typeChangeObj1 = item;
        $scope.isShowTypeChange1 = false;
        if($scope.typeChangeObj1.orderType=='ALL'){
        	if ($scope.postDataObj.orderType != undefined) {
                delete $scope.postDataObj.orderType;
            }
        }else{      
        	
        	$scope.postDataObj.orderType=$scope.typeChangeObj1.orderType;       	
        }           
    }
    

  
    /* 搜索 */
    $scope.doSearch = function () {
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
         getDataListForCondition($scope.postDataObj);
    }
    
    
    /* 删除 */
    $scope.orderHasPayed = function (item) {

        showTips.showAlert('确定修改该订单支付状态吗?', function () {
            gatewayHttpRequest("/admin/setMeal/update", { id: item.id }, function (res) {
                if (res.resultCode) {
                    showTips.showTopToast('success', '修改成功,页面即将刷新...', 2000);
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

/** 用户购买详情-新增*/
app.controller('setMealPurchaseAdd', ['$scope', '$rootScope', function ($scope, $rootScope) {

	// 续费时间数组
	  $scope.changeTypeList = [

	      {
	          'name': '日',
	          'type': '1'
	      },
	      {
	          'name': '月',
	          'type': '2'
	      },
	      {
	          'name': '年',
	          'type': '3'
	      },
	    
	  ];
	  
	  // 续费时间 类型
	  $scope.typeChangeObj={};
	  // 是否显示公众号弹框
	  $scope.isShowWechatBox=false;	  
	  // 微信公众号弹框
	  $scope.showWechatBox=function(){
		 $scope.isShowWechatBox = !$scope.isShowWechatBox;
		 $scope.isShowTypeChange=false;
		 $scope.isShowAppBox = false;
		 if($scope.isShowWechatBox){
			  gatewayHttpRequest("/admin/merchant/list", {}, function (res) {
				    if(res.data[0]==undefined||res.data.length<1){
				    	 $scope.authorizerList = [];
				    }else{
				    	 $scope.authorizerList = res.data;
				    }
		            $scope.$apply();
		        });
		 }
		 
		 if(showClick){
	        	showClick=false;
	        }
	}
	// 微信公众号选中对象
	$scope.wechatObj={};
	// 微信公众号选中
	$scope.weChatChoise=function(item){
			$scope.wechatObj={};
			 $scope.wechatObj = item;
		     $scope.isShowWechatBox = false;
	}
	
	 // 是否显示应用弹框
	  $scope.isShowAppBox=false;	  
	  // 应用弹框
	  $scope.showAppBox=function(){
		 $scope.isShowAppBox = !$scope.isShowAppBox;
		 
		  $scope.isShowTypeChange=false;
		  $scope.isShowWechatBox=false;
		 
		 if($scope.isShowAppBox){
			  gatewayHttpRequest("/admin/setMeal/list", {}, function (res) {
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
		     $scope.isShowAppBox = false;
	}

	 var showClick = false;
	 $scope.isShowTypeChange = false;
	 // 是否显示续费时间下拉框
	 $scope.showTypechange = function () {
		 
	        $scope.isShowTypeChange = !$scope.isShowTypeChange;    
	        $scope.isShowAppBox=false;
			$scope.isShowWechatBox=false;
 
	        if(showClick){
	        	showClick=false;
	        }
	    }
  
	    /* 关闭所有弹框 */
	    $scope.closeAllBox=function(){  	
	    	if(showClick){
	    		$scope.isShowTypeChange=false;
	    		$scope.isShowWechatBox = false;
	    		$scope.isShowAppBox = false;
	    		showClick = false;
	    	}else{
	    		showClick = true;
	    	} 
	
	 	
	    }
	    
	    /* 类型选择 */
	    $scope.typeChoise = function (item) {
	    	$scope.typeChangeObj={};
	        $scope.typeChangeObj = item;
	        $scope.isShowTypeChange = false;
	    }

	    // 提交
	     $scope.commit=function(){
	    	 
	    	 if ($scope.wechatObj.id == undefined || $scope.wechatObj.id == null || $scope.wechatObj.id == '') {
	    		 showTips.showTopToast('error', '请选择商户', 1500);
	             return;
	         }

	    	 if ($scope.appObj.id == undefined || $scope.appObj.id == null || $scope.appObj.id == '') {
	    		 showTips.showTopToast('error', '请选择套餐', 1500);
	             return;
	         }
	    	 
//	    	 if ($scope.number == undefined || $scope.number == null || $scope.number == '') {
//	    		 showTips.showTopToast('error', '请输入时间', 1500);
//	             return;
//	         }
//	    	 
//	    	 if (!/^[1-9]\d*$/.test($scope.number)) {
//	             showTips.showTopToast('error', '时间为正整数', 1500);
//	             return;
//	         }
//	    	 
//	    	 if ($scope.typeChangeObj.type == undefined || $scope.typeChangeObj.type == null || $scope.typeChangeObj.type == '') {
//	    		 showTips.showTopToast('error', '请选择单位', 1500);
//	             return;
//	         }
	    	 
	    	 var postDataObj={};
	    	 postDataObj.userID=$scope.wechatObj.id+'';
	    	 postDataObj.setMealID=$scope.appObj.id+'';
	    	 //postDataObj.type=$scope.typeChangeObj.type;

	    	 gatewayHttpRequest("/admin/setMealPurchase/save", postDataObj, function (res) {
	    	      
	    		 if(res.resultCode){
	    			 showTips.showTopToast('success', '新增成功', 1500);
	    			 setTimeout(function(){
	    				 window.location.hash='/setMealPurchase/list';
	    				 return;
	    			 },1500)
	    		 }
	    		  
	    	    $scope.$apply();
	    	    });
  	
	    }

}]);


/** 用户购买详情-列表*/
app.controller('setMealPurchaseList', ['$scope', '$rootScope', function ($scope, $rootScope) {
	
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

        gatewayHttpRequest("/admin/setMealPurchase/list", conditionObj, function (res) {
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
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
    	// 搜索关键词
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.userName != undefined) {
                delete $scope.postDataObj.userName;
            }         
        } else {
            $scope.currentPage = 1;
            $scope.postDataObj.userName = $scope.searchKey + '';         
        }
        getDataListForCondition($scope.postDataObj);
    } 
    
    
    var showClick = false;
	
	 // 是否显示应用弹框
	  $scope.isShowAppBox=false;	  
	  // 应用弹框
	  $scope.showAppBox=function(){
		 $scope.isShowAppBox = !$scope.isShowAppBox;
		 
		 if($scope.isShowAppBox){
			  gatewayHttpRequest("/admin/setMeal/list", {}, function (res) {
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
    
    
    /* 续费 */
    $scope.renewal=function(item){
    	
    	$scope.renewalDetailObj={};
    	$scope.renewalDetailObj=item;
    	showTips.showShadeBg(true);	
        $scope.isShowRenewalBox=true;
    }
    
    $scope.closeRenewalBox=function(){
    	showTips.showShadeBg(false);	
    	$scope.isShowRenewalBox=false;
    	$scope.renewalDetailObj={};
    }
    
    
   // 续费提交
    $scope.renewalCommit=function(){
   	
   	 if ($scope.renewalDetailObj.userID == undefined || $scope.renewalDetailObj.userID  == null || $scope.renewalDetailObj.userID  == '') {
   		 showTips.showTopToast('error', '请选择用户', 1500);
            return;
        }
	 
   	 if ($scope.appObj.id == undefined || $scope.appObj.id == null || $scope.appObj.id == '') {
   		 showTips.showTopToast('error', '请选择套餐', 1500);
            return;
        }

   	 var postDataObj={};
   	 postDataObj.userID=$scope.renewalDetailObj.userID+'';
   	 postDataObj.setMealID=$scope.appObj.id+'';

   	 gatewayHttpRequest("/admin/setMealPurchase/save", postDataObj, function (res) {
   	      
   		 if(res.resultCode){
   			 showTips.showTopToast('success', '编辑成功', 1500);
   			 setTimeout(function(){
   				 window.location.reload();
   				 return;
   			 },1500)
   		 }
   		  
   	    $scope.$apply();
   	    });
	
   }

    

}]);




/** 系统配置 */
app.controller('settingConfig', ['$scope', '$rootScope', function ($scope, $rootScope) {
	
	

			// 类型选择数组
		$scope.changeTypeList = [
		
			{
				name:'微信',
				type:'wechat'
			},
			{
				name:'支付宝',
				type:'alipay'
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
		// 类型-默认第一个
		$scope.typeChangeObj={};
		//$scope.typeChangeObj= $scope.changeTypeList[0];
		
		/* 类型选择 */
		$scope.typeChoise = function (item) {
			$scope.typeChangeObj={};
		    $scope.typeChangeObj = item;
		    $scope.isShowTypeChange = false;
		}

		$scope.overdueTimeObj={
				isCheck:false,
		}
		$scope.orvrUseAdmission={
				isCheck:false,
		}
	

		 gatewayHttpRequest("/admin/config/info", {}, function (res) {
				 
				 var data=res.data;
				 
				 if(data==null||data=='null'){
					 $scope.overdueTime='';
					 $scope.overdueTimeObj.isCheck=false;
					 $scope.orvrUseAdmission.isCheck=false;
					 //$scope.typeChangeObj= $scope.changeTypeList[0];
				 }else{
					 
					 //data=JSON.parse(data);
					 
					 if(data.overdueTime!=undefined&&data.overdueTime!=null&&data.overdueTime!='null'){
						 $scope.overdueTime=data.overdueTime;
					 }
					
					 if(data.immediately!=undefined&&data.immediately!=null&&data.immediately!='null'){
						 
						 if(data.immediately=='0'){
							 $scope.overdueTimeObj.isCheck=false;
						 }else{
							 $scope.overdueTimeObj.isCheck=true; 
						 }				
					 }
					 
					 if(data.type!=undefined&&data.type!=null&&data.type!=''){
							for(var i =0;i<$scope.changeTypeList.length;i++){
								if($scope.changeTypeList[i].type==data.type){
									$scope.typeChangeObj= $scope.changeTypeList[i];
								}
							}
					  }
					 if(data.useAdmission == "1"){
						 $scope.orvrUseAdmission.isCheck=true;
					 }
					 
				 }
				 
		         $scope.$apply();
		     });
		
		//提交
		 $scope.commit=function(){
			 
			 if($scope.typeChangeObj.name==undefined||$scope.typeChangeObj.name==null||$scope.typeChangeObj.name==''){
		   		 	showTips.showTopToast('error', '请选择支付方式', 1500);
		  		     return;
		     }

			 if($scope.overdueTime==undefined||$scope.overdueTime==null||$scope.overdueTime==''){
		  		 	showTips.showTopToast('error', '请输入二维码过期时间', 1500);
		 		     return;
		   	 }
			 
			 if (!/^[1-9]\d*$/.test($scope.overdueTime)) {
		            showTips.showTopToast('error', '过期时间只能是大于0的正整数', 1500);
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
			 if($scope.orvrUseAdmission.isCheck){
				 postDataObj.useAdmission='1';
			 }else{
				 postDataObj.useAdmission='2'; 
			 }
			 postDataObj.type=$scope.typeChangeObj.type;
 
			 gatewayHttpRequest("/admin/config/save", postDataObj, function (res) {

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


/** 充值管理-列表*/
app.controller('rechargeList', ['$scope', '$rootScope', function ($scope, $rootScope) {

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

        gatewayHttpRequest("/admin/recharge/list", conditionObj, function (res) {
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

   /* // 搜索
    $scope.searchKey;

     搜索 
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
    */
    
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
    	
    	var title=$scope.addName;
    	var price=$scope.addFee*100+'';
    	
 	   gatewayHttpRequest("/admin/recharge/save", {title:title,price:price}, function (res) {
 		   
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
	  		upLoadFile(file,'/admin/file/recharge/upload',function(res){
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
  	
    }

    
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
			   obj.fee=Math.round($scope.qrCodePicUrlList[i].price*100)+'';
			   obj.qrcodeUrl=$scope.qrCodePicUrlList[i].qrCodeUrl;
			   obj.type=$scope.qrCodePicUrlList[i].type;
			   obj.rechargeID =$scope.itemClickObj.id;
			   dataJson.push(obj);
		   }
		   
		   dataJson=JSON.stringify(dataJson);
		   console.log(dataJson);
		   gatewayHttpRequest("/admin/rechargeQrcode/save", {dataJson:encodeURI(dataJson)}, function (res) {
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
		   
	   } 
	   

	  
	  /* 删除 */
	    $scope.delete = function (item) {

	        showTips.showAlert('确定删除 ' + item.title + ' 的吗?', function () {
	            gatewayHttpRequest("/admin/recharge/delete", { id: item.id }, function (res) {
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

	        showTips.showAlert('确定清空 ' + item.title + ' 的二维码吗?', function () {
	            gatewayHttpRequest("/admin//clear/rechargeQrcode", { id: item.id }, function (res) {
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




/** 商户管理-编辑*/
app.controller('merchantEdit', ['$scope', '$rootScope','$routeParams', function ($scope, $rootScope,$routeParams) {

	var id = $routeParams.id;
    if (id == '' || id == undefined || id == null) {
        return;
    }

    gatewayHttpRequest("/admin/merchant/info", {id:id}, function (res) {
		var data=res.data;
		
		if(data.userName!=undefined&&data.userName!=null&&data.userName!=''){
			$scope.userName=data.userName;
		}
		if(data.account!=undefined&&data.account!=null&&data.account!=''){
			$scope.account=data.account;
		}
		if(data.password!=undefined&&data.password!=null&&data.password!=''){
			$scope.password=data.password;
		}
		if(data.mobile!=undefined&&data.mobile!=null&&data.mobile!=''){
			$scope.mobile=data.mobile;
		}
		
        $scope.$apply();
    });
    
   
    $scope.commit=function(){
    	
    	if($scope.userName==undefined||$scope.userName==null||$scope.userName==''){
   		 	showTips.showTopToast('error', '请输入账号', 1500);
  		     return;
    	}
    	
    	if($scope.account==undefined||$scope.account==null||$scope.account==''){
   		 	showTips.showTopToast('error', '请输入用户名', 1500);
  		     return;
    	}
    	
    	if($scope.password==undefined||$scope.password==null||$scope.password==''){
   		 	showTips.showTopToast('error', '请输入密码', 1500);
  		     return;
    	}
    	
    	if($scope.mobile==undefined||$scope.mobile==null||$scope.mobile==''){
   		 	showTips.showTopToast('error', '请输入电话', 1500);
  		     return;
    	}
    	
    	if (!/^1[34578]\d{9}$/.test($scope.mobile) && !/^0\d{2,3}-?\d{7,8}$/.test($scope.mobile)) {
             showTips.showTopToast('error', '电话号码或者手机号码格式不正确');
             return;
        }
  	
    	var postDataObj={};  	
    	postDataObj.id=id;
    	postDataObj.userName=$scope.userName;
    	postDataObj.account=$scope.account;
    	postDataObj.password=$scope.password;
		postDataObj.mobile=$scope.mobile;
		
    	 gatewayHttpRequest("/admin/merchant/save", postDataObj, function (res) {
    		 if(res.resultCode){
    			 showTips.showTopToast('success', '编辑成功,页面即将跳转...', 2500); 
    			 setTimeout(function(){
    				 window.location.hash='/merchant/list';
    				 return;
    			 },2500); 
    		 } 
             $scope.$apply();
         });
    }
	
}]);




/** 商户管理-新增*/
app.controller('merchantAdd', ['$scope', '$rootScope', function ($scope, $rootScope) {

  
    $scope.commit=function(){
    	
    	if($scope.userName==undefined||$scope.userName==null||$scope.userName==''){
   		 	showTips.showTopToast('error', '请输入账号', 1500);
  		     return;
    	}
    	
    	if($scope.account==undefined||$scope.account==null||$scope.account==''){
   		 	showTips.showTopToast('error', '请输入用户名', 1500);
  		     return;
    	}
    	
    	if($scope.password==undefined||$scope.password==null||$scope.password==''){
   		 	showTips.showTopToast('error', '请输入密码', 1500);
  		     return;
    	}
    	
    	if($scope.mobile==undefined||$scope.mobile==null||$scope.mobile==''){
   		 	showTips.showTopToast('error', '请输入电话', 1500);
  		     return;
    	}
    	
    	if (!/^1[34578]\d{9}$/.test($scope.mobile) && !/^0\d{2,3}-?\d{7,8}$/.test($scope.mobile)) {
             showTips.showTopToast('error', '电话号码或者手机号码格式不正确');
             return;
        }
  	
    	var postDataObj={};  	
    	postDataObj.userName=$scope.userName;
    	postDataObj.account=$scope.account;
    	postDataObj.password=$scope.password;
		postDataObj.mobile=$scope.mobile;
		
    	 gatewayHttpRequest("/admin/merchant/save", postDataObj, function (res) {
    		 if(res.resultCode){
    			 showTips.showTopToast('success', '新增成功,页面即将跳转...', 2500); 
    			 setTimeout(function(){
    				 window.location.hash='/merchant/list';
    				 return;
    			 },2500); 
    		 } 
             $scope.$apply();
         });
    }
	
	
}]);





/** 商户管理-列表*/
app.controller('merchantList', ['$scope', '$rootScope', function ($scope, $rootScope) {
	
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

        gatewayHttpRequest("/admin/merchant/page", conditionObj, function (res) {
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
            
            for(var i =0;i<$scope.rowList.length;i++){
	           	 if($scope.rowList[i].state=='0'){
	           		 $scope.rowList[i].isUse=true;
	           	 }else{
	           		 $scope.rowList[i].isUse=false;
	           	 }
	           	if($scope.rowList[i].useAdmission=='1'){
	           		 $scope.rowList[i].useOpen=true;
	           	 }else{
	           		 $scope.rowList[i].useOpen=false;
	           	 }
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
   	
    	 $scope.currentPage = 1;
         $scope.postDataObj.pageNum = $scope.currentPage;
    	// 搜索关键词
        if ($scope.searchKey == null || $scope.searchKey == '' || $scope.searchKey == undefined) {
            if ($scope.postDataObj.userName != undefined) {
                delete $scope.postDataObj.userName;
            }         
        } else {
            $scope.currentPage = 1;
            $scope.postDataObj.userName = $scope.searchKey + '';         
        }
        getDataListForCondition($scope.postDataObj);
    }
    
    /* 删除 */
    $scope.delete = function (item) {

        showTips.showAlert('确定删除 ' + item.userName + ' 的吗?', function () {
            gatewayHttpRequest("/admin/merchant/delete", { id: item.id }, function (res) {
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
    
    $scope.isShowRechargeBalanceBox = false;
    $scope.isShowRechargeIntegralBox = false;

    /* 关闭积分充值弹框 */
    $scope.closeRechargeIntegralBox = function () {
        showTips.showShadeBg(false);
        $scope.isShowRechargeIntegralBox = false;
    }

    /* 关闭余额充值弹框 */
    $scope.closeRechargeBalanceBox = function () {
        showTips.showShadeBg(false);
        $scope.isShowRechargeBalanceBox = false;
    }

    /* 充值余额 */
    $scope.rechargeBalance = function (item) {
        showTips.showShadeBg(true);
        $scope.isShowRechargeBalanceBox = true;
        $scope.rechargeBalanceObj = item;
        $scope.moneyInput = '';
    }

    /* 充值积分 */
    $scope.rechargeIntegral = function (item) {
        showTips.showShadeBg(true);
        $scope.isShowRechargeIntegralBox = true;
        $scope.rechargeIntegralObj = item;
        $scope.integralInput = '';

    }

    // 充值余额输入
    $scope.moneyInput;

    /* 充值余额提交 */
    $scope.rechargeBalanceCommit = function () {


        if ($scope.moneyInput == '' || $scope.moneyInput == undefined) {
            showTips.showTopToast('error', '请输入金额', 1500);
            return;
        }

        if (!/^\d+\.?\d{0,2}$/.test($scope.moneyInput)) {
            showTips.showTopToast('error', '请输入正确的金额', 1500);
            return;
        }

        var fee = $scope.moneyInput.toFixed(2);
        var postDataObj = {};
        postDataObj.userID = $scope.rechargeBalanceObj.id;
        postDataObj.fee = fee * 100;
        
        $scope.moneyInput = '';
        $scope.rechargeMoneyNote = '';
        gatewayHttpRequest("/admin/merchant/recharge/add", postDataObj, function (res) {
            if (res.resultCode) {
                showTips.showTopToast('success', '充值成功!', 1500);
                $scope.closeRechargeBalanceBox();
                $scope.$apply();
                
                setTimeout(function(){
                	window.location.reload();
                },1500);
                return;
            }
        });
    }

    // 充值积分输入
    $scope.integralInput;
    /* 充值积分提交 */
    $scope.rechargeIntegralCommit = function () {

//        if ($scope.integralInput == '' ||$scope.integralInput == null) {
//            showTips.showTopToast('error', '请输入手续费', 1500);
//            return;
//        }

        if (!/^[0-9]\d*$/.test($scope.integralInput)) {
            showTips.showTopToast('error', '手续费只能是大于等于0的整数', 1500);

            return;
        }

        var fee = $scope.integralInput;
        var postDataObj = {};
        postDataObj.userID = $scope.rechargeIntegralObj.id;
        postDataObj.procedures = fee * 1;
        $scope.integralInput = '';
     
        gatewayHttpRequest("/admin/merchant/setMeal/update", postDataObj, function (res) {
            if (res.resultCode) {
                showTips.showTopToast('success', '手续费修改成功!', 1500);
                $scope.closeRechargeIntegralBox();
                $scope.$apply();
                setTimeout(function(){
                	window.location.reload();
                },1500);
                return;
            }
        });
    }
    
    
    $scope.useClick=function(item){
    	gatewayHttpRequest("/admin/merchant/updateState", {id:item.id}, function (res) {
    		getDataListForCondition($scope.postDataObj);
            $scope.$apply();
        });
    }
    $scope.openUseAdmission=function(item){
    	gatewayHttpRequest("/admin/merchant/updateUseAdmission", {id:item.id}, function (res) {
    		getDataListForCondition($scope.postDataObj);
            $scope.$apply();
        });
    }  
    
    

}]);


/** 套餐管理-编辑*/
app.controller('setMealEdit', ['$scope', '$rootScope','$routeParams', function ($scope, $rootScope,$routeParams) {

	var id = $routeParams.id;
    if (id == '' || id == undefined || id == null) {
        return;
    }

    gatewayHttpRequest("/admin/setMeal/info", {id:id}, function (res) {
		var data=res.data;
		
		if(data.title!=undefined&&data.title!=null&&data.title!=''){
			$scope.title=data.title;
		}
		if(data.price!=undefined&&data.price!=null&&data.price!=''){
			$scope.price=data.price;
		}
		if(data.procedures!=undefined&&data.procedures!=null&&data.procedures!=''){
			$scope.procedures=data.procedures;
		}
		if(data.number!=undefined&&data.number!=null&&data.number!=''){
			$scope.number=data.number;
		}
		
		if(data.type!=undefined&&data.type!=null&&data.type!=''){
			for(var i =0;i<$scope.changeTypeList.length;i++){
				if($scope.changeTypeList[i].type==data.type){
					$scope.typeChangeObj= $scope.changeTypeList[i];
				}
			}
		}
		
		if(data.describes!=undefined&&data.describes!=null&&data.describes!=''){
			$scope.describe=data.describes;
		}
		
        $scope.$apply();
    });
    
    
	
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
   
    // 类型-性别默认第一个
    //$scope.typeChangeObj= $scope.changeTypeList[0];

    /* 类型选择 */
    $scope.typeChoise = function (item) {
    	$scope.typeChangeObj={};
        $scope.typeChangeObj = item;
        $scope.isShowTypeChange = false;
    }
    
    
    /*金额 检查*/
    $scope.checkFee=function(obj){
    	 var value= checkFeeInputValidFunc(obj);
         $scope.price =value; 
    }
    
   
    $scope.commit=function(){
    	
    	if($scope.title==undefined||$scope.title==null||$scope.title==''){
   		 	showTips.showTopToast('error', '请输入名称', 1500);
  		     return;
    	}
    	
    	if($scope.price==undefined||$scope.price==null||$scope.price==''){
   		 	showTips.showTopToast('error', '请输入价格', 1500);
  		     return;
    	}
    	
    	if (!/^[0-9]\d*$/.test($scope.price)) {
            showTips.showTopToast('error', '价格为非负整数', 1500);
            return;
        }
    	
    	if($scope.procedures==undefined||$scope.procedures==null||$scope.procedures==''){
   		 	showTips.showTopToast('error', '请输入手续费', 1500);
  		     return;
    	}
    	
    	if (!/^[0-9]\d*$/.test($scope.procedures)) {
            showTips.showTopToast('error', '手续费为非负整数', 1500);
            return;
        }
    	
    	if($scope.number==undefined||$scope.number==null||$scope.number==''){
   		 	showTips.showTopToast('error', '请输入时间', 1500);
  		     return;
    	}
    	
    	if (!/^[1-9]\d*$/.test($scope.number)) {
            showTips.showTopToast('error', '时间为正整数', 1500);
            return;
        }
    	
    	
    	if($scope.typeChangeObj.name==undefined||$scope.typeChangeObj.name==null||$scope.typeChangeObj.name==''){
   		 	showTips.showTopToast('error', '请选择单位', 1500);
  		     return;
    	}
    	
    	if($scope.describe==undefined||$scope.describe==null||$scope.describe==''){
   		 	showTips.showTopToast('error', '请输入描述', 1500);
  		     return;
    	}
    	
    	var postDataObj={};  
    	postDataObj.id=id;
    	postDataObj.title=$scope.title;
    	postDataObj.price=$scope.price;
    	postDataObj.procedures=$scope.procedures;
		postDataObj.number=$scope.number;
		postDataObj.type=$scope.typeChangeObj.type;
		postDataObj.describes=$scope.describe;
		

    	 gatewayHttpRequest("/admin/setMeal/save", postDataObj, function (res) {
    		 if(res.resultCode){
    			 showTips.showTopToast('success', '编辑成功,页面即将跳转...', 2500); 
    			 setTimeout(function(){
    				 window.location.hash='/setMeal/list';
    				 return;
    			 },2500); 
    		 } 
             $scope.$apply();
         });
    }
	
	
}]);


/** 套餐管理-新增*/
app.controller('setMealAdd', ['$scope', '$rootScope', function ($scope, $rootScope) {

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
   
    // 类型-性别默认第一个
    $scope.typeChangeObj= $scope.changeTypeList[0];

    /* 类型选择 */
    $scope.typeChoise = function (item) {
    	$scope.typeChangeObj={};
        $scope.typeChangeObj = item;
        $scope.isShowTypeChange = false;
    }
    
    
    /*金额 检查*/
    $scope.checkFee=function(obj){
    	 var value= checkFeeInputValidFunc(obj);
         $scope.price =value; 
    }
    
   
    $scope.commit=function(){
    	
    	if($scope.title==undefined||$scope.title==null||$scope.title==''){
   		 	showTips.showTopToast('error', '请输入名称', 1500);
  		     return;
    	}
    	
    	if($scope.price==undefined||$scope.price==null||$scope.price==''){
   		 	showTips.showTopToast('error', '请输入价格', 1500);
  		     return;
    	}
    	
    	if (!/^[0-9]\d*$/.test($scope.price)) {
            showTips.showTopToast('error', '价格为非负整数', 1500);
            return;
        }
    	
    	if($scope.procedures==undefined||$scope.procedures==null||$scope.procedures==''){
   		 	showTips.showTopToast('error', '请输入手续费', 1500);
  		     return;
    	}
    	
    	if (!/^[0-9]\d*$/.test($scope.procedures)) {
            showTips.showTopToast('error', '手续费为非负整数', 1500);
            return;
        }
    	
    	if($scope.number==undefined||$scope.number==null||$scope.number==''){
   		 	showTips.showTopToast('error', '请输入时间', 1500);
  		     return;
    	}
    	
    	if (!/^[1-9]\d*$/.test($scope.number)) {
            showTips.showTopToast('error', '时间为正整数', 1500);
            return;
        }
    	
    	
    	if($scope.typeChangeObj.name==undefined||$scope.typeChangeObj.name==null||$scope.typeChangeObj.name==''){
   		 	showTips.showTopToast('error', '请选择单位', 1500);
  		     return;
    	}
    	
    	if($scope.describe==undefined||$scope.describe==null||$scope.describe==''){
   		 	showTips.showTopToast('error', '请输入描述', 1500);
  		     return;
    	}
    	
    	var postDataObj={};  	
    	postDataObj.title=$scope.title;
    	postDataObj.price=$scope.price;
    	postDataObj.procedures=$scope.procedures;
		postDataObj.number=$scope.number;
		postDataObj.type=$scope.typeChangeObj.type;
		postDataObj.describes=$scope.describe;
		

    	 gatewayHttpRequest("/admin/setMeal/save", postDataObj, function (res) {
    		 if(res.resultCode){
    			 showTips.showTopToast('success', '新增成功,页面即将跳转...', 2500); 
    			 setTimeout(function(){
    				 window.location.hash='/setMeal/list';
    				 return;
    			 },2500); 
    		 } 
             $scope.$apply();
         });
    }
	
	
}]);


/** 套餐管理-列表*/
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
    // 汇总数据对象
    $scope.allInOneObj = {};
    // 带条件的列表请求方法
    var getDataListForCondition = function (conditionObj) {

        gatewayHttpRequest("/admin/setMeal/list", conditionObj, function (res) {
            var data = res.data;
            console.log(data);
            if (data[0] == undefined || data.length < 1) {
                $scope.rowList = [];
                $scope.totalPage = 0;
                $scope.totalPageList = [];
            } else {
                $scope.rowList = data;
                $scope.totalPage = 1;
                $scope.currentPage =1;
                $scope.totalPageList = calculateIndexes($scope.currentPage, $scope.totalPage, 6);
            }
            
            for(var i =0;i<$scope.rowList.length;i++){
            	for(var j =0;j< $scope.changeTypeList.length;j++){
            		if($scope.rowList[i].type==$scope.changeTypeList[j].type){
            			$scope.rowList[i].timeStr=$scope.rowList[i].number+' '+$scope.changeTypeList[j].name;
                	}
            	} 	
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
    
    
	  /* 删除 */
    $scope.delete = function (item) {

        showTips.showAlert('确定删除 ' + item.title + ' 的吗?', function () {
            gatewayHttpRequest("/admin/setMeal/delete", { id: item.id }, function (res) {
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
	  		upLoadFile(file,'/admin/file/upload',function(res){
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
  	
    }

    
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
			   obj.fee=Math.round($scope.qrCodePicUrlList[i].price*100)+'';
			   obj.qrcodeUrl=$scope.qrCodePicUrlList[i].qrCodeUrl;
			   obj.type=$scope.qrCodePicUrlList[i].type;
			   obj.setMealID =$scope.itemClickObj.id;
			   dataJson.push(obj);
		   }
		   
		   dataJson=JSON.stringify(dataJson);
		   console.log(dataJson);
		   gatewayHttpRequest("/admin/setMealQrcode/save", {dataJson:encodeURI(dataJson)}, function (res) {
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
		   
	   } 
	   

	   
	   /* 清空二维码 */
	    $scope.deleteAllQrCode = function (item) {

	        showTips.showAlert('确定清空 ' + item.title + ' 的二维码吗?', function () {
	            gatewayHttpRequest("/admin/clear/setMeatQrcode", { id: item.id }, function (res) {
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


/** 首页*/
app.controller('index', ['$scope', '$rootScope', function ($scope, $rootScope) {

	gatewayHttpRequest("/admin/index/data", {}, function (res) {
        var data = res.data;
        console.log(data);
        $scope.proceduresDto=data.proceduresDto;
        $scope.rechargeDto=data.rechargeDto;
        $scope.setMealDto=data.setMealDto;
        $scope.statistics=data.statistics;
        $scope.list=data.list;
        $scope.$apply();
    });
}]);

angular.bootstrap(document.getElementById("app"), ['app']);