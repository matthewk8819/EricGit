import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GitTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File f0 = new File("text0.txt");
		File f1 = new File("text1.txt");
		File f2 = new File("text2.txt");
		File f3 = new File("text3.txt");
		File f4 = new File("text4.txt");
		File f5 = new File("text5.txt");
		FileWriter w = new FileWriter(f0);
		w.append("This is some content 0");
		w.close();
		w = new FileWriter (f1);
		w.append("This is some content 1");
		w.close();
		w = new FileWriter (f2);
		w.append("This is some content 2");
		w.close();
		w = new FileWriter (f3);
		w.append("This is some content 3");
		w.close();
		w = new FileWriter (f4);
		w.append("This is some content 4");
		w.close();
		w = new FileWriter (f5);
		w.append("This is some content 5");
		w.close();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() throws IOException {
		Git git = new Git();
		git.initRepo("NewREPO");
		git.stageFile("text0.txt");
		git.addCommit("S", "F");
		git.stageFile("text1.txt");
		git.stageFile("text2.txt");
		git.stageFile("text3.txt");
		//need to fix it if it is in the same commit - fix later 
		//should be the lines with the two for loops 
		git.addCommit("SUMMARY2","SAME AUTHOR");
		git.stageDelete("text1.txt");
		git.stageFile("text4.txt");
		git.stageFile("text5.txt");
		git.addCommit("Summary3", "NEW AUTHOR");
	}

}
