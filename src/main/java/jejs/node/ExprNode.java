package jejs.node;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jejs.Token;

/**
 * 表达式节点
 * @author luminocean
 *
 */
public class ExprNode extends Node{
	private static Logger logger = LoggerFactory.getLogger(ExprNode.class);
	// 表达式字符串
	private String[] idChain;

	public ExprNode(Token token) {
		super(token);
		String expr = token.value; // 形如 human.name
		idChain = expr.split("\\.");
	}

	@Override
	public String render(Map<String, Object> context) {
		Object obj = context;
		
		// 沿着id链往下找
		for(String id: idChain){
			// 如果是map
			if(obj instanceof Map){
				try{
					@SuppressWarnings("unchecked")
					Map<String, Object> item = (Map<String, Object>)obj;
					obj = item.get(id);
					if(obj == null){
						logger.warn("找不到"+id+"对应的值");
					}
				}catch(Exception e){
					logger.warn("渲染map类型不是<String,Object>");
					return "";
				}
			}
			// 如果是其他类型（普通对象）
			else{
				Class<?> cls = obj.getClass();
				try {
					// 使用反射获取对应域的值
					Field field = cls.getDeclaredField(id);
					field.setAccessible(true);
					obj = field.get(obj);
					if(obj == null){
						logger.warn("找不到"+id+"对应的值");
					}
				} catch (NoSuchFieldException e) {
					logger.warn("渲染时找不到域"+id+":"+e.getMessage());
					return "";
				} catch (SecurityException e) {
					logger.warn("安全异常"+e.getMessage());
					return "";
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.warn("获取域数据异常"+e.getMessage());
					return "";
				}
			}
		}
		
		// 没有迭代或者迭代出来为空，返回空字符串
		if(obj == context || obj == null) return "";
		
		return obj.toString();
	}
}
