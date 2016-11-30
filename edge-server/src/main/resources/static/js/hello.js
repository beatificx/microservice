angular.module('hello', [ 'ngRoute' ])
  .config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'home',
		controllerAs : 'controller'
//	}).when('/login', {
//		templateUrl : 'login.html',
//		controller : 'navigation'
	}).otherwise('/');

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    $httpProvider.defaults.headers.common['Accept'] = 'application/json';

  }).controller('home', function($http) {
		var self = this;
		$http.get('product/').then(function(response) {
			self.product = response.data;
		})
		$http.get('point/').then(function(response) {
			self.user = response.data;
		})
		$http.get('composite/').then(function(response) {
			self.composite = response.data;
		})
  })
  .controller('navigation',

		  function($rootScope, $http, $location, $route) {

		var self = this;

		self.tab = function(route) {
			return $route.current && route === $route.current.controller;
		};

		$http.get('user').then(function(response) {
			if (response.data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
		}, function() {
			$rootScope.authenticated = false;
		});

		self.credentials = {};

		self.logout = function() {
			$http.post('logout', {}).finally(function() {
				$rootScope.authenticated = false;
				$location.path("/");
			});
		}

});