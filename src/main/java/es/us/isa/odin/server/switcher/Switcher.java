package es.us.isa.odin.server.switcher;

public interface Switcher<S, T> {

	public T convert(S source, String scope);
}
