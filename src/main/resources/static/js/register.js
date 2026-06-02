dictionaryModule.controller('indexController', function($scope, $http) {

    $scope.user = {
        name: '',
        password: ''
    };

    $scope.register = () => {
        $http.post('../register', $scope.user)
            .then(function(response) {
                $scope.userId = response.data;
            });
    };
});