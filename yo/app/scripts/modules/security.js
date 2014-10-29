

var module = angular.module('securityModule', [])

module.factory('User',  function($resource) {
	return $resource('/user/')
});


module.controller('securityCtrl', function ($scope, User) {
	$scope.user = User.get()
})