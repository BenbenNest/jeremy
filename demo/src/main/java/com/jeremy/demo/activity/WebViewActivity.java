package com.jeremy.demo.activity;

import android.content.Context;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jeremy.demo.R;
import com.jeremy.library.jsobject.ImageViewJavaScriptObj;
import com.jeremy.library.widget.MyWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

//http://m.blog.csdn.net/article/details?id=52150051#weixin.qq.com

public class WebViewActivity extends AppCompatActivity {
    private ImageViewJavaScriptObj mImageViewJavaScriptObj;

    @BindView(R.id.webview)
    MyWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
//        init();
        mImageViewJavaScriptObj = new ImageViewJavaScriptObj(this);
        webview.addJavascriptInterface(mImageViewJavaScriptObj, "imageListener");
//        webview.loadUrl("file:///android_asset/js.html");
        webview.loadUrl("http://www.baidu.com");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mImageViewJavaScriptObj.parseHTML(view);
//                mImageViewJavaScriptObj.setImageClickListner(view);
//                view.loadUrl("javascript:window.imageListener.getAllImageUrlFromHtml(document.getElementsByTagName('html')[0].innerHTML);");
//                view.loadUrl("javascript:window.imageListener.getAllImageUrlFromHtml('<head>'+"
//                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                view.loadUrl("javascript:window.imageListener.getAllImageUrlFromHtml("
//                        + "document.getElementsByTagName('html')[0].innerHTML);");
//                view.loadUrl("javascript:alert(document.getElementsByTagName('html')[0].innerHTML);");
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

//        webview.setPictureListener(new WebView.PictureListener() {
//            @Override
//            public void onNewPicture(WebView view, Picture picture) {
//            }
//        });

    }

    class JavaScriptObject {
        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface //sdk17版本以上加上注解
        public void fun1FromAndroid(String name) {
            Toast.makeText(mContxt, name, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void fun2(String name) {
            Toast.makeText(mContxt, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
//        mImageViewJavaScriptObj = new ImageViewJavaScriptObj(this);

//        String html = "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "    <head>\n" +
//                "        <meta charset=\"utf-8\">\n" +
//                "        <meta name=\"renderer\" content=\"webkit\">\n" +
//                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n" +
//                "        <title>蔚来-创造愉悦生活方式活</title>\n" +
//                "        <style type=\"text/css\">\n" +
//                "            html {\n" +
//                "                -ms-text-size-adjust: 100%;\n" +
//                "                -webkit-text-size-adjust: 100%;\n" +
//                "                line-height: 1.6;\n" +
//                "                font-family: -apple-system-font, \"PingFang SC\", \"Hiragino Sans GB\", \"Microsoft YaHei\", \"Helvetica Neue\", sans-serif, Helvetica, Arial;\n" +
//                "                word-break: break-word;\n" +
//                "            }\n" +
//                "            body {\n" +
//                "                font-size: 14px;\n" +
//                "                -webkit-touch-callout: none;\n" +
//                "                background-color: #fff;\n" +
//                "            }\n" +
//                "            * {\n" +
//                "                margin: 0;\n" +
//                "                padding: 0;\n" +
//                "            }\n" +
//                "            a {\n" +
//                "                color: #607fa6;\n" +
//                "                text-decoration: none;\n" +
//                "            }\n" +
//                "            .rich_media_content {\n" +
//                "                overflow: hidden;\n" +
//                "                color: #3e3e3e; \n" +
//                "            }\n" +
//                "            .rich_media_content * {\n" +
//                "                max-width: 100% !important;\n" +
//                "                box-sizing: border-box !important;\n" +
//                "                -webkit-box-sizing: border-box !important;\n" +
//                "                word-wrap: break-word !important;\n" +
//                "            }\n" +
//                "            .rich_media_content p {\n" +
//                "                clear: both;\n" +
//                "                min-height: 1em;\n" +
//                "                white-space: pre-wrap;\n" +
//                "            }\n" +
//                "            .rich_media_content img {\n" +
//                "                vertical-align: middle;\n" +
//                "            }\n" +
//                "            .rich_media_content em {\n" +
//                "                font-style: italic;\n" +
//                "            }\n" +
//                "            .rich_media_content fieldset {\n" +
//                "                min-width: 0;\n" +
//                "            }\n" +
//                "            .rich_media_content .list-paddingleft-2 {\n" +
//                "                padding-left: 30px;\n" +
//                "            }\n" +
//                "            .rich_media_content blockquote {\n" +
//                "                margin: 0;\n" +
//                "                padding-left: 10px;\n" +
//                "                border-left: 3px solid #dbdbdb;\n" +
//                "            }\n" +
//                "        </style>\n" +
//                "    </head>\n" +
//                "\n" +
//                "    <body>\n" +
//                "        <div class=\"rich_media_content\">\n" +
//                "            <section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"color: rgb(108, 108, 108);\"><p style=\"text-align: justify; white-space: normal;\">最怕假期突然结束，<span style=\"letter-spacing: 0px;\">最怕领导突然关心，最怕邮箱突然刷出无数 brief ，最怕的上班日又来到，假期续费无效，节后症状频发，让人如何是好？<span style=\"letter-spacing: 0px; color: rgb(1, 1, 1);\">“节后综合症”自救指南已上线，对号入座，自行诊断。</span></span></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: center; position: static;\"><img class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/880931cf-d2f8-4510-beb7-b28d114bf295.png\" data-ratio=\"1.4953271\" data-w=\"640\" data-width=\"640\" data-height=\"428\" imguploaded=\"1\"></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"font-size: 18px; color: rgb(19, 212, 200);\"><p>症状一：每逢佳节胖三斤</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section></section><div class=\"h5Component pkContainer\" version=\"1\" id=\"20390\"><div class=\"pkTopic\">大吃大喝爽翻天，现在香菇蓝瘦了？</div><div class=\"pkBodyContainer\"><div class=\"flagContainer\"><div class=\"pointAFlag pkFlag\">红方观点</div><div class=\"pkV pkVS\">V</div><div class=\"pkS pkVS\">S</div><div class=\"pointBFlag pkFlag\">蓝方观点</div></div><div class=\"pointContainer\"><div class=\"pointA pkPoint\">别提了，衣服都穿不下了。</div><div class=\"pointB pkPoint\">还行还行，只是略微长了点肉。</div></div><div class=\"pkZone\"><div class=\"pkButtonA pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10780\"></div><div class=\"pkButtonB pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10781\"></div><div class=\"barContainer\"><div class=\"pkBar barA\"></div><div class=\"pkBar barB\"></div><span class=\"pkCount countA\">0</span><span class=\"pkCount countB\">0</span><span class=\"pkRate rateA\">0%</span><span class=\"pkRate rateB\">0%</span></div></div></div><script id=\"commonScript\">function $init(callback) { window.$environment = \"sharingPage\"; window.$userInfo = { host: \"\" }; if (document.body.className == \"preview\") { $environment = \"previewer\"; callback && callback(); } else if (navigator.userAgent.match(\"Lifestyle-Android$\")) { $environment = \"android\"; } else if (navigator.userAgent.match(\"Lifestyle-iOS$\")) { $environment = \"ios\"; } else if (typeof $targetComponent != 'undefined') { $environment = \"editor\"; } else { callback && callback(); } if ($environment == \"android\" || $environment == \"ios\") { if (typeof getJsBridge == 'undefined') { setTimeout(function () { $init(callback); }, 10); return; } getJsBridge().call(\"getLoginInfo\", {}, function (responseData) { window.$userInfo = responseData ? JSON.parse(responseData) : {}; callback && callback(); }); } }; function $redirect() { if ($environment == \"android\" || $environment == \"ios\") { getJsBridge().call(\"redirectToLoginView\", {}, function () {}); } else if ($environment == \"sharingPage\" || $environment == \"previewer\") { if ($(\".download-link\").length) { $(\".download-link\").click(); } } } function ajax(method, url, header, body, callback) { if (window.$environment == \"android\" || window.$environment == \"ios\") { getJsBridge().call(\"ajax\", { method: method, url: window.$userInfo.host + url, header: header, postData: body }, function (responseText) { var responseData = responseText ? JSON.parse(responseText) : null; callback && callback(responseData); }); } else { var xhr = new XMLHttpRequest(); xhr.open(method, url, true); xhr.onreadystatechange = function () { if (xhr.readyState != 4) { return; } if (xhr.status == 200) { var responseData = JSON.parse(xhr.responseText); callback && callback(responseData); } else { callback && callback(); } }; if (!body) { xhr.send(); return; } var keys = Object.keys(body); var queryString = \"\"; keys.forEach(function (key) { queryString += key + \"&\" + body[key] + \"&\" }); xhr.send(queryString.substring(0, queryString.length - 1)); } }</script><script id=\"pkScript\">$init(initCallback); function initCallback() { var targets = document.getElementsByClassName(\"pkContainer\"); \n" +
//                "Array.prototype.forEach.call(targets, function (target) { if (window.$environment == \"android\" || window.$environment == \"ios\") { target.querySelector(\".pkButtonA\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_redline_icon.png\" + \"')\"; target.querySelector(\".pkButtonB\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_blueline_icon.png\" + \"')\"; } ajax(\"GET\", \"/api/1/plugin/point/\" + target.id + \"/status?app_id=\" + (window.$userInfo.app_id || \"\") + \"&region=cn&lang=zh-cn\", { Authorization: \"Bearer \" + (window.$userInfo.access_token || \"\") }, null, function (responseData) { \n" +
//                "if (!responseData) { return; } \n" +
//                "var totalWidth = parseFloat(window.getComputedStyle(target.querySelector(\".barContainer\")).width); \n" +
//                "var aCount = responseData.data.options[target.querySelector(\".pkButtonA\").id].pick_count; \n" +
//                "var bCount = responseData.data.options[target.querySelector(\".pkButtonB\").id].pick_count; \n" +
//                "var barA = target.querySelector(\".barA\"); \n" +
//                "var barB = target.querySelector(\".barB\"); \n" +
//                "var countA = target.querySelector(\".countA\"); \n" +
//                "var countB = target.querySelector(\".countB\"); \n" +
//                "var rateA = target.querySelector(\".rateA\"); \n" +
//                "var rateB = target.querySelector(\".rateB\"); \n" +
//                "var aRate = 0.5; \n" +
//                "countA.innerText = aCount; \n" +
//                "countB.innerText = bCount; if (aCount || bCount) { \n" +
//                "aRate = aCount / (aCount + bCount); } \n" +
//                "var aWidth = totalWidth * aRate; \n" +
//                "barA.style.width = aWidth + \"px\"; \n" +
//                "barB.style.width = Math.max(0, aWidth ? totalWidth - aWidth - 1 : totalWidth) + \"px\"; \n" +
//                "var rateAValue = parseInt(aRate * 100 + 0.5); \n" +
//                "rateA.innerText = rateAValue + \"%\"; \n" +
//                "rateB.innerText = (100 - rateAValue) + \"%\"; if (responseData.data.options[target.querySelector(\".pkButtonA\").id].user_picked) { \n" +
//                "target.$voted = true; \n" +
//                "target.querySelector(\".pkButtonA\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_red_icon.png\" + \"')\"; \n" +
//                "target.querySelector(\".pkButtonB\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_gry_right_icon.png\" + \"')\"; } else if (responseData.data.options[target.querySelector(\".pkButtonB\").id].user_picked) { \n" +
//                "target.$voted = true; \n" +
//                "target.querySelector(\".pkButtonA\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_gry_left_icon.png\" + \"')\"; \n" +
//                "target.querySelector(\".pkButtonB\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_blue_icon.png\" + \"')\"; } }); }); } function onPKOptionSelected(e) { if (window.$userInfo.access_token) { var pkContainer = e.parentNode.parentNode.parentNode; if (pkContainer.$voted) { return; } else { pkContainer.$voted = true; ajax(\"POST\", \"/api/1/plugin/point/pick?app_id=\" + window.$userInfo.app_id + \"&region=cn&lang=zh-cn\", { Authorization: \"Bearer \" + (window.$userInfo.access_token || \"\") }, { point_id: pkContainer.id, option_id: e.id }, function (responseData) { if (!responseData) { pkContainer.$voted = false; return; } pkContainer.$voted = true; if (responseData.result_code == \"already_voted\") { return; } \n" +
//                "var barA = pkContainer.querySelector(\".barA\"); \n" +
//                "var barB = pkContainer.querySelector(\".barB\"); \n" +
//                "var countA = pkContainer.querySelector(\".countA\"); \n" +
//                "var countB = pkContainer.querySelector(\".countB\"); \n" +
//                "var rateA = pkContainer.querySelector(\".rateA\"); \n" +
//                "var rateB = pkContainer.querySelector(\".rateB\"); \n" +
//                "var aCount = parseInt(countA.innerText); \n" +
//                "var bCount = parseInt(countB.innerText); if (e.className.indexOf(\"pkButtonA\") >= 0) { \n" +
//                "aCount++; \n" +
//                "e.style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_red_icon.png\" + \"')\"; \n" +
//                "pkContainer.querySelector(\".pkButtonB\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_gry_right_icon.png\" + \"')\"; } else { \n" +
//                "bCount++; \n" +
//                "pkContainer.querySelector(\".pkButtonA\").style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_gry_left_icon.png\" + \"')\"; \n" +
//                "e.style.backgroundImage = \"url('\" + window.$userInfo.host + \"/dist/imgs/vote_blue_icon.png\" + \"')\"; } \n" +
//                "countA.innerText = aCount; \n" +
//                "countB.innerText = bCount; \n" +
//                "var aRate = aCount / (aCount + bCount); \n" +
//                "var totalWidth = parseFloat(window.getComputedStyle(pkContainer.querySelector(\".barContainer\")).width); \n" +
//                "var aWidth = totalWidth * aRate; \n" +
//                "barA.style.width = aWidth + \"px\"; \n" +
//                "barB.style.width = Math.max(0, aWidth ? totalWidth - aWidth - 1 : totalWidth) + \"px\"; \n" +
//                "var rateAValue = parseInt(aRate * 100 + 0.5); \n" +
//                "rateA.innerText = rateAValue + \"%\"; \n" +
//                "rateB.innerText = (100 - rateAValue) + \"%\"; }); } } else { $redirect(); } }</script></div><section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">诊断：春节中的“满汉全席”，造就了我们如今的模样。穿衣显胖脱衣肥肉，过去的裤围是我逝去的青春，早已满足不了日益剧增的肥膘。</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 40%; padding-right: 5px;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"line-height: 1.4; font-size: 16px; letter-spacing: 2px; color: rgb(19, 212, 200);\"><p>运动卫衣</p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"font-size: 12px;\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: right; margin-top: 10px; margin-bottom: 10px; transform: translate3d(40px, 0px, 0px); position: static;\"><img style=\"border: 4px solid rgb(255, 255, 255); border-radius: 0px;\" class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/03b2cc65-46f4-4e25-a50f-607358eaeeec.jpg\" data-ratio=\"0.855615\" data-w=\"640\" data-width=\"640\" data-height=\"748\" imguploaded=\"1\"></section></section> </section><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 60%;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: center; margin: 15px 0% 10px; position: static;\"><img class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/8140ce76-0a42-48bf-9b6c-3d172c804ede.png\" data-ratio=\"1.3361169\" data-w=\"640\" data-width=\"640\" data-height=\"479\" imguploaded=\"1\"></section></section> </section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"color: rgb(108, 108, 108);\"><p>指南：运动让你满血复活，找回自信状态。为自己准备一套甩脂战衣，让减肥终止你的假期。</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"font-size: 18px; color: rgb(19, 212, 200);\"><p>症状二: 身在“曹营”，心在“汉”</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section></section><div class=\"h5Component pkContainer\" version=\"1\" id=\"20391\"><div class=\"pkTopic\">今天的上班状态如何？</div><div class=\"pkBodyContainer\"><div class=\"flagContainer\"><div class=\"pointAFlag pkFlag\">红方观点</div><div class=\"pkV pkVS\">V</div><div class=\"pkS pkVS\">S</div><div class=\"pointBFlag pkFlag\">蓝方观点</div></div><div class=\"pointContainer\"><div class=\"pointA pkPoint\">人在公司，心在外，宝宝心里苦！</div><div class=\"pointB pkPoint\">像打了鸡血一样，满血复活！</div></div><div class=\"pkZone\"><div class=\"pkButtonA pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10782\"></div><div class=\"pkButtonB pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10783\"></div><div class=\"barContainer\"><div class=\"pkBar barA\"></div><div class=\"pkBar barB\"></div><span class=\"pkCount countA\">0</span><span class=\"pkCount countB\">0</span><span class=\"pkRate rateA\">0%</span><span class=\"pkRate rateB\">0%</span></div></div></div></div><section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">诊断：疯玩模式关闭，一键开启上班模式，快速进入另一个状态难免有些不适应，往办公桌前一坐就犯困，看着屏幕望眼欲穿，生无可恋。</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 60%; border-width: 0px;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: center; margin: 15px 0% 10px; position: static;\"><img class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/61dbb8e2-0fd6-44ea-b88b-6e92676eaa4f.jpg\" data-ratio=\"1.3333333\" data-w=\"640\" data-width=\"640\" data-height=\"480\" imguploaded=\"1\"></section></section> </section><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 40%; padding-left: 5px;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: right; line-height: 1.4; font-size: 16px; letter-spacing: 2px; color: rgb(19, 212, 200);\"><p>笔记本/笔</p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: right; font-size: 12px;\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"margin: 20px 0% 10px; transform: translate3d(-40px, 0px, 0px); position: static;\"><img style=\"border: 4px solid rgb(255, 255, 255); border-radius: 0px; width: 100%;\" class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/89589178-7969-4245-9c7c-a3e2e84d0672.jpeg\" data-ratio=\"1.3333333\" data-w=\"640\" _width=\"100%\" data-width=\"640\" data-height=\"480\" imguploaded=\"1\"></section></section> </section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">指南：给自己准备喜欢的纸笔，梳理近期的工作任务，找回工作的感觉。</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"font-size: 18px; color: rgb(19, 212, 200);\"><p>症状三：摆拍无数找不到好的角度<br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section></section><div class=\"h5Component pkContainer\" version=\"1\" id=\"20392\"><div class=\"pkTopic\">现在的你比较像“国宝”，还是“网红”？</div><div class=\"pkBodyContainer\"><div class=\"flagContainer\"><div class=\"pointAFlag pkFlag\">红方观点</div><div class=\"pkV pkVS\">V</div><div class=\"pkS pkVS\">S</div><div class=\"pointBFlag pkFlag\">蓝方观点</div></div><div class=\"pointContainer\"><div class=\"pointA pkPoint\">大熊猫多可爱，大家都爱我！</div><div class=\"pointB pkPoint\">我就是专业靠脸“吃饭”的！</div></div><div class=\"pkZone\"><div class=\"pkButtonA pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10784\"></div><div class=\"pkButtonB pkButton\" onclick=\"onPKOptionSelected(this)\" id=\"10785\"></div><div class=\"barContainer\"><div class=\"pkBar barA\"></div><div class=\"pkBar barB\"></div><span class=\"pkCount countA\">0</span><span class=\"pkCount countB\">0</span><span class=\"pkRate rateA\">0%</span><span class=\"pkRate rateB\">0%</span></div></div></div><style id=\"pkStyle\">.pkContainer { margin: 5px 0; border: 1px solid #eee; border-radius: 4px; } .pkTopic { margin: 10px; color: #424a4d; font-size: 20px; line-height: 24px; } .pkBodyContainer { border-radius: 0 0 10px 10px; } .flagContainer { position: relative; height: 27px; } .pkFlag { position: absolute; width: calc(50% - 22px); height: 100%; line-height: 27px; top: 0; color: #fff; font-size: 16px; } .pointAFlag { left: 0; padding-left: 10px; border-bottom: 27px solid #ff4949; border-right: 14px solid transparent; } .pointBFlag { right: 0; padding-right: 10px; text-align: right; border-bottom: 27px solid #42bfff; border-left: 14px solid transparent; } .pkVS { position: absolute; top: -5px; font-size: 36px; line-height: 36px; width: 50%; } .pkV { left: 0; color: #ff4949; text-align: right; } .pkS { right: 0; color: #42bfff; text-align: left; } .pkPoint { width: calc(50% - 22px); display: inline-block; vertical-align: top; padding: 5px 10px; line-height: 16px; } .pointA { margin-right: 44px; } .pkZone { position: relative; height: 60px; } .barContainer { position: absolute; left: 60px; right: 60px; height: 100%; } .pkButton { position: absolute; top: 10px; width: 40px; height: 40px; background-size: cover; } .pkButtonA { left: 10px; background-image: url(\"/dist/imgs/vote_redline_icon.png\"); } .pkButtonB { right: 10px; background-image: url(\"/dist/imgs/vote_blueline_icon.png\"); } .pkBar { position: absolute; top: 26px; height: 8px; border-radius: 3px; } .barA { left: 0; background-color: #ff4949; } .barB { right: 0; background-color: #42bfff; } .pkCount { position: absolute; top: 6px; color: #959595; } .countA { left: 0; } .countB { right: 0; } .pkRate { position: absolute; top: 34px; } .rateA { left: 0; color: #ff4949; } .rateB { right: 0; color: #42bfff; }</style></div><section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">诊断：春节期间昼夜不分，生物钟完全被打乱。上班第一天办公室挤满了“国宝大熊猫”，好端端的网红脸就这样毁了。</p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 40%; padding-right: 5px;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"line-height: 1.4; font-size: 16px; letter-spacing: 2px; color: rgb(19, 212, 200);\"><p>自拍补光灯</p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"font-size: 12px;\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: right; margin-top: 10px; margin-bottom: 10px; transform: translate3d(40px, 0px, 0px); position: static;\"><img style=\"border: 4px solid rgb(255, 255, 255); border-radius: 0px;\" class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/c16aa2fe-6a19-44cf-ac1a-15dc4a07437e.jpg\" data-ratio=\"1.3333333\" data-w=\"640\" data-width=\"640\" data-height=\"480\" imguploaded=\"1\"></section></section> </section><section class=\"\" style=\"display: inline-block; vertical-align: top; width: 60%;\"> <section class=\"Powered-by-XIUMI V5\" style=\"position: static;\" powered-by=\"xiumi.us\"><section class=\"\" style=\"text-align: center; margin: 15px 0% 10px; position: static;\"><img class=\"\" src=\"https://cdn-app-test.nio.com/user/2017/2/3/8f213446-a99d-472b-9099-9a7ca126c0fd.png\" data-ratio=\"1.3333333\" data-w=\"640\" data-width=\"640\" data-height=\"480\" imguploaded=\"1\"></section></section> </section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">指南：拿出自拍神器“补光灯”，配合美颜相机，就算玩照骗，咱们也要装备十足。<br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\"><p><br></p></section></section></section><section class=\"Powered-by-XIUMI V5\" powered-by=\"xiumi.us\"><section class=\"\" style=\"position: static;\"><section class=\"\" style=\"text-align: justify; color: rgb(108, 108, 108);\"><p style=\"white-space: normal;\">虽说休养生息的长假硬生生掰弯了生物钟，但找对了解决办法就能够迅速满血复活！是时候该醒醒了，今天的&nbsp;brief 都整理清楚了吗？那就认真上班啦。</p></section></section></section></section>\n" +
//                "        </div>\n" +
//                "        \n" +
//                "        <script type=\"text/javascript\">\n" +
//                "            window.onload = function() {\n" +
//                "                var mediaWidth = window.innerWidth;\n" +
//                "                var mediaHeight = mediaWidth * 0.7;\n" +
//                "                var allIfames = document.getElementsByTagName(\"iframe\");\n" +
//                "\n" +
//                "                for (var i = allIfames.length - 1; i >= 0; i--) {\n" +
//                "                    allIfames[i].setAttribute(\"width\", \"100%\");\n" +
//                "                    allIfames[i].setAttribute(\"height\", mediaHeight + \"px\");\n" +
//                "                }\n" +
//                "            } \n" +
//                "        </script>\n" +
//                "    </body>\n" +
//                "</html>\n";

//        setImageEvent();
//        webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
//        webview.loadUrl("http://www.baidu.com");
//        setImageEvent();
        // 长按点击事件
//        webview.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final WebView.HitTestResult hitTestResult = webview.getHitTestResult();
//                // 如果是图片类型或者是带有图片链接的类型
//                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
//                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//                    // 弹出保存图片的对话框
//                    AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
//                    builder.setTitle("提示");
//                    builder.setMessage("保存图片到本地");
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String url = hitTestResult.getExtra();
//                            // 下载图片到本地
////                            DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){
////
////                                @Override
////                                public void getDownPath(String s) {
////                                    Toast.makeText(context,"下载完成",Toast.LENGTH_LONG).show();
////                                    Message msg = Message.obtain();
////                                    msg.obj=s;
////                                    handler.sendMessage(msg);
////                                }
////                            });
//
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        // 自动dismiss
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//                return true;
//            }
//        });
    }

    private void setImageEvent() {
        mImageViewJavaScriptObj = new ImageViewJavaScriptObj(this);
        webview.addJavascriptInterface(mImageViewJavaScriptObj, "imageListener");
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mImageViewJavaScriptObj.setWebImageLongClickListener(view);
                return true;
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mImageViewJavaScriptObj.setImageClickListner(view);
                mImageViewJavaScriptObj.parseHTML(view);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }


        });

//        webview.setPictureListener(new WebView.PictureListener() {
//            @Override
//            public void onNewPicture(WebView view, Picture picture) {
//                view.loadUrl("javascript:(function(){" + "alert('test');"+
//                        "var objs = document.getElementsByTagName(\"img\");" +
//                        "for(var i=0;i<objs.length;i++)  " +
//                        "{"
//                        + "    objs[i].onclick=function()  " +
//                        "    {  "
//                        + "        window.imageListener.startShowImageActivity(this.src);  " +
//                        "    }  " +
//                        "}" +
//                        "})()");
//                // web 页面加载完成，添加监听图片的点击 js 函数
////                mImageViewJavaScriptObj.setImageClickListner(view);
////                //解析 HTML
////                mImageViewJavaScriptObj.parseHTML(view);
//            }
//        });
    }

}
