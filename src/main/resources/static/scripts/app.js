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
	
var app = angular.module('odinClientApp', ['ngResource', 'ui.router', 'ui.bootstrap', 'listDocumentsModule']);

app.config(function($stateProvider, $urlRouterProvider){
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
        /*.state('about', {
             
        });*/
})

app.factory('BackendUrl', function() {
	return {
		get: function() {return $backendUrl}
	}
})

app.run(function($rootScope) {
    $rootScope.backendUrl = $backendUrl;
})

