'use strict';
angular.module('MyApp', [ 'ngMaterial', 'ngMessages', 'md.data.table' ])
		.controller('HaccpCtrl', HaccpCtrl).factory('providerService',
				providerService);
function providerService($http, $log, $q) {
	return {
		listProviders : function(page, size) {
			var deferred = $q.defer();
			$http.get('fornitoris?page=' + (page-1)+"&size="+size).then(
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
function HaccpCtrl($timeout, $http, $q, $log, providerService, $scope) {
	var self = this;
	self.listProviders = listProviders;
	self.provider = new Object();
	function listProviders(page, size) {
		self.provider.promise=  providerService.listProviders(page, size);
		self.provider.promise.then(function (result) {
			self.provider.result = result;
			self.provider.result.page.number = self.provider.result.page.number+1;
		});
	}
	listProviders(1,10);
}