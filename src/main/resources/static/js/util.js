var Util = function(){};

Util.initCommonMethods = function($scope) {
    $scope.INDEFINITE = -100;
    $scope.EXCLUDED = -1;
    $scope.KNOW = 0;
    $scope.DO_NOT_KNOW = 1;
    $scope.HAVE_KNOWN = 2;
    $scope.ALMOST_KNOW = 3;
};