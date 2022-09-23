import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Tree {
	
	public static void main (String [] args) {
		String[] arr = {"hello","there","sir"};
		Tree t = new Tree(arr);
	}
	
	String[] pairs = new String[0];
	StringBuilder combinedString = new StringBuilder("");
	String sha1;
	
	public Tree(String[] pairs) {
		this.pairs = pairs;
		for (int i = 0; i < pairs.length; i++) {
			combinedString.append(pairs[i]).append('\n');
		}
		String built = combinedString.toString();
		sha1 = Blob.createHash(built.getBytes());
		try {
			Files.writeString(Paths.get("objects/" + sha1), built);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public String getHash() {
		return sha1;
	}
}
