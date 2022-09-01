import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index {
	public void init() {
		Path index = Paths.get("index");
		Path objects = Paths.get("objects");
		try {
			if(!Files.exists(index)) Files.createFile(index);
			if(!Files.exists(objects)) Files.createDirectory(objects);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
	
	HashMap<String, String> parseIndex(String content) {
		HashMap<String, String> ret = new HashMap<String, String>;
		
		String[] lines = content.split("\n");
		for(String line : lines) {
			if(!line.contains(":")) continue;
			String[] split = line.split(":");
			ret.put(split[0], line[1]);
		}
	}
	
	String stringifyIndex(HashMap<String, String> map) {
		
	}
	
	public void add(String filename) {
		
	}
	public void remove(String filename) {
		
	}
}
