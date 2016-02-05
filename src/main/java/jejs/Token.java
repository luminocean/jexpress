package jejs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分词单元
 * @author luminocean
 *
 */
public class Token {
	private static Logger logger = LoggerFactory.getLogger(Token.class);
	public static enum TOKEN_TYPE{
		EXPR, FOR_BLOCK_START, IF_BLOCK_START, BLOCK_END, TEXT
	}
	
	public TOKEN_TYPE type;
	public String raw; // 正则匹配出来的原始字符串
	public String trimed; // trim以后的字符串
	public String value; // 去掉包裹符号并trim以后的字符串
	
	public Token(String str) {
		this.raw = str;
		this.trimed = str.trim();
		
		// 表达式token
		if(trimed.startsWith("{{")){
			type = TOKEN_TYPE.EXPR;
			assert trimed.endsWith("}}");
			value = trimed.substring(2, trimed.length()-2).trim();
		}
		// 块token
		else if(trimed.startsWith("<%")){
			assert trimed.endsWith("%>");
			value = trimed.substring(2, trimed.length()-2).trim();
			
			// block token有两种
			if(value.equals("end")){
				type = TOKEN_TYPE.BLOCK_END;
			}else if(value.startsWith("for")){
				type = TOKEN_TYPE.FOR_BLOCK_START;
			}else if(value.startsWith("if")){
				type = TOKEN_TYPE.IF_BLOCK_START;
			}else{
				logger.error("未知block语法："+ value);
			}
		}
		// 普通文本token
		else{
			type = TOKEN_TYPE.TEXT;
			value = str;
		}
	}
	
	public String toString(){
		return String.format("[\"%s\"(%s) - %s]", raw, value, type);
	}
}
