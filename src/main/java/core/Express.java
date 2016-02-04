package core;

import util.FileSystem;
import util.PathUtil;

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
	 * @param dirPath
	 * @return
	 */
	public static Middleware statics(String dirPath) {
		final String dp = PathUtil.normalizeDirPath(dirPath);
		
		return new Middleware() {
			@Override
			public boolean handle(Request req, Response res) {
				String file = FileSystem.readTextFile(dp + PathUtil.normalizeFilePath(req.pathBeyondCaptured));
				res.sendText(file);
				return false; // 停止继续往下传递
			}
		};
	}
}
