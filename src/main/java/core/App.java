package core;

import core.server.Server;

public class App {
	private Chain chain = new Chain();
	/**
	 * 处理服务器解析而来的请求
	 * @param req
	 * @param res
	 */
	public void handle(Request req, Response res) {
		String path = req.path;
		chain.handle(path, req, res);
	}
	/**
	 * 添加一个get请求的handler
	 * @param path
	 * @param handler
	 */
	public void get(String pathStr, Handler handler) {
		Path path = new Path(pathStr);
		chain.addHandler(path, Method.GET, handler);
	}
	
	/**
	 * 添加一个中间件
	 * @param path
	 * @param middleware 
	 */
	public void use(String pathStr, Middleware middleware) {
		Path path = new Path(pathStr);
		chain.addMiddleware(path, middleware);
	}
	
	/**
	 * 添加一个默认中间件，对所有请求生效
	 * @param path
	 * @param middleware 
	 */
	public void use(Middleware middleware) {
		Path path = new Path("/");
		chain.addMiddleware(path, middleware);
	}

	/**
	 * 开启服务器监听指定端口
	 * @param port 要坚挺的端口号
	 */
	public void listen(int port) {
		new Server(this).listen(port);
	}
}