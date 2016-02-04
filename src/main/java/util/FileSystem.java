package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.server.Server;

public class FileSystem {
	private static Logger logger = LoggerFactory.getLogger(FileSystem.class);

	public static String readFile(String filePath) {
		try {
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filePath)));
			String buf;
			while((buf = reader.readLine()) != null){
				builder.append(buf);
			}
			String result = builder.toString();
			reader.close();
			
			return result;
		}catch (FileNotFoundException e1) {
			logger.error(e1.getMessage());
		}catch(IOException e2){
			logger.error(e2.getMessage());
		}
		
		return "";
	}
}
