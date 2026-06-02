dictionaryModule.service('wordTranslateService', function() {
	this.update = function($scope, $http) {
		var params = URL_HELPER.urlParametersToObject();
		
		$http.get('../translate/word?wordId=' + params.wordId)
		.then(function(response) {
			$scope.word = response.data;
		});
	};
}).controller('wordTranslateController', function($scope, $http, wordTranslateService) {
	Util.initCommonMethods($scope);

	$scope.update = function() {
		wordTranslateService.update($scope, $http);
	};
	
	$scope.update();
});