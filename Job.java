
public class Job {
	int id;
	int dueDate; //in seconds.
	int volume;
	int arrivalTime;
	int decisionVariable;
	
	public Job(int iD, int dD, int v, int aT) {
		id = iD;
		dueDate = dD;
		volume = v;
		arrivalTime = aT;
	}
	
	public Job() {
		id = 0;
		dueDate = 0;
		volume = 0;
		arrivalTime = 0;
		decisionVariable = 0;
	}
	
	public void setId(int idD) {
		id = idD;
	}
	
	public void setDueDate(int dD) {
		dueDate = dD;
	}
	
	public void setVolume(int v) {
		volume = v;
	}
	
	public void setArrivalTime(int aT) {
		arrivalTime = aT;
	}
	
	public void setDecisionVariable(int dV){
		decisionVariable = dV;
	}
	
	public int getId(){
		return id;
	}
	
	public int getDueDate() {
		return dueDate;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getDecisionVariable() {
		return decisionVariable;
	}
	
	public int calculateDecisionVariable(Job[] jobs, Batch[][] batches) {
		Job[] jobsInBatch;
		Job[] allJobs = new Job[jobs.length];
		int inc = 0;
		int xijk = 0;
		for(int i = 0; i < jobs.length; i++) {
			jobsInBatch = batches[i][0].getJobs();
			allJobs[inc] = jobsInBatch[0];
			for(int k = 0; k < jobsInBatch.length; k++) {
				if(jobs[i].getId() == jobsInBatch[k].getId()) {
					xijk = 1;
				}
				else xijk = 0;
			}
		}
		return xijk;
	}
	
	public void print() {
		System.out.println("Job ID: " + this.getId());
		System.out.println("Job Due Date: " + this.getDueDate());
		System.out.println("Job Volume: " + this.getVolume());
		System.out.println();
	}
}
