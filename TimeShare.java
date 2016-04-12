import java.util.*;
import java.io.*;

public class TimeShare
{
	public static Queue inputQueue = new Queue();
	public static Queue jobQueue = new Queue();
	public static Queue finishedQueue = new Queue();
	public static int clock = 1;
	public static int currentCounter = 0;
	public static int finishedCounter = 0;


	public static void main(String[] args) throws CloneNotSupportedException
	{
//		Creates the initial input queue
		getFile(args[0]);

//		Driver that keeps the process going until all jobs have been processed.
		while(finishedCounter < currentCounter)
		{	
			inputToJobQueueCheck();
			jobQueueToFinishedQueueCheck();
			increaseClock();
		}

		Queue finalCopy = (Queue) finishedQueue.clone();

//		This is the string formatting the titles at the top.
		System.out.printf("\n\n%5s %8s %3s %5s %8s %14s", "job id", "arrival", "start", "run", "wait", "turnaround\n");
		System.out.printf("%12s %7s %7s %7s %7s\n", "time", "time", "time", "time", "time");
		System.out.println("------  -----   -----   -----   ------  ----------");

//		Prints out the final results of from the finished job queue.
		while(!(finalCopy.isEmpty()))
		{
			Job currentJob = (Job) finalCopy.dequeue();
			System.out.println(currentJob.toString());
		}

		double averageWaitTime = averageWaitTime();
		int usage = usageOfCPU();
		int idleTime = idleTime();
		double percentUsage = percentUsageOfCPU(usage, idleTime);

//		This is the string formatting for the analysis section.
		System.out.printf("\n\n%20s %.2f", "Average Wait Time =>", averageWaitTime);
		System.out.printf("\n%20s %.2f", "CPU Usage =>", (double) usage);
		System.out.printf("\n%20s %.2f", "CPU Idle =>", (double) idleTime);
		System.out.printf("\n%20s %.2f", "CPU Usage (%) =>", percentUsage*100);
		System.out.print("%\n");

	}

//		This method takes an input file name, and reads in the data. It creates
//		jobs and inputs them into the initial inputQueue.
	public static void getFile(String fileName)
	{
		File file = new File(fileName);

		try{
			Scanner input = new Scanner(file);
			while(input.hasNext())
			{
				String job = input.next();
				int arrivalTime = input.nextInt();
				int runTime = input.nextInt();
				Job newJob = new Job(job, arrivalTime, runTime);

				inputQueue.enqueue(newJob);
				currentCounter++;
			}

		}
		catch(Exception e){}
	}

//		This method checks to see if the current job's arrival time is equal to
//		the current clock time. If so, it is moved from the input queue to the
//		jobs queue to be processed. If it is the first job to be processed, then
//		the start time is initialized to the current clock time.
	public static void inputToJobQueueCheck()
	{
		if(inputQueue.isEmpty() == false)
		{
			Job currentJob = (Job) inputQueue.front();

			if(currentJob.arrivalTime == clock)
			{
				if(jobQueue.isEmpty())
				{
					currentJob.startTime = clock;
					jobQueue.enqueue(inputQueue.dequeue());
				}
				else
				{
					jobQueue.enqueue(inputQueue.dequeue());
				}
			}
		}

	}

//		This method checks to see if the job that is being processed is 
//		finished by checking the end time with the current clock time. If
//		it is equal, the calculations for its wait time and turnaround time
//		are made, and the job is sent to the finished queue.
	public static void jobQueueToFinishedQueueCheck()
	{
		if(!(jobQueue.isEmpty()))
		{
			Job currentJob = (Job) jobQueue.front();

			if((currentJob.startTime + currentJob.runTime) == clock)
			{
				currentJob.turnTime = clock - currentJob.arrivalTime;
				currentJob.waitTime = currentJob.startTime - currentJob.arrivalTime;

				finishedQueue.enqueue(jobQueue.dequeue());
				finishedCounter++;

				if(!(jobQueue.isEmpty()))
				{
					currentJob = (Job) jobQueue.front();
					currentJob.startTime = clock;
				}
			}

		}
	}

	//This method increments the clock by one.
	public static void increaseClock()
	{
		clock++;
	}

//		This method computes the time the CPU spent running and processing.
//		It does this by copying the finished queue, and adding all of the
//		run times together.
	public static int usageOfCPU() throws CloneNotSupportedException
	{
		int totalRunTime = 0;
		Queue copy = (Queue) finishedQueue.clone();
		while(!(copy.isEmpty()))
		{
			Job currentJob = (Job) copy.dequeue();
			totalRunTime = totalRunTime + currentJob.runTime;
		}
		return totalRunTime;
	}

//		This method computes the average wait time of the CPU during this 
//		process (the average amount of time the jobs spent waiting in queue).
//		It does this by copying the finished queue, and adding all of the 
//		wait times together and dividing by the total number of jobs.
	public static double averageWaitTime() throws CloneNotSupportedException
	{
		int totalWaitTime = 0;
		Queue copy = (Queue) finishedQueue.clone();

		while(!(copy.isEmpty()))
		{
			Job currentJob = (Job) copy.dequeue();
			totalWaitTime = currentJob.waitTime + totalWaitTime;
		}
		double average = ((double)totalWaitTime/((double)finishedCounter));
		return average;
	}

//		This method computer the total time the CPU spent in idle (not 
//		processing anything). This is computed by summing all of the differences
//		between the current job's arrival time and the sum of the previous
//		job's start and run times.
	public static int idleTime() throws CloneNotSupportedException
	{
		int totalIdleTime = 0;
		Queue firstCopy = (Queue) finishedQueue.clone();
		Queue secondCopy = (Queue) finishedQueue.clone();

		Job previousJob = (Job) firstCopy.dequeue();
		secondCopy.dequeue();
		Job currentJob = (Job) secondCopy.dequeue(); 

		while(!(firstCopy.isEmpty()))
		{
			totalIdleTime = (currentJob.startTime - (previousJob.startTime + previousJob.runTime)) + totalIdleTime;
			previousJob = (Job) firstCopy.dequeue();
			if(secondCopy.isEmpty())
				return totalIdleTime;
			currentJob = (Job) secondCopy.dequeue();
		}
		return totalIdleTime;
	}

//		This method computes the percent of time the CPU spent processing. 
//		It is calculated by diving the total time the CPU spent processing by
//		the total time the CPU spent processing and the total CPU idle time.
	public static double percentUsageOfCPU(int usageofCPU, int idleOfCPU)
	{
		double percentUsage = ((double)usageofCPU)/((double)(usageofCPU + idleOfCPU));
		return percentUsage;
	}

}

