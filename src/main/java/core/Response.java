package core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jejs.Template;

public class Response {
	private static final Logger logger = LoggerFactory.getLogger(Response.class);
	private OutputStream out;

	public Response(OutputStream out) {
		this.out = out;
	}
	
	public void render(String templateText, Map<String, Object> context){
		Template template = new Template(templateText);
		String rendered = template.compile().render(context);
		send(rendered);
	}

	/**
	 * 发送文本响应
	 * @param file
	 */
	public void send(String file){
		byte[] data;
		try {
			data = file.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return;
		}
		
		send(data, "text/html");
	}
	
	/**
	 * 发送任意数据响应
	 * @param data
	 * @param mime
	 */
	public void send(byte[] data, String mime){
		try {			
			String responseHead = "HTTP/1.1 200 OK\r\n";
			responseHead += "Connection: keep-alive\r\n";
			responseHead += String.format("Content-type: %s; charset=utf-8\r\n", mime);
			responseHead += "Content-Length: "+data.length+"\r\n";
			responseHead += "\r\n";
			out.write(responseHead.getBytes("utf-8"));
			out.write(data);
			out.flush(); // flush很重要！
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
