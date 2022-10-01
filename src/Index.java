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
	public static boolean newTree = false;
	public static String nT = "";
	
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
//			System.out.println("INTAKE " + intake);
			if (intake.substring(0,8).equals("*DELETE*")) {
				delete.add(intake);
			}
			else if (intake.substring(0,6).equals("*EDIT*")) {
				edit.add(intake);
			}
			else {
				if (!arr.contains(intake))
				arr.add(intake);
			}
		}
		
		//rn everything should be added if it isn't a call
		//Purpose: for both edit and delete arrays, check if the array has it, if it does then delete it from the delete or edit arrays
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
		if (delete.size()>0||edit.size()>0) {
			for (String str:arr) {
				if (str.substring(0,3).equals("obj"))
					arr.remove(str);
			}
		}
		while (delete.size()>0) {
			String finding = "Blob : " + Blob.createHash(delete.get(0).substring(8)) + " : " + delete.get(0).substring(8);
			ArrayList<String> allBlobs = deleteBlob(finding,tree);		
			for (String str:allBlobs) {
				arr.add(str.substring(7));
			}
			delete.remove(0);
//			for (String str:delete) {
//				System.out.println("IN DELETE: " + str);
//			}
		}
		while (edit.size()>0) {
			
		}
		
		
		
		//CONVERT TO REGULAR ARRAY 
		for (String str:arr) {
			System.out.println("IN ARR: " + str);
		}
		String[] array = new String[arr.size()];
		for (int i = 0; i < array.length; i++) {
			array[i]=arr.get(i);
		}
		clearIndex();
		//THIS RETURN WILL HAVE THE FINAL THING - NOTHING AFTER THIS SHOULD BE CHANGED
		return array;
	}
	
	private static ArrayList<String> deleteBlob(String finding, String tree) throws FileNotFoundException {
		ArrayList<String> allBlobs = new ArrayList<String>();
		boolean found = false;
		File tr = new File(tree);
		String maybetree = "";
		while (found == false) {
			Scanner ts = new Scanner(tr);
			while (ts.hasNextLine()) {
				String next = ts.nextLine();
				System.out.println("NEXT:" + next);
				if (next.equals(finding)) {
					found = true;
				}
				else {
					if (next.substring(0,4).equals("Blob")) {
						allBlobs.add(next);//in correct format
					}
					else if (!next.equals("")){//line = tree line
						maybetree = next.substring(7);
					}
				}
				
			}
			if (found==false) {
				tr = new File(maybetree);
			}
			else {
				if (!(maybetree=="")) {
					//allBlobs.add("Blob : Tree : " + maybetree);
					nT = maybetree;
					newTree = true;
				}
			}
		}
		return allBlobs;
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
	
	String stringifyIndex(HashMap<String, String> map) throws IOException {
		String ret = "";
		for(Map.Entry<String, String> i : map.entrySet()) {
			if (!ret.contains((i.getValue()) + (" : ") + (i.getKey()) + ('\n')));
			ret+=(i.getValue()) + (" : ") + (i.getKey()) + ('\n');
		}
		File index = new File("index");
		Scanner in = new Scanner(index);
		while (in.hasNextLine()) {
			ret+=(in.nextLine() + "\n");
		}
		return ret;
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
	
	void saveIndex() throws IOException {
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
	
	public void add(String filename) throws IOException{
		loadIndex();
		Blob blob = new Blob(filename);
		index.put(filename, blob.getHash());
		saveIndex();
//		
//		File idf = new File("index");
//		Scanner indexScanner = new Scanner(idf);
//		String alreadyThere = "";
//		while (indexScanner.hasNextLine()) {
//			String str = indexScanner.nextLine();
//			alreadyThere = str + "\n";
//		}
//		String addLine = Blob.createHash(filename) + " : " + filename; //ex: *DELETE*text2.txt
//		alreadyThere = alreadyThere + addLine;
//		FileWriter fww = new FileWriter(idf);
//		fww.append(alreadyThere); 
//		fww.close();		
	}

	public void remove(String filename) throws IOException {
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
