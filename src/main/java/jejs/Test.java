package jejs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jejs.node.Node;

public class Test {
	
	public static void main(String[] args) {
		// 渲染数据准备
		Map<String, Object> data = new HashMap<>();
		List<String> items = new ArrayList<String>();
		items.add("Pistol");
		items.add("Excalibur");
		
		data.put("city", "NYC");
		data.put("human", new Human());
		data.put("items", items);
		
		// 表达式测试
		Template t1 = new Template("Hello dear {{human.name}}!");
		Node r1 = t1.compile();
		System.out.println(r1.render(data));
		
		// 循环测试
		Template t2 = new Template("<% for item in items %> I have {{item}} <% end %>");
		Node r2 = t2.compile();
		// System.out.println(r2);
	}
}

class Human{
	String name = "luMinO";
}
