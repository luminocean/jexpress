package jejs;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jejs.node.ExprNode;
import jejs.node.ForNode;
import jejs.node.IfNode;
import jejs.node.Node;
import jejs.node.TextNode;

/**
 * 模板类
 * @author luminocean
 *
 */
public class Template {
	private static Logger logger = LoggerFactory.getLogger(Template.class);
	private String templateText;
	
	public Template(String templateText){
		this.templateText = templateText;
	}
	
	/**
	 * 编译模板，使用模板文本构建语法树
	 * @return
	 */
	public Node compile(){
		Tokenizer tokenizer = new Tokenizer(templateText);
		Node root = new Node();
		Stack<Node> scopeStack = new Stack<>();
		scopeStack.push(root);
		
		for(Token token: tokenizer){
			// 当前的scope
			Node scope = scopeStack.peek();
			
			// 表达式
			if(token.type == Token.TOKEN_TYPE.EXPR){
				ExprNode node = new ExprNode(token);
				scope.children.add(node);
			}
			// for块开始
			else if(token.type == Token.TOKEN_TYPE.FOR_BLOCK_START){
				Node node = new ForNode(token);
				scope.children.add(node);
				scopeStack.push(node); // 块node本身会构成一个作用域，因此加入scope栈
			}
			// if块开始
			else if(token.type == Token.TOKEN_TYPE.IF_BLOCK_START){
				Node node = new IfNode(token);
				scope.children.add(node);
				scopeStack.push(node); // 块node本身会构成一个作用域，因此加入scope栈
			}
			// 块结束
			else if(token.type == Token.TOKEN_TYPE.BLOCK_END){
				scopeStack.pop(); // 简单地退出当前scope即可
			}
			// 普通文本
			else if(token.type == Token.TOKEN_TYPE.TEXT){
				TextNode node = new TextNode(token);
				scope.children.add(node);
			}
			// 错误
			else{
				logger.error("解析到语法错误："+ token.raw);
			}
		}
		return root;
	}
}
