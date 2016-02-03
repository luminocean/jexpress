package core;

public class Express {
	/**
	 * 创建一个web app对象
	 * @return 新建的web app对象
	 */
	public static App createApp() {
		return new App();
	}

	/**
	 * 添加一个服务静态资源的中间件
	 * @param string 请求的资源路径
	 * @return
	 */
	public static Middleware statics(String path) {
		return null;
	}
}
