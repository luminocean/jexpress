package core;

/**
 * 请求处理接口
 * @author luminocean
 *
 */
@FunctionalInterface
public interface Handler{
	public void handle(Request req, Response res);
}
