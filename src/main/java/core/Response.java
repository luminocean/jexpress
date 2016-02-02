package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import jejs.Template;
import jejs.node.Node;

public class Response {
	private BufferedWriter writer;

	public Response(BufferedWriter writer) {
		this.writer = writer;
	}
	
	public void render(String templateText, Map<String, Object> context){
		Template template = new Template(templateText);
		String rendered = template.compile().render(context);
		send(rendered);
	}

	public void send(String file){	
		try {			
			String responseText = "HTTP/1.1 200 OK\r\n";
			responseText += "Connection: keep-alive\r\n";
			responseText += "Content-type: text/html; charset=utf-8\r\n";
			responseText += "Content-Length: "+file.getBytes().length+"\r\n";
			responseText += "\r\n";
			responseText += file;
			
			writer.write(responseText);
			writer.flush(); // flush很重要！
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
