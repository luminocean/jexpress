package jejs.node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jejs.Token;

public class ForNode extends Node{
	private Logger logger = LoggerFactory.getLogger(ForNode.class);
	// 要循环的目标对象
	private Object collectionId;
	private String iteratorId;

	public ForNode(Token token) {
		super(token);
		
		String command = text;
		String[] splits = command.split(" ");
		
		// 命令形如 for item in items
		if(splits.length != 4 || !splits[0].equals("for") || !splits[2].equals("in")){
			logger.warn("for命令格式不正确：" + command);
			return;
		}
		
		// 记录迭代id和迭代集合id
		iteratorId = splits[1];
		collectionId = splits[3];
	}

	@Override
	@SuppressWarnings("unchecked")
	public String render(Map<String, Object> context) {
		// 无可迭代返回空字符串
		if(collectionId == null || iteratorId == null) 
			return "";
		
		// 迭代目标集合
		Object collection = context.get(collectionId);
		if(!(collection instanceof Iterable) && !(collection instanceof Object[])){
			logger.warn("for循环的迭代对象并不可迭代");
			return "";
		}
		
		// 获取迭代目标集合的迭代器
		Iterator<Object> iter;
		if(collection instanceof Object[]){
			List<Object> co = Arrays.asList((Object[])collection);
			iter = co.iterator();
		}else{
			iter = ((Iterable<Object>)collection).iterator();
		}
		
		StringBuilder builder = new StringBuilder();
		// 执行for循环体
		while(iter.hasNext()){
			Object element = iter.next();
			// 复制出一个内部context
			Map<String, Object> innerContext = new HashMap<>(context);
			// 将迭代变量加入内部context
			innerContext.put(iteratorId, element);
			
			// 渲染所有的子节点
			for(Node child: children){
				builder.append(child.render(innerContext));
			}
		}
		
		return builder.toString();
	}
}
