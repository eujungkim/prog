package krog.jetty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MyJson {

	public static void main(String[] args) {
		JsonElement jsonElement = JsonParser.parseString("{ \"key\":\"value\" }");
		System.out.println(jsonElement.toString());
		
		// map to json string
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "value1");
		List<String> list = new ArrayList<String>();
		list.add("listvalue1");
		list.add("listvalue2");
		map.put("list", list);
		String json = gson.toJson(map);
		System.out.println(json); // {"key1":"value1","list":["listvalue1","listvalue2"]}
		
		// json string to map
	    Map<String, Object> map3 = new HashMap<String, Object>();
		map3 = (Map<String, Object>)gson.fromJson(json, map3.getClass());
	    System.out.println(map3);
	}
	
	static {
		String jsonStr = "{\"name\":\"hong\",\"age\":\"24\"}";
		
		// json string to map
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		map = (Map<String, String>)gson.fromJson(jsonStr, map.getClass());
	    System.out.println(map);
	    
	    // map to json string
	    String resultStr = gson.toJson(map);
	    System.out.println(resultStr);
	    System.out.println(jsonStr.equals(resultStr));
	}
	
	static {
		String jsonStr = "{\"name\":\"hong\",\"age\":\"24\"}";
		
		Gson gson = new Gson();
		User user = gson.fromJson(jsonStr, User.class);
		System.out.println(user);
		
		String resultStr = gson.toJson(user);
		System.out.println(resultStr);
	}
  
	static {
		String jsonStr = "{users:[{\"name\":\"hong\",\"age\":\"24\"}"
				+ ",{\"name\":\"kim\",\"age\":\"22\"}]}";
		
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(jsonStr, map.getClass());
		System.out.println(map);
		
		String resultStr = gson.toJson(jsonStr);
		System.out.println(resultStr);
	    System.out.println(jsonStr.equals(resultStr));
		
	}
}

class User {
	private String name;
	private String age;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
}