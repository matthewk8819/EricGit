import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Index {
	
//	public static void main (String [] args) throws IOException {
//		Index index = new Index();
//		index.init();
//		index.add("file1");
//		index.add("file2");
//		//index.remove("file1");
//		String[] arr = new String[1];
//		try {
//			arr = getIndexContents();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(arr[0] + "\n" + arr[1]);
//	}
	//CAN REMOVE THE STATIC AFTER DONE WITH MAIN TESTER 
	static HashMap<String, String> index = new HashMap<String, String>();
	public static String[] getIndexContents(String tree) throws IOException {
		ArrayList<String> delete = new ArrayList<String>();//contains filenames of deletes
		ArrayList<String> edit = new ArrayList<String>();//contains filenames of edits 
		
		System.out.println("Tree: " + tree);
		
		File indexFile = new File("index");
		Scanner indexScanner = new Scanner(indexFile);
		ArrayList<String> arr = new ArrayList<String>();
		while (indexScanner.hasNextLine()) {
			String intake = indexScanner.nextLine();
			if (intake.substring(0,8).equals("*DELETE*")) {
				delete.add(intake);
			}
			else if (intake.substring(0,6).equals("*EDIT*")) {
				edit.add(intake);
			}
			else {
				arr.add(intake);
			}
		}
		//rn everything should be added if it isn't a call
		//should contain 
		//for both edit and delete arrays, check if the array has it, if it does then delete it from the delete or edit arrays
		for (String str:delete) {
			String finding = Blob.createHash(str) + " : " + str;
			if (arr.remove(finding)) {
				delete.remove(str);
			}
		}
		for (String str:edit) {
			String finding = Blob.createHash(str) + " : " + str;
			if (arr.remove(finding)) {
				edit.remove(str);
			}
		}
		
		//checked in current staged things, now start the traversal process of remaining deletes/edits
		while (delete.size()>0) {
			
		}
		while (edit.size()>0) {
			
		}
		
		
		
		//CONVERT TO REGULAR ARRAY 
		String[] array = new String[arr.size()];
		for (int i = 0; i < array.length; i++) {
			array[i]=arr.get(i);
		}
		clearIndex();
		//THIS RETURN WILL HAVE THE FINAL THING - NOTHING AFTER THIS SHOULD BE CHANGED
		return array;
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
			if(!line.contains(" : ")) continue;
			String[] split = line.split(" : ");
			ret.put(split[1], split[0]);
		}
		
		return ret;
	}
	
	String stringifyIndex(HashMap<String, String> map) {
		StringBuilder ret = new StringBuilder();
		for(Map.Entry<String, String> i : map.entrySet()) {
			ret.append(i.getValue()).append(" : ").append(i.getKey()).append('\n');
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
