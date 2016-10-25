package os_1_lab_4;

public class Process implements Comparable<Process>{
	
	private String pID;
	private int burstTime;
	
	public Process(){
		pID = "";
		burstTime = 0;
		
	}
	
	public Process(String pid, int bTime){
		this.pID = pid;
		this.burstTime = bTime;
		
	}

	public String getpID() {
		return pID;
	}

	public void setpID(String pID) {
		this.pID = pID;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	@Override
	public String toString() {
		return "Process [pID=" + pID + ", burstTime=" + burstTime + "]";
	}
		
	//get the compareTO method
	public int compareTo(Process p2) {		
		return this.getBurstTime() - ((Process)p2).getBurstTime();//calculate the difference between current process(this) and the other process
	}
	
	

}
