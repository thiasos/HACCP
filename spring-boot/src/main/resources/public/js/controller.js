'use strict';
angular.module('MyApp', [ 'ngMaterial', 'ngMessages', 'md.data.table' ])
		.controller('HaccpCtrl', HaccpCtrl)//
		.factory('crudService', crudService)//
		.factory('providerService', providerService);
function providerService($http, $log, $q, crudService) {
	return {
		list : function(page, size, sortColumns, sortOrder) {
			return crudService.list('fornitoris', page, size, sortColumns, sortOrder)
		}
	}
}
function crudService($http, $log, $q) {
	return {
		list : function(servicename, page, size, sortColumns, sortOrder) {
			var deferred = $q.defer();
			$http.get(servicename + '?page=' + (page - 1) + "&size=" + size+'&sort='+sortColumns +','+sortOrder)
					.then(function(result) {
						deferred.resolve(result.data);
					}, function(msg, code) {
						deferred.reject(msg);
						$log.error(msg, code);
					});
			return deferred.promise;

		}
	}
}
function HaccpCtrl($timeout, $http, $q, $log, providerService, $scope) {
	var self = this;
	self.listProviders = listProviders;
	self.sortProviders = sortProviders;
	self.nextProviders = nextProviders;
	self.provider = new Object();
 
	function sortProviders(sortColumns) {
		self.listProviders(self.provider.result.page.number, self.provider.result.page.size, sortColumns)
	}
	function nextProviders(page, size) {
		self.listProviders(page, size, self.provider.order)
	}
	function listProviders(page, size, sortColumn) {
		var sortOrder = 'asc';
		if (sortColumn !=undefined && sortColumn.startsWith("-")) {
			  sortOrder = 'desc';	
			  sortColumn = sortColumn.substr(1, sortColumn.length-1);
		}
		self.provider.promise = providerService.list(page, size, sortColumn, sortOrder);
		self.provider.promise
				.then(function(result) {
					self.provider.result = result;
					self.provider.result.page.number = self.provider.result.page.number + 1;
				});
	}
	self.provider.order = 'id';
	listProviders(1, 10);
	
}