$(function() {

	var fileUploadSelector = '.image-picker';
	var maxFileSize = 2000;

	function blobToBase64(blob, done) {
		var reader = new FileReader();
		reader.readAsDataURL(blob);
		reader.onloadend = function() {
			var base64data = reader.result;
			done(base64data);
		};
	};
	
	function previewImage($el, base64Data) {
		$preview = $el.find('.preview');
		
		$preview.attr('hidden', '');
		if (typeof base64Data === 'string' && base64Data.length) {
			$preview.find('img').attr('src', base64Data);
			$preview.removeAttr('hidden');
		}
	};

	$(fileUploadSelector).each(function(idx, el) {
		console.log('el', el);

		var $el = $(el);
		var $value = $el.find('input[type=hidden]');
		var $file = $el.find('input[type=file]');
		var preview = previewImage.bind(this, $el);
		
		$file.change(function(evt) {			
			var files = evt.target.files;
			
			if (!files.length) {
				return false;
			} else if (files[0].size > maxFileSize) {
				console.error('max file size exceeded');
				// TODO display error
			}
			
			blobToBase64(files[0], function setValueData(base64Data) {
				$value.val(base64Data);
				preview(base64Data);
			});
		});
		
		if ($value.val().length) {
			preview($value.val());		
		}
	});


});