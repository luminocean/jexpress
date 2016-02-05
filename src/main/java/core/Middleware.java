package core;

/**
 * 中间件接口
 * @author luminocean
 *
 */
public interface Middleware extends Interceptor{
	@Override
	default boolean intercept(Request req, Response res) {
		return middleProcess(req, res);
	}

	public boolean middleProcess(Request req, Response res);
}
