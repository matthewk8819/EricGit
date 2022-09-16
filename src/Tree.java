import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tree {
	ArrayList<String> pairs = new ArrayList<String>();
	String combinedString;
	String sha1;
	public Tree(ArrayList<String> p) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		pairs = p;
		for (int i = 0; i < pairs.size(); i++) {
			combinedString += pairs.get(i);
		}
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset(); 
		digest.update(combinedString.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
	}
	public void storeArrayList() throws IOException {
		File treeFile = new File("/objects/" + sha1);
		FileWriter myWriter = new FileWriter(treeFile);
		for (int i = 0; i < pairs.size(); i++) {
			myWriter.write(pairs.get(i));
		}
		myWriter.close();
	}
	
	public static void main(String [] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f");
		list.add("blob : 01d82591292494afd1602d175e165f94992f6f5f");
		list.add("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83");
		list.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
		list.add("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");
		Tree t = new Tree(list);
		t.storeArrayList();
	}
}
