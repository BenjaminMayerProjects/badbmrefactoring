package edu.touro.mco152.bm.commands;

public class BMInvoker {
    /**
     * This is the invoker, which calls the commands for us. It enables the desired function to be executed without
     * the calling object knowing how to execute.
     *
     */
    private BMCommands command;
    public BMInvoker(BMCommands command)
    {
        this.command = command;
    }
    public BMInvoker()
    {

    }
    public boolean runCommand()
    {
        command.execute();
        return true;
    }

    public void setCommand(BMCommands command) {
        this.command = command;
    }
}
