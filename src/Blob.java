import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DeflaterOutputStream;

public class Blob {
	
	public static void main (String [] args) throws IOException {
		File f = new File("file1");
		FileWriter w = new FileWriter (f);
		w.append("File 1 contents");
		w.close();
		Blob b = new Blob("file1");
	}
	
	String hash;
	
	public String getHash() { return hash; }
	
	public static String createHash(String filename) {
		byte[] content = new byte[0];
		try {
			content = Files.readAllBytes(Paths.get(filename));
		} catch(IOException e) {
			System.out.println(e);
			return "";
		}
		return createHash(content);
	}
	public static String createHash(byte[] input) {
		// https://www.geeksforgeeks.org/sha-1-hash-in-java/
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] hashed = digest.digest(input);
			BigInteger bigint = new BigInteger(1, hashed);
			return bigint.toString(16);
		} catch(NoSuchAlgorithmException e) {
			System.out.println(e);
			return "";
		}
	}
	
	byte[] zip(byte[] input) {
		// https://stackoverflow.com/questions/51332314/java-byte-array-compression
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream deflater = new DeflaterOutputStream(baos);
		try {
			deflater.write(input);
			deflater.flush();
			deflater.close();
		} catch(IOException e) {
			System.out.println(e);
			return new byte[0];
		}
		return baos.toByteArray();
	}
	
	public Blob(String filename) {
		byte[] content = new byte[0];
		try {
			content = Files.readAllBytes(Paths.get(filename));
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
		hash = createHash(content);
		Path parent = Paths.get("objects");
		Path newPath = Paths.get("objects", hash);
		byte[] zipped = zip(content);
		try {
			if(!Files.exists(parent)) Files.createDirectory(parent);
			if(!Files.exists(newPath)) Files.createFile(newPath);
			Files.write(newPath, zipped);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
}
