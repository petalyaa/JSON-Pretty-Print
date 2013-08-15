Application.prototype.constructor = Application;

function Application() {
	
}

Application.prototype.toJsonObject = function(string) {
	var json = new Object();
	try {
		json = $.parseJSON(string);
	} catch (e) {
	}
	return json;
};

Application.prototype.loadDiv = function(url, container, data) {
	var parent = this;
	var complete = function(data, textStatus, jqXHR) {
		$("#" + container).html(jqXHR.responseText);
	};
	parent.ajaxCall({url : url, data : data, callbackFn : complete});
};

Application.prototype.showLoadingWindow = function(jqXHR, settings,parentThis) {
	// TODO : Show loading window
};

Application.prototype.hideLoadingWindow = function() {
	// TODO : Hide loading window
};

Application.prototype.ajaxCall = function(config) {
	var parent = this;
	var async = true;
	var type = 'GET';
	var url  = '';
	var showLoading = false;
	var beforeSend = null;
	var complete = null;
	var callbackFn = null;
	var showErrorFn = null;
	var data = null;
	if(typeof config != 'undefined') {
		if(typeof config.async != 'undefined') {
			async = config.async;
		}
		if(typeof config.data != 'undefined') {
			type = 'POST';
			data = config.data;
		}
		if(typeof config.url != 'undefined') {
			url = config.url;
		}
		if(typeof config.showLoading != 'undefined') {
			showLoading = config.showLoading;
		}
		if(typeof config.callbackFn != 'undefined') {
			callbackFn = config.callbackFn;
		}
		if(typeof config.showErrorFn != 'undefined') {
			showErrorFn = config.showErrorFn;
		}
		if(showLoading) {
			beforeSend = function(jqXHR, settings){
				parent.showLoadingWindow(jqXHR, settings,parentThis);
			};
			complete=function (){
				parent.hideLoadingWindow();
			};
		}
	}
	var ajax = $.ajax({  
		  type: type,  
		  url: url,  
		  data: data,
		  beforeSend:beforeSend,
		  complete:complete,
		  success: callbackFn,
		  error: showErrorFn,
		  async : async
	});
	if(!async)
		return ajax.responseText;
};