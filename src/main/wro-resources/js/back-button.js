$(function() {

	var buttonSelector = '*[data-back-btn]';

	function goBack(evt) {
		evt.preventDefault();
		window.history.back();
	}

	$(buttonSelector).on('click', goBack);


});