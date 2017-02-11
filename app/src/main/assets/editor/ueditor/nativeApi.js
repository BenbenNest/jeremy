/**
 * Android Native Interface related
 */
(function(g) {
	if (g.FastClick) FastClick.attach(document.body);

	var api = g.$nativeApi = {},
		$wrapper = $('.wrapper');
	g.$nativeSimulator = {
		'$_editor': {}
	};

	function tryget(o, path) {
		var parts = path.split('.'),
			part, len = parts.length,
			prev;

		for (var t = o, i = 0; i < len; ++i) {
			part = parts[i];
			if (part in t) {
				prev = t;
				t = t[parts[i]]; 
			} else {
				return {
					obj: null,
					prop: null
				};
			}
		}
		return {
			obj: prev,
			prop: t
		};
	}

	function navigateToUrl(url) {
		g.location.href = decodeURIComponent(url);
		return false;
	}
	/**
	 * 生成native接口调用函数
	 * @param  {String} path   [native接口,比如$_object.method]
	 * @param  {Function} before [调用native接口之前调用的函数，如果该函数返回false则不调用native接口]
	 * @param  {Function} after  [调用native接口之后调用的函数，如果native函数返回false则不调用该函数]
	 * @return {Function}        [调用native接口的包装函数]
	 */
	function __(path, before, after) {
		path = path.replace(/\(.*$/, '')

		function f() {

			var ret, x = tryget(g, path),
				isSimulator = false;
			if (x.prop == null) {
				x = tryget(g.$nativeSimulator, path);
				if (x) isSimulator = true;
			}
			if (x.prop && x.obj) {

				console.log('//调用Native接口 ' + path + "参数:\n" + JSON.stringify(arguments));
				if (typeof x.prop == 'function') {
					x.prop.before = before;
					x.prop.after = after;
					if (x.prop.length != arguments.length) {
						console.log('//警告:实参与形参数量不一致:' + arguments.length + '/' + x.prop.length);
					}
					//如果实在安卓客户端，就必须用x.obj
					//否则会出现NPMethod called on non-NPObject错误
					ret = x.prop.apply(isSimulator ? this : x.obj, arguments);

				} else {
					ret = x.prop;
				}
				console.log('//完毕,返回:' + JSON.stringify(ret));
			} else {
				if (g.console && console.log) {
					console.log('//未实现Native接口 ' + path + "参数:\n" + JSON.stringify(arguments));
				}
			}
			return ret;
		}
		f.before = before;
		f.after = after;
		return f;
	}
	$nativeApi.__ = __;

	var $_editor = api.editor = {};

	$_editor.getEditorContent = __('$_editor.OnSendContent()');

	function postIfoToNative(fnName,arg){
		var args = [arg],
			x = tryget(api, fnName),
			isCallNative = true,
			isCallAfter,
			tmp;
		
		if (x.prop && x.prop.before) {
			isCallNative = x.prop.before;
		}
		if (isCallNative && x.prop) {
			isCallAfter = x.prop.apply(this, args);

			if (isCallAfter !== false && x.prop.after) {
				x.prop.after;
			}
		}
	}

	 window.postIfoToNative = postIfoToNative;

	// 生成没有工具栏的编辑器
	window.noToolbar = new UE.Editor();

	noToolbar.render('myEditor',{
		autoFloatEnabled : false
	})

	// 保存编辑内容的时候调用此方法
	window.onSaveEditor = function(){
		var contents = noToolbar.getContent();
		if(contents){
			contents = '<div class=\"view\">'+contents+'</div>';
		}
		postIfoToNative('editor.getEditorContent',contents)
	}

	//noToolbar.ready(function(){

		//分割线
		/*noToolbar.execCommand( 'horizontal');*/

		//链接
		/*noToolbar.execCommand( 'link', {
			'href':'http://www.so.com', // href属性的值
			'textValue':"好搜",// 链接内容
			'target':"_self"} // target属性
		);*/

		// //有序列表
		/*noToolbar.execCommand( 'insertorderedlist','decimal');*/

		// //无序列表
		/*noToolbar.execCommand( 'insertunorderedlist','dot');*/


		// // 添加引用
		/*noToolbar.execCommand( 'blockquote' )*/

		//通过 getContent 和 setContent 方法可以设置和读取编辑器的内容
		/*noToolbar.getContent();*/
		//noToolbar.setContent('<hr/><p><a href="http://www.so.com" target="_self">好搜</a>hello<br/></p>');

		//状态改变时获取当前行的状态
		// noToolbar.addListener('selectionchange',function(){
		// 	 if( noToolbar.queryCommandState( 'insertorderedlist' )){
		// 		console.log('有序列表')
		// 	 }else if( noToolbar.queryCommandState( 'insertunorderedlist' )){
		// 		console.log('无序列表')
		// 	 }else if( noToolbar.queryCommandState( 'blockquote' )){
		// 		console.log('blockquote')
		// 	 } else{
		// 		console.log('普通p标签')
		// 	 };
		// 	 if( noToolbar.queryCommandValue( 'link' )){
		// 		console.log('链接')
		// 	 }
		//  })
		
	//})

	
})(this)