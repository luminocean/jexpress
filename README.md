# jexpress

jexpress是一个轻量级的web服务器，使用Java实现，可以作为嵌入式http服务器使用。
接口风格与[Express.js](http://expressjs.com)相似。

支持功能:
- 中间件支持
- 实现了简单的模板引擎jejs（虽然完全没有参考ejs）
- 静态资源捕获

## 1. 部署指南

### 1.1 打包

在安装Maven后在项目根目录下执行`mvn clean compile assembly:single`即可将本项目达成jar包

### 1.2 独立测试运行

如果想查看演示站点，直接执行`java -jar target/jexpress-x.x.x-SNAPSHOT-jar-with-dependencies.jar`启动服务器（版本号可能有所不同）。然后访问`localhost:8080`即可。

### 1.3 嵌入使用

如果要作为嵌入式服务器使用，只要将打包后的jar包加入你的项目classpath中，然后参考[示例驱动](src/main/java/core/driver/Driver.java)使用即可。


## 2. 基本使用

基本使用方式类似于Express.js：

```java
public class Driver {
	public static void main(String[] args) {
		// 创建app实例
		App app = Express.createApp();

		// 客户端请求"/"时返回某页面
		app.get("/", (req, res) -> {
			// 准备页面渲染数据
			Map<String, Object> context = getDisplayData();

			// 读取模板文件
			String template = FileSystem.readTextFile("hello.html");

			// 渲染并发送页面
			res.render(template, context);
		});

		// 启动服务器并监听端口
		app.listen(8080);
	}

	// 其他工具方法...
}
```

为了简单起见，jexpress内置的`FileSystem`类目前只支持从classpath中查找并返回文件内容。
因此在你的项目中如果要使用这个FileSystem类访问文件，请将资源目录放进classpath。(当然你也完全可以自行读出你需要的文件 :P)

## 3. 静态资源捕获

静态资源捕获的用处在于可以在**某一段请求url**和**服务器上具体一个静态文件或目录**之间建立映射关系。

比如在请求地址`/static`和本地目录`public`之间建立映射后，
客户端请求`localhost:8080/static/balabala.jpg`，服务器可以自动去本地路径`public/balabala.jpg`查找并返回该文件。

这个特性使得在访问js,css或图片等静态资源时可以把一个文件夹下的各种资源一并挂到服务器上，而不用对这些资源一一设置路由，使用起来很方便。

使用方法如下：
```java
// 静态资源请求捕获
app.use("/static", Express.statics("public"));
```

## 4. jejs模板引擎

jexpress自行实现了一个简易的模板引擎jejs，相关内容如下

### 4.1 使用方式

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

### 4.2 语法说明

目前内置实现的模板引擎支持以下语法：（可参考src/main/resources/hello.html）

#### 1. 变量值取出
```
// varible = "foo"
{{varible}}
```
生成结果：
```
foo
```

#### 2. if循环

```html
// greetings = true
<% if greetings %>
	<p>Hello</p>
<% end %>

<% if !greetings %>
	<p>Bye</p>
<% end %>
```
生成结果：
```
Hello
```

#### 3. for循环
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
