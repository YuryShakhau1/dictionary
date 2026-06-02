dictionaryModule.controller('fileController', function($scope, $http, fileSystemService) {
	var params = URL_HELPER.urlParametersToObject();
	$scope.uri = $.param(params);

	$scope.currentUri = window.location.search;

	if (params.wordStatus) {
		$scope.wordStatus = +params.wordStatus;
	} else {
		$scope.wordStatus = null;
	}

	if (params.wordStatusFrom) {
		$scope.wordStatusFrom = +params.wordStatusFrom;
	} else {
		$scope.wordStatusFrom = null;
	}

	Util.initCommonMethods($scope);

	$scope.update = function() {
		$scope.fileId = params.fileId;
		$scope.folderId = params.folderId;

		$http.get('../fileSystem/file/' + params.fileId)
		.then(function(response) {
			$scope.textFile = response.data;
		});

		$http.get('../userWord/frequency' + $scope.currentUri)
		.then(function(response) {
			$scope.wordFrequencyInfo = response.data;
		});
	};

	$scope.addUserWord = function(wordId, status) {
		fileSystemService.addUserWord(wordId, status, $scope);
	};

	$scope.openFolder = function(id) {
		if (id) {
			params.folderId = id;
		}
		var uri = "";
		if (params.folderId) {
			uri += "folderId=" + params.folderId;
		}
		if (uri.length > 0) {
			window.location.href = 'file-system.html?' + uri;
		} else {
			window.location.href = 'file-system.html';
		}
	};

	$scope.deleteUserWord = function(userWordId) {
		$http.post('../userWord/delete/' + userWordId)
		.then(function(responce) {
			$scope.update();
		});
	};

	$scope.update();
});