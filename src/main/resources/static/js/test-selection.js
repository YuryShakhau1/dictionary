dictionaryModule.controller('testSelectionController', function($scope, $http) {

	Util.initCommonMethods($scope);
	
	$scope.currentUri = window.location.search;

	var params = URL_HELPER.urlParametersToObject();
	$scope.fileId = params.fileId;
	$scope.folderId = params.folderId;
});