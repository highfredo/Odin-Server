package es.us.isa.odin.server.switcher;

import org.json.JSONObject;

import es.us.isa.odin.server.domain.Document;

public interface DocumentSwitcherJsonObject<T extends Document<?>> extends Switcher<T, JSONObject> {

}
