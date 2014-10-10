package es.us.isa.odin.server.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.services.MongoDocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private MongoDocumentService documentService;
	
	
	@RequestMapping(value="/testsave")
	public MongoDocument save(){
		MongoDocument document = new MongoDocument();
		document.setName("HOLA MUNDO");
		documentService.save(document);
		return document;
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
