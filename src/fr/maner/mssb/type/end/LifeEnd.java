package fr.maner.mssb.type.end;

public class LifeEnd extends GameEnd {
	
	private int nbLife;
	
	public LifeEnd() {
		setNBLife(5);
	}

	@Override
	public void initGameEnd() {
		
	}
	
	public void setNBLife(int nbLife) {
		if (nbLife < 1) return;
		this.nbLife = nbLife;
	}
	
	public void addLife(int nbLife) {
		this.nbLife += nbLife;
		if (this.nbLife < 1) setNBLife(1);
	}
	
	public int getNBLife() {
		return nbLife;
	}

	@Override
	public void reset() {}
}
