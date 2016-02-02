package jejs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jejs.node.Node;

public class Test {

	public static void main(String[] args) {
		Map<String, Object> data = new HashMap<>();
		data.put("name", "luMinO");
		data.put("items", new ArrayList<String>());
		
		// "<% for item in human.items %> I have {{item}} <% end %>"
		Template t1 = new Template("Hello dear {{name}}!");
		Node r1 = t1.compile();
		System.out.println(r1);
		
		Template t2 = new Template("<% for item in items %> I have {{item}} <% end %>");
		Node r2 = t2.compile();
		System.out.println(r2);
	}
}
