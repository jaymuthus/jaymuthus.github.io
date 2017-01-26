(function (window, document) {
	'use strict';

	var test = {};
	test.getMessage = function() {
		return "Message from github test.js";
	}
	window.test = test;
}(window, document));
