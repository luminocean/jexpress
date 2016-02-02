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
	protected String id;
	
	public Node(){
		this(new Token(""));
	}
	public Node(Token token){
		id = token.raw;
	}
	
	// 子节点的有序列表
	public List<Node> children = new ArrayList<>();
	
	public String render(Map<String, Object> data) {
		return null;
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
