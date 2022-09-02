import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Index {
	HashMap<String, String> index = new HashMap<String, String>();
	
	public void init() {
		Path indexFile = Paths.get("index");
		Path objects = Paths.get("objects");
		try {
			if(!Files.exists(indexFile)) Files.createFile(indexFile);
			if(!Files.exists(objects)) Files.createDirectory(objects);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
	
	HashMap<String, String> parseIndex(String content) {
		HashMap<String, String> ret = new HashMap<String, String>();
		
		String[] lines = content.split("\n");
		for(String line : lines) {
			if(!line.contains(":")) continue;
			String[] split = line.split(":");
			ret.put(split[0], split[1]);
		}
		
		return ret;
	}
	
	String stringifyIndex(HashMap<String, String> map) {
		StringBuilder ret = new StringBuilder();
		for(Map.Entry<String, String> i : map.entrySet()) {
			ret.append(i.getKey()).append(':').append(i.getValue()).append('\n');
		}
		return ret.toString();
	}
	
	void loadIndex() {
		Path indexFile = Paths.get("index");
		String content;
		try {
			content = new String(Files.readAllBytes(indexFile));
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
		index = parseIndex(content);
	}
	
	void saveIndex() {
		Path indexFile = Paths.get("index");
		String content = stringifyIndex(index);
		try {
			if(!Files.exists(indexFile)) Files.createFile(indexFile);
			Files.writeString(indexFile, content);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
	
	public void add(String filename) {
		loadIndex();
		Blob blob = new Blob(filename);
		index.put(filename, blob.getHash());
		saveIndex();
	}
	public void remove(String filename) {
		loadIndex();
		Path path = Paths.get("objects", Blob.createHash(filename));
		try {
			if(Files.exists(path)) Files.delete(path);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
		index.remove(filename);
		saveIndex();
	}
}
