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
	
	public void stageDelete(String fileName) throws IOException{
		//add a line to the index - literally file write it 
		File f = new File("index");
		FileWriter fw = new FileWriter(f);
		Scanner indexScanner = new Scanner(f);
		String alreadyThere = "";
		while (indexScanner.hasNextLine()) {
			alreadyThere = indexScanner.nextLine() + "\n";
		}
		String deleteLine = "*DELETE*" + fileName; //ex: *DELETE*text2.txt
		alreadyThere = alreadyThere + deleteLine;
		fw.append(alreadyThere); 
		
		
		//when copying in getIndexContents, if find something with *DELETE* or *EDIT* in it, then don't add it
		//if you find edit or delete,boolean = true and add it to the delete or edit arraylist 
		//0th order: still add everything to the arr of contents 
		// - NEXT STEPS: DO IT PER DELETE/EDIT: start with filename, finish with a new arraylist (don't submit because might have more edits/deletes) - JUST LOGICED IT OUT AND IT WOULD WORK - AS LONG AS IT WORKS CORRECTLY AND EACH ITERATION WOULD LEAVE THE arraylist in good condition with at most 1 tree and the rest of the blobs 
		//first order 1: look at current arraylist and see if it exists - if it does, delete it from the array and the delete array 
		//first order: traverse starting the last tree (can get tree from parameter)
		// - if there is no tree, means it is the first commit - just go thru the existing contents array and delete the file to be deleted
		//second order: code the traversal - go thru and do the thing (thru files)
		// - search for the blob (create a string called finding), same process as before
		// - go thru each tree, collect all the blobs - two conditions: find or don't find - can traverse using tree location in that tree
		//third order: rn you should have an array of allBlobs (which potentially has a tree)
		//fourth order: go thru the arr made in getContents, delete the one with tree (if it exists)
		//fifth order: create a new array with combined contents and allblobs, return it 
		//sixth order: this should have all of the things its supposed to with the correct pointers 
		
		//new ideology is that its centered around the getIndexContents - only thing thats changing is the tree pointers 
		
		
	}
	
	//adds a commit: if first, sets to head; else makes two way connection with this and prev commit
	public void addCommit (String summary, String auth) throws IOException {
		if (headFile==null) {
			File headF = new File ("HEAD");
			//read in blobs from index
			String [] indexContents = i.getIndexContents("NO TREE HERE");//for the tree
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
			String [] indexContents = i.getIndexContents(lastTree);
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
