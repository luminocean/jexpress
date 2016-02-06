package core;

import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestPath {
	@Test
	public void testNormal() {
		Path path = new Path("/group/user{id}/{no}resume");
		Map<String, String> params = path.matches("/group/user10086/autoresume");
		assertEquals("10086", params.get("id"));
		assertEquals("auto", params.get("no"));
	}
	
	@Test
	public void testDouble() {
		// 一个url片段里面只允许一个{}
		Path path = new Path("/group/user{id}{carol}/resume");
		Map<String, String> params = path.matches("/group/userfoo/resume");
		assertEquals("foo", params.get("id}{carol"));
	}
	
	@Test
	public void testPlain() {
		Path path = new Path("/group/user");
		Map<String, String> params = path.matches("/group/user/autoresume");
		assertNotNull(params);
	}
	
	@Test
	public void testUnmatch() {
		Path path = new Path("/group/user/autoresume");
		Map<String, String> params = path.matches("/foo");
		assertNull(params);
	}
	
	@Test
	public void testEmpty() {
		Path path = new Path("");
		Map<String, String> params = path.matches("/foo");
		assertNotNull(params);
	}
}
