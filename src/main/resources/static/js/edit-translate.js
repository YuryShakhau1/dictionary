dictionaryModule.service('translateService', function() {

	this.addTranslate = function($scope, translate) {
		$scope.word.translates.push({
			sourceWord: null,
			translateWord: {
				language: {
					name: 'RU'
				},
				value: translate
			}
		});
	};

	this.deleteTranslate = function($scope) {
		var translates = $scope.word.translates;
		if (translates.length > 1 && !translates[translate.length - 1].translateWord.id) {
			translates.pop();
		}
	};

	this.deleteTranslateByIndex = function($scope, index) {
		var translates = $scope.word.translates;
		translates.splice(index, 1);
	};

	this.update = function($scope, $http) {
		var that = this;
		var params = URL_HELPER.urlParametersToObject();
		$http.get('../translate/word?wordId=' + params.wordId)
		.then(function(responce) {
			$scope.word = responce.data;
			that.addTranslate($scope, '');
		});
	};
}).controller('translateController', function($scope, $http, translateService) {

	Util.initCommonMethods($scope);

	$scope.update = function() {
		translateService.update($scope, $http);
	};

	$scope.addTranslateRow = function() {
		translateService.addTranslate($scope, '');
	};

	$scope.deleteTranslate = function(translateId) {
		$http.post('../translate/delete?translateId=' + translateId)
		.then(function(response) {
			$scope.update();
		});
	};
	
	$scope.deleteTranslateRow = function(index) {
		if (!index && index > 0) {
			translateService.deleteTranslate($scope);
		} else {
			translateService.deleteTranslateByIndex($scope, index);
		}
	};

	$scope.fillOfSuggestion = function(wordId) {
		$http.get('../translate/search?wordId=' + wordId)
		.then(function(responce) {
			var word = responce.data;
			if (word.translates.length) {
				$scope.word = word;
			}
		});
	};

	$scope.saveTranslate = function() {
		$http.post('../translate/save', $scope.word)
		.then(function(response) {
			$scope.update();
		});
	};
	
	$scope.update();
});