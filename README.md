# jexpress

jexpress是一个轻量级的web服务器，可以作为嵌入式http服务器使用

它是Express.js的极简Java实现，纯属玩具性质

支持功能:
- 实现了简单的模板引擎jejs（虽然完全没有参考ejs）
- 静态文件中间件支持
- 使用长连接

### 基本使用

基本使用方式类似于Express.js：
```java
// src/main/java/util/Driver.java
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
			String template = FileSystem.readTextFile("hello.html");
			res.render(template, context);
		});
		// 监听端口
		app.listen(8080);
	}

	// getDisplayData等工具方法...
}
```

### 静态资源捕获

静态资源捕获的用处在于可以在某一段url和具体一个静态文件目录之间建立映射关系。
比如在`/static`和public目录之间建立映射后，
请求`localhost:8080/static/balabala.jpg`，服务器可以自动去路径`/public/balabala.jpg`查找并返回该文件。
这个特性对于访问js,css或图片等静态资源时很方便。

使用方法如下：
```java
// 静态资源请求捕获
app.use("/static", Express.statics("public"));
```

### jejs模板引擎

目前内置实现的模板引擎支持以下语法：（可参考src/main/resources/hello.html）

#### 1. 变量值取出：
```
// varible = "foo"
{{varible}}
```
生成结果：
```
foo
```

#### 2. for循环:
```html
// items = ['apple', 'flag', 'dummy']
<% for item in items %>
	display:{{item}}
<% end %>
```
生成结果：
```
display:apple
display:flag
display:dummy
```


使用方式如下：
```java
// 动态请求
app.get("/user/info", (req, res) -> {
	// 页面渲染数据
	Map<String, Object> data = getData();
	// 获取模板文件
	String template = FileSystem.readTextFile("user.html");
	// 渲染并发送文件
	res.render(file, context);
});
```
其中`user.html`可以是正常的html文件，只要在需要动态生成html的地方加入上述所示的语法即可。
