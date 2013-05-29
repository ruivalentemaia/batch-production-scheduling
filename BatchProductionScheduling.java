import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.util.Configuration;
import org.jfree.util.DefaultConfiguration;


public class BatchProductionScheduling {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Job job1 = new Job(1, 10, 5);
		Job job2 = new Job(2, 8, 6);
		Job job3 = new Job(3, 12, 7);
		Job job4 = new Job(4, 2, 7);
		Job job5 = new Job(5, 4, 8);
		Job[] jobs = {job1, job2, job3, job4, job5};
		
		Machine m1 = new Machine(1, 12);
		Machine m2 = new Machine(2, 11);
		Machine m3 = new Machine(3, 14);
		Machine[] machines = {m1,m2,m3};
		
		//sets all jobs in different batches.
		Batch[][] batches = new Batch[5][1];
		Job[] tempJobs = new Job[1];
		for(int i = 0; i < jobs.length; i++) {
			for(int j = 0; j < batches[i].length; j++) {
				tempJobs[0] = jobs[i];
				batches[i][j] = new Batch(tempJobs);
				//batches[i][j].print();
			}
		}
		
		Scheduler scheduler = new Scheduler();
		scheduler.setBatches(batches);
		scheduler.setJobs(jobs);
		scheduler.setMachines(machines);
		scheduler.setMaintenanceCost(5);
		scheduler.setRoundTripTime(1);
		scheduler.setRoundTripCost(0.5);
		scheduler.setSetupCost(1);
		scheduler.setSetupTime(1);
		double fit = scheduler.objectiveFunction(scheduler.getSetupCost(), scheduler.getRoundTripCost(), batches.length, scheduler.getMaintenanceCost(), jobs, batches);
		System.out.println(fit);
		
		//decision variables check
		System.out.println("Xijk variables:");
		for(int var = 0; var < jobs.length; var++){
			int xijk = jobs[var].calculateDecisionVariable(jobs, batches);
			System.out.println("Job in position "+ var + ": " + xijk);
		}
		
		//build 1st chromossome
		int inc = 0;
		List<Integer> jobsList = new ArrayList<Integer>();
		Job[] firstChromossome = new Job[jobs.length];
		for(; inc < jobs.length;inc++) {
			jobsList.add(jobs[inc].getDueDate());
		}
		Collections.sort(jobsList);
		inc = 0;
		for(; inc < jobsList.size(); inc++){
			for(int inc2 = 0; inc2 < jobs.length; inc2++){
				if(jobsList.get(inc) == jobs[inc2].getDueDate()) {
					firstChromossome[inc] = jobs[inc2];
				}
			}
		}
		
		//build 2nd chromossome
		inc = 0;
		List<Integer> jobsList2 = new ArrayList<Integer>();
		Job[] secondChromossome = new Job[jobs.length];
		for(; inc < jobs.length;inc++) {
			jobsList2.add(jobs[inc].getDueDate());
		}
		Collections.sort(jobsList2);
		inc = 0;
		for(; inc < jobsList2.size(); inc++){
			for(int inc2 = 0; inc2 < jobs.length; inc2++){
				if(jobsList2.get(inc) == jobs[inc2].getDueDate()) {
					secondChromossome[inc] = jobs[inc2];
				}
			}
		}
		inc = 0;
		for(; inc < secondChromossome.length; inc++){
			for(int inc2 = 0; inc2 < machines.length; inc2++) {
				if(inc != secondChromossome.length-1){
					if( (secondChromossome[inc].getVolume() + secondChromossome[inc+1].getVolume()) <= machines[inc2].getCapacity())
						secondChromossome[inc].setDecisionVariable(1);
					else secondChromossome[inc].setDecisionVariable(0);
				}
				else secondChromossome[inc].setDecisionVariable(0);
			}
			System.out.println("Decision variable of job " + inc + ": "+ secondChromossome[inc].getDecisionVariable());
		}
		
		Configuration conf = new DefaultConfiguration();
	}

}
