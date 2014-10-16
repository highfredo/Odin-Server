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
		},
		remove: {
			method: 'DELETE', 
			params: {
				action: 'remove'
			}
		}
	});
});

module.controller('listDocumentsCtrl', function ($scope, $resource, $state, $modal, Document) {
	$scope.path = $state.params.path.split("/");
	$scope.currentPath = "/" + $state.params.path;
		
	/*
	$('.download-btn').tooltip({
	   container: 'body',
		title: "Descargar"
	}) 
	$('.remove-btn').tooltip({
		container: 'body',
		title: "Borrar"
	}) 
	*/
	
	Document.list({path: $scope.currentPath}, function(result){
		$scope.documents = result;
	})
	
	$scope.viewDocument = function(doc, editMode) {
		var templateUrl, controller;
		
		if(doc == null || doc == "document") {
			templateUrl = 'views/view_document.html'
			controller = 'viewDocumentCtrl'
		} else if(doc == "folder") {
			templateUrl = 'views/view_folder.html'
			controller = 'viewFolderCtrl'
		} else {
			templateUrl = doc.isFolder ? 'views/view_folder.html' : 'views/view_document.html'
			controller = doc.isFolder ? 'viewFolderCtrl' : 'viewDocumentCtrl'
		}
			
		var modalInstance = $modal.open({
		      templateUrl: templateUrl,
		      controller: controller,
		      resolve: {
		      	document: function () {
		      		if(typeof doc != "string")
		      			return $.extend(true, {}, doc);
		      		else 
		      			return {}
		      	}, 
		      	editMode: function() {
		      		return editMode
		      	},
		      	currentPath: function() {
		      		return $scope.currentPath
		      	}
		      }
		});
	}
	
	/*
	$scope.viewFolder = function(folderId, editMode) {
		var modalInstance = $modal.open({
		      templateUrl: 'views/view_folder.html',
		      controller: 'viewFolderCtrl',
		      size: 'sm',
		      resolve: {
			    editMode: function() {
		      		return editMode
		      	}
			  }
		});		
		
		modalInstance.result.then(function (folderName) {
		      var newFolder = new Document({name: folderName, path: $scope.currentPath, isFolder: true})
		      newFolder.$save(function(response){
		    	  $scope.documents.unshift(response)
		      })
		});
	}
	*/
	$scope.remove = function(docId) {
		if (confirm('¿Desea borrar el fichero?')) {
			Document.remove({id: docId}, function(result){
				if(result.remove == "OK") {
					$state.go($state.current, {}, {reload: true});
				} else {
					alert("Ha ocurrido un error al borrar el Documento.")
				}
			})
		}
	}
});

module.controller('viewDocumentCtrl', function ($scope, $modalInstance, $upload, $state, Document, document, editMode, currentPath) {	
	$scope.document = document; 
	
	
	$scope.onFileSelect = function($files) {
		$scope.file = $files[0]
		if(!document.id)
			$scope.document.name = $scope.file.name;
		$scope.fileName = $scope.file.name;
	}
	
	$scope.ok = function () {
		$.extend($scope.document, {path: currentPath, isFolder: false})
	    var newDocument = new Document($scope.document)
		$scope.uploading = true;
		newDocument.$save(function(response){
			if($scope.file) {
				$scope.upload = $upload.upload({
			        url: $backendUrl+'document/upload', 
			        method: 'POST',
			        data: {id: response.id},
			        file: $scope.file,
			    }).progress(function(evt) {
			    	$scope.progress = parseInt(100.0 * evt.loaded / evt.total);
			    }).success(function(data, status, headers, config) {
			        $modalInstance.close(newDocument);
			        $state.go($state.current, {}, {reload: true});
			        $scope.uploading = false;
			    }).error(function(data) {
			    	alert("Ha ocurrido un error subiendo el fichero.")
			    });		
			} else{
				$state.go($state.current, {}, {reload: true});
				$modalInstance.close(response);
			}
	    })
	};
	$scope.cancel = function () {
		if($scope.upload && $scope.uploading) {
			$scope.upload.abort()
		}
		$modalInstance.dismiss('cancel');
	};
})


module.controller('viewFolderCtrl', function ($scope, $modalInstance, $state, Document, document, editMode, currentPath) {
	$scope.document = document; 
	
	$scope.ok = function () {
		$.extend($scope.document, {path: currentPath, isFolder: true})
		var newFolder = new Document($scope.document)
		newFolder.$save(function(response){
			$state.go($state.current, {}, {reload: true});
			$modalInstance.close(response);
		})
	};
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
})


