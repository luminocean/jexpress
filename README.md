# jexpress

jexpress是一个轻量级的web服务器，可以作为嵌入式http服务器使用

它是Express.js的极简Java实现，纯属玩具性质

支持功能:
- 读取返回classpath下的静态页面文件
- 实现了简单的模板引擎
- 长连接

目前内置实现的模板引擎支持以下语法：（可参考src/main/resources/hello.html）
- 变量值取出：{{varible}}
- for循环:
```
<% for item in items %>
	display:{{item}}
<% end %>
```

### 基本使用

基本使用方式类似于Express.js：
```java
// src/main/java/util/Driver.java
public class Driver {
	public static void main(String[] args) {
		// 创建app实例
		App app = Express.createApp();
		// 设定路由, 在这里当浏览器对根目录发起get请求时，执行本回调并返回页面文本
		app.get("/", (req, res) -> {
			// 读取classpath下的模板文件
			String template = FileSystem.readFile("hello.html");
			// 准备页面渲染数据
			Map<String, Object> context = getDisplayData();
			// 渲染并发送页面
			res.render(template, context);
		});
		// 监听端口
		app.listen(8080);
	}

	// getDisplayData等工具方法...
}
```
