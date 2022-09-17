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
	
	static Path path1 = Paths.get("BLOB1.txt");
	static Path path2 = Paths.get("BLOB2.txt");
	static Path path3 = Paths.get("BLOB3.txt");
	
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
		Files.deleteIfExists(path1);
		Files.deleteIfExists(path2);
		Files.deleteIfExists(path3);
		
		Files.writeString(path1, "A Bug's Life");
		Files.writeString(path2, "The Emoji Movie");
		Files.writeString(path3, "Morbius");
		
		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
	}
	
	@AfterAll
	static void afterAll() throws Exception {
		Files.deleteIfExists(path1);
		Files.deleteIfExists(path2);
		Files.deleteIfExists(path3);
		
		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
	}
	
	@Test
	void testBlob() throws IOException {
		Blob newBlob = new Blob("BLOB1.txt");
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
		i.add("BLOB1.txt");
		i.add("BLOB2.txt");
		i.add("BLOB3.txt");
		
		Blob blob1 = new Blob("BLOB1.txt");
		Blob blob2 = new Blob("BLOB2.txt");
		Blob blob3 = new Blob("BLOB3.txt");
		
		Path filePath = Path.of("index");
		String fileContent = Files.readString(filePath);
		assertEquals(fileContent, "BLOB1.txt:" + blob1.getHash() + "\nBLOB2.txt:" + blob2.getHash() + "\nBLOB3.txt:" + blob3.getHash() + "\n");
	}
	@Test
	void testRemove() throws IOException {
		Index i = new Index();
		i.init();
		i.add("BLOB1.txt");
		i.add("BLOB2.txt");
		i.add("BLOB3.txt");
		
		Blob blob1 = new Blob("BLOB1.txt");
		Blob blob2 = new Blob("BLOB2.txt");
		Blob blob3 = new Blob("BLOB3.txt");
		
		Path filePath = Path.of("index");
		String fileContent = Files.readString(filePath);
		assertEquals(fileContent, "BLOB1.txt:" + blob1.getHash() + "\nBLOB2.txt:" + blob2.getHash() + "\nBLOB3.txt:" + blob3.getHash() + "\n");
		
		i.remove("BLOB1.txt");
		assertFalse(Files.exists(Paths.get("objects/" + blob1.getHash())));
		fileContent = Files.readString(filePath);
		assertEquals(fileContent, "BLOB2.txt:" + blob2.getHash() + "\nBLOB3.txt:" + blob3.getHash() + "\n");
	}
	
	@Test
	void testTree() throws IOException {
		Index index = new Index();
		index.init();
		Blob bugsLife = new Blob("BLOB1.txt");
		Blob emojiMovie = new Blob("BLOB2.txt");
		Blob morbius = new Blob("BLOB3.txt");
		
		String[] treeContentOne = new String[] {
			"blob:" + bugsLife.getHash(),
			"blob:" + emojiMovie.getHash()
		};
		Tree treeOne = new Tree(treeContentOne);
		
		Path pathOne = Paths.get("objects/" + treeOne.getHash());
		assertTrue(Files.exists(pathOne));
		assertEquals(Files.readString(pathOne), "blob:" + bugsLife.getHash() + "\nblob:" + emojiMovie.getHash() + "\n");
		
		String[] treeContentTwo = new String[] {
				"blob:" + morbius.getHash(),
				"tree:" + treeOne.getHash()
		};
		Tree treeTwo = new Tree(treeContentTwo);
		
		Path pathTwo = Paths.get("objects/" + treeTwo.getHash());
		assertTrue(Files.exists(pathTwo));
		assertEquals(Files.readString(pathTwo), "blob:" + morbius.getHash() + "\ntree:" + treeOne.getHash() + "\n");
	}
	
	static void writeFile(String fileName, String data) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write(data);
		writer.close();
	}

}
