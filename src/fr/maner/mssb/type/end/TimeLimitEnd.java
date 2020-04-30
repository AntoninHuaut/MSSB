package fr.maner.mssb.type.end;

public class TimeLimitEnd extends GameEnd {
	
	private double timeLimit;
	
	public TimeLimitEnd() {
		setTimeLimit(7.5);
	}

	@Override
	public void initGameEnd() {
				
	}
	
	@Override
	public void reset() {
		// TODO STOP SCHEDULE
		
	}
	
	private class TimeLimitRun implements Runnable {
		@Override
		public void run() {
			double msTimeLit = timeLimit * 60 * 1000;
		}
	}
	
	public void setTimeLimit(double timeLimit) {
		if (timeLimit < 1.0) return;
		this.timeLimit = timeLimit;
	}
	
	public void addTimeLimit(double d) {
		this.timeLimit += d;
		if (this.timeLimit < 1) setTimeLimit(1.0);
	}
	
	public double getTimeLimit() {
		return timeLimit;
	}
}
