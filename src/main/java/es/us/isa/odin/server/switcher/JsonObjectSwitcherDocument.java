package es.us.isa.odin.server.switcher;

import org.json.JSONObject;

import es.us.isa.odin.server.domain.Document;

public interface JsonObjectSwitcherDocument<T extends Document<?>> extends Switcher<JSONObject, T> {

}
