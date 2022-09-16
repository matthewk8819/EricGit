import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tree {
	String[] pairs = new String[0];
	String combinedString;
	String sha1;
	public Tree(String[] pairs) {
		this.pairs = pairs;
		for (int i = 0; i < pairs.length; i++) {
			combinedString += pairs[i];
		}
		sha1 = Blob.createHash(combinedString.getBytes());
		try {
			Files.writeString(Paths.get("objects/" + sha1), combinedString);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public String getHash() {
		return sha1;
	}
}
