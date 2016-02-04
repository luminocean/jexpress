package core;

/**
 * 中间件接口
 * @author luminocean
 *
 */
public interface Middleware {
	/**
	 * 中间件处理
	 * @param req
	 * @param res
	 * @return 继续处理后面的中间件返回true，否则返回false
	 */
	public boolean handle(Request req, Response res);
}
