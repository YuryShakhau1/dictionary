dictionaryModule.service('wordTestService', function() {

	this.update = function($scope, $http) {
		$scope.answer = {
				correctId: 0,
				incorrectId: 0
		};
		$scope.answered = false;
		var params = URL_HELPER.urlParametersToObject();

		var url = '../word/test/question';

		if (params.fileId) {
			url += '?fileId=' + params.fileId;
		} else if (params.folderId) {
			url += '?folderId=' + params.folderId;
		}
		
		$http.get(url)
		.then(function(response) {
			$scope.wordAnswerOption = response.data;
		});
	};
}).controller('wordTestController', function($scope, $http, wordTestService) {

	Util.initCommonMethods($scope);

	$scope.update = function() {
		wordTestService.update($scope, $http);
	};
	
	$scope.answerQuestion = function(id) {
		$http.post('../word/test/answer', $scope.wordAnswerOption)
		.then(function(response) {
			$scope.answer = response.data;
			$scope.answered = true;
		});
	};
	
	$scope.nextQuestion = function() {
		$scope.update();
	};
	
	$scope.update();
});