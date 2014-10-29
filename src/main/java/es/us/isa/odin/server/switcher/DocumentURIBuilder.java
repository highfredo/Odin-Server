package es.us.isa.odin.server.switcher;

import java.net.URI;

import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

public interface DocumentURIBuilder {

	public URI build(String path) throws NoSuchRequestHandlingMethodException;
}
