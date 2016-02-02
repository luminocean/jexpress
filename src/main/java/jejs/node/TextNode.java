package jejs.node;

import java.util.Map;

import jejs.Token;

public class TextNode extends Node{
	public TextNode(Token token) {
		super(token);
	}

	@Override
	public String render(Map<String, Object> context) {
		return text;
	}
}