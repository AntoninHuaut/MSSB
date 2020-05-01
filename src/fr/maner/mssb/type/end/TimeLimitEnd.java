package fr.maner.mssb.type.end;

import fr.maner.mssb.type.state.InGameState;

public class TimeLimitEnd extends GameEnd {
	
	private double timeLimit;
	private long timeMS;
	
	public TimeLimitEnd() {
		setTimeLimit(7.5);
	}

	public void setTimeLimit(double timeLimit) {
		if (timeLimit < 1.0) return;
		this.timeLimit = timeLimit;
		this.timeMS = (long) (timeLimit * 60 * 1000);
	}
	
	public void addTimeLimit(double d) {
		this.timeLimit += d;
		this.timeMS = (long) (d * 60 * 1000);
		if (this.timeLimit < 1) setTimeLimit(1.0);
	}
	
	public double getTimeLimit() {
		return timeLimit;
	}

	@Override
	public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
		return inGameState.getStartTime() + timeMS < System.currentTimeMillis();
	}
}
