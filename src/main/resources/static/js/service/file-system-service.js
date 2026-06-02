var fileSystemService = dictionaryModule.service('fileSystemService', function($http) {
	this.update = function($scope) {
		$http.get('../fileSystem/folder/list' + $scope.currentUri)
		.then(function(response) {
			var folderView = response.data;
			$scope.currentFolderId = folderView.currentFolderId;
			$scope.parentFolderId = folderView.parentFolderId;
			$scope.folders = folderView.folders;
			$scope.folderPath();
		});

		$http.get('../fileSystem/file/list' + $scope.currentUri)
		.then(function(response) {
			$scope.textFiles = response.data.textFiles;
		});

		$http.get('../userWord/frequency' + $scope.currentUri)
		.then(function(responce) {
			$scope.wordFrequencyInfo = responce.data;
		});
	};
	this.addUserWord = function(wordId, status, $scope) {
		$http.post('../userWord/add/' + wordId + '/status/' + status)
		.then(function(responce) {
			var wordInfo = responce.data;
			var wordFrequencyInfo = $scope.wordFrequencyInfo;
			var frequencies = wordFrequencyInfo.frequencies;
			for (var i = 0; i < frequencies.length; i++) {
				var frequency = frequencies[i];
				var word = frequency.word;
				if (word.id == wordInfo.wordId) {
					var summary = wordFrequencyInfo.summary;
					if (wordInfo.prevStatus >= $scope.DO_NOT_KNOW) {
						summary.doNotKnowSize--;
						if (status == $scope.KNOW) {
							summary.knowSize++;
						}
					} else if (wordInfo.prevStatus == $scope.KNOW) {
						summary.knowSize++;
						if (status >= $scope.DO_NOT_KNOW) {
							summary.doNotKnowSize--;
						}
					} else if (!wordInfo.prevStatus == -1) {
						summary.unclassifiedSize--;
						if (status == $scope.KNOW) {
							summary.knowSize++;
						}
						if (status >= $scope.DO_NOT_KNOW) {
							summary.doNotKnowSize--;
						}
					}
					frequency.wordStatus = status;
					frequencies.splice(i, 1);
					break;
				}
			}
		});
	};
})