package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.PathUtil;

/**
 * 带中间件功能的请求分发类
 * @author luminocean
 *
 */
public class Chain {
	private final static Logger logger = LoggerFactory.getLogger(Chain.class);
	private List<Bundle> bundles = new ArrayList<Bundle>();
	
	/**
	 * 链式处理请求
	 * 路径上所有符合的中间件都要处理
	 * 最后处理最后一个节点上的handler
	 * @param path 请求的全路径
	 * @param req
	 * @param res
	 */
	public void handle(String path, Request req, Response res) {
		List<Bundle> collected = collectInterceptors(path, req.method);
		boolean continues = true;
		for(Bundle bundle: collected){
			Method method = bundle.method;
			if(method != req.method && method != Method.ALL) 
				continue; // method类型不符合的请求跳过
			
			// 记录剩余路径
			String watchedPath = bundle.watchPath;
			req.pathBeyondCaptured = path.substring(watchedPath.length());
			
			// 执行本拦截器
			Interceptor interceptor = bundle.interceptor;
			continues = interceptor.intercept(req, res);
			
			// 清空剩余路径
			req.pathBeyondCaptured = null;
			// 拦截器终止继续处理
			if(!continues) break;
		}
		
		// 如果都处理结束但是continues还为真，说明这个请求没有人实际处理，警告
		if(continues){
			logger.warn("无效路径" + path);
		}
	}

	/**
	 * 添加一个中间件
	 * @param path
	 * @param middleware
	 */
	public void addMiddleware(String path, Middleware middleware){
		Bundle bundle = new Bundle(path, Method.ALL, middleware);
		bundles.add(bundle);
	}
	
	/**
	 * 添加一个handler
	 * @param method
	 * @param path
	 * @param handler
	 */
	public void addHandler(String path, Method method, Handler handler){
		Bundle bundle = new Bundle(path, method, handler);
		bundles.add(bundle);
	}
	
	/**
	 * 收集符合指定路径的所有interceptors
	 * @param path
	 * @param method
	 * @return
	 */
	private List<Bundle> collectInterceptors(String path, Method method) {
		List<Bundle> collected = new ArrayList<Bundle>();
		for(Bundle bundle: bundles){
			String watchedPath = bundle.watchPath;
			if(path.startsWith(watchedPath)){
				collected.add(bundle);
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
	public String watchPath; // 本拦截器正在监控的路径
	public Method method;
	public Interceptor interceptor;
	
	public Bundle(String watchPath, Method method, Interceptor interceptor){
		this.watchPath = watchPath;
		this.method = method;
		this.interceptor = interceptor;
	}
}


