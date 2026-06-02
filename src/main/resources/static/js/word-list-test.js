dictionaryModule.service('wordListTestService', function() {
	this.updateAnswerSummary = function($scope) {
		$scope.answerSummary = {
				correct: 0,
				incorrect: 0,
				total: 0
		};
		$scope.currentIndex = -1;
		$scope.wordList = {
				words: []
		};
		$scope.wrongAnswers = [];
		$scope.correctAnswers = [];
	};
	this.updateVariables = function($scope) {
		$scope.answer = {
				correctId: 0,
				incorrectId: 0
		};
		$scope.answered = false;
	};
	this.update = function($scope, $http) {
		var that = this;
		var params = URL_HELPER.urlParametersToObject();

		$scope.wrongAnswers = [];
		$scope.correctAnswers = [];
		$scope.status = params.status;
		delete params.status;
		$scope.urlWithoutStatus = '?' + $.param(params);
		this.updateVariables($scope);
		this.updateAnswerSummary($scope);

		$http.get('../word/test/question/list' + $scope.currentUri)
		.then(function(response) {
			$scope.wordAnswerOptionList = response.data;
			that.nextQuestion($scope);
		});
	};
	this.selectNext = function($scope) {
		$scope.currentIndex++;
		return $scope.wordAnswerOptionList.wordTestQuestions[$scope.currentIndex];
	};
	this.nextQuestion = function($scope) {
		var wordAnswerOptionList = $scope.wordAnswerOptionList;
		var wordTestQuestions = wordAnswerOptionList.wordTestQuestions;
		$scope.wordAnswerOption = this.selectNext($scope);
		$scope.restCount = wordTestQuestions.length - $scope.currentIndex;
		this.updateVariables($scope);
	};
	this.clearSelections = function(wordAnswerOption) {
		wordAnswerOption.answerOptions.forEach(function(answerOption, i, answerOptions) {
			answerOption.selected = false;
		});
	};
	this.wordSelected = function(wordAnswerOption) {
		var answerOptions = wordAnswerOption.answerOptions;
		for (var i = 0; i < answerOptions.length; i++) {
			var answerOption = answerOptions[i];
			if (answerOption.selected) {
				return true;
			}
		}
		return false;
	};
}).controller('wordListTestController', function($scope, $http, wordListTestService) {
	var params = URL_HELPER.urlParametersToObject();

	Util.initCommonMethods($scope);

	$scope.currentUri = window.location.search;

	$scope.fileId = params.fileId;
	$scope.wordStatus = params.status;

	$scope.update = function() {
		wordListTestService.update($scope, $http);
	};

	$scope.nextQuestion = function() {
		wordListTestService.nextQuestion($scope);
	};

	$scope.repeatWrongAnswers = function() {
		$http.post('../word/test/question/list/repeat?status=' + $scope.status, $scope.wrongAnswers)
		.then(function(response) {
			wordListTestService.updateAnswerSummary($scope);
			$scope.wordAnswerOptionList = response.data;
			wordListTestService.nextQuestion($scope);
		});
	};

	$scope.repeatCurrentAnswers = function() {
		$http.post('../word/test/question/list/repeat?status=' + $scope.status, $scope.wordAnswerOptionList.wordTestQuestions)
		.then(function(response) {
			wordListTestService.updateAnswerSummary($scope);
			$scope.wordAnswerOptionList = response.data;
			wordListTestService.nextQuestion($scope);
		});
	};

	$scope.answerQuestion = function(answer) {
		var wordSelected = wordListTestService.wordSelected($scope.wordAnswerOption);
		if (answer && !wordSelected) {
			return;
		}

		$http.post('../word/test/answer', $scope.wordAnswerOption)
		.then(function(response) {
			$scope.answer = response.data;
			$scope.answered = true;
			wordListTestService.clearSelections($scope.wordAnswerOption);
			if ($scope.answer.answerId == $scope.answer.correctId) {
				$scope.answerSummary.correct++;
				$http.get('../translate/word?wordId=' + $scope.wordAnswerOption.word.id)
				.then(function(r) {
					$scope.correctAnswers.push(r.data);
				});
			} else {
				$scope.answerSummary.incorrect++;
				$scope.wrongAnswers.push($scope.wordAnswerOption);

				$http.get('../translate/word?wordId=' + $scope.wordAnswerOption.word.id)
				.then(function(r) {
					$scope.wordList.words.push(r.data);
				});
			}
			$scope.answerSummary.total++;
		});
	};

	$scope.levelUp = function(wordId) {
		if (!wordListTestService.wordSelected($scope.wordAnswerOption)) {
			return;
		}

		$http.post('../word/test/answer/level/up', $scope.wordAnswerOption)
		.then(function(response) {
			$scope.answer = response.data;
			$scope.answered = true;
			wordListTestService.clearSelections($scope.wordAnswerOption);
			if ($scope.answer.answerId == $scope.answer.correctId) {
				$scope.answerSummary.correct++;
			} else {
				$scope.answerSummary.incorrect++;
				$scope.wrongAnswers.push($scope.wordAnswerOption);

				$http.get('../translate/word?wordId=' + $scope.wordAnswerOption.word.id)
				.then(function(r) {
					$scope.wordList.words.push(r.data);
				});
			}
			$scope.answerSummary.total++;
		});
	};

	$scope.update();
});