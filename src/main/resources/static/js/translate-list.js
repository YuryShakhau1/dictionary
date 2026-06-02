dictionaryModule.service('translateListService', function() {
	this.update = function($scope, $http) {
		var params = URL_HELPER.urlParametersToObject();
		var url = '../translate/user/list' + window.location.search;

		$scope.params = params;
		$http.get(url)
		.then(function(response) {
			$scope.words = response.data;
		});
	};
}).controller('translateListController', function($scope, $http, translateListService) {
	var params = URL_HELPER.urlParametersToObject();

	$scope.currentUri = window.location.search;

	$scope.fileId = params.fileId;
	$scope.folderId = params.folderId;
	$scope.wordStatus = params.wordStatus;

	Util.initCommonMethods($scope);

	delete params.wordStatus;
	
	$scope.uriWithoutWordStatus = $.param(params);

	$scope.update = function() {
		translateListService.update($scope, $http);
	};
	
	$scope.update();
});