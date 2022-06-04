package fr.maner.mssb.cmd;

import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class MSSBArgs {

    private final String desc;
    private final String perm;
    private final boolean usableByPlayer;
    private final boolean usableByConsole;
    private final Consumer<CommandSender> command;

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