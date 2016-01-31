package util;

import core.App;
import core.Express;

public class Driver {

	public static void main(String[] args) {
		App app = Express.createApp();
		app.get("/", (req, res) -> {
			String file = FileSystem.readFile("hello.html");
			res.send(file);
		});
		app.listen(8080);
	}
}
