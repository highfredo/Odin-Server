

var module = angular.module('securityModule', [])

module.factory('User',  function($resource) {
	return $resource('/user/:id')
});


module.controller('securityCtrl', function ($scope, $state, User) {
	User.get({id:"me"}, function(response){
		user = response;
		user.authenticated = user.username != "anon" ? true : false;
		$scope.user = user;
		
		if(user.authenticated == false) {
			$state.go('login');
		}		
	})
})