package jejs.node;

import jejs.Token;

/**
 * 表达式节点
 * @author luminocean
 *
 */
public class ExprNode extends Node{
	private String expr;

	public ExprNode(Token token) {
		super(token);
		// 形如 human.name，暂时简化成name处理
		String expr = token.value;
		this.expr = expr;
	}
}
