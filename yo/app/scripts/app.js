'use strict';

/**
 * @ngdoc overview
 * @name odinClientApp
 * @description
 * # odinClientApp
 *
 * Main module of the application.
 */
	
var app = angular.module('odinClientApp', ['ngResource', 'ui.router', 'ui.bootstrap', 'angularFileUpload', 'angular-loading-bar', 
                                           'ngCookies', 'pascalprecht.translate',
                                           'listDocumentsModule', 'errorsModule', 'securityModule']);

app.config(function($stateProvider, $urlRouterProvider, $httpProvider, cfpLoadingBarProvider, $translateProvider){
	cfpLoadingBarProvider.includeSpinner = false;
	
	$translateProvider.useStaticFilesLoader({
		prefix : '/scripts/lang/',
		suffix : '.json'
	});
	$translateProvider.preferredLanguage('es');
	$translateProvider.useCookieStorage();
	
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
        .state('login', {
        	url: '/login',
        	templateUrl: 'views/login.html'
        })      
})

app.controller('userMenuCtrl', [ '$translate', '$scope', function($translate, $scope) {
	$scope.setLang = function(langKey) {
		$translate.use(langKey);
	};
} ]);

app.run(function($rootScope, $state) {
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
    	event.preventDefault();
    	$state.get('error').error = error.data;
    	return $state.go('error', {code: error.status}, {reload: true});
    });
})

