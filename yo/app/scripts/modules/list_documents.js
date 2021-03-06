'use strict';


var module = angular.module('listDocumentsModule', [])
    
module.factory('Document',  function($resource) {
	return $resource('document/:id')
});


module.controller('listDocumentsCtrl', function ($scope, $resource, $state, $modal, Document) {
	$scope.path = $state.params.path.split("/");
	$scope.currentPath = "/" + $state.params.path;
	
	Document.query({uri: $scope.currentPath}, function(result){
		$scope.documents = result;
	})
	
	$scope.viewDocument = function(doc, editMode) {
		var templateUrl, controller, size;
		
		if(doc == null || doc == "document") {
			templateUrl = 'views/view_document.html'
			controller = 'viewDocumentCtrl'
		} else if(doc == "folder") {
			templateUrl = 'views/view_folder.html'
			controller = 'viewFolderCtrl'
			size = 'sm'
		} else {
			templateUrl = doc.type == "folder" ? 'views/view_folder.html' : 'views/view_document.html'
			controller = doc.type == "folder" ? 'viewFolderCtrl' : 'viewDocumentCtrl'
			size = doc.type == "folder" ? 'sm' : ''
		}
			
		var modalInstance = $modal.open({
		      templateUrl: templateUrl,
		      controller: controller,
		      size: size,
		      resolve: {
		      	document: function () {
		      		if(typeof doc != "string")
		      			return $.extend(true, {}, doc);
		      		else 
		      			return {type: "file"}
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
	
	$scope.firstFolderers = function(document) {
		return document.type != "folder";
	}
});

module.controller('viewDocumentCtrl', function ($scope, $modalInstance, $upload, $state, Document, document, editMode, currentPath) {	
	$scope.document = document; 
	$scope.payload = {};
		
	if(document.type == "text")
		$scope.payload.text = document.payload;
	else if(document.type == "link")
		$scope.payload.link = document.payload;
	
	// este "hack" hace falta por que en "selected" dentro de "tab" no funciona el operador "=="
	$scope.selectedTabs = [document.type == "file", document.type == "text", document.type == "link"];
	$scope.changeType = function(type) {
		$scope.selectedTabs = [type == "file", type == "text", type == "link"];
		$scope.document.type = type
	}
		
	$scope.onFileSelect = function($files) {
		$scope.changeType('file')
		$scope.payload.file = $files[0]
		$scope.fileName = $scope.payload.file.name;
		if(!document.id)
			$scope.document.name = $scope.payload.file.name;
	}
	
	var uploadFile = function(document) {
		$scope.upload = $upload.upload({
	        url: 'document/'+document.id, 
	        method: 'POST',
	        data: {upload: true},
	        file: $scope.payload.file,
	    }).progress(function(evt) {
	    	$scope.progress = parseInt(100.0 * evt.loaded / evt.total);
	    }).success(function(data, status, headers, config) {
	        $modalInstance.close(document);
	        $state.go($state.current, {}, {reload: true});
	        $scope.uploading = false;
	    }).error(function(data) {
	    	alert("Ha ocurrido un error subiendo el fichero.")
	    });	
	}
	
	$scope.ok = function () {
		$.extend($scope.document, {path: currentPath+$scope.document.name+"/"})
		
		if($scope.document.type == 'text')
			$scope.document.payload = $scope.payload.text;
		else if($scope.document.type == 'link')
			$scope.document.payload = $scope.payload.link;
		
		
	    var newDocument = new Document($scope.document)
		$scope.uploading = true;
		newDocument.$save(function(response) {
			if($scope.payload.file && $scope.document.type == 'file') {
				uploadFile(response);
			} else{
				$scope.uploading = false;
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
		$.extend($scope.document, {path: currentPath+$scope.document.name+"/", type: 'folder'})
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


