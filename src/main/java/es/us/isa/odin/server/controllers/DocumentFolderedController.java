package es.us.isa.odin.server.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import es.us.isa.odin.server.domain.Document;
import es.us.isa.odin.server.domain.documenttype.DocumentTypes;
import es.us.isa.odin.server.services.DocumentFolderService;
import es.us.isa.odin.server.switcher.DocumentSwitcherJsonObject;
import es.us.isa.odin.server.switcher.DocumentURIBuilder;
import es.us.isa.odin.server.switcher.JsonObjectSwitcherDocument;

@RestController
@RequestMapping("/document")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocumentFolderedController {

	@Autowired
	private DocumentFolderService documentService;
	@Autowired
	private DocumentSwitcherJsonObject<Document<?>> toJsonObject;
	@Autowired
	private JsonObjectSwitcherDocument<Document<?>> toDocument;
	@Autowired
	private DocumentURIBuilder uriBuilder;
	@Autowired
	private DocumentTypes documentTypes;
	
	
	@RequestMapping(value="", method=RequestMethod.GET, params="uri")
	public List<JSONObject> list(@RequestParam String uri) throws NoSuchRequestHandlingMethodException {
	    URI docUri = uriBuilder.build(uri);
	    
		List<JSONObject> result = new ArrayList<JSONObject>();
		List<Document<?>> documents = documentService.listDocuments(docUri);
		
		for(Document<?> doc : documents) {
			result.add(toJsonObject.convert(doc, "list"));
		}
		
		return result;
	}
	
	
	@RequestMapping(value="/**", method=RequestMethod.PUT, params="move")
	public JSONObject move(@RequestParam("move") String moveTo, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		JSONObject response = new JSONObject();
		String docId = extractPathFromPattern(request);
	    URI fromUri = uriBuilder.build(docId);
	    URI toUri = uriBuilder.build(moveTo);
	    
		try {
			documentService.move(fromUri, toUri);
			response.put("OK", "Fichero copiado correctamente");
		} catch (Exception e) {
			response.put("error", e.getMessage());
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
