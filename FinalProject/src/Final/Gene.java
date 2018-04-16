package Final;
import java.util.Random;

public class Gene {
	public static final int DOWN = 0;
	public static final int LEFT = 1;
	public static final int UP = 2;
	public static final int RIGHT = 3;
	private int gene;
	
	public Gene() {
		randomGene();
	}
	public Gene(int i) {
		this.gene=i;
	}
	
	public int getGene() {
		return gene;
	}
	public void setGene(int gene) {
		this.gene = gene;
	}
	
	private void randomGene() {
		Random r = new Random();
		this.gene = r.nextInt(4);
	}
	
}
