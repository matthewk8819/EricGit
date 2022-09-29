import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Index {
	
	public static void main (String [] args) throws FileNotFoundException {
		Index index = new Index();
		index.init();
		index.add("file1");
		index.add("file2");
		//index.remove("file1");
		String[] arr = new String[1];
		try {
			arr = getIndexContents();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(arr[0] + "\n" + arr[1]);
	}
	//CAN REMOVE THE STATIC AFTER DONE WITH MAIN TESTER 
	static HashMap<String, String> index = new HashMap<String, String>();
	public static String[] getIndexContents() throws IOException {
		File indexFile = new File("index");
		Scanner indexScanner = new Scanner(indexFile);
		String[] arr = new String[index.size()];
		int counter = 0;
		while (indexScanner.hasNextLine()) {
			arr[counter] = indexScanner.nextLine();
			counter++;
		}
		//clear index
		clearIndex();
		return arr;
	}
	
	private static void clearIndex() throws IOException {//REMOVE STATIC LATER 
		File f = new File("index");
		FileWriter fw = new FileWriter(f);
		fw.append("");
		fw.close();
	}
	
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
			ret.append(i.getKey()).append(":").append(i.getValue()).append('\n');
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
