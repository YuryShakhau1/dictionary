dictionaryModule.service('languageService', function() {
	this.update = function($scope, $http) {
		$http.get('../language/list')
		.then(function(response) {
			$scope.languages = response.data.languages;
			$scope.languageName = '';
		});
	};
}).controller('language', function($scope, $http, languageService) {

	Util.initCommonMethods($scope);

	$scope.update = function() {
		languageService.update($scope, $http);
	};
	
	$scope.addLanguage = function() {
		$http.post('../language/add?languageName=' + $scope.languageName)
		.then(function(response) {
			$scope.update();
		});
	};
	
	$scope.deleteLanguage = function(languageId) {
		$http.post('../language/delete?languageId=' + languageId)
		.then(function(response) {
			$scope.update();
		});
	};
	
	$scope.update();
});