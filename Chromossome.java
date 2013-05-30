
public class Chromossome {
	int id;
	Gen[] genes;
	
	public Chromossome(int iD, Gen[] g) {
		id = iD;
		genes = g;
	}
	
	public int getId(){
		return id;
	}
	
	public Gen[] getGenes() {
		return genes;
	}
	
	public void setId(int iD) {
		id = iD;
	}
	
	public void setGenes(Gen[] g) {
		genes = g;
	}
	
	public void print() {
		System.out.println("Chromossome");
		System.out.println("ID: " + this.id);
		System.out.println("Genes:");
		for(int i = 0; i < genes.length; i++) {
			System.out.println("ID: " + genes[i].getID());
			System.out.println("Due date: " + genes[i].getDueDate());
		}
	}
}
