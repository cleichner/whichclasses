'use strict';

// TODO(optional): wrap in Closure support? Jetty could auto-recompile etc.
var wc = {};

// Declare app level module which depends on views, and components
wc.module = angular.module('whichclasses', [
  'ngRoute',
  'wc.home',
]);

// Global config.
wc.module.config([
  '$routeProvider',
  '$locationProvider',
  function($routeProvider, $locationProvider) {
    $routeProvider.otherwise({redirectTo: '/'});
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }
]);
