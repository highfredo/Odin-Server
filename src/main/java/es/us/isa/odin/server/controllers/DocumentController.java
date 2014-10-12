package es.us.isa.odin.server.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.services.DocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService<MongoDocument> documentService;
	
	
	@RequestMapping(value="/save")
	public MongoDocument save(){
		MongoDocument document = new MongoDocument();
		document.setName("carpetita");
		document.setFolder(true);
		document.setPath("/");
		documentService.save(document);
		return document;
	}
	
	@RequestMapping("/list")
	public List<MongoDocument> list(@RequestParam("path") String path) {
		return documentService.listDocuments(path);
	}
	
	@RequestMapping("/get")
	public MongoDocument get(@RequestParam("id") String id) {
		return documentService.get(id); 
	}
	
	@RequestMapping("/download")
	public ResponseEntity<InputStreamResource> download(@RequestParam("id") String id) throws IOException, NoSuchRequestHandlingMethodException {
		MongoDocument doc = documentService.get(id);
		
		InputStreamResource inputStreamResource = new InputStreamResource(documentService.getDocumentPayload(id));
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Disposition", "attachment; filename=\"" + doc.getName() + "\"");
	    headers.setContentLength(doc.getLength());
	    
	    return new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);
	}

    
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, @RequestParam String id){
    	HashMap<String, String> response = new HashMap<String, String>();
        if (!file.isEmpty()) {
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
