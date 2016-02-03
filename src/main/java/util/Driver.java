package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.App;
import core.Express;

public class Driver {
	public static void main(String[] args) {
		App app = Express.createApp();
		
		// TODO
		app.use("/static", Express.statics("public"));
		
		app.get("/", (req, res) -> {
			// 页面渲染数据
			Map<String, Object> context = getDisplayData();
			
			// 渲染并发送页面
			String file = FileSystem.readFile("hello.html");
			res.render(file, context);
		});
		// 监听端口
		app.listen(8080);
	}
	
	private static Map<String, Object> getDisplayData(){
		Map<String, Object> data = new HashMap<>();
		data.put("os", System.getProperty("os.name"));
		data.put("architecture", System.getProperty("os.arch"));
		data.put("osversion", System.getProperty("os.version"));
		
		List<String> foods = new ArrayList<>();
		foods.add("Sushi");
		foods.add("Salmon");
		foods.add("Spring Roll");
		data.put("foods", foods);
		
		return data;
	}
}
