import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JUnitTester {
	HashMap<String, String> map = new HashMap<String, String>();
	
	static void deleteDirectory(File directoryToBeDeleted) { // https://www.baeldung.com/java-delete-directory
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    directoryToBeDeleted.delete();
	}
	
	@BeforeEach
	void beforeEach() throws Exception {
		Path path1 = Paths.get("firstFile.txt");
		Files.deleteIfExists(path1);
		Path path2 = Paths.get("secondFile.txt");
		Files.deleteIfExists(path2);
		Path path3 = Paths.get("thirdFile.txt");
		Files.deleteIfExists(path3);
		Path path4 = Paths.get("tester.txt");
		Files.deleteIfExists(path4);
		
		File file1 = new File("firstFile.txt");
		file1.createNewFile();
		File file2 = new File("secondFile.txt");
		file2.createNewFile();
		File file3 = new File("thirdFile.txt");
		file3.createNewFile();
		File testFile = new File("tester.txt");
		testFile.createNewFile();
		writeFile("firstFile.txt", "some content");
		writeFile("secondFile.txt", "more content");
		writeFile("thirdFile.txt", "more stuff");
		writeFile("testFile.txt", "stuff");
		
		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
	}
	
	@AfterAll
	static void afterAll() throws Exception {
		Path path1 = Paths.get("firstFile.txt");
		Files.deleteIfExists(path1);
		Path path2 = Paths.get("secondFile.txt");
		Files.deleteIfExists(path2);
		Path path3 = Paths.get("thirdFile.txt");
		Files.deleteIfExists(path3);
		Path path4 = Paths.get("tester.txt");
		Files.deleteIfExists(path4);
		
		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
	}
	
	@Test
	void testBlob() throws IOException {
		Blob newBlob = new Blob("tester.txt");
		File blobFile = new File("objects/" + newBlob.getHash());
		assertTrue(blobFile.exists());
	}
	@Test
	void testInit() {
		Index i = new Index();
		i.init();
		File file = new File("index");
		File objectsFile = new File("objects");
		assertTrue(objectsFile.exists());
		assertTrue(file.exists());
	}
	@Test
	void testAdd() throws IOException {
		Index i = new Index();
		i.init();
		i.add("firstFile.txt");
		i.add("secondFile.txt");
		i.add("thirdFile.txt");
		map.put("firstFile.txt", new Blob("firstFile.txt").getHash());
		map.put("secondFile.txt", new Blob("secondFile.txt").getHash());
		map.put("thirdFile.txt", new Blob("thirdFile.txt").getHash());
		Path filePath = Path.of("index");
		String fileContent = Files.readString(filePath);
		String producedText = i.stringifyIndex(map);
		assertTrue(fileContent.equals(producedText));
	}
	@Test
	void testRemove() throws IOException {
		Index i = new Index();
		i.init();
		Blob b = new Blob("firstFile.txt");
		i.add("firstFile.txt");
		String sha1 = b.getHash();
		File blobFile = new File("objects/" + sha1);
		i.remove("firstFile.txt");
		map.remove("firstFile.txt");
		assertFalse(blobFile.exists());
		Path filePath = Path.of("index");
		String fileContent = Files.readString(filePath);
		String producedText = i.stringifyIndex(map);
		assertTrue(fileContent.equals(producedText));
	}
	
	static void writeFile(String fileName, String data) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write(data);
		writer.close();
	}

}
