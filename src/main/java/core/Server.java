package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
	// 线程池
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	// 路由表
	private Map<String, Handler> routeMap;
	
	public Server(Map<String, Handler> routeMap) {
		this.routeMap = routeMap;
	}

	public void listen(int port) {
		threadPool.execute(()->{
			while(true){
				try(ServerSocket ss = new ServerSocket(port)){
					Socket socket = ss.accept();
					threadPool.execute(()->{
						handle(socket);
					});
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
	}
	
	private void handle(Socket socket){
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())); 
			
			StringBuilder builder = new StringBuilder();
			String buf = null;
			
			while(true){
				while((buf = reader.readLine()) != null && !buf.equals("")){
					builder.append(buf+"\n");
				}
				
				// 读完了报文头
				if(buf != null){
					String headerStr = builder.toString();
					HttpHeader header = new HttpHeader(headerStr);
					serve(header, writer);
					
					builder = new StringBuilder();
					buf = null;
				}
				// socket读完，处理结束
				else{
					socket.close();
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务器处理方法
	 * @param header 报文头对象
	 * @param writer 
	 */
	private void serve(HttpHeader header, BufferedWriter writer) {
		String path = header.path;
		Handler handler = routeMap.get(path);
		
		Request req = new Request(header);
		Response res = new Response(writer);
		handler.handle(req, res);
	}
}
