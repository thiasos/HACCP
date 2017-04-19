'use strict';
angular.module('MyApp', [ 'ngMaterial', 'ngMessages', 'md.data.table' ])
		.controller('HaccpCtrl', HaccpCtrl)//
		.factory('crudService', crudService)//
;
function crudService($http, $log, $q) {
	return {
		list : function(servicename, page, size, sortColumns, sortOrder) {
			var deferred = $q.defer();
			$http.get(
					servicename + '?page=' + (page - 1) + "&size=" + size
							+ '&sort=' + sortColumns + ',' + sortOrder).then(
					function(result) {
						deferred.resolve(result.data);
					}, function(msg, code) {
						deferred.reject(msg);
						$log.error(msg, code);
					});
			return deferred.promise;

		},
		get : function(url) {
			var deferred = $q.defer();
			$http.get(url).then(function(result) {
				deferred.resolve(result.data);
			}, function(msg, code) {
				deferred.reject(msg);
				$log.error(msg, code);
			});
			return deferred.promise;

		}

	}
}
function HaccpCtrl($timeout, $http, $q, $log, crudService, $scope) {
	var self = this;
	self.sortProviders = sortProviders;
	self.nextProviders = nextProviders;
	self.provider = new Object();
	self.list = list;
	self.sortArticles = sortArticles;
	self.nextArticles = nextArticles;
	self.article = new Object();
	
	function sortProviders(sortColumns) {
		self.list('fornitoris', self.provider,
				self.provider.result.page.number,
				self.provider.result.page.size, sortColumns)
	}
	function nextProviders(page, size) {
		self.list('fornitoris', self.provider, page, size, self.provider.order)
	}
	function sortArticles(sortColumns) {
		self.list('articolis', self.article, self.article.result.page.number,
				self.article.result.page.size, sortColumns)
	}
	function nextArticles(page, size) {
		self.list('articolis', self.article, page, size, self.article.order)
	}
	function list(service, destObj, page, size, sortColumn) {
		var sortOrder = 'asc';
		if (sortColumn != undefined && sortColumn.startsWith("-")) {
			sortOrder = 'desc';
			sortColumn = sortColumn.substr(1, sortColumn.length - 1);
		}
		destObj.promise = crudService.list(service, page, size, sortColumn,
				sortOrder);
		destObj.promise.then(function(result) {
			destObj.result = result;
			destObj.result.page.number = destObj.result.page.number + 1;
		});
	}

	self.article.order = 'id';
	nextProviders(1, 10);
	self.provider.order = 'id';
	nextArticles(1, 10);
}