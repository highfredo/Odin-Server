package es.us.isa.odin.server.converters;

import java.net.URI;

import org.json.JSONObject;
import org.springframework.core.convert.converter.Converter;

import es.us.isa.odin.server.domain.Document;

public interface JsonObjectToDocumentConverter<S extends Document<?>> {

	public S convert(JSONObject source);
}
