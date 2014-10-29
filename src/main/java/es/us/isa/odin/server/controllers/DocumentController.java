package es.us.isa.odin.server.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.services.DocumentService;
import es.us.isa.odin.server.switcher.DocumentSwitcherJsonObject;
import es.us.isa.odin.server.switcher.DocumentURIBuilder;
import es.us.isa.odin.server.switcher.JsonObjectSwitcherDocument;

@RestController
@RequestMapping("/document")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocumentController {

	@Autowired
	private DocumentService documentService;
	@Autowired
	private DocumentSwitcherJsonObject<Document> toJsonObject;
	@Autowired
	private JsonObjectSwitcherDocument<Document> toDocument;
	@Autowired
	private DocumentURIBuilder uriBuilder;
	
	
	@RequestMapping(value="/**", method=RequestMethod.GET)
	public JSONObject get(HttpServletRequest request) {
		String docId = extractPathFromPattern(request);
	    URI docUri = uriBuilder.build(docId);
		return toJsonObject.convert(documentService.get(docUri), "get"); 
	}
	
	@RequestMapping(value="/**", method=RequestMethod.GET, params="download=true")
	public ResponseEntity<InputStreamResource> download(HttpServletRequest request) throws IOException, NoSuchRequestHandlingMethodException {
		String docId = extractPathFromPattern(request);
		URI docUri = uriBuilder.build(docId);
		Document doc = documentService.get(docUri);
		
		InputStreamResource inputStreamResource = new InputStreamResource(documentService.getDocumentPayload(docUri));
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Disposition", "attachment; filename=\"" + doc.getName() + "\"");
		headers.setContentLength(doc.getLength());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		return new ResponseEntity<InputStreamResource>(inputStreamResource, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value="/**", method=RequestMethod.DELETE)
	public JSONObject remove(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		String docId = extractPathFromPattern(request);
	    URI docUri = uriBuilder.build(docId);
		if(documentService.remove(docUri)) {
			result.append("remove", "OK");
		} else {
			result.append("remove", "Fail");
		}
		return result; 
	}
	
	@RequestMapping(value="", method=RequestMethod.GET, params="uri")
	public List<JSONObject> list(@RequestParam String uri) {
	    URI docUri = uriBuilder.build(uri);
	    
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<Document> documents = documentService.listDocuments(docUri);
		
		for(Document doc : documents) {
			result.add(toJsonObject.convert(doc, "list"));
		}
		
		return result;
	}
	
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public JSONObject save(@RequestBody JSONObject documentForm) {
		Document document = toDocument.convert(documentForm, "save");
		document = documentService.save(document);
		
		return toJsonObject.convert(document, "save");
	}
	
	@RequestMapping(value="/**", method=RequestMethod.PUT)
	public JSONObject update(@RequestBody JSONObject documentForm) {
		Document document = toDocument.convert(documentForm, "update");
		document = documentService.save(document);
		
		return toJsonObject.convert(document, "update");
	}
		
	@RequestMapping(value="/**", method=RequestMethod.POST, params="upload=true")
	public Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		String docId = extractPathFromPattern(request);
	    URI docUri = uriBuilder.build(docId);
	    
		HashMap<String, String> response = new HashMap<String, String>();
		if (!file.isEmpty()) {
			Document doc = documentService.get(docUri);
			doc.setType(file.getContentType());
			try {
				documentService.save(doc, file.getInputStream());
				response.put("OK", "Fichero subido correctamente");
			} catch (Exception e) {
				response.put("error", e.getMessage());
			}
		} else {
			response.put("error", "File empty");
		}
		
		return response;
	}
	

    
    
    
	/**
	 * http://stackoverflow.com/questions/3686808/spring-3-requestmapping-get-path-value
	 * Extract path from a controller mapping. /controllerUrl/** => return matched **
	 * @param request incoming request.
	 * @return extracted path
	 */
	public static String extractPathFromPattern(final HttpServletRequest request){
	    String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
	    String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

	    AntPathMatcher apm = new AntPathMatcher();
	    String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

	    return finalPath;
	}
}
