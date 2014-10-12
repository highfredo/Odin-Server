package es.us.isa.odin.server.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.us.isa.odin.server.domain.MongoDocument;
import es.us.isa.odin.server.services.DocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService<MongoDocument> documentService;
	
	
	@RequestMapping(value="/testsave")
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
	public HttpEntity<byte[]> download(@RequestParam("id") String id) throws IOException {
		MongoDocument doc = documentService.get(id);
	    byte[] file = IOUtils.toByteArray(documentService.getDocumentPayload(id));

	    HttpHeaders header = new HttpHeaders();
	    //header.setContentType(new MediaType("application", "pdf")); TODO:
	    header.set("Content-Disposition", "attachment; filename=" + doc.getName().replace(" ", "_"));
	    header.setContentLength(file.length);
	    
	    return new HttpEntity<byte[]>(file, header);
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
