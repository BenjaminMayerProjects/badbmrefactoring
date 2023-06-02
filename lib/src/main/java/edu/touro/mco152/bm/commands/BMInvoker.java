package edu.touro.mco152.bm.commands;

public class BMInvoker {
    private BMCommands command;
    public BMInvoker(BMCommands command)
    {
        this.command = command;
    }
    public BMInvoker()
    {

    }
    public void runCommand()
    {
        command.execute();
    }

    public void setCommand(BMCommands command) {
        this.command = command;
    }
}
