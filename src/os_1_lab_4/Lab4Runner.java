package os_1_lab_4;

import java.util.Scanner;

public class Lab4Runner {

	private static Scanner scan = new Scanner(System.in);
	public static void main(String[] args) {
		
		Calculation cal = new Calculation();
		int choice;
		
		System.out.println("Welcome to Process Scheduling calculator.");
		System.out.println("Press 1 to add new queue, 2 to use same queue, or -1 to exit program");	
		choice = scan.nextInt();
		
		while(choice != -1){
				
			switch(choice){
				case 1:
					cal.showMenu();//showMenu gets called to see how many processes are needed
					cal.inputProcesses();//get user to input process id's and burst times			
					cal.chooseSchedule();//let them choose what scheduling
					break;
					
				case 2:
					cal.chooseSchedule();//let them choose what scheduling
					break;
			}
						
			System.out.println("Press 1 to add new queue, 2 to use same queue, or -1 to exit program");	
			choice = scan.nextInt();
			
			
		}
		System.out.println("Application Finished");
	}//end of main

}
