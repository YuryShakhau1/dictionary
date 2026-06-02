dictionaryModule.controller('folderController', function($scope, $http, fileSystemService) {
	var params = URL_HELPER.urlParametersToObject();
	$scope.uri = $.param(params);

	$scope.folderId = params.folderId;
	$scope.currentFolderId = params.folderId;
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
		fileSystemService.update($scope);
	}

	$scope.addFolder = function() {
		var url = '../fileSystem/folder/add?folderName=' + $scope.newFolder.name;
		if ($scope.currentFolderId) {
			url += '&parentFolderId=' + $scope.currentFolderId;
		}
		$http.post(url)
		.then(function(response) {
			$scope.newFolder.name = '';
			$scope.update();
		});
	};

	$scope.folderPath = function() {
		var url = '../fileSystem/folder/path';
		if ($scope.currentFolderId) {
			url += '?folderId=' + $scope.currentFolderId;
		}
		$http.get(url)
		.then(function(response) {
			$scope.currentPath = '/' + response.data.join('/');
		});
	};

	$scope.openFolder = function(id) {
		if (id) {
			params.folderId = id;
		} else {
			delete params.folderId;
		}
		delete params.fileId;
		window.location.href = 'file-system.html?' + $.param(params);
	};

	$scope.deleteFolder = function(id) {
		$http.post('../fileSystem/folder/delete?folderId=' + id)
		.then(function(response) {
			$scope.update();
		});
	};

	$scope.uploadTextFile = function(fileInput) {
		var file = fileInput.files[0];
		var formData = new FormData();
		formData.append('textFile', file);
		if ($scope.currentFolderId) {
			formData.append('parentFolderId', $scope.currentFolderId);
		}
		$http.post('../fileSystem/file/upload', formData, {
			headers: { 'Content-Type': undefined }
		})
		.then(function success(response) {
			fileInput.value = '';
			$scope.update();
		}, function error(response) {

		});
	};

	$scope.openFile = function(id) {
		if (window.location.search.length > 0) {
			window.location.href = '../view/file.html' + window.location.search + '&fileId=' + id;
		} else {
			window.location.href = '../view/file.html?fileId=' + id;
		}
	};
	
	$scope.normalize = function() {
		var url = '../normalizer/normalize';
		$http.post(url)
		.then(function(response) {
			$scope.update();
		});
	};
	
	$scope.deleteTextFile = function(id) {
		var url = '../fileSystem/file/delete?fileId=' + id;
		$http.post(url)
		.then(function(response) {
			$scope.update();
		});
	};

	$scope.addUserWord = function(wordId, status) {
		fileSystemService.addUserWord(wordId, status, $scope);
	};
	
	$scope.deleteUserWord = function(userWordId) {
		$http.post('../userWord/delete/' + userWordId)
		.then(function(responce) {
			$scope.update();
		});
	};

	$scope.update();
});