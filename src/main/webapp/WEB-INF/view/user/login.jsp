<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=yes">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
	<link rel="icon" href="http://120.76.47.203:81/favicon.ico" type="image/x-icon" />
	<!-- <link rel="icon" href="https://static.zixu.hk/zixuapp.com/app/cms/file/20180928/151607/b0bcde76da8c41be80c9c36f72eda2a8.ico" mce_href="https://static.zixu.hk/zixuapp.com/app/cms/file/20180928/151607/b0bcde76da8c41be80c9c36f72eda2a8.ico" type="image/x-icon" />
     --> <script src="/static/common/common.js"></script>
    <style>
        body,
        body div,
        p,
        html,
        span,
        ul,
        li,
        input {
            margin: 0;
            padding: 0;
            border: 0;
            background: 0 0;
            vertical-align: baseline;
            font-weight: normal;
            font-size: 100%;
        }

        html {
            box-sizing: border-box;
            overflow: hidden;
            width: 100%;
            height: 100%;

        }

        body {
            font-family: 'Microsoft YaHei', Tahoma, Arial, 'Lantinghei SC', 'Microsoft YaHei', sans-serif;
            font-size: 14px;
            color: #000;
            overflow: hidden;
            position: relative;
            width: 100%;
            height: 100%;
            max-height: 100%;
        }

        body::before {
            content: '';
            display: block;
            position: absolute;
            left: 0px;
            left: 0px;
            width: 100%;
            height: 100%;
            /*background-image: url('/static/user/login/images/backgroundImage.png');*/
            background: #d3d3d3;
            background-size: 100% 100%;
            background-repeat: no-repeat;

        }

        *,
        :after,
        :before {
            box-sizing: border-box;
        }

        a {
            margin: 0;
            padding: 0;
            background: 0 0;
            vertical-align: baseline;
            font-size: 100%;
            text-decoration: none;
            color: #fff;
        }

        img {
            max-width: 100%;
            height: 100%;
        }

        input {
            outline: none;
            -webkit-appearance: none;
            background-color: transparent;
            border-radius: 0;
            border: none;
        }

        @font-face {
            font-family: 'iconfont';
            /* project id 699627 */
            src: url('/static/user/login/fonts/iconfont.eot');
            src: url('/static/user/loginfonts/iconfont.eot?#iefix') format('embedded-opentype'),
            url('/static/user/login/fonts/iconfont.woff') format('woff'),
            url('/static/user/login/iconfont.ttf') format('truetype'),
            url('/static/user/login/iconfont.svg#iconfont') format('svg');
        }

        .iconfont {
            font-family: "iconfont" !important;
            font-size: 16px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            vertical-align: middle;
        }

        .icon-mima:before {
            content: "\e61b";
        }

        .icon-zhanghao:before {
            content: "\e6b6";
        }

        .loginBox {
            width: 520px;
            height: 460px;
            background-color: rgba(255, 255, 255, .4);
            -webkit-border-radius: 6px;
            -moz-border-radius: 6px;
            -ms-border-radius: 6px;
            border-radius: 6px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            padding: 48px 70px;
            text-align: center;
            margin-bottom: 22px;
        }

        .loginLogo {
            width: 144px;
            height: 60px;
            overflow: hidden;
            margin: 0 auto;
            margin-bottom: 22px;
        }

        .loginBox>p {
            line-height: 30px;
        }

        .username,
        .password {
            width: 380px;
            padding-bottom: 10px;
            border-bottom: 1px solid rgba(0, 0, 0, .1);
            position: relative;
            margin-top: 36px;
        }

        .username>input,
        .password>input {
            width: 100%;
            padding-left: 36px;
            letter-spacing: 2px;
            line-height: 24px;
            margin-top: 2px;
        }

        .username>i,
        .password>i {
            position: absolute;
            top: 2px;
            bottom: 0px;
            color: rgba(0, 0, 0, .54);
            font-size: 22px;
            vertical-align: middle;
        }

        .loginBtn {
            margin-top: 48px;
            width: 380px;
            height: 50px;
            background-color: rgba(255, 103, 0, 1);
            -webkit-border-radius: 6px;
            -moz-border-radius: 6px;
            -ms-border-radius: 6px;
            border-radius: 6px;
            overflow: hidden;
        }

        .loginBtn>input {
            width: 100%;
            color: #fff;
            line-height: 50px;
            text-align: center;
            cursor: pointer;
        }

        .loginBtn:hover {
            box-shadow: 0px 4px 8px 0px rgba(199, 87, 10, 0.75);
            -webkit-box-shadow: 0px 4px 8px 0px rgba(199, 87, 10, 0.75);
            -moz-box-shadow: 0px 4px 8px 0px rgba(199, 87, 10, 0.75);
            -ms-box-shadow: 0px 4px 8px 0px rgba(199, 87, 10, 0.75);
            -webkit-transition: all .3s ease-in-out;
            -moz-transition: all .3s ease-in-out;
            transition: all .3s ease-in-out;
        }

        .loginInput:hover {
            border-bottom: 1px solid rgba(255, 103, 0, 1);
            -webkit-transition: all .3s ease-in-out;
            -moz-transition: all .3s ease-in-out;
            transition: all .3s ease-in-out;

        }

        .loginInput:hover i {
            color: rgba(255, 103, 0, 1);
            -webkit-transition: all .3s ease-in-out;
            -moz-transition: all .3s ease-in-out;
            transition: all .3s ease-in-out;
            transform: scale(1.1);
        }

        .loginAq {
            position: absolute;
            bottom: 30px;
            left: 50%;
            transform: translate(-50%, 0%);
            -webkit-transform: translate(-50%, 0%);
            -moz-transform: translate(-50%, 0%);
            -ms-transform: translate(-50%, 0%);
            color: #fff;
            font-size: 12px;
        }

        .loginAq>span:first-of-type {
            margin-right: 16px;
        }

        .errorTip {
            position: absolute;
            top: -26px;
            left: 36px;
            letter-spacing: 2px;
            color: #ff0000;
            -webkit-transition: all .3s ease-in-out;
            -moz-transition: all .3s ease-in-out;
            transition: all .3s ease-in-out;
        }
    </style>
    <style>
        .shadeBg {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, .1);
        }

        .loadingBox {
            position: fixed;
            top: 50%;
            left: 50%;
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            -o-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            z-index: 21;
        }

        svg {
            height: 60px;
            width: 60px;
            overflow: visible;
        }

        .g-circles {
            -webkit-transform: scale(0.9) translate(7px, 7px);
            -ms-transform: scale(0.9) translate(7px, 7px);
            transform: scale(0.9) translate(7px, 7px);
        }

        circle {
            fill: #ff6700;
            fill-opacity: 0;
            -webkit-animation: opacity 1.2s linear infinite;
            animation: opacity 1.2s linear infinite;
        }

        circle:nth-child(12n + 1) {
            -webkit-animation-delay: -0.1s;
            animation-delay: -0.1s;
        }

        circle:nth-child(12n + 2) {
            -webkit-animation-delay: -0.2s;
            animation-delay: -0.2s;
        }

        circle:nth-child(12n + 3) {
            -webkit-animation-delay: -0.3s;
            animation-delay: -0.3s;
        }

        circle:nth-child(12n + 4) {
            -webkit-animation-delay: -0.4s;
            animation-delay: -0.4s;
        }

        circle:nth-child(12n + 5) {
            -webkit-animation-delay: -0.5s;
            animation-delay: -0.5s;
        }

        circle:nth-child(12n + 6) {
            -webkit-animation-delay: -0.6s;
            animation-delay: -0.6s;
        }

        circle:nth-child(12n + 7) {
            -webkit-animation-delay: -0.7s;
            animation-delay: -0.7s;
        }

        circle:nth-child(12n + 8) {
            -webkit-animation-delay: -0.8s;
            animation-delay: -0.8s;
        }

        circle:nth-child(12n + 9) {
            -webkit-animation-delay: -0.9s;
            animation-delay: -0.9s;
        }

        circle:nth-child(12n + 10) {
            -webkit-animation-delay: -1s;
            animation-delay: -1s;
        }

        circle:nth-child(12n + 11) {
            -webkit-animation-delay: -1.1s;
            animation-delay: -1.1s;
        }

        circle:nth-child(12n + 12) {
            -webkit-animation-delay: -1.2s;
            animation-delay: -1.2s;
        }

        @-webkit-keyframes opacity {
            3% {
                fill-opacity: 1;
            }

            75% {
                fill-opacity: 0;
            }
        }

        @keyframes opacity {
            3% {
                fill-opacity: 1;
            }

            75% {
                fill-opacity: 0;
            }
        }
        
    </style>

</head>

<body>

    <div class="loginBox">
        <div class="loginLogo" style="display: none;">
            <!-- <img src="/static/user/login/images/loginLogo.svg" alt=""> -->
            <!-- <img src="/static/user/login/images/haotian-loginLogo.png" alt=""> -->
            <!-- <img src="/static/user/login/images/zhifubalogo.png" alt=""> -->
        </div>
        <!-- <p>欢迎使用昊天商服统一认证服务</p> -->
        <!-- <p>欢迎使用统一认证服务</p> -->
        <div>
           <img src="/static/user/login/images/GCNY.png" style="width: 70%;height: 80px">
        </div>
        
        <div class="username loginInput">
            <p class="errorTip" style="display: none" id="userNameError">请输入正确的用户名</p>
            <i class="iconfont icon-zhanghao"></i>
            <input type="text" placeholder="请输入用户名" id="userName">

        </div>
        <div class="password loginInput">
            <p class="errorTip" style="display: none" id="passwordError">请输入正确的密码</p>
            <i class="iconfont icon-mima"></i>
            <input type="password" placeholder="请输入密码" id="password">
        </div>

        <div class="loginBtn">
            <input type="button" value="登录" id="userLogin">
        </div>
        
        <!-- <div style="text-align: center;margin-top:24px;">
            <a href="/forgetPassword.htm" style="color: #ff6700">忘记密码</a>
        </div> -->
        
    </div>
    <div class="loginAq">
        <!-- <span>梓旭网® 粤ICP备12345678号-6</span>
        <span>24小时服务热线：0757-82700607</span> -->
    </div>
    <div class="loadingBox" style="display:none" id="loadingBox">
        <svg viewBox="0 0 120 120" version="1.1">
            <g class="g-circles">
                <circle transform="translate(35, 16.698730) rotate(-30) translate(-35, -16.698730) " cx="35" cy="16.6987298" r="8"></circle>
                <circle transform="translate(16.698730, 35) rotate(-60) translate(-16.698730, -35) " cx="16.6987298" cy="35" r="8"></circle>
                <circle transform="translate(10, 60) rotate(-90) translate(-10, -60) " cx="10" cy="60" r="8"></circle>
                <circle transform="translate(16.698730, 85) rotate(-120) translate(-16.698730, -85) " cx="16.6987298" cy="85" r="8"></circle>
                <circle transform="translate(35, 103.301270) rotate(-150) translate(-35, -103.301270) " cx="35" cy="103.30127" r="8"></circle>
                <circle cx="60" cy="110" r="8"></circle>
                <circle transform="translate(85, 103.301270) rotate(-30) translate(-85, -103.301270) " cx="85" cy="103.30127" r="8"></circle>
                <circle transform="translate(103.301270, 85) rotate(-60) translate(-103.301270, -85) " cx="103.30127" cy="85" r="8"></circle>
                <circle transform="translate(110, 60) rotate(-90) translate(-110, -60) " cx="110" cy="60" r="8"></circle>
                <circle transform="translate(103.301270, 35) rotate(-120) translate(-103.301270, -35) " cx="103.30127" cy="35" r="8"></circle>
                <circle transform="translate(85, 16.698730) rotate(-150) translate(-85, -16.698730) " cx="85" cy="16.6987298" r="8"></circle>
                <circle cx="60" cy="10" r="8"></circle>
            </g>
        </svg>
    </div>
    <!--遮罩层-->
    <div class="shadeBg" style="display:none" id="shadeBg"></div>

