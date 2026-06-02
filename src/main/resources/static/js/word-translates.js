dictionaryModule.service('wordTranslatesService', function() {
	this.update = function($scope, $http) {

		$http.get('../translate/some-words')
		.then(function(response) {
			$scope.wordList.words = response.data;
		});
	};
}).controller('wordTranslatesController', function($scope, $http, wordTranslatesService) {
	Util.initCommonMethods($scope);

	$scope.wordList = {
		words: []
	};

	$scope.update = function() {
		wordTranslatesService.update($scope, $http);
	};
	
	$scope.update();
});