package es.us.isa.odin.server.controllers;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import es.us.isa.odin.server.converters.DocumentToJsonObjectConverter;
import es.us.isa.odin.server.converters.JsonObjectToDocumentConverter;
import es.us.isa.odin.server.converters.StringToDocumentURIConverter;
import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.services.DocumentFolderService;

@RestController
@RequestMapping("/document")
@SuppressWarnings("rawtypes")
public class DocumentController {

	@Autowired
	private DocumentFolderService documentService;
	@Autowired
	private StringToDocumentURIConverter stringToDocumentURIConverter;
	@Autowired
	private JsonObjectToDocumentConverter<Document> jsonObjectToDocumentConverter;
	@Autowired
	private DocumentToJsonObjectConverter<Document> documentToJsonObjectConverter;
	
	
	
	@RequestMapping(value="/list/**", method=RequestMethod.GET)
	@SuppressWarnings("unchecked")
	public JSONArray list(HttpServletRequest request) {
	    String docId = extractPathFromPattern(request);
	    URI docUri = stringToDocumentURIConverter.convert(docId);
		List<Document> docs = documentService.listDocuments(docUri);
	    JSONArray result = new JSONArray();
	    
	    for(Document doc : docs) {
	    	result.put(documentToJsonObjectConverter.convert(doc));
	    }
	    
	    return result;
	}
	
	@RequestMapping(value="/**", method=RequestMethod.GET)
	public JSONObject get(HttpServletRequest request) {
	    String docId = extractPathFromPattern(request);
	    URI docUri = stringToDocumentURIConverter.convert(docId);
	    
	    return documentToJsonObjectConverter.convert(documentService.get(docUri));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/", method={RequestMethod.POST, RequestMethod.PUT})
	public JSONObject save(@RequestBody JSONObject json) {
	    Document doc = jsonObjectToDocumentConverter.convert(json);
	    doc = documentService.save(doc);
	    
	    return documentToJsonObjectConverter.convert(doc);
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
