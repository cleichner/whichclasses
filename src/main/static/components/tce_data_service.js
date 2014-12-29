'use strict';

var wc = typeof wc == 'undefined' ? {} : wc;

/**
 * Service for getting TCE data from the HTTP backend.
 */
wc.TceDataService = function($http, $q) {
  this.departmentList_ = null;

  this.$http_ = $http;
  this.$q_ = $q;
};

wc.TceDataService.angular = ['$http', '$q', wc.TceDataService];

wc.TceDataService.prototype.getDepartmentList = function() {
  if (!this.departmentList_) {
    this.departmentList_ = this.$q_.defer();
    this.$http_.get('/api/departments').
        success(angular.bind(this, function(data) {
          this.departmentList_.resolve(data);
        })).
        error(angular.bind(this, function() {
          this.departmentList_.reject(data);
        }));
  }

  return this.departmentList_.promise;
};
