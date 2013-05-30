import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;


public class BatchProductionScheduling {
	
	public static void main(String[] args) throws InvalidConfigurationException {
		// TODO Auto-generated method stub
		Job job1 = new Job(1, 10, 5, 2);
		Job job2 = new Job(2, 8, 6, 5);
		Job job3 = new Job(3, 12, 7, 3);
		Job job4 = new Job(4, 2, 7, 4);
		Job job5 = new Job(5, 4, 8, 1);
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
		
		Gene[] genesFirstUpper = new Gene[firstChromossome.length];
		Gene[] genesFirstDown = new Gene[firstChromossome.length];
		Gene[] genesFirst = new Gene[firstChromossome.length*2];
		inc = 0;
		for(; inc < firstChromossome.length; inc++) 
			genesFirstUpper[inc] = new IntegerGene(conf, 1, firstChromossome.length);
		for(inc = 0; inc < genesFirstDown.length; inc++)
			genesFirstDown[inc] = new IntegerGene(conf, 0, 1);
		for(inc = 0; inc < (genesFirstUpper.length)/2; inc++) 
			genesFirst[inc] = genesFirstUpper[inc];
		for(inc = genesFirstDown.length/2; inc < genesFirstDown.length; inc++) 
			genesFirst[inc] = genesFirstDown[inc];
		
		Gene[] genesSecondUpper = new Gene[secondChromossome.length];
		Gene[] genesSecondDown = new Gene[secondChromossome.length];
		Gene[] genesSecond = new Gene[secondChromossome.length*2];
		inc = 0;
		for(; inc < secondChromossome.length; inc++)
			genesSecondUpper[inc] = new IntegerGene(conf, 1, secondChromossome.length);
		for(inc = 0; inc < genesSecondDown.length; inc++) 
			genesSecondDown[inc] = new IntegerGene(conf,1, secondChromossome.length);
		for(inc = 0; inc < (genesSecondUpper.length)/2; inc++) 
			genesSecond[inc] = genesSecondUpper[inc];
		for(inc = genesSecondDown.length/2; inc < genesSecondDown.length; inc++) 
			genesSecond[inc] = genesSecondDown[inc];
		
		Chromosome chromosome1 = new Chromosome(conf, genesFirst);
		Chromosome chromosome2 = new Chromosome(conf, genesSecond);
		conf.setSampleChromosome(chromosome1);
		conf.setSampleChromosome(chromosome2);
		
		conf.setPopulationSize(300);
	}

}
