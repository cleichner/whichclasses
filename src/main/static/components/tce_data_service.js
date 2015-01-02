'use strict';

var wc = typeof wc == 'undefined' ? {} : wc;

/**
 * Service for getting TCE data from the HTTP backend.
 */
wc.TceDataService = function($http, $q) {
  // References to necessary Angular services.
  this.$http_ = $http;
  this.$q_ = $q;

  // Cached promises for values that can be requested.
  this.departmentList_ = null;
  this.searchResults_ = {};
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
          this.departmentList_.reject('Failed loading department list.');
        }));
  }

  return this.departmentList_.promise;
};


wc.TceDataService.VALID_SEARCH_VALUES_QUALITY_ =
    ['best', 'worst', 'easiest', 'hardest'];


wc.TceDataService.VALID_SEARCH_VALUES_ENTITY_TYPE_ = ['classes', 'professors'];


/**
 * Gets the ranked search results for a department.
 * @param {string} quality See VALID_SEARCH_VALUES_QUALITY_.
 * @param {string} entityType See VALID_SEARCH_VALUES_ENTITY_TYPE_.
 * @param {string} department Department identifier, e.g. "CSC".
 * @return {!Promise} Promise which will contain the search results if possible.
 *     The promise will be rejected if any of the search values are invalid or
 *     if the search itself fails.
 */
wc.TceDataService.prototype.getSearchResults = function(
    quality, entityType, department) {
  var searchResultsKey = quality + '|' + entityType + '|' + department;
  var searchResults = this.searchResults_[searchResultsKey];
  if (!searchResults) {
    searchResults = this.$q_.defer();
    this.searchResults_[searchResultsKey] = searchResults;
    this.$http_.get('/api/search', {
      'params': {
        'quality': quality,
        'entityType': entityType,
        'department': department
      },
    }).
        success(function(data) { searchResults.resolve(data['results']); }).
        error(function() { searchResults.reject('Failed loading results.'); });
  }

  if (wc.TceDataService.VALID_SEARCH_VALUES_QUALITY_.indexOf(quality) < 0) {
    searchResults.reject('Invalid parameters: quality');
  }

  if (wc.TceDataService.VALID_SEARCH_VALUES_ENTITY_TYPE_.indexOf(
      entityType) < 0) {
    searchResults.reject('Invalid parameters: entity type');
  }

  return searchResults.promise;
};
