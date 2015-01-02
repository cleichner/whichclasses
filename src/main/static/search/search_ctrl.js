'use strict';

var wc = typeof wc == 'undefined' ? {} : wc;

/**
 * Search-page controller.
 */
wc.SearchCtrl = function($scope, $route, results) {
  $scope.query = $route.current.params;
  $scope.results = results;
};

wc.SearchCtrl.angular = ['$scope', '$route', 'results', wc.SearchCtrl];
