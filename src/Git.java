import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Git {
	
	//initialize repo
	//work on files + commit
	// - write some files 
	// - stage those files (add to index as blob)
	//commmit - create a tree by copying index to tree (and then clear index)and also look for previous history 
	//refer to the functionality for the super stuff of deleting and editing 
	
	
	//this is the step of writing some files text1.txt and text2.txt
	public static void main (String [] args) throws IOException {
		Git git = new Git();
		git.initRepo("NewREPO");
		writeFilesTest();
		git.stageFile("text1.txt");
		git.stageFile("text2.txt");
		git.stageFile("text3.txt");
		git.addCommit("SUMMARY2","SAME AUTHOR");
		git.stageFile("text4.txt");
		git.stageFile("text5.txt");
		git.addCommit("Summary3", "NEW AUTHOR");
	}
	
	
	private Index i;
	private String headFile;//file with the location/name of the current commit object 
	private String repoName;
	
	public Git () {
		
	}
	
	//initialize index, get name of repo
	public void initRepo (String repoName) {
		this.repoName = repoName;//idk if this ever comes into play actually
		i = new Index();
		i.init();
	}
	
	public static void writeFilesTest() throws IOException {
		File f1 = new File("text1.txt");
		File f2 = new File("text2.txt");
		File f3 = new File("text3.txt");
		File f4 = new File("text4.txt");
		File f5 = new File("text5.txt");
		FileWriter w = new FileWriter(f1);
		w.append("This is some content 1");
		w.close();
		w = new FileWriter (f2);
		w.append("This is some content 2");
		w.close();
		w = new FileWriter (f3);
		w.append("This is some content 3");
		w.close();
		w = new FileWriter (f4);
		w.append("This is some content 4");
		w.close();
		w = new FileWriter (f5);
		w.append("This is some content 5");
		w.close();
	}
	
	public void stageFile(String fileName) {
		i.add(fileName);//creates blob in the index
	}
	
	//adds a commit: if first, sets to head; else makes two way connection with this and prev commit
	public void addCommit (String summary, String auth) throws IOException {
		if (headFile==null) {
			File headF = new File ("HEAD");
			//read in blobs from index
			String [] indexContents = i.getIndexContents();
			Commit newCommit = new Commit("",indexContents,summary,auth,null);
			FileWriter fw = new FileWriter(headF);
			fw.append(newCommit.getHash());
			fw.close();
			headFile = headF.getName();
			System.out.println(headFile);
		}
		else {//not first commit
			File f = new File(headFile);
			Scanner headReader = new Scanner(f);
			String prevCommit = headReader.nextLine();//prevCommit = name of now past commit
			File lastCommit = new File("objects/" + prevCommit);
			Scanner commitReader = new Scanner(lastCommit);
			String lastTree = commitReader.nextLine();
			String [] indexContents = i.getIndexContents();
			Commit newCommit = new Commit(lastTree,indexContents,summary,auth,"objects/"+prevCommit);
			//set the prevCommit next to this current one 
			Commit.setNext(prevCommit, newCommit.getHash());
			FileWriter fw = new FileWriter(f);
			fw.append(newCommit.getHash());
			fw.close();
			
		}
		ArrayList<String> indexList = new ArrayList<String>();
		
	}
	
	
}
