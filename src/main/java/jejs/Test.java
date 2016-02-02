package jejs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		Map<String, Object> data = new HashMap<>();
		data.put("name", "luMinO");
		data.put("items", new ArrayList<String>());
		
		// "<% for item in human.items %> I have {{item}} <% end %>"
		Template t1 = new Template("Hello dear {{name}}!");
		t1.compile().render(data);
		
		Template t2 = new Template("<% for item in items %> I have {{item}} <% end %>");
		t2.compile().render(data);
	}
}
