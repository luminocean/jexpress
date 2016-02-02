package jejs.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jejs.Token;

/**
 * 语法树节点类，可以使用数据渲染返回文本
 * @author luminocean
 *
 */
public class Node {
	// 每个node是识别符（仅测试用）
	protected String id;
	// 子节点的有序列表
	public List<Node> children = new ArrayList<>();
	
	public Node(){
		this(new Token(""));
	}
	public Node(Token token){
		id = token.raw;
	}
	
	/**
	 * 使用context数据来渲染本节点
	 * @param context
	 * @return 渲染后的html字符串
	 */
	public String render(Map<String, Object> context) {
		StringBuilder builder = new StringBuilder();
		for(Node child: children){
			builder.append(child.render(context));
		}
		return builder.toString();
	}
	
	@Override
	public String toString(){
		if(children.size() == 0) return id;
		
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for(Node n: children){
			String str = n.toString();
			builder.append(str+',');
		}
		builder.deleteCharAt(builder.length()-1);
		builder.append("]");
		
		return id + "-" + builder.toString();
	}
}
