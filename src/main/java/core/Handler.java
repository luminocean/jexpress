package core;

/**
 * 请求处理接口
 * @author luminocean
 *
 */
public interface Handler extends Interceptor{
	@Override
	default boolean intercept(Request req, Response res) {
		handle(req, res);
		return false;
	}

	public void handle(Request req, Response res);
}
