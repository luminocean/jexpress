package jexpress;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jejs.Template;
import jejs.node.Node;

public class TestTemplate {
	private Map<String, Object> data;
	private Template t;
	private Node n;
	
	@Before
	public void setUp() throws Exception {
		// 渲染数据准备
		data = new HashMap<>();
		List<String> items = new ArrayList<String>();
		items.add("Pistol");
		items.add("Excalibur");
		
		Human human = new Human();
		human.name = "luMinO";
		
		data.put("city", "NYC");
		data.put("superior", true);
		data.put("lower", false);
		data.put("human", human);
		data.put("items", items);
	}

	/**
	 * 表达式测试
	 */
	@Test
	public void testExpression() {
		// 普通变量
		t = new Template("Greeting {{city}}!");
		n = t.compile();
		assertEquals("Greeting NYC!", n.render(data));
		
		// 连续变量
		t = new Template("Greeting {{human.name}}!");
		n = t.compile();
		assertEquals("Greeting luMinO!", n.render(data));
	}
	
	/**
	 * for循环测试
	 */
	@Test
	public void testFor(){
		t = new Template("<% for item in items %>I have {{item}}\n<% end %>");
		n = t.compile();
		assertEquals("I have Pistol\nI have Excalibur\n", n.render(data));
	}
	
	/**
	 * if测试
	 */
	@Test
	public void testIf(){
//		t = new Template("<% if superior %>super!<% end %>");
//		n = t.compile();
//		assertEquals("super!", n.render(data));
		
//		t = new Template("haha<% if lower %>lower!<% end %>foo");
//		n = t.compile();
//		assertEquals("hahafoo", n.render(data));
	}
}

class Human{
	String name;
}
