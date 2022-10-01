import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Tree {
	
	String[] pairs = new String[0];
	StringBuilder combinedString = new StringBuilder("");
	String sha1;
	
	public Tree(String[] pairs) {
		this.pairs = pairs;
		for (int i = 0; i < pairs.length; i++) {
			if (pairs[i].length()<6) {
				
			}
			else if (pairs[i].substring(0,6).equals("object")) {
				if (Index.newTree==false) {
					combinedString.append("Tree : " +pairs[i]).append('\n');
				}
				else {
					combinedString.append("Tree : " + Index.nT + "\n");
					Index.newTree = false;
				}
			}
			else {
				combinedString.append("Blob : " +pairs[i]).append('\n');
			}
		}
		String built = combinedString.toString();
		sha1 = Blob.createHash(built.getBytes());
		try {
			Files.writeString(Paths.get("objects/" + sha1), built);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public String getHash() {//gets the fileName 
		return sha1;
	}
	
	
}
