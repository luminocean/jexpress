package core;

/**
 * 中间件接口
 * @author luminocean
 *
 */
public interface Middleware {
	public void handle(Request req, Response res, Middleware next);
}
