$(function() {

	var buttonSelector = '*[data-back-btn]';
	
	var url = window.location.href;
	var searchParams = new URLSearchParams(url.substr(url.indexOf('?') + 1));
	 
	function goBack(evt) {
		evt.preventDefault();

		var backUrl = searchParams.get('back');
		console.log('back',  backUrl);
		if (backUrl != null) {
			window.location.href = backUrl;
		} else {
			window.history.back();
		}
	}

	$(buttonSelector).on('click', goBack);


});