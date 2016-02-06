package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 链式请求分发类
 * @author luminocean
 *
 */
public class Chain {
	private final static Logger logger = LoggerFactory.getLogger(Chain.class);
	private List<Bundle> bundles = new ArrayList<Bundle>();
	
	/**
	 * 链式地使用符合要求的拦截器处理请求
	 * @param pathStr 请求的全路径
	 * @param req
	 * @param res
	 */
	public void handle(String pathStr, Request req, Response res) {
		// 收集所有符合要求的拦截器
		List<Bundle> collected = collectInterceptors(pathStr, req.method);
		
		boolean continues = true;
		for(Bundle bundle: collected){
			// method类型不符合的请求跳过
			Method method = bundle.method;
			if(method != req.method && method != Method.ALL) 
				continue;
			
			// 记录剩余路径（处理请求时可能会用到）
			Path watchedPath = bundle.watchPath;
			req.pathBeyondCaptured = watchedPath.beyondCapturedStr(pathStr);
			req.params = watchedPath.matchedParams(pathStr);
			
			// 执行当前拦截器
			Interceptor interceptor = bundle.interceptor;
			continues = interceptor.intercept(req, res);
			
			// 清空剩余路径
			req.pathBeyondCaptured = null;
			// 拦截器终止继续处理
			if(!continues) break;
		}
		
		// 如果都处理结束但是continues还为真，说明这个请求没有人实际处理，发出警告
		if(continues){
			logger.warn("无效路径" + pathStr);
		}
	}

	/**
	 * 添加一个中间件
	 * @param path
	 * @param middleware
	 */
	public void addMiddleware(Path matcher, Middleware middleware){
		Bundle bundle = new Bundle(matcher, Method.ALL, middleware);
		bundles.add(bundle);
	}
	
	/**
	 * 添加一个handler
	 * @param method
	 * @param matcher
	 * @param handler
	 */
	public void addHandler(Path matcher, Method method, Handler handler){
		Bundle bundle = new Bundle(matcher, method, handler);
		bundles.add(bundle);
	}
	
	/**
	 * 收集符合指定路径的所有interceptors
	 * @param path
	 * @param method
	 * @return
	 */
	private List<Bundle> collectInterceptors(String pathStr, Method method) {
		List<Bundle> collected = new ArrayList<Bundle>();
		
		for(Bundle bundle: bundles){
			Path path = bundle.watchPath;
			// 前缀路径符合
			if(path.matches(pathStr)){
				// 中途中间件或是路径完全匹配的handler，加入
				if(bundle.interceptor instanceof Middleware 
						|| path.completeMatches(pathStr)){
					collected.add(bundle);
				}
			}
		}
		
		return collected;
	}
}

/**
 * 将拦截器和相关实用数据打包的类
 * @author luminocean
 *
 */
class Bundle{
	public Path watchPath; // 本拦截器正在监控的路径
	public Method method;
	public Interceptor interceptor;
	
	public Bundle(Path watchPath, Method method, Interceptor interceptor){
		this.watchPath = watchPath;
		this.method = method;
		this.interceptor = interceptor;
	}
}


