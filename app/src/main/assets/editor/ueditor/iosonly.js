var iosOnly = (function () {
	var IO = {},
		bridge = null,
		body = $('body');

	// 消息配发中心
	var sendMessageToApp = function (data) {
		bridge.callHandler('JSCallCenter', data, function (response) {
                           
		});
	};
	IO.initialize = function (_bridge) {
		bridge = _bridge;
	};
	window.noToolbar = new UE.Editor();
	noToolbar.render('myEditor',{
		autoFloatEnabled : false
	});
	
	// 保存编辑内容的时候调用此方法
	IO.onSaveEditor = function(){
		var contents = noToolbar.getContent();
		if(contents){
			contents = '<div class=\"view\">'+contents+'</div>';
		}
		sendMessageToApp({
			handle: 'link',
			type: '3005',
			data: contents
		});
	}

	return IO;
})();

/**
*	WebViewReady启动入口
*/
window.onerror = function(err) {
	log('window.onerror: ' + err)
}

function connectWebViewJavascriptBridge(callback) {
	if (window.WebViewJavascriptBridge) {
		callback(WebViewJavascriptBridge)
	} else {
		document.addEventListener('WebViewJavascriptBridgeReady', function() {
		  callback(WebViewJavascriptBridge)
		}, false)
	}
}

connectWebViewJavascriptBridge(function(bridge) {

	var uniqueId = 1
	function log(message, data) {
		var log = document.getElementById('log')
		var el = document.createElement('div')
		el.className = 'logLine'
		el.innerHTML = uniqueId++ + '. ' + message + ':<br/>' + JSON.stringify(data)
		if (log.children.length) { log.insertBefore(el, log.children[0]) }
		else { log.appendChild(el) }
	}

	bridge.init(function(message, responseCallback) {});

	/* 分割线 */
	bridge.registerHandler('JavascriptEditorHrHandler', function(data, responseCallback) {
		noToolbar.execCommand( 'horizontal');
	});

	/* 有序列表 */
	bridge.registerHandler('JavascriptOrderedlistHandler', function(data, responseCallback) {
		noToolbar.execCommand( 'insertorderedlist','decimal');
	});

	/* 无序列表 */
	bridge.registerHandler('JavascriptUnOrderedlistHandler', function(data, responseCallback) {
		noToolbar.execCommand( 'insertunorderedlist','dot');
	});

	/* 添加引用 */
	bridge.registerHandler('JavascriptBlockquoteHandler', function(data, responseCallback) {
       
		noToolbar.execCommand( 'blockquote');
	});

	/* 添加链接 */
	bridge.registerHandler('JavascriptLinkHandler', function(data, responseCallback) {
		/** 
		 * 链接的格式：
		 {
			"href":"http://www.so.com", // href属性的值
			"textValue":"好搜",// 链接内容
			"target":"_self"// target属性
		 } 
		*/
		noToolbar.execCommand( 'link', data);
	});
	/* 初始化编辑器内容 */
	bridge.registerHandler('JavascriptInitEditorHandler', function(data, responseCallback) {
		if(data){
			noToolbar.setContent(data);
		}
		 iosOnly.initialize(bridge);
		
	});

	/* 保存编辑器内容 */
	bridge.registerHandler('JavascriptSaveEditorHandler', function(data, responseCallback) {
		iosOnly.onSaveEditor();
	});

});
