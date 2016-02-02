package jejs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分词器
 * @author luminocean
 *
 */
public class Tokenizer implements Iterable<Token>{
	private final static String EXPR_OPEN = "\\{\\{";
	private final static String EXPR_CLOSE = "\\}\\}";
	private final static String BLOCK_OPEN = "<%";
	private final static String BLOCK_CLOSE = "%>";
	private final static String TEXT = "((?!<%)(?!\\{\\{)[\\s\\S])+"; // 拒绝<%或{{开头的任意文本
	
	private List<String> tokenStrs = new ArrayList<>();
	
	public Tokenizer(String text) {
		String reg = String.format("(%s.*?%s)|(%s.*?%s)|%s"
				, EXPR_OPEN, EXPR_CLOSE, BLOCK_OPEN, BLOCK_CLOSE, TEXT);
		Pattern pattern = Pattern.compile(reg);
		
		Matcher matcher = pattern.matcher(text);
		int si = 0, ei = text.length();
		matcher.region(si, ei); // 设置正则搜索区间
		
		while(matcher.lookingAt()){
			int li = matcher.start();
			int ri = matcher.end();
			String str = text.substring(li, ri);
			tokenStrs.add(str);
			
			si = ri;
			matcher.region(si, ei);
		}
	}
	
	@Override
	public Iterator<Token> iterator() {
		return new Iter(tokenStrs);
	}
}

/**
 * 分词结果的迭代器
 * @author luminocean
 *
 */
class Iter implements Iterator<Token>{
	private List<String> strs;
	private int index = 0;
	
	public Iter(List<String> strs){
		this.strs = strs;
	}
	@Override
	public boolean hasNext() {
		return index < strs.size();
	}

	@Override
	public Token next() {
		String str = strs.get(index++);
		return new Token(str);
	}
}
