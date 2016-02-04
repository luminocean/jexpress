package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带中间件功能的请求分发类
 * @author luminocean
 *
 */
public class Chain {
	private final static Logger logger = LoggerFactory.getLogger(Chain.class);
	private Node root = new Node("/");
	
	/**
	 * 链式处理请求
	 * 路径上所有符合的中间件都要处理
	 * 最后处理最后一个节点上的handler
	 * @param path
	 * @param req
	 * @param res
	 */
	public void handle(String path, Request req, Response res) {
		Node handlerNode = findNode(path, (passingNode) ->{			
			List<Middleware> middlewares = passingNode.middlewares;
			
			// 调用路过node的各个中间件
			boolean continues = true;
			for(Middleware mw: middlewares){
				continues = mw.handle(req, res);
				if(!continues) break;
			}
			return continues;
		});
		
		if(handlerNode == null) return;
		
		// 正式处理请求的handler
		Handler handler = handlerNode.handlers.get(req.method);
		handler.handle(req, res);
	}
	
	/**
	 * 添加一个中间件
	 * @param path
	 * @param middleware
	 */
	public void addMiddleware(String path, Middleware middleware){
		Node node = setupNode(path);		
		// 为这个node添加一个中间件
		node.middlewares.add(middleware);
	}
	
	/**
	 * 添加一个handler
	 * @param method
	 * @param path
	 * @param handler
	 */
	public void addHandler(String method, String path, Handler handler){
		Node node = setupNode(path);	
		node.handlers.put(method, handler);
	}
	
	/**
	 * 根据路径建立对应的节点，包括路径上的中间节点
	 * @param path
	 * @return
	 */
	private Node setupNode(String path){		
		// 找到传入的path对应的node
		Node node = root;
		String[] fragments = path.split("/");
		for(String frag: fragments){
			if(frag.trim().equals("")) continue;
			
			if(node.nexts.get(frag) == null){
				Node newNode = new Node(frag);
				node.nexts.put(frag, newNode);
				node = newNode;
			}else{
				node = node.nexts.get(frag);
			}
		}
		
		return node;
	}
	
	/**
	 * 根据路径查找并返回对应的节点，包括路径上的中间节点
	 * @param path
	 * @return
	 */
	private Node findNode(String path, NodeInspector inspector){		
		// 找到传入的path对应的node
		Node node = root;
		inspector.inspect(node);
		
		String[] fragments = path.split("/");
		for(String frag: fragments){
			if(frag.trim().equals("")) continue;
			
			if(node.nexts.get(frag) == null){
				logger.error("无效路径");
				return null;
			}else{
				node = node.nexts.get(frag);
			}
			
			// 调用node的各个中间件
			boolean continues = inspector.inspect(node);
			if(!continues) return null;
		}
		
		return node;
	}
}

/**
 * 查找链的节点类
 * @author luminocean
 *
 */
class Node{
	public String fragment;
	public Map<String, Node> nexts = new HashMap<>();
	public List<Middleware> middlewares = new ArrayList<>();
	public Map<String, Handler> handlers; // method -> handler
	
	public Node(String fragment){
		this.fragment = fragment;
		this.handlers = new HashMap<String, Handler>();
	}
}

@FunctionalInterface
interface NodeInspector{
	/**
	 * 回调node
	 * @param node
	 * @return 终止处理返回false，否则返回true
	 */
	public boolean inspect(Node node);
}
