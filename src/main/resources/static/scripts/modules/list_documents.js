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
	
	$scope.viewDocument = function(docId, editMode) {
		var modalInstance = $modal.open({
		      templateUrl: 'views/view_document.html',
		      controller: 'viewDocumentCtrl',
		      resolve: {
		      	document: function () {
		      		if(docId != null)
		      			return $.extend(true, {}, $.grep($scope.documents, function(doc){ return doc.id == docId; })[0]);
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
	
	$scope.viewFolder = function(editMode) {
		var modalInstance = $modal.open({
		      templateUrl: 'views/view_folder.html',
		      controller: 'viewFolderCtrl',
		      size: 'sm',
		      resolve: {
			    editMode: editMode
			  }
		});		
		modalInstance.result.then(function (folderName) {
		      var newFolder = new Document({name: folderName, path: $scope.currentPath, isFolder: true})
		      newFolder.$save(function(response){
		    	  $scope.documents.unshift(response)
		      })
		});
	}
	
	$scope.remove = function(docId) {
		if (confirm('¿Desea borrar el fichero?')) {
			Document.remove({id: docId}, function(result){
				if(result.remove == "OK") {
					$state.go($state.current, {}, {reload: true});
				} else {
					alert("Ha ocurrido un error al borrar el Documento.")
				}
			}).error(function(data){
				alert("Ha ocurrido un error al borrar el Documento.")
				console.log(data)
			})
		}
	}
	/*
	$scope.ordeDocuments = function(document) {
		var field = $scope.orderByField
		return document[field] < 
	}*/
		
});

module.controller('viewDocumentCtrl', function ($scope, $modalInstance, $upload, $state, Document, document, editMode, currentPath) {	
	console.log(editMode, currentPath)

	$scope.document = document; // $.extend(true, {}, document);
	
	
	$scope.onFileSelect = function($files) {
		$scope.file = $files[0]
		$scope.document.name = $scope.file.name;
		$scope.fileName = $scope.file.name;
	}
	
	$scope.ok = function () {
		$.extend($scope.document, {path: currentPath, isFolder: false})
	    var newDocument = new Document($scope.document)
		$scope.uploading = true;
		newDocument.$save(function(response){
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
	    })
	};
	$scope.cancel = function () {
		if($scope.upload && $scope.uploading) {
			$scope.upload.abort()
		}
		$modalInstance.dismiss('cancel');
	};
})


module.controller('viewFolderCtrl', function ($scope, $modalInstance) {
	$scope.ok = function () {
		$modalInstance.close($scope.folderName);
	};
	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
})


