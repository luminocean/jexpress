package core;

public class Request {
	public String path;

	public Request(HttpHeader header) {
		this.path = header.path;
	}
}
