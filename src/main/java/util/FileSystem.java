package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileSystem {

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
			e1.printStackTrace();
		}catch(IOException e2){
			e2.printStackTrace();
		}
		
		return "";
	}
}
