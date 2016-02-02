package jejs;

/**
 * 分词单元
 * @author luminocean
 *
 */
public class Token {
	public enum TOKEN_TYPE{
		EXPR, BLOCK_START, BLOCK_END, TEXT
	}
	public TOKEN_TYPE type;
	
	public String raw;
	public String trimed;
	public String value;
	
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
			}else{
				type = TOKEN_TYPE.BLOCK_START;
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
