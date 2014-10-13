package es.us.isa.odin.server.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.forms.DocumentForm;
import es.us.isa.odin.server.services.DocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService<MongoDocument> documentService;
	
	
	@RequestMapping(value="/save")
	public DocumentForm save(@RequestBody DocumentForm documentForm) {
		
		MongoDocument document;
		if(documentForm.getId() == null) {
			document = new MongoDocument();
		} else {
			document = documentService.get(documentForm.getId());
		}
		
		document.setRevision(documentForm.getRevision());
		document.setName(documentForm.getName());
		document.setPath(documentForm.getPath());
		document.setDescription(documentForm.getDescription());
		document.setFolder(documentForm.getIsFolder());
		document.setMetadata(documentForm.getMetadata());
		
		document = documentService.save(document);
		
		return new DocumentForm(document);
	}
	
	@RequestMapping("/list")
	public List<DocumentForm> list(@RequestParam("path") String path) {
		List<DocumentForm> result = new ArrayList<DocumentForm>();
		for(MongoDocument doc : documentService.listDocuments(path)) {
			result.add(new DocumentForm(doc));
		}
		
		return result;
	}
	
	@RequestMapping("/get")
	public DocumentForm get(@RequestParam("id") String id) {
		return new DocumentForm(documentService.get(id)); 
	}
	
	@RequestMapping("/remove")
	public JSONObject remove(@RequestParam("id") String id) {
		JSONObject result = new JSONObject();
		if(documentService.remove(id)) {
			result.append("remove", "OK");
		} else {
			result.append("remove", "Fail");
		}
		return result; 
	}
	
	@RequestMapping("/download")
	public ResponseEntity<InputStreamResource> download(@RequestParam("id") String id) throws IOException, NoSuchRequestHandlingMethodException {
		MongoDocument doc = documentService.get(id);
		
		InputStreamResource inputStreamResource = new InputStreamResource(documentService.getDocumentPayload(id));
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Disposition", "attachment; filename=\"" + doc.getName() + "\"");
	    headers.setContentLength(doc.getLength());
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    
	    return new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);
	}

    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, @RequestParam String id){
    	HashMap<String, String> response = new HashMap<String, String>();
        if (!file.isEmpty()) {
        	MongoDocument doc = documentService.get(id);
        	doc.setType(file.getContentType());
        	// FIXME: saveDocumentPayload deberia aceptar mejor MultipartFile
        	documentService.save(doc); 
            try {
                documentService.saveDocumentPayload(id, file.getInputStream());
                response.put("OK", "Fichero subido correctamente");
            } catch (Exception e) {
            	response.put("error", e.getMessage());
            }
        } else {
        	response.put("error", "File empty");
        }
        
        return response;
    }
    
}
