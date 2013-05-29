

public class Batch {
	Job[] jobs;
	
	public Batch(Job[] j) {
		jobs = j;
	}
	
	public void setJobs(Job[] j) {
		jobs = j;
	}
	
	public Job[] getJobs() {
		return jobs;
	}
	
	public void print() {
		for(int i = 0; i < jobs.length; i++) {
			System.out.println("Job ID: " + jobs[i].getId());
			System.out.println("Job Due Date: " + jobs[i].getDueDate());
			System.out.println("Job Volume: " + jobs[i].getVolume());
			System.out.println();
		}
	}
}