</body>
<script>

    var loginBoolean = true;

    document.onkeydown = function (event) {
        if (event.keyCode == 13) {
            login();
        }
    }

    document.getElementById('userLogin').onclick = function () {
        login();
    }



    function login() {
        if (!loginBoolean) {
            return;
        }
        var userName = document.getElementById("userName").value;
        var password = document.getElementById("password").value;
        if (userName == null || userName == "") {
            var userNameError = document.getElementById("userNameError");
            userNameError.innerHTML = "请输入正确的用户名";
            userNameError.style.display = "block";
            return;
        }
        if (password == null || password == "") {
            var passwordError = document.getElementById("passwordError");
            passwordError.innerHTML = "请输入正确的密码";
            passwordError.style.display = "block";
            return;
        }
        document.getElementById('userNameError').style.display = "none";
        document.getElementById('passwordError').style.display = "none";
        document.getElementById('shadeBg').style.display = "block";
        document.getElementById('loadingBox').style.display = "block";

        httpRequest({
            url: baseUrl + '/user/login',
            type: "post",
            data: JSON.stringify({
                userName: userName,
                password: password
            }),
            success: function (res) {
                loginBoolean = true;
                if (!res.resultCode) {
                    switch (res.errCode) {
                        case 100002:
                            var userNameError = document.getElementById("userNameError");
                            userNameError.innerHTML = "用户不存在";
                            userNameError.style.display = "block";
                            return;
                        case 100003:
                            var passwordError = document.getElementById("passwordError");
                            passwordError.innerHTML = "密码错误";
                            passwordError.style.display = "block";
                            return;
                        default:
                            alert("系统繁忙");
                            return;
                    }
                    return;
                }
                
                if(res.data!=null&&res.data!='null'){
                    res.data=JSON.parse(res.data)
                }
                
                localStorage.setItem("loginCookieKey", res.data.cookieKey);
                localStorage.setItem('loginUserName', res.data.name);
                localStorage.setItem('loginToken', res.data.token);
                setCookie(res.data.cookieKey, res.data.token, 9999999999999);
                setTimeout(function () {
                     window.location.href = "/" + res.data.type;
                }, 100);
            },
            error: function () {
                alert('网络错误 ,请检查您的网络连接是否畅通无阻!')
            },
            complete: function () {
                loginBoolean = true;
                document.getElementById('shadeBg').style.display = "none";
                document.getElementById('loadingBox').style.display = "none";
            },
        });
    }

    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires + "; path=/";
    }
    
    function IEVersion() {
        var userAgent = navigator.userAgent;
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1;
        var isEdge = userAgent.indexOf("Edge") > -1 && !isIE;
        var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
        if(isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if(fIEVersion == 7) {
                return 7;
            } else if(fIEVersion == 8) {
                return 8;
            } else if(fIEVersion == 9) {
                return 9;
            } else if(fIEVersion == 10) {
                return 10;
            } else {
                return 6;
            }   
        } else if(isEdge) {
            return 'edge';
        } else if(isIE11) {
            return 11;
        }else{
            return -1;
        }
    }
    if(IEVersion() != -1){
        window.location.href = "/isie";
    }

</script>
</html>