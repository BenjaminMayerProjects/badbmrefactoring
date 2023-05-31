package edu.touro.mco152.bm.Commands;

public class BMOperatonInvoker {
    private BMOperationsCommand command;

    public BMOperatonInvoker(BMOperationsCommand command)
    {
        this.command = command;
    }

    public BMOperatonInvoker()
    {

    }

    public void  setCommand(BMOperationsCommand command)
    {
        this.command = command;
    }

    public void runCommand()
    {
        command.execute();
    }
}
