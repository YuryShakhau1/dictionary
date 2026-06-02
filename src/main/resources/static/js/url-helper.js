var URL_HELPER = function(){};

URL_HELPER.urlParametersToObject = function() {
	var stringParams = window.location.search.substring(1);

	if (!stringParams) {
		return {};
	}

	if (stringParams.endsWith('&')) {
		stringParams = stringParams.substring(0, stringParams.length - 1);
	}
	var jsonContent = decodeURI(stringParams)
		.replace(/"/g, '\\"')
		.replace(/&/g, '","')
		.replace(/=/g, '":"')
		.replace(/,""/g, '');
	var params = JSON.parse('{"' + jsonContent + '"}');
	return params;
};
