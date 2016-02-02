package jejs;

/**
 * 模板类
 * @author luminocean
 *
 */
public class Template {
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
		for(Token token: tokenizer){
			System.out.println(token);
		}
		return new Node();
	}
}
