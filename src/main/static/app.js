'use strict';

/**
 * @fileoverview Primary module for starting up WhichClasses frontend.
 */

// TODO(optional): wrap in Closure support? Jetty could auto-recompile etc.
var wc = typeof wc == 'undefined' ? {} : wc;

// Declare app level module which depends on views, and components
wc.module = angular.module('whichclasses', [
  'ngRoute'
]);

wc.module.controller('homeCtrl', wc.HomeCtrl.angular);
wc.module.service('tceDataService', wc.TceDataService.angular);

// Global config.
wc.module.config([
  '$routeProvider',
  '$locationProvider',
  function($routeProvider, $locationProvider) {
    $routeProvider.
        when('/', {
          templateUrl: '/static/home/home.ng',
          controller: wc.HomeCtrl,
          controllerAs: 'home',
          resolve: {
            'deptList': ['tceDataService', function(tceDataService) {
              return tceDataService.getDepartmentList();
            }],
          },
        }).
        otherwise({redirectTo: '/'});

    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }
]);
