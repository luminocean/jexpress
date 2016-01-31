package core;

import java.util.HashMap;
import java.util.Map;

public class App {
	private Map<String, Handler> routeMap = new HashMap<>();
	
	public void get(String path, Handler handler) {
		routeMap.put(path, handler);
	}

	public void listen(int port) {
		new Server(routeMap).listen(port);
	}
}