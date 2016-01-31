# jexpress

jexpress是一个轻量级的web服务器，可以作为嵌入式http服务器使用。它是Express.js的Java实现，纯属玩具性质

目前支持返回classpath下的静态文本文件，默认使用长连接

### 基本使用

基本使用方式类似于Express.js：
```java
public class Driver {
	public static void main(String[] args) {
		// 创建一个app实例
		App app = Express.createApp();

		// 设置路由
		// 在这里对访问根目录/的请求返回hello.html文件（classpath下检索）
		app.get("/", (req, res) -> {
			String file = FileSystem.readFile("hello.html");
			res.send(file);
		});

		// 设定监听的端口
		app.listen(8080);
	}
}
```
