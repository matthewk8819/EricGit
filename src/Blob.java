import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Blob {
	String hash(byte[] input) {
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
		// https://stackoverflow.com/questions/357851/in-java-how-to-zip-file-from-byte-array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream stream = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry("temp");
		entry.setSize(input.length);
		try {
			stream.putNextEntry(entry);
			stream.write(input);
			stream.closeEntry();
			stream.close();
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
		String hashed = hash(content);
		Path newPath = Paths.get("objects", hashed);
		byte[] zipped = zip(content);
		try {
			Files.createDirectories(newPath);
			Files.delete(newPath);
			Files.createFile(newPath);
			Files.write(newPath, zipped);
		} catch(IOException e) {
			System.out.println(e);
			return;
		}
	}
}
