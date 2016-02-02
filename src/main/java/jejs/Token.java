package jejs;

/**
 * 分词单元
 * @author luminocean
 *
 */
public class Token {
	public enum TOKEN_TYPE{
		EXPR, BLOCK, TEXT
	}
	public TOKEN_TYPE type;
	
	public String raw;
	public String trimed;
	public String value;
	
	public Token(String str) {
		this.raw = str;
		this.trimed = str.trim();
		
		if(trimed.startsWith("{{")){
			type = TOKEN_TYPE.EXPR;
			assert trimed.endsWith("}}");
			value = trimed.substring(2, trimed.length()-2).trim();
		}else if(trimed.startsWith("<%")){
			type = TOKEN_TYPE.BLOCK;
			assert trimed.endsWith("%>");
			value = trimed.substring(2, trimed.length()-2).trim();
		}else{
			type = TOKEN_TYPE.TEXT;
			value = str;
		}
	}
	
	public String toString(){
		return String.format("[\"%s\"(%s) - %s]", raw, value, type);
	}
}
