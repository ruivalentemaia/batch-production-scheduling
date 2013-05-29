
public class Machine {
	int id;
	int capacity;
	
	public Machine(int iD, int c) {
		id = iD;
		capacity = c;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setId(int iD) {
		id = iD;
	}
	
	public void setCapacity(int c) {
		capacity = c;
	}
}
