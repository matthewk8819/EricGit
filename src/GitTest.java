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
		File f6 = new File("text6.txt");
		File f7 = new File("text7.txt");
		File f8 = new File("text8.txt");
		File f9 = new File("text9.txt");
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
		w = new FileWriter (f6);
		w.append("This is some content 6");
		w.close();
		w = new FileWriter (f7);
		w.append("This is some content 7");
		w.close();
		w = new FileWriter (f8);
		w.append("This is some content 8");
		w.close();
		w = new FileWriter (f9);
		w.append("This is some content 9");
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
		git.addCommit("1st commit: added 0", "me");
		git.stageFile("text1.txt");
		git.stageFile("text2.txt");
		git.stageFile("text3.txt");
		//need to fix it if it is in the same commit - fix later 
		//should be the lines with the two for loops 
		git.addCommit("2nd commit: added 1 2 3 ","SAME AUTHOR");
		git.stageDelete("text1.txt");
		git.stageFile("text4.txt");
		git.stageFile("text5.txt");
		git.addCommit("3rd commit: added 4 5 deleted 1", "NEW AUTHOR");
		git.stageFile("text6.txt");
		git.stageFile("text7.txt");
		git.stageDelete("text4.txt");
		git.addCommit("4th commit: added 6 7 delete 4","me");
//		git.stageFile("text8.txt");
		git.stageFile("text9.txt");
//		git.stageEdit("text7.txt");
		git.addCommit("5th commit: added 8 9 edited 7", "still me brother");
	}

}
