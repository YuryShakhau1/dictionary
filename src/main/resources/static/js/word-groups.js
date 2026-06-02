dictionaryModule.service('wordGroupsService', function() {
	this.update = function($scope, $http) {

		$http.get('../translate/some-words')
		.then(function(response) {
			$scope.wordList.words = response.data;
		});
	};
}).controller('wordGroupsController', function($scope, $http,wordGroupsService) {
	Util.initCommonMethods($scope);

	$scope.wordGropupName = '';

	$scope.createGroup = function() {
		$http.post('../word-groups', $scope.wordAnswerOption)
			.then(function(response) {
				$scope.wordGropupName = '';
				$scope.update();
			});
	};

	$scope.update = function() {
		$http.get('../word-groups/list', $scope.wordAnswerOption)
			.then(function(response) {
				$scope.wordGropus = response.data;
			});
	};
	
	$scope.update();
});