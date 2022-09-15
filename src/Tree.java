import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tree {
	ArrayList<String> pairs = new ArrayList<String>();
	String combinedString;
	String sha1;
	public Tree(ArrayList p) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		pairs = p;
		for (int i = 0; i < pairs.size(); i++) {
			combinedString += pairs.get(i);
		}
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset(); 
		digest.update(combinedString.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
	}
}
