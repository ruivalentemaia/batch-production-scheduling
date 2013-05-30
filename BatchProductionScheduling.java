import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;



public class BatchProductionScheduling {
	
	public static void main(String[] args) throws InvalidConfigurationException {
		
		int maximumPopulation = 300;
		
		Job job1 = new Job(1, 10, 5, 2);
		Job job2 = new Job(2, 8, 6, 5);
		Job job3 = new Job(3, 12, 7, 3);
		Job job4 = new Job(4, 2, 7, 4);
		Job job5 = new Job(5, 4, 8, 1);
		Job job6 = new Job(6, 3, 5, 1);
		Job[] jobs = {job1, job2, job3, job4, job5, job6};
		
		Machine m1 = new Machine(1, 12);
		Machine m2 = new Machine(2, 11);
		Machine m3 = new Machine(3, 14);
		Machine[] machines = {m1,m2,m3};
		
		//sets all jobs in different batches.
		Batch[][] batches = new Batch[jobs.length][1];
		Job[] tempJobs = new Job[1];
		for(int i = 0; i < jobs.length; i++) {
			for(int j = 0; j < batches[i].length; j++) {
				tempJobs[0] = jobs[i];
				batches[i][j] = new Batch(tempJobs);
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
		FitnessFunction f = new MinimizingMakespan(fit);
		conf.setFitnessFunction(f);
		
		Gen[] genesFirst = new Gen[firstChromossome.length];
		for(inc = 0; inc < firstChromossome.length; inc++) {
			Gen g = new Gen(firstChromossome[inc].getId(), firstChromossome[inc].getDecisionVariable());
			genesFirst[inc] = g;
		}
		
		Gen[] genesSecond = new Gen[secondChromossome.length];
		for(inc = 0; inc < genesSecond.length; inc++) {
			Gen g = new Gen(secondChromossome[inc].getId(), secondChromossome[inc].getDecisionVariable());
			genesSecond[inc] = g;
		}
		
		Chromossome c1 = new Chromossome(1, genesFirst);
		Chromossome c2 = new Chromossome(2, genesSecond);
		int id = 3;
		
		Chromossome[] operationalChromossome = new Chromossome[maximumPopulation];
		operationalChromossome[0] = c1;
		operationalChromossome[1] = c2;
		int fpos = 2;
		//randomly assigns the rest of the population
		for(inc = 0; inc < maximumPopulation; inc++) {
			Gen[] c1Genes = c1.getGenes();
			Gen[] newGenes = new Gen[c1Genes.length];
			List<Integer> indexes = new ArrayList<Integer>();
			int decay = 0;
			for(int a = 0; a < c1Genes.length; a++) {
				Random r = new Random();
				int pos=r.nextInt(c1Genes.length-1);
				if(a == 0) {
					newGenes[pos] = c1Genes[a];
					indexes.add(pos);
					System.out.println("Position " + pos + " added to indexes list.");
					System.out.println("Value of a: " + a);
				}
				else {
					for(int b = 0; b < indexes.size(); b++) {
						if(indexes.size() == c1Genes.length) {
							System.out.println("Should be done by now.");
							decay = 0;
							break;
						}
						if( pos == indexes.get(b)) {
							decay = 1;
							break;
						}
						decay = 0;
					}
					if(decay == 1) {
						a--;
						continue;
					}
					else {
						newGenes[pos] = c1Genes[a];
						indexes.add(pos);
						System.out.println("Position " + pos + " added to indexes list.");
						System.out.println("Value of a: " + a);
						System.out.println("2");
						decay = 0;
					}
				}
			}
			Chromossome c = new Chromossome(id, newGenes);
			operationalChromossome[fpos] = c;
			operationalChromossome[fpos].print();
			fpos++;
			id++;
		}
	}
}
