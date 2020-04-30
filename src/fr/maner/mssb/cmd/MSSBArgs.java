package fr.maner.mssb.cmd;

import java.util.function.Consumer;

import org.bukkit.command.CommandSender;

public class MSSBArgs {

	private String desc;
	private String perm;
	private boolean usableByPlayer;
	private boolean usableByConsole;
	private Consumer<CommandSender> command;

	public MSSBArgs(String desc, String perm, boolean usableByPlayer, boolean usableByConsole, Consumer<CommandSender> command) {
		this.desc = desc;
		this.perm = perm;
		this.usableByPlayer = usableByPlayer;
		this.usableByConsole = usableByConsole;
		this.command = command;
	}

	public String getDesc() {
		return desc;
	}

	public String getPerm() {
		return perm;
	}

	public boolean isUsableByPlayer() {
		return usableByPlayer;
	}
	
	public boolean isUsableByConsole() {
		return usableByConsole;
	}

	public Consumer<CommandSender> getCommand() {
		return command;
	}
}