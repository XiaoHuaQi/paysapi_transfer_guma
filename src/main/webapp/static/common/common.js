//var baseUrl = 'http://www.zixuapp.com';
//var baseUrl = 'http://192.168.1.4:8080';
//var baseUrl = 'http://127.0.0.1:8080';
//var baseUrl = 'http://192.168.1.4:8080';
//var baseUrl = 'http://192.168.1.4:8080';
var baseUrl = 'http://wanma.wmpayinc.com:8080';
//var baseUrl = 'http://192.168.1.4:8080';
//var baseUrl = 'http://127.0.0.1:8080';
//var baseUrl = 'http://test.zixuapp.com';
//var baseUrl = 'http://47.107.86.21';
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return ""; 
}


var showTips;
showTips = {
    showLoading: function (state) {
        var html = '<div class="load loadingBox"><svg viewBox="0 0 120 120" version="1.1"> <g id="circle" class="g-circles g-circles--v1">'
        html += '<circle id="12" transform="translate(35, 16.698730) rotate(-30) translate(-35, -16.698730) " cx="35" cy="16.6987298" r="8"></circle>';
        html += '<circle id="11" transform="translate(16.698730, 35) rotate(-60) translate(-16.698730, -35) " cx="16.6987298" cy="35" r="8"></circle>';
        html += '<circle id="10" transform="translate(10, 60) rotate(-90) translate(-10, -60) " cx="10" cy="60" r="8"></circle>';
        html += '<circle id="9" transform="translate(16.698730, 85) rotate(-120) translate(-16.698730, -85) " cx="16.6987298" cy="85" r="8"></circle>';
        html += '<circle id="8" transform="translate(35, 103.301270) rotate(-150) translate(-35, -103.301270) " cx="35" cy="103.30127" r="8"></circle>';
        html += '<circle id="7" cx="60" cy="110" r="8"></circle><circle id="6" transform="translate(85, 103.301270) rotate(-30) translate(-85, -103.301270) " cx="85" cy="103.30127" r="8"></circle>';
        html += '<circle id="5" transform="translate(103.301270, 85) rotate(-60) translate(-103.301270, -85) " cx="103.30127" cy="85" r="8"></circle>';
        html += '<circle id="4" transform="translate(110, 60) rotate(-90) translate(-110, -60) " cx="110" cy="60" r="8"></circle>';
        html += '<circle id="3" transform="translate(103.301270, 35) rotate(-120) translate(-103.301270, -35) " cx="103.30127" cy="35" r="8"></circle>';
        html += '<circle id="2" transform="translate(85, 16.698730) rotate(-150) translate(-85, -16.698730) " cx="85" cy="16.6987298" r="8"></circle>';
        html += '<circle id="1" cx="60" cy="10" r="8"></circle></g><use xlink:href="#circle" class="use"/>';
        html += '</svg></div>';
        if (state) {
            document.getElementsByTagName('body')[0].insertAdjacentHTML("beforeend", html);
            showTips.showShadeBg(true);
        } else {
            var load = document.getElementsByClassName('load  loadingBox')[0];
            if (load == undefined) {
                return;
            }
            load.remove();
            showTips.showShadeBg(false);
        }
    },
    showTopToast: function (type, msg, timeout) {
        if (timeout == undefined) {
            timeout = 3000;
        }
        var html = '';
        if (type == 'success') {
            html += '<div class="popoutSkip ">'
        } else if (type = 'error') {
            html += '<div class="popoutSkip popoutLoseBg">'
        }
        html += '<p class="popoutSkipText"><span>' + msg + '</span>';
        html += '</p><a href="javascript:showTips.hideTopToast();" class="iconfont icon-disable"></a></div>';
        document.getElementsByTagName('body')[0].insertAdjacentHTML("beforeend", html);
        showTips.showShadeBg(true);
        setTimeout(function () {
            showTips.hideTopToast();
        }, timeout);
    },
    hideTopToast: function () {
        var popoutSkip = document.getElementsByClassName('popoutSkip')[0];
        if (popoutSkip == undefined) {
            return;
        }
        popoutSkip.remove();
        showTips.showShadeBg(false);
    },
    showAlertComfirmFunc: function () {

    },
    showAlertcancelFunc: function () {
    },
    showAlert: function (msg, comfirmFunc, cancelFunc) {
        var t = this;
        if (comfirmFunc != undefined) {
            showTips.showAlertComfirmFunc = comfirmFunc;
        } else {
            showTips.showAlertComfirmFunc = showAlertComfirmFunc = function () {
                showTips.hideAlert();
            };
        }
        if (cancelFunc != undefined) {
            showTips.showAlertcancelFunc = cancelFunc;
        } else {
            showTips.showAlertcancelFunc = showAlertComfirmFunc = function () { };
        }
        var html = '';
        html += '<div class="popoutBg border-radius" id="alertSys"><div class="row fr">';
        html += '<a href="javascript:showTips.hideAlert(\'cancelFunc\');" class="closeBtn"><i class="iconfont icon-disable"></i></a></div>';
        html += '<div class="textColor1 alertTitle"><h4>' + msg + '</h4></div><div class="row">';
        html += '<a href="javascript:showTips.hideAlert(\'comfirmFunc\');  " class="alertBtn btn select mg-r-8">确定</a>';
        html += '<a href="javascript:showTips.hideAlert(\'cancelFunc\');" class="alertBtn btn">取消</a></div></div>';
        document.getElementsByTagName('body')[0].insertAdjacentHTML("beforeend", html);
        showTips.showShadeBg(true);
    },


    hideAlert: function (func) {

        document.getElementById('alertSys').remove();
        showTips.showShadeBg(false);

        if (func == "cancelFunc") {
            showTips.showAlertcancelFunc();
        } else {
            showTips.showAlertComfirmFunc();
        }


    },
    showShadeBg: function (state) {
        var html = '<div class="shadeBg"></div>';
        if (state) {
            document.getElementsByTagName('body')[0].insertAdjacentHTML("beforeend", html);
        } else {
            var shadeBg = document.getElementsByClassName('shadeBg')[0];
            if (shadeBg == undefined) {
                return;
            }
            shadeBg.remove();
        }
    }
}


