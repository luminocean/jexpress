package core;

/**
 * 拦截器接口
 * @author luminocean
 *
 */
@FunctionalInterface
public interface Interceptor {
	/**
	 * 拦截处理
	 * @param req
	 * @param res
	 * @return 继续后续处理返回true，否则返回false
	 */
	public boolean intercept(Request req, Response res);
}
