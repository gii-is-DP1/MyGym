$(function() {

	var buttonSelector = '.button[data-back-btn]';

	function goBack() {
		window.history.back();
	}

	$(buttonSelector).on('click', goBack);


});