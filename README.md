# jexpress

jexpress is a light-weight web server implemented in Java.
jexpress can be used as a stand-alone server or a build-in server.
The API design is similar to [Express.js](http://expressjs.com) and yes, this is exactly how the name came from.

Features:
- url parameter capture
- simple template engine jejs
- support for middle ware
- static resource mapping

## 1. Deployment Guide

### 1.1 Packaging

Install Maven first.
Simply run `mvn clean compile assembly:single` under the project root and you can get the project jar file.

### 1.2 Use in stand-alone mode

If you want to see the demo site, just run `java -jar target/jexpress-x.x.x-SNAPSHOT-jar-with-dependencies.jar` to launch the server.
Visit `localhost:8080` to see the demo page.

### 1.3 Use in built-in mode

If you want to use jexpress as a built-in server, just add the packaged jar file to your classpath.
See [Demo Driver](src/main/java/core/driver/Driver.java) for a reference.


## 2. Basic usage

The basic usage is pretty like the Express.js：

```java
public class Driver {
	public static void main(String[] args) {
		// create an app instance
		App app = Express.createApp();

		// return some page when client requested '/'
		app.get("/", (req, res) -> {
			// prepare rendering data
			Map<String, Object> context = getDisplayData();

			// read the template file
			String template = FileSystem.readTextFile("hello.html");

			// render and send the result page
			res.render(template, context);
		});

		// start the server
		app.listen(8080);
	}

	// other utility methods...
}
```

For simplicity, for now the built-in `FileSystem` class only supports read files that are relative to paths in classpath.
So if you want to use the `FileSystem` class, please put the resource path in classpath. (or read the file manually by your self :P)

## 3. Static Resouce Mapping

Static resource mapping is used to establish a mapping between a `url` and an actuall `file path` on the server machine.

For example, if there's a mapping between request url `/static` and local directory `public`,
when the client requests `localhost:8080/static/balabala.jpg`, the server looks up the file in `public/balabala.jpg` (relative to a path in classpath) and return it.

Usage：
```java
app.use("/static", Express.statics("public"));
```

## 4. url parameter capture

Url parameter capture means that when the client requests url like `/user/info/1456`, the server can capture the `1456` part and do something with it.
You can get this part from `req.params`(which is a map)

Usage：
```java
// request with parameterized url
app.get("/user/{name}", (req, res) -> {
	Map<String, Object> context = getDisplayData();

	// get the name parameter
	context.put("name", req.params.get("name"));

	// render and send the result page
	String template = FileSystem.readTextFile("hello.html");
	res.render(template, context);
});
```

## 5. jejs template engine

jexpress implemented a simple template engine jejs.

### 5.1 Usage

jejs is used to render user html template.

```java
app.get("/user/info", (req, res) -> {
	Map<String, Object> data = getData();
	String template = FileSystem.readTextFile("user.html");
	res.render(file, context);
});

// implementation of res.render()
public void render(String templateText, Map<String, Object> context){
	// use the template string to create a template object
	Template template = new Template(templateText);
	// compile the template and render the context
	String rendered = template.compile().render(context);
	// return rendered string
	sendText(rendered);
}
```

### 5.2 Syntax

For now the built-in template engine supports following syntaxes：（see [demo page](src/main/resources/hello.html) for more information）

#### 1. variable evaluation
```
// varible = "foo"
{{varible}}
```
result：
```
foo
```

#### 2. conditional rendering

```html
// greetings = true
<% if greetings %>
	<p>Hello</p>
<% end %>

<% if !greetings %>
	<p>Bye</p>
<% end %>
```
result：
```
Hello
```

Rules for conditional rendering：
- false and null are treated as false
- others are treated as true

#### 3. list rendering
```html
// items = ['apple', 'flag', 'dummy']
<% for item in items %>
	display:{{item}}
<% end %>
```
result：
```
display:apple
display:flag
display:dummy
```
