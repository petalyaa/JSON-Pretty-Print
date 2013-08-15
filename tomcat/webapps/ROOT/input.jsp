<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="row">
	<form id="input-form">
		<div class="input-title">
			<h3><fmt:message key="label.input" /></h3>
			<div class="title-control-buttons">
				<button type="button" class="btn btn-link pull-right" id="submit"><fmt:message key="label.prettify" /> &gt;</button>
				<button type="reset" class="btn btn-link pull-right" style="margin-right : 5px;" id="reset"><fmt:message key="label.clear" /></button>
			
				<button type="button" class="btn btn-link" id="save"><fmt:message key="label.save" /></button>
				<button type="button" class="btn btn-link" id="load"><fmt:message key="label.load" /></button>
			</div>
		</div>
		<div class="input-container">
			<div class="alert hide alert-error" id="error-box">
  				<strong><fmt:message key="label.error" /></strong> <span id="error-msg"></span>
			</div>
			<div class="form-group text-center">
				<textarea rows="10" cols="50" class="form-control" style="width : 100%; height: 411px;" id="json-input"></textarea>
			</div>
		</div>
	</form>
	<span class="affix"></span>
	<div class="output-title">
		<h3><fmt:message key="label.output" /></h3> 
		<div class="title-control-buttons">
			<button class="btn btn-link"><fmt:message key="label.expand.all" /></button>
			<button class="btn btn-link"><fmt:message key="label.collapse.all" /></button>
		</div>
		
	</div>
	<div class="output-container">
		<div class="path-box">
			<div class="path-label">
				<b>Path</b> <i id="help-icon" class="icon-question-sign" data-toggle="tooltip" title='<fmt:message key="msg.hover.path" />'></i> :
			</div>
			<div class="path-value" id="path-string"></div>
			<div class="clearBoth"></div>
		</div>
		<div id="debug-box"></div>
	</div>
</div>
<div class="modal hide fade in" id="save-modal">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3>Save</h3>
	</div>
	<div class="modal-body">
		<form class="form-horizontal">
			<div class="control-group error">
				<label class="control-label" for="save-modal-name">Name</label>
				<div class="controls">
					<input type="text" name="name" id="save-modal-name" placeholder='Name' />
					<span class="help-inline" id="save-modal-name-error"></span>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button class="btn btn-danger" data-dismiss="modal" aria-hidden="true" id="save-modal-no">Cancel</button>
		<button class="btn btn-primary" id="save-modal-yes">Save</button>
	</div>
</div>
<div class="modal hide fade in" id="load-modal">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3>&nbsp;</h3>
	</div>
	<div class="modal-body" style="padding-top: 0px;">
		<div class="table-static-header">
			<div class="column-name">
				Name
			</div>
			<div class="column-created-date">
				Created Date
			</div>
			<div class="column-size">
				Size (bytes)
			</div>
			<div class="clearBoth"></div>
		</div>
		<div class="table-content">
			<ul class="content-listing">
			</ul>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-danger" data-dismiss="modal" aria-hidden="true">Cancel</button>
		<button class="btn btn-primary" id="load-modal-yes">Open</button>
	</div>
