package core.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.App;
import core.Request;
import core.Response;

/**
 * 服务器类
 * @author luminocean
 *
 */
public class Server{
	private final static Logger logger = LoggerFactory.getLogger(Server.class);
	// 本Server从属的app对象
	private App app;
	// 线程池
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public Server(App app) {
		this.app = app;
	}

	public void listen(int port) {		
		// 开进程执行服务器主循环
		threadPool.execute(()->{
			logger.info("服务器进程开启");
			while(true){
				try(ServerSocket ss = new ServerSocket(port)){
					Socket socket = ss.accept();
					// 每次有新的连接就交付线程池里面的线程处理
					threadPool.execute(()->{
						handle(socket);
					});
				}catch(IOException e){
					logger.error(e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 处理一个新的socket连接
	 * @param socket
	 */
	private void handle(Socket socket){
		try {
			// TODO: 改为二进制读取，而不是文本的方式
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			OutputStream out = socket.getOutputStream();
			
			StringBuilder builder = new StringBuilder();
			String buf = null;
			
			logger.debug("读取请求内容...");
			// 持续从socket中读取数据
			while(true){
				while((buf = reader.readLine()) != null && !buf.equals("")){
					builder.append(buf+"\n");
				}
				
				// 读完了报文头
				if(buf != null){
					String headerStr = builder.toString();
					Header header = new Header(headerStr);
					
					// 对该请求进行正式的服务
					serve(header, out);
					
					// 重置缓冲
					builder = new StringBuilder();
					buf = null;
				}
				// socket读完，处理结束
				else{
					logger.debug("请求处理完毕，结束本次请求");
					socket.close();
					return;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 服务器处理方法
	 * @param header 报文头对象
	 * @param writer 
	 */
	private void serve(Header header, OutputStream out) {
		// 构造请求与响应对象
		Request req = new Request(header);
		Response res = new Response(out);
		
		// 回调app对象处理请求
		app.handle(req, res);
	}
}
