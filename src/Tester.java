import java.util.Scanner;

public class Tester {

	public static void main(String[] args) {
		Scanner scanny = new Scanner(System.in);
		
		Index tracker = new Index();
		tracker.init();
		tracker.add("your_mom.txt");
		tracker.add("bees.txt");
		scanny.next(); // Wait for input
		tracker.remove("your_mom.txt");
	}

}
