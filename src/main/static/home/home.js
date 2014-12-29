'use strict';

var wc = typeof wc == 'undefined' ? {} : wc;

/**
 * Home-page controller.
 * @param deptList {!Array}
 */
wc.HomeCtrl = function($scope, deptList) {
  $scope.deptList = deptList;
};

wc.HomeCtrl.angular = ['$scope', 'deptList', wc.HomeCtrl];
