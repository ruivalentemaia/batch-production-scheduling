
public class Scheduler {
	Job[] jobs;
	Batch[][] batches;
	Machine[] machines;
	int roundTripTime;
	double roundTripCost;
	int setupTime;
	double setupCost;
	double maintenanceCost;
	
	public Scheduler() {
		roundTripTime = 0;
		roundTripCost = 0;
		setupTime = 0;
		setupCost = 0;
		maintenanceCost = 0;
	}
	
	//Gets and sets
	public Job[] getJobs() {
		return jobs;
	}
	
	public Batch[][] getBatches() {
		return batches;
	}
	
	public Machine[] getMachines() {
		return machines;
	}
	
	public int getRoundTripTime() {
		return roundTripTime;
	}
	
	public double getRoundTripCost() {
		return roundTripCost;
	}
	
	public int getSetupTime() {
		return setupTime;
	}
	
	public double getSetupCost() {
		return setupCost;
	}
	
	public double getMaintenanceCost() {
		return maintenanceCost;
	}
	
	public void setJobs(Job[] j) {
		jobs = j;
	}
	
	public void setBatches(Batch[][] b) {
		batches = b;
	}
	
	public void setMachines(Machine[] m) {
		machines = m;
	}
	
	public void setRoundTripTime(int rTT) {
		roundTripTime = rTT;
	}
	
	public void setRoundTripCost(double rTC) {
		roundTripCost = rTC;
	}
	
	public void setSetupTime(int sT) {
		setupTime = sT;
	}
	
	public void setSetupCost(double sC) {
		setupCost = sC;
	}
	
	public void setMaintenanceCost(double mC) {
		maintenanceCost = mC;
	}
	
	public double objectiveFunction(double sc, double nc, int u, double beta, Job[] jobs, Batch[][] batches) {
		double z = 0;
		double sum = 0;
		int xijk = 0;
		Job[] jobsInBatch;
		Job[] allJobs = new Job[jobs.length];
		z = (sc+nc)*u + beta;
		int inc = 0;
		for(int j = 0; j < batches.length; j++) {
			for(int i = 0; i < jobs.length; i++) {
				jobsInBatch = batches[j][0].getJobs();
				allJobs[inc] = jobsInBatch[0];
				for(int k = 0; k < jobsInBatch.length; k++) {
					if(jobs[i].getId() == jobsInBatch[k].getId()) {
						xijk = 1;
					}
					else xijk = 0;
				}
				sum += allJobs[inc].getDueDate()*xijk - allJobs[inc].getArrivalTime();
			}
			inc++;
		}
		z=z*sum;
		return z;
	}
}
