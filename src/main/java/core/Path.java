package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径匹配器
 * @author luminocean
 *
 */
public class Path {
	private final static Pattern splitPattern = Pattern.compile("[^{}]*(\\{.+\\})?[^{}]*");
	
	private Pattern pattern;
	private List<String> ids = new ArrayList<>();

	public Path(String path) {		
		String regexStr = ""; // 路径匹配正则的字符串
		
		// 形如/group/user{id}/{no}resume
		// 转为/group/user(.*?)/(.*?)resume
		// 每一个片段只允许一个{}匹配单元
		String[] splitPaths = path.split("/");
		for(String split: splitPaths){
			Matcher matcher = splitPattern.matcher(split);
			assert matcher.matches();
			
			int si = matcher.start(1);
			if( si != -1){
				int ei = matcher.end(1);
				
				String wrappingId = matcher.group(1);
				assert wrappingId.startsWith("{") && wrappingId.endsWith("}");
				// 加入id字符串列表
				ids.add(wrappingId.substring(1, wrappingId.length()-1));
				
				// 将占位符替换为对应的正则
				String replacedId = "(.*?)";
				String replacedSplit = split.substring(0, si) + replacedId + split.substring(ei);
				regexStr += (replacedSplit + "/");
			}else{
				regexStr += (split + "/");
			}
		}
		
		// 保证原字符串结尾有/替换后也有/，反之原字符串没有/替换后也没有/
		if(splitPaths.length > 0 && !splitPaths[splitPaths.length-1].equals("/"))
			regexStr = regexStr.substring(0, regexStr.length()-1);
		
		// 传进来的是""或"/"会导致regexStr为空，设置默认为/
		if(regexStr.length() == 0)
			regexStr = "/";

		pattern = Pattern.compile(regexStr);
	}

	/**
	 * 判断给定的路径字符串是否匹配
	 * @param string
	 * @return 匹配成功返回路径字符串中的各参数值，匹配失败返回null
	 */
	public Map<String, String> matches(String path) {
		// 匹配出来的键值对
		Map<String, String> result = new HashMap<>();
		
		Matcher matcher = pattern.matcher(path);
		if(!matcher.find()) return null;
		
		int size = ids.size();
		for(int i=0; i<size; i++){
			String id = ids.get(i);
			String value = matcher.group(i+1);
			result.put(id, value);
		}
		
		return result;
	}
	
	/**
	 * 判断给定的路径字符串是否完全匹配
	 * @param string
	 * @return 匹配成功返回路径字符串中的各参数值，匹配失败返回null
	 */
	public Map<String, String> completeMatches(String path) {
		// 匹配出来的键值对
		Map<String, String> result = new HashMap<>();
		
		Matcher matcher = pattern.matcher(path);
		if(!matcher.matches()) return null;
		
		int size = ids.size();
		for(int i=0; i<size; i++){
			String id = ids.get(i);
			String value = matcher.group(i+1);
			result.put(id, value);
		}
		
		return result;
	}
	
	/**
	 * 返回给定路径字符串匹配完剩下的字符串
	 * 比如请求/user/info/resume 去匹配正则 /user/info 返回 resume
	 * @param path
	 * @return
	 */
	public String beyondCapturedStr(String path){
		Matcher matcher = pattern.matcher(path);
		if(!matcher.find())
			return "";
		
		int ei = matcher.end();
		if(ei == path.length())
			return "";
		
		return path.substring(ei);
	}
}
