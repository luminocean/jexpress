package core.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import core.App;
import core.Express;
import util.FileSystem;

public class Driver {
	public static void main(String[] args) {
		App app = Express.createApp();
		// 静态资源请求捕获
		app.use("/static", Express.statics("public"));
		
		// 动态请求
		app.get("/", (req, res) -> {
			// 页面渲染数据
			Map<String, Object> context = getDisplayData();
			// 渲染并发送页面
			String template = FileSystem.readTextFile("jexpress.html");
			res.render(template, context);
		});
		
		// 带参数请求
		app.get("/user/{id}", (req, res) -> {
			// 页面渲染数据
			Map<String, Object> context = getDisplayData();
			// 渲染并发送页面
			String template = FileSystem.readTextFile("index.html");
			res.render(template, context);
		});
		
		// 404处理
		app.use((req, res) -> {
			String page404 = FileSystem.readTextFile("404.html");
			res.sendText(page404);
			return false; // 不再往下处理
		});
		
		// 启动服务器，监听端口
		app.listen(8080);
	}
	
	private static Map<String, Object> getDisplayData(){
		Map<String, Object> data = new HashMap<>();
		data.put("greetings", true);
		data.put("osname", System.getProperty("os.name"));
		data.put("architecture", System.getProperty("os.arch"));
		data.put("osversion", System.getProperty("os.version"));
		
		List<String> foods = new ArrayList<>();
		foods.add("Coke");
		foods.add("Chips");
		foods.add("Umaru");
		data.put("foods", foods);
		
		return data;
	}
}