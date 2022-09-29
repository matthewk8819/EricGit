import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.nio.file.*;

public class Commit {
	public static void main (String [] args) {
		//Commit c = new Commit("summary of first commit", "i am the author boy",null);
		//Commit child = new Commit( "summary of second commit", "second author sir doctor", c);
	}
	
	Commit next;
	Commit parent;
	Tree pTree;
	String treeLocation;
	String summary;
	String author;
	String date;
	String cachedHash;
	
	private Tree initializeTree(String pTree, String[] blobs) {
		String[] withTree = new String[blobs.length + 1];
		for (int i = 0; i < blobs.length; i++) {
			withTree[i] = blobs[i];
		}
		withTree[withTree.length-1] = pTree;
		Tree newTree = new Tree(withTree);
		return newTree;//has all the blobs to be added and the last tree
		
	}
	
	public Commit(String lastTree, String[] indexBlobs, String summary, String author, Commit parent) {
		this.pTree = initializeTree(lastTree,indexBlobs);
		this.treeLocation = "objects/" + pTree.getHash();
		this.summary = summary;
		this.author = author;
		this.date = Instant.now().toString();
		//this.pTree = pTree;
		this.parent = parent;
		cachedHash = null;
		writeToDisk();
		if(parent != null) parent.setNext(this);
		
		/*
		 Very confused
		 so we need to hash the contents of the current commit to get its sha, right?
			1) we hash the new commit.
			2) but now we need to update the parent commit to point to this commit. the parent's hash therefore changes.
			3) but now since the parent's hash changed, the new commit's data needs to be updated with the new parent hash. so we change this commiit's content, which therefore changes this commit's hash.
			4) now the parent commit has the wrong hash for the pointer to this commit, and the cycle continues
		 */
	}
	
	public void setNext(Commit next) {
		this.next = next;
		cachedHash = null;
		writeToDisk();
	}
	
	public String getDate() {
		return date;
	}
	
	public String getTreeLocation() {
		return treeLocation;
	}
	
	public String stringify(boolean includeNextCommit) {
		StringBuilder builder = new StringBuilder();
		
		if(includeNextCommit)
			builder.append(treeLocation).append('\n');
		
		builder.append(parent != null ? parent.getPath() : "").append('\n');
		
		if(includeNextCommit)
			builder.append(next != null ? next.getPath() : "").append('\n');
		
		builder
			.append(author).append('\n')
			.append(date).append('\n')
			.append(summary).append('\n');
		
		return builder.toString();
	}
	
	public String getHash() {
		if(cachedHash == null) {
			cachedHash = Blob.createHash(stringify(false).getBytes());
		}
		return cachedHash;
	}
	
	public String getPath() {
		return "objects/" + getHash();
	}
	
	public void writeToDisk() {
		try {
			Files.writeString(Paths.get(getPath()), stringify(true));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
