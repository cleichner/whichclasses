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
wc.module.controller('searchCtrl', wc.SearchCtrl.angular);
wc.module.service('tceDataService', wc.TceDataService.angular);

// Global config.
wc.module.config([
  '$routeProvider',
  '$locationProvider',
  function($routeProvider, $locationProvider) {
    // TODO: need to handle $routeChangeError event.
    $routeProvider.
        when('/', {
          templateUrl: '/static/home/home.ng',
          controller: 'homeCtrl',
          resolve: {
            'deptList': ['tceDataService', function(tceDataService) {
              return tceDataService.getDepartmentList();
            }],
          },
        }).
        // Note: this pattern matches the old site. Keep as-is if possible.
        when('/search/:quality/:entityType/:department/', {
          templateUrl: '/static/search/search.ng',
          controller: 'searchCtrl',
          resolve: {
            'results': ['$route', 'tceDataService',
                function($route, tceDataService) {
              return tceDataService.getSearchResults(
                  $route.current.params['quality'],
                  $route.current.params['entityType'],
                  $route.current.params['department']);
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