</div>
<script type="text/JavaScript">
	var $submitBtn = $("#submit");
	var $resetBtn = $("#reset");
	var $errorBox = $("#error-box");
	var $saveBtn = $("#save");
	var $loadBtn = $("#load");
	var $saveModal = $("#save-modal");
	var $loadModal = $("#load-modal");
	var $errorHelper = $("#save-modal-name-error");
	var $contentListing = $(".content-listing");
	var $loadModalYes = $("#load-modal-yes");
	var selectedName = null;
	$("#help-icon").tooltip({placement : 'bottom'});
	function hideError() {
		 $errorHelper.hide();
		 $errorHelper.parent("div").parent("div").removeClass("error");
	}
	
	function showError(msg) {
		$errorHelper.html(msg);
		$errorHelper.show();
		$errorHelper.parent("div").parent("div").addClass("error");
	}
	function populateContentListing() {
		var url = 'input?${WebConstants.ACTION_NAME}=${WebConstants.GET_SAVED_FILE}';
		app.ajaxCall({url : url, callbackFn : function(data, textStatus, jqXHR) {
			var json = app.toJsonObject(jqXHR.responseText);
			if(json.status) {
				var data = json.data;
				$contentListing.empty();
				if(data.length > 0) {
					for(var i = 0; i < data.length; i++) {
						var dataRow = data[i];
						var $li = $("<li class='content-row'></li>");
						var $nameDiv = $("<div class='column-content-name'></div>");
						var $createdDateDiv = $("<div class='column-content-created-date'></div>");
						var $sizeDiv = $("<div class='column-content-size'></div>");
						var $clearBothDiv = $("<div class='clearBoth'></div>");
						$nameDiv.append(dataRow.name);
						$createdDateDiv.append(dataRow.createdDate);
						$sizeDiv.append(dataRow.size);
						$li.append($nameDiv);
						$li.data("name", dataRow.name);
						$li.append($createdDateDiv);
						$li.append($sizeDiv);
						$li.append($clearBothDiv);
						$contentListing.append($li);
					}
					
					$contentListing.find("li").each(function() {
						$(this).click(function() {
							if($(this).hasClass('active')) {
								// Remove 'active' class
								$(this).removeClass("active");
								selectedName = null;
							} else {
								clearAllActive();
								// Add 'active' class
								$(this).addClass("active");
								selectedName = $(this).data("name");
							}
						});
					});
				} else {
					var $li = $("<li class='content-row'></li>");
					var $noDataDiv = $('<div class="no-data"><i class="icon-remove"></i> <fmt:message key="label.no.save.data" /></div>');
					$li.append($noDataDiv);
					$contentListing.append($li);
				}
			} else {
				var $li = $("<li class='content-row'></li>");
				var $noDataDiv = $('<div class="no-data"><i class="icon-remove"></i> <fmt:message key="label.no.save.data" /></div>');
				$li.append($noDataDiv);
				$contentListing.append($li);
			}
		}});
	}
	
	$errorBox.hide();
	$resetBtn.unbind("click");
	$resetBtn.click(function() {
		$errorBox.hide();
	});
	$loadBtn.unbind("click");
	$loadBtn.click(function() {
		populateContentListing();
		$loadModalYes.unbind("click");
		$loadModalYes.click(function() {
			var data = {name : selectedName};
			var url = 'input?${WebConstants.ACTION_NAME}=${WebConstants.DO_LOAD_FILE}';
			app.ajaxCall({url : url, data : data, callbackFn : function(data, textStatus, jqXHR) {
				var json = app.toJsonObject(jqXHR.responseText);
				if(json.status) {
					$("#json-input").val(json.input);
					$loadModal.modal('hide');
				} else {
					$loadModal.modal('hide');
				}
			}});
		});
		$loadModal.modal();
	});
	function clearAllActive() {
		$contentListing.find("li").each(function() {
			if($(this).hasClass('active')) {
				// Remove 'active' class
				$(this).removeClass("active");
			} 
		});
	}
	
	$saveBtn.click(function() {
		if($("#json-input").val() == null || $("#json-input").val() == '') {
			return;
		}
		$saveModal.modal();
		var $saveBtn = $("#save-modal-yes");
		var $nameField = $("#save-modal-name");
		hideError();
		$nameField.val("");
		$saveBtn.unbind("click");
		$saveBtn.click(function() {
			hideError();
			var nameVal = $nameField.val();
			if(nameVal == null || nameVal == '') {
				showError('<fmt:message key="label.error.name.required" />');
			} else {
				var data = {name : nameVal, json : $("#json-input").val()};
				var url = 'input?${WebConstants.ACTION_NAME}=${WebConstants.DO_SAVE_JSON}';
				app.ajaxCall({url : url, data : data, callbackFn : function(data, textStatus, jqXHR) {
					var json = app.toJsonObject(jqXHR.responseText);
					if(json.status) {
						$saveModal.modal('hide');	
					} else {
						showError(json.statusMsg);
					}
				}});
				
			}
		});
	});
	$submitBtn.click(function() {
		$errorBox.hide();
		var $jsonInput = $("#json-input");
		var inputStr = $jsonInput.val();
		var $errorMsg = $("#error-msg");
		if(inputStr == null || inputStr == '') {
			$errorMsg.html('<fmt:message key="label.error.no.input" />');
			$errorBox.show();
		} else { // POST it!
			var data = {json : inputStr};
			var url = 'input?${WebConstants.ACTION_NAME}=${WebConstants.DO_PRETTY_PRINT}';
			app.ajaxCall({url : url, data : data, callbackFn : function(data, textStatus, jqXHR) {
				var json = app.toJsonObject(jqXHR.responseText);
				if(json.status) {
					// Printing success
					$("#debug-box").html(json.output);
					$(".json-tree-output").treeview();
					var $pathStr = $("#path-string");
					$(".hoverJson").each(function() {
						var path = $(this).data("path");
						$(this).hover(function() {
							$pathStr.html(path);
						});
					});
				} else {
					$errorMsg.html(json.statusMsg);
					$errorBox.show();
				}
			}});
		}
	});
</script>