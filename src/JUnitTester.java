import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.junit.jupiter.api.AfterAll;
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
		
//		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
//		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
	}
	
	@AfterAll
	static void afterAll() throws Exception {
		Files.deleteIfExists(path1);
		Files.deleteIfExists(path2);
		Files.deleteIfExists(path3);
		
//		if(Files.exists(Paths.get("objects"))) deleteDirectory(Paths.get("objects").toFile());
//		if(Files.exists(Paths.get("index"))) Files.delete(Paths.get("index"));
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
	
	@Test
	void testCommit() throws IOException {
		Index index = new Index();
		index.init();
		
		String time = Instant.now().toString();
		Blob blobby = new Blob("BLOB1.txt");
		
		Commit a = new Commit("objects/" + blobby.getHash(), "Commit A", "Eric", null);
		Path pathA = Paths.get(a.getPath());
		assertTrue(Files.exists(pathA));
		assertTrue(commitStringEquals(Files.readString(pathA), "objects/" + blobby.getHash() + "\n\n\nEric\n" + time + "\nCommit A\n"));
		
		Commit b = new Commit("objects/" + blobby.getHash(), "Commit B", "Eric", a);
		Path pathB = Paths.get(b.getPath());
		assertTrue(Files.exists(pathB));
		assertTrue(commitStringEquals(Files.readString(pathA), "objects/" + blobby.getHash() + "\n\nobjects/" + b.getHash() + "\nEric\n\" + time + \"\nCommit A\n"));
		assertTrue(commitStringEquals(Files.readString(pathB), "objects/" + blobby.getHash() + "\nobjects/" + a.getHash() + "\n\nEric\n\" + time + \"\nCommit B\n"));
		
		Commit c = new Commit("objects/" + blobby.getHash(), "Commit C", "Eric", b);
		Path pathC = Paths.get(c.getPath());
		assertTrue(Files.exists(pathC));
		assertTrue(commitStringEquals(Files.readString(pathB), "objects/" + blobby.getHash() + "\nobjects/" + a.getHash() + "\nobjects/" + c.getHash() + "\nEric\n\" + time + \"\nCommit B\n"));
		assertTrue(commitStringEquals(Files.readString(pathC), "objects/" + blobby.getHash() + "\nobjects/" + b.getHash() + "\n\nEric\n\" + time + \"\nCommit C\n"));
	}
	
	boolean commitStringEquals(String a, String b) {
		String[] aSplit = a.split("\n");
		String[] bSplit = b.split("\n");
		if(aSplit.length != bSplit.length) return false;
		for(int i = 0; i < aSplit.length; i++) {
			if(i == 4) continue; // Don't check dates are equal
			if(!aSplit[i].equals(bSplit[i])) return false;
		}
		return true;
	}
}
