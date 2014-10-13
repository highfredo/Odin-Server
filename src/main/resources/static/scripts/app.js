'use strict';

/**
 * @ngdoc overview
 * @name odinClientApp
 * @description
 * # odinClientApp
 *
 * Main module of the application.
 */

var $backendUrl = "http://localhost:8080/"
	
var app = angular.module('odinClientApp', ['ngResource', 'ui.router', 'ui.bootstrap', 'angularFileUpload', 'angular-loading-bar', 'listDocumentsModule']);

app.config(function($stateProvider, $urlRouterProvider, $httpProvider, cfpLoadingBarProvider){
	cfpLoadingBarProvider.includeSpinner = false;
	
    $urlRouterProvider.otherwise('/list/');
    
    $stateProvider
        .state('list', {
            url: '/list/{path:.*}',
            templateUrl: 'views/list_documents.html',
            controller: 'listDocumentsCtrl',
            data: {
            	ncyBreadcrumbLabel: 'Home' 
            }
        })
    
        
    $httpProvider.interceptors.push(function($q, $location) {
    		return {
    			'responseError': function(response) {
    				console.log(response.status);
    				if(response.status >= 500 && response.status <= 599) {
    					//$location.path("/500", false);
    					alert("Ha ocurrido un error interno en el servidor");
    				} else if(response.status == 401) {
    					$location.path("/401", false);
    				} else if(response.status == 403) {
    					$location.path("/403", false);
    				} else if(response.status == 404) {
    					$location.path("/404", false);
    				}
    				
    				return $q.reject(response); //response || $q.when(response); 
    			}
    		};
    });
})

app.factory('BackendUrl', function() {
	return {
		get: function() {return $backendUrl}
	}
})

app.run(function($rootScope) {
    $rootScope.backendUrl = $backendUrl;
})

