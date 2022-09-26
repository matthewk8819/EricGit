



public class Git {
	Index i;
	Commit head;
	Commit prevCommit;
	
	public Git () {
		this.i = new Index();
		i.init();
		
	}

	public void addCommit (Commit c) {
		if (head!=null) prevCommit = head;
		head = c;
		
	}
}
