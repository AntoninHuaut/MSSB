package fr.maner.mssb.type.end;

public class KillEnd extends GameEnd {
	
	private int nbKill;
	
	public KillEnd() {
		setNBKill(10);
	}
	
	@Override
	public void initGameEnd() {
		
	}
	
	public void setNBKill(int nbKill) {
		if (nbKill < 1) return;
		this.nbKill = nbKill;
	}
	
	public void addKill(int nbKill) {
		this.nbKill += nbKill;
		if (this.nbKill < 1) setNBKill(1);
	}
	
	public int getNBKill() {
		return nbKill;
	}

	@Override
	public void reset() {}
}
