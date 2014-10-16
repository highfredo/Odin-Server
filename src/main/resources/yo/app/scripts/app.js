'use strict';

/**
 * @ngdoc overview
 * @name odinClientApp
 * @description
 * # odinClientApp
 *
 * Main module of the application.
 */

var $backendUrl = "/"; // "http://localhost:8080/"
	
var app = angular.module('odinClientApp', ['ngResource', 'ui.router', 'ui.bootstrap', 'angularFileUpload', 'angular-loading-bar', 'listDocumentsModule', 'errorsModule']);

app.config(function($stateProvider, $urlRouterProvider, $httpProvider, cfpLoadingBarProvider){
	cfpLoadingBarProvider.includeSpinner = false;
	
    $urlRouterProvider.otherwise('/list/');
    
    $stateProvider
        .state('list', {
            url: '/list/{path:.*}',
            templateUrl: 'views/list_documents.html',
            controller: 'listDocumentsCtrl'
        })
        .state('error', {
            url: '/error/{code}',
            templateUrl: 'views/error.html',
            controller: 'ErrorCtrl',
            resolve: {
                errorInfo: function () {
                    return this.self.error;
                }
            }
        })      
})

app.factory('BackendUrl', function() {
	return {
		get: function() {return $backendUrl}
	}
})

app.run(function($rootScope, $state) {
    $rootScope.backendUrl = $backendUrl;
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
    	event.preventDefault();
    	$state.get('error').error = error.data;
    	return $state.go('error', {code: error.status}, {reload: true});
    });
})

