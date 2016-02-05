package jejs.node;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jejs.Token;

public class IfNode extends Node{
	private static Logger logger = LoggerFactory.getLogger(IfNode.class);
	private static Pattern idPattern = Pattern.compile("(!*)(.*)");
	
	private String ifId; // if命令要判断的id
	
	public IfNode(Token token) {
		super(token);
		
		String command = text;
		String[] splits = command.split(" ");
		
		// 命令形如 for item in items
		if(splits.length != 2 || !splits[0].equals("if")){
			logger.warn("if命令格式不正确：" + command);
			return;
		}
		
		ifId = splits[1];
	}

	@Override
	public String render(Map<String, Object> context) {
		if(ifId == null) return "";

		Matcher matcher = idPattern.matcher(ifId);
		if(!matcher.find()) return "";
		
		// 感叹号字符串
		String exclamations = matcher.group(1);
		// 去掉了前置!的id字符串
		String id = matcher.group(2);
		boolean bool;
		try{
			bool = (boolean)context.get(id);
		}catch(Exception e){
			logger.warn(e.getMessage());
			return "";
		}
		// 奇数个感叹号则反置
		if(exclamations.length() % 2 == 1){
			bool = !bool;
		}
		// 如果是false就返回
		if(!bool) return "";
		
		return super.render(context);
	}
}
