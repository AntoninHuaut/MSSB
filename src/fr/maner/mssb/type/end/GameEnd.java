package fr.maner.mssb.type.end;

public abstract class GameEnd {

	public abstract void initGameEnd();
	public abstract void reset();
	
	public boolean isKillEnd() { return this instanceof KillEnd; }
	public boolean isLifeEnd() { return this instanceof LifeEnd; }
	public boolean isTimeLimitEnd() { return this instanceof TimeLimitEnd; }
}
