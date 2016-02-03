package core;

import java.util.HashMap;
import java.util.Map;

public class App {
	private Map<String, Handler> routeMap = new HashMap<>();
	
	/**
	 * 添加一个get请求的handler
	 * @param path
	 * @param handler
	 */
	public void get(String path, Handler handler) {
		routeMap.put(path, handler);
	}
	
	/**
	 * 为发送给执行基路径的请求处理中添加一个中间件
	 * @param statics
	 * @param middleware 
	 */
	public void use(String basePath, Middleware middleware) {
		// TODO Auto-generated method stub
	}

	public void listen(int port) {
		new Server(routeMap).listen(port);
	}
}