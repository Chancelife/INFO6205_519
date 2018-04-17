package Final;
public class Generic implements Comparable {
	// init state = 0; state=1 means that he knows that he needs a key. state=2
	// means he has the key. state = 3 means mission complete.
	private Gene[] genearr;
	private int achievements = 0;
	private int state;
	final double pro = 0.001;
	private double score = 0;
	private int step = 0;
	private int diedStep;
	private boolean alive = true;
	private static final int gLength = 150;
	static MagicTower mt = new MagicTower();

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getStep() {
		return step;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Gene[] getGenearr() {
		return genearr;
	}

	public void setGenearr(Gene[] genearr) {
		this.genearr = genearr;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public void setDiedStep(int x) {
		this.diedStep = x;
	}

	public Generic() {
		genearr = new Gene[gLength];
		state = 0;
	}
	
	public Generic(Generic father, Generic mother) {
		crossOver(father, mother);
	}

	public void crossOver(Generic father, Generic mother) {
		// update state
//		if (father.getState() > mother.getState())
//			this.state = father.getState();
//		else
//			this.state = mother.getState();
//		if (this.state > 1)
//			this.state = 1;
		
		// generate new generic from parents
		genearr = new Gene[gLength];
		
		for (int i = 0; i < gLength; i++) {
			if(i < father.diedStep) {
				if (father.genearr[i].getGene() == mother.genearr[i].getGene()) {
					this.genearr[i] = father.genearr[i];
				} else {
					double flag = Math.random();
					// System.out.println("flag:"+flag);
					//if (flag < 0.5)
					if(flag < (father.getScore()/(father.getScore()+mother.getScore())))
						this.genearr[i] = father.genearr[i];
					else
						this.genearr[i] = mother.genearr[i];
				}
			} else {
				this.genearr[i] = new Gene();
			}
		}
	}

	public void Mutate() {
		for (Gene g : this.genearr) {
			double f = Math.random();
			if (f <= pro) {
				double c = Math.random();
				if (c < 0.25)
					g.setGene(0);
				else if (c < 0.5)
					g.setGene(1);
				else if (c < 0.75)
					g.setGene(2);
				else
					g.setGene(3);
				break;
			}
		}
	}

	public void Move() {
		while (this.state >= 0 && this.step < gLength && this.isAlive()) {
			switch (this.getGenearr()[step].getGene()) {
				case 0:
					this.state = mt.getStatus(1, 0);
					break;
				case 1:
					this.state = mt.getStatus(0, -1);
					break;
				case 2:
					this.state = mt.getStatus(-1, 0);
					break;
				case 3:
					this.state = mt.getStatus(0, 1);
					break;
			}
			// Log status, every move
			this.step++;
			// System.out.println("Step "+step+ " Move to:" + mt.getX()+","+mt.getY()+" Status:"+ this.state);
			if(this.state == 0) {
				continue;
			}
			if (this.state == -1) {
				this.alive = false;
				for(int i=step;i<gLength;i++) {
					this.genearr[i] = new Gene();
				}
				this.diedStep = this.step-1>0?this.step-1:0;
				break;
			}
			// call map to update alive or state
			if (this.state == 3) {
				break;
			}
		}
		this.setScore(mt.fitnessFunction());
		// call fitness function
	}

	@Override
	public int compareTo(Object o) {
		Generic that = (Generic) o;
		if(this.getScore()>that.getScore()) return -1;
		else if(this.getScore() < that.getScore()) return 1;
		else return 0;
	}
}
