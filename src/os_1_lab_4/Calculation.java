package os_1_lab_4;

//import necessary Java libraries
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Calculation {

	//Declare the variables and data structures
	//Will be working with user input so need to import Scanner console
	Scanner sc = new Scanner(System.in);
	

	private int numOfProc = 0;//int for total number of processes to schedule/* set to 6 if debugging */
	private String pID = "";//get the name of the process
	private int bTime = 0;//get the time process will take to finish
	private int schChoice = 0;//get the scheduling type choice from user
	private int quantum = 0;//for RR option will need to ask for quantum
	private float avgWaitTime = 0;//Average wait time for the selected method of process scheduling	
	private int i;//int for using inside loops.
	private int waitTime = 0;//count the wait time for all processes in arrayList
	private int procAtZero;//counter for how many processes are at 0. once it reaches same amount as process amount it means all processes are done.
	private float sum;//variable to calculate sum total of all wait times

	private List <Process> processes;//List of process objects	
	private List<Process> procL2; //secondary process list that will refill original processes list after sjf was used and queue is being reused
	private Integer[] waitTimes;//List of wait times for processes as they get processed.
	private int startTime;//start time counter for RR mode
	private Integer[] runTimes;//list to keep values for start times for RR
	private Integer[] qCount;//array for amount of quantums the process ran
	private Integer[] endTime;//array to keep end times of the processes last run
	
	

	//----------- Methods ------------------------

	public void showMenu(){		
		//get the number of processes from user(negative values not accepted)
		do{
			System.out.println("Please enter the amount of processes to calculate wait times for:");
			numOfProc = sc.nextInt();
		}while(numOfProc < 0);
	}//end of showMenu()

	public void chooseSchedule(){		
		System.out.println("Please choose one of the following options:");
		System.out.println("1 = FCFS mode; 2 = SJF mode; 3 = RR mode; -1 = Exit");
		schChoice = sc.nextInt();//ask user to choose type of Process scheduling to use	
		
		while(schChoice != -1){//wrap it switch in a while loop so that wrong key entry doesn't send it back to entering process details
			switch(schChoice){//switch function for scheduling mode choice
				case 1:
					System.out.println("\nFCFS Chosen");
					calcFCFS();//call calculation method for FCFS
					printResult();//call print the results method
					schChoice = -1;//set choice to -1 so it exits while loop
					break;
				case 2:
					System.out.println("\nSJF Chosen");
					calcSJF();//call calculation for SJF
					printResult();
					reloadProcesses();//reload processes queue to original order before Collections.sort() was called
					schChoice = -1;//set choice to -1 so it exits while loop
					break;
				case 3:
					System.out.println("\nRR Chosen");
					System.out.println("Please enter quantum for RR mode");
					quantum = sc.nextInt();//get user to enter desired quantum time
					System.out.println("Thank you. calculating wait times for RR mode!\n\n");
					calcRR();
					showRRResults();
					schChoice = -1;//set choice to -1 so it exits while loop
					break;
				case -1:
					System.out.println("Exiting application");
					System.exit(0);
					break;
				default:
					System.out.println("Not a valid option");
					System.out.println("Please choose one of the following options:");
					System.out.println("1 = FCFS mode; 2 = SJF mode; 3 = RR mode; -1 = Exit");
					schChoice = sc.nextInt();//ask user to choose type of Process scheduling to use
			}//end of switch(schChoice)
		}//end of while loop
	}//end of chooseSchedule()	
	
	//get the ArrayList filled
	public void inputProcesses(){		
		processes = new ArrayList<Process>();
		procL2 = new ArrayList<Process>();
		for(i = 0; i< numOfProc; i++){		
			System.out.println("Please enter process-"+(i+1)+" id:");
			pID = sc.next();
			System.out.println("Please enter process-"+(i+1)+" burst time:");
			bTime = sc.nextInt();
			
			processes.add(new Process(pID, bTime));//add the process id and burst time into processes ArrayList
			procL2.add(new Process(pID, bTime));
			
		}//end of for loop
		
		//check that objects get properly written into ArrayList
		System.out.println("Contents of process queue to be processed");
		System.out.println("=====================================================================================");
		for(i = 0; i< numOfProc; i++){			
			System.out.println(processes.get(i));
		}//end of for loop
		System.out.println("=====================================================================================");
	}//end of inputProcesses() method
	
	//Calculate FCFS mode scheduling
	public void calcFCFS(){
		waitTimes = new Integer[numOfProc];
		sum = 0;
		for(i = 0; i < numOfProc; i++){				
				waitTimes[i] = waitTime;			
			//after waitTime is set into waitTimes arrayList waitTime var gets incremented by burst time of current process
			waitTime += processes.get(i).getBurstTime();
			sum += waitTimes[i];//count sum total wait times for all processes
		}		
		avgWaitTime = sum/numOfProc;
		waitTime = 0;//waitTime gets reset back to 0 for another iteration of the application
		sum = 0;
	}//end of calcFCFS()
	
	//print out FCFS to see results
	public void printResult(){//printResult method mostly finalised
		System.out.println("\n\nResult of wait times calculation");
		System.out.println("=====================================================================================");
		System.out.println("Process ID      Burst Time	 Wait Time");
		System.out.println("=====================================================================================");
		for(i = 0; i < numOfProc; i++){
			System.out.format("%10s%16d%16d\n", processes.get(i).getpID(), processes.get(i).getBurstTime(), waitTimes[i]);
			if(i < numOfProc-1){
				System.out.println("-------------------------------------------------------------------------------------");
			}
		}//end of for loop	
		System.out.println("=====================================================================================");
		System.out.format("Average wait time: %.2f\n", avgWaitTime);
		System.out.println("=====================================================================================");
	}//end of printFCFSResult
	
	//calculation for SJF mode
	public void calcSJF(){
		Collections.sort(processes);
		calcFCFS();
	}//end of calcSJF method
	
	/* 
	 * This method is only used if SJF was used and user wants to reuse existing queue. It reloads processes
	 * back to original order so that other scheduling algorithms can be used properly
	 */
	public void reloadProcesses(){
		for(i = 0; i< numOfProc; i++){
			processes.get(i).setpID(procL2.get(i).getpID());
			processes.get(i).setBurstTime(procL2.get(i).getBurstTime());
		}
		
	}//end of reload processes
	
	//Calculation for RR mode.
	public void calcRR(){
		//instantiate variables
		startTime = 0;
		procAtZero = 0;
		//instantiate arrays
		runTimes = new Integer[numOfProc];
		qCount = new Integer[numOfProc];
		waitTimes = new Integer[numOfProc];		
		endTime = new Integer[numOfProc];
		
		//set initial values to the arrays
		for(i = 0; i < numOfProc; i++){
			runTimes[i] = processes.get(i).getBurstTime();//set the remaining run time array to current length of processes in queue
			qCount[i] = 0;
			waitTimes[i] = 0;
			endTime[i] = 0;
		}
		
		while(procAtZero < numOfProc){				
			for(i = 0; i < numOfProc; i++){	
				if(runTimes[i] == 0){
					System.out.println("Process "+processes.get(i).getpID()+" has zero remaining burst time!"+"\n");
					continue;
				}else if(runTimes[i] <= quantum && runTimes[i] > 0){						
					System.out.println("Process-"+processes.get(i).getpID()+" last start time: "+ startTime);//check to see if processes start at proper times									
					endTime[i] = startTime + runTimes[i];//end time of current process is now set to current end time plus end time for current process.					
					waitTimes[i] = startTime - (qCount[i]*quantum);//calculate waiting time for current process						
					startTime+= runTimes[i];//start time for next process is set to existing start time plus remaining run time of current process.
					runTimes[i] = runTimes[i] - runTimes[i];//remaining run time get decremented by itself to bring it to zero.	
					System.out.println("Process-"+processes.get(i).getpID()+" last end time: "+endTime[i]+"\n");					
					procAtZero++;// procAtZero counter gets incremented by 1 since current process is now zero.						
				}else if(runTimes[i] > quantum){					
					runTimes[i] = runTimes[i] - quantum;//remaining run time of a process get decremented by size of quantum.					
					System.out.println("Process-"+processes.get(i).getpID()+" start time: "+startTime);//check to see if processes start at proper times					
					endTime[i] = startTime +quantum;//end time of current process is a sum of previous process end time and quantum amount.
					startTime += quantum;//start time for next process is sum of current start time and quantum amount
					System.out.println("Process-"+processes.get(i).getpID()+" end time: "+endTime[i]+"\n");
					qCount[i] = qCount[i]+1;//quantum count gets incremented by 1 for that process
				}				
			}//end of for(i = 0; i < numOfProc; i++)
			//after for loop executes program checks if while loop condition has been met.
			//if condition has not been met then for loop runs again.				
		}//end of while(procAtZero < numOfProc)
		
		for(i = 0; i < numOfProc; i++){
			sum += waitTimes[i];//calculate sum total of all wait times
		}
		
		avgWaitTime = sum/numOfProc;		
		sum = 0;//set sum back to zero for next run
	}//end of calcRR method
	
	//show the results of RR mode calculation
	public void showRRResults(){
		System.out.println("\n\nResult of wait times for RR mode");
		System.out.println("=====================================================================================");
		System.out.println("Process ID	Burst Time	 Wait Time");
		System.out.println("=====================================================================================");
		for(i=0; i<numOfProc; i++){
			System.out.format("%10s%16d%16d\n", processes.get(i).getpID(), processes.get(i).getBurstTime(), waitTimes[i]);
			if(i < numOfProc-1){
				System.out.println("-------------------------------------------------------------------------------------");
			}		
		}//end of for loop
		System.out.println("=====================================================================================");
		System.out.format("Average wait time: %.2f\n", avgWaitTime);
		System.out.println("Quantum used: "+ quantum);		
		System.out.println("=====================================================================================");
	}//end of showRRResults()	
}//end of Calculation Class

