'use strict';


var module = angular.module('listDocumentsModule', [])
    
module.factory('Document',  function($resource) {
	return $resource($backendUrl+'document/:action/', {}, {
		list: {
			method: 'GET', 
			params: {
				action: 'list'
			}, 
			isArray: true
		},
		save: {
			method: 'POST', 
			params: {
				action: 'save'
			}
		}
	});
});

module.controller('listDocumentsCtrl', function ($scope, $resource, $state, $modal, Document) {
	$scope.path = $state.params.path.split("/");
	$scope.currentPath = "/" + $state.params.path;
	
	Document.list({path: $scope.currentPath}, function(result){
		$scope.documents = result;
	})
	
	$scope.viewDocument = function(docId) {
		var modalInstance = $modal.open({
		      templateUrl: 'views/view_document.html',
		      controller: 'viewDocumentCtrl',
		      resolve: {
		      	document: function () {
		      		return $.grep($scope.documents, function(doc){ return doc.id == docId; })[0];
		      	}
		      }
		});
		/*
		modalInstance.result.then(function (document) {
		      $scope.document = document; //TODO: renovar datos de verdad
		      var d = $.grep($scope.documents, function(doc){ return doc.id == docId; })[0]
		      d = document;
		    }, function () {
		      $log.info('Modal dismissed at: ' + new Date());
		});*/
	}
	
	$scope.createFolder = function() {
		var modalInstance = $modal.open({
		      templateUrl: 'views/new_folder.html',
		      controller: 'newFolderCtrl',
		      size: 'sm'
		});		
		modalInstance.result.then(function (folderName) {
		      var newFolder = new Document({name: folderName, path: $scope.currentPath, isFolder: true})
		      newFolder.$save(function(response){
		    	  $scope.documents.unshift(response)
		      })
		});
	}
	
});

module.controller('viewDocumentCtrl', function ($scope, $modalInstance, document) {
	
	document.name = "aaaaaa"
	
	//$scope.document = document;
	
	
	$scope.ok = function () {
		$modalInstance.close($scope.document);
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
})


module.controller('newFolderCtrl', function ($scope, $modalInstance) {
	$scope.ok = function () {
		$modalInstance.close($scope.folderName);
	};
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
})