function httpRequest(obj) {
    if (obj.error === undefined) {
        obj.error = function () {
        };
    }
    if (obj.success === undefined) {
        obj.success = function () {
        };
    }
    if (obj.type === undefined) {
        obj.type = "GET";
    }
    if (obj.data === undefined) {
        obj.data = null;
    }
    if (obj.data === undefined) {
        obj.data = null;
    }
    if (obj.complete === undefined) {
        obj.complete = function () {
        };
    }
    if (obj.url === undefined) {
        obj.error();
        return;
    }
    var httpRequestObj;
    if (window.XMLHttpRequest) {
        httpRequestObj = new XMLHttpRequest();
    } else {
        httpRequestObj = new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url = obj.url;
    if(serializeForm(JSON.parse(obj.data)) != ""){
    	url +="?" + serializeForm(JSON.parse(obj.data))
    }
    
    httpRequestObj.open(obj.type, url);
    httpRequestObj.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    httpRequestObj.send(JSON.stringify(obj.data));
    httpRequestObj.onreadystatechange = function () {
        if (httpRequestObj.readyState === 4 && httpRequestObj.status === 200) {
            var data = httpRequestObj.responseText;
            obj.complete();
            obj.success(JSON.parse(data));
            return;
        } else if (httpRequestObj.readyState === 4 && httpRequestObj.status !== 200) {
            obj.complete();
            obj.error();
            return;
        }
    }
}

function serializeForm(data){
	if(data != null){
		var parts = new Array();
		for (var i in data) {
			parts.push(i + "=" + data[i]);
		}
		return parts.join("&");
	}
	return "";
}


function gatewayHttpRequest(url, data, succFunc, errFunc, completeFunc, isShowLoading) {


    if (isShowLoading === undefined) {
        isShowLoading = true;
    }


    if (errFunc === undefined) {
        errFunc = function () {
        };
    }
    if (completeFunc === undefined) {
        completeFunc = function () {
        };
    }
    var method = "get";
    var postStr = null;
    if (data !== null) {
        method = "post";
        var loginCookieKey = localStorage.getItem("loginCookieKey");
        //data.token = getCookie(loginCookieKey);
        //console.log(data.token)
        postStr = JSON.stringify(data);

    }

    if (isShowLoading) {
        showTips.showLoading(true);
    }


    httpRequest({
        // url: baseUrl + url + '?t=' + Math.random(),
        url: baseUrl + url,
        type: method,
        data: postStr,
        success: function (res) {
            if (!res.resultCode) {
                switch (res.errCode) {
                    case 100004:
                        window.location.href = "/user/loginPage";
                        return;
                }
                showTips.showTopToast('error',res.errCodeDes);
                errFunc(res.errCode, res.errCodeDes);
                return;
            }
            
            if(res.data!=null&&res.data!='null'){
            	res.data=JSON.parse(res.data)
            }
            
            
            succFunc(res);
        },
        error: function () {
            showTips.showTopToast('error', '请求失败，请检查你的网络是否有通畅！');
        },
        complete: function () {
            completeFunc();
            if (isShowLoading) {
                showTips.showLoading(false);
            }

        }

    });

}


function upLoadFile(file, url, successFunc) {
//    if (fileInputObj.value.length <= 0) {
//        return;
//    }
//    var file = fileInputObj;
//
//    var dataForm = new FormData();
//    dataForm.append('file', file.files[0]);
	
//	 if (fileInputObj.value.length <= 0) {
//	        return;
//	    }
//	    var file = fileInputObj;

	var dataForm = new FormData();
	dataForm.append('file', file);

    var httpRequestObj;
    if (window.XMLHttpRequest) {
        httpRequestObj = new XMLHttpRequest();
    } else {
        httpRequestObj = new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    httpRequestObj.open("POST", url);
    httpRequestObj.send(dataForm);
    httpRequestObj.onreadystatechange = function () {
    	console.log(httpRequestObj);
        if (httpRequestObj.readyState === 4 && httpRequestObj.status === 200) {
            var data = httpRequestObj.responseText;
            console.log('1');
            successFunc(JSON.parse(data));
            return;
        } else if (httpRequestObj.readyState === 4 && httpRequestObj.status !== 200) {
            error();
            console.log('2');
            return;
        }
        console.log('3');
    }
}


function calculateIndexes(current, length, displayLength) {
    var indexes = [];
    var start = Math.round(current - displayLength / 2);
    var end = Math.round(current + displayLength / 2);
    if (start <= 1) {
        start = 1;
        end = start + displayLength - 1;
        if (end >= length - 1) {
            end = length - 1;
        }
    }
    if (end >= length - 1) {
        end = length;
        start = end - displayLength + 1;
        if (start <= 1) {
            start = 1;
        }
    }
    for (var i = start; i <= end; i++) {
        indexes.push(i);
    }
    return indexes;
}



var getListDataForPage = function (url, page, successFunc) {

    gatewayHttpRequest(url, { pageNum: page }, function (res) {
        console.log(res);
        var rowList = res.data.row;
        var nowPage = res.data.nowPage;
        var totalPage = res.data.totalPage;
        var currentPage = page;
        var totalPageList = calculateIndexes(nowPage, totalPage, 6);

        var obj = {};
        obj.rowList = rowList;
        //obj.nowPage=nowPage;
        obj.totalPage = totalPage;
        obj.currentPage = currentPage;
        obj.totalPageList = totalPageList;
        successFunc(obj);
    });

}


var formatDateTime = function (timeStamp) {
    var date = new Date();
    date.setTime(timeStamp * 1000);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
};




var updateFun = {

    showDiv: function () {
        var html = '';
        html += '<div class="popoutBg border-radius" style="display: block" id="passwordBox">';
        html += '<div class="row fr">';
        html += '<a href="javascript:updateFun.closeBox()" class="closeBtn">';
        html += '<i class="iconfont icon-disable"></i>';
        html += '</a>';
        html += '</div>';
        html += '<div class="textColor1 alertTitle">';
        html += '<h4 class="textDold text-md">修改密码</h4>';
        html += '</div>';
        html += '<div class="formHorizontal">';
        html += '<div class="row">';
        html += '<label class="controlLabel timeInput mg-r-24">旧密码</label>';
        html += '<div class="controlInput">';
        html += '<input class="formInput" type="password" placeholder="请输入您的旧密码" id="oldPassword" name="oldPassword" value="">';
        html += '</div>';
        html += '</div>';
        html += '<div class="row mg-t-24">';
        html += '<label class="controlLabel timeInput  mg-r-24">新密码</label>';
        html += '<div class="controlInput ">';
        html += '<input class="formInput" type="password" placeholder="请输入您的新密码" id="newPassword" value="">';
        html += '</div>';
        html += '</div>';
        html += '<div class="row mg-t-24"><label class="controlLabel timeInput  mg-r-24">确认密码</label><div class="controlInput "><input class="formInput" type="password" placeholder="请再次输入您的新密码" id="newPasswordAg" value=""></div></div>';
        html += '<div class="row mg-t-24"><label class="controlLabel timeInput  mg-r-24"></label><div class="controlBtn"><a href="javascript:updateFun.commit()"><input class="formBtn submitBtn border-radius" type="button" name="name" value="提交"></a></div></div>';
        html += '</div>';
        html += '</div>';
        document.getElementsByTagName('body')[0].insertAdjacentHTML("beforeend", html);
        showTips.showShadeBg(true);
    },
    closeBox: function () {
        var passwordBox = document.getElementById('passwordBox');
        if (passwordBox == undefined) {
            return;
        }
        passwordBox.remove();
        showTips.showShadeBg(false);

    },

    commit: function () {
        updatePasswordFun();
    }
}


var updatePasswordFun = function () {

    var oldPassword = document.getElementById("oldPassword").value;
    var newPassword = document.getElementById("newPassword").value;
    var newPasswordAg = document.getElementById("newPasswordAg").value;


    if (oldPassword == undefined || oldPassword == null || oldPassword == '') {
        showTips.showTopToast('error', '旧密码不能为空')
        return;
    }
    if (newPassword == undefined || newPassword == null || newPassword == '') {
        showTips.showTopToast('error', '新密码不能为空')
        return;
    }
    if (newPasswordAg == undefined || newPasswordAg == null || newPasswordAg == '') {
        showTips.showTopToast('error', '确认密码不能为空')
        return;
    }

    if (newPassword !== newPasswordAg) {
        showTips.showTopToast('error', '新密码与确认密码不一致')
        return;
    }


    gatewayHttpRequest("/admin/updatePassword", { oldPassword: oldPassword, newPassword: newPassword }, function (res) {

        if (res.resultCode) {
            showTips.showTopToast('success', '修改密码成功,即将跳转到登陆页面...', 3000);
        }

        setTimeout(function () {
            window.location.href = "/user/loginPage";
            return;
        }, 3000);

    });

}

/**支付类型*/
var getPaymentTypeFunc=function(type){
	var paymentTypeText='';
	switch(type){
		case 'pay.wechat.jsapi':
			paymentTypeText='微信支付';
			break;
		case 'pay.wechatNativeVip.balance':
			paymentTypeText='会员卡余额支付';
			break;
		case 'pay.alipay.jsapi':
			paymentTypeText='支付宝快速买单';
			break;
		case 'pay.alipay.micropay':
			paymentTypeText='支付宝刷卡支付';
			break;
		case 'pay.alipay.native':
			paymentTypeText='支付宝扫码支付';
			break;
		case 'pay.wechat.micropay':
			paymentTypeText='微信付款码支付';
			break;	
	    default:
	    	paymentTypeText='其他支付';
	    	break;	    		
	}
	
	return paymentTypeText;	
}

/** 检测输入金额是否合法 */
var checkFeeInputValidFunc=function(obj){
	
	if (obj.value != '' && obj.value.substr(0, 1) == '.') {
        obj.value = "";
    }
    obj.value = obj.value.replace(/^0*(0\.|[1-9])/, '$1');// 解决 粘贴不生效
    obj.value = obj.value.replace(/[^\d.]/g, "");  // 清除“数字”和“.”以外的字符
    obj.value = obj.value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
    obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 只能输入两个小数
    if (obj.value.indexOf(".") < 0 && obj.value != "") {// 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于
														// 01、02的金额
        if (obj.value.substr(0, 1) == '0' && obj.value.length == 2) {
            obj.value = obj.value.substr(1, obj.value.length);
        }
    }
    
    return  obj.value;
}














