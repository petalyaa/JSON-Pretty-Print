<button class="btn btn-primary" id="back">&lt; Back</button>

<script type="text/JavaScript">
	var $backBtn = $("#back");
	$backBtn.click(function() {
		app.loadDiv('input', 'main-container');
	});
</script>