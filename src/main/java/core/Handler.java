package core;

@FunctionalInterface
public interface Handler{
	public void handle(Request req, Response res);
}
