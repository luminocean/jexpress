package jejs.node;

import java.util.Map;

import jejs.Token;

public class TextNode extends Node{
	private String text;
	
	public TextNode(Token token) {
		super(token);
		text = token.raw;
	}

	@Override
	public String render(Map<String, Object> context) {
		return text;
	}
}
