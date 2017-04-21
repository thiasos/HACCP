'use strict';
angular.module('MyApp',
		[ 'ngMaterial', 'ngMessages', 'md.data.table', 'materialCalendar' ])
		.controller('HaccpCtrl', HaccpCtrl)//
		.factory('crudService', crudService)//
		.factory('registryService', registryService)//
;
function registryService($http, $log, $q) {
	return {
		countEntry : function(date, registryType) {
			var deferred = $q.defer();
			$http.get('/registroCaricoes/search/countByData?data=' + date)
					.then(function(result) {
						deferred.resolve(result.data);
					}, function(msg, code) {
						deferred.reject(msg);
						$log.error(msg, code);
					});
			return deferred.promise;
		},
		listEntry : function(date, registryType) {
			var deferred = $q.defer();
			$http.get('/registroCaricoes/search/findByData?data=' + date).then(
					function(result) {
						deferred.resolve(result.data);
					}, function(msg, code) {
						deferred.reject(msg);
						$log.error(msg, code);
					});
			return deferred.promise;
		}
	}
}
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
function HaccpCtrl($timeout, $http, $q, $log, crudService, $scope,
		registryService) {
	$scope.selected = [];
	var self = this;
	self.sortProviders = sortProviders;
	self.nextProviders = nextProviders;
	self.provider = new Object();
	self.list = list;
	self.sortArticles = sortArticles;
	self.nextArticles = nextArticles;
	self.setDayContent = setDayContent;
	self.listLoadRegistryByDate = listLoadRegistryByDate;
	self.article = new Object();
	self.currDayInfo = new Object();
	self.currDayInfo.load = new Object();

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
	function setDayContent(date) {
		var day = date.getDate();
		var monthIndex = date.getMonth();
		var year = date.getFullYear();
		return registryService.countEntry((monthIndex + 1) + '/' + day + '/'
				+ year);

	}
	function listLoadRegistryByDate(date) {
		var day = date.getDate();
		var monthIndex = date.getMonth();
		var year = date.getFullYear();
		self.currDayInfo.load.promise = registryService
				.listEntry((monthIndex + 1) + '/' + day + '/' + year);
		self.currDayInfo.load.promise.then(function(result) {
			self.currDayInfo.load.result = result;
			self.currDayInfo.load.selected = [] ;
			
		});

	}
	;
	self.article.order = 'id';
	nextProviders(1, 10);
	self.provider.order = 'id';
	nextArticles(1, 10);
}