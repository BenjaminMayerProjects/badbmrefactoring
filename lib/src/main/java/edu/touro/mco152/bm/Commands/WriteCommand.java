package edu.touro.mco152.bm.Commands;

import edu.touro.mco152.bm.UserExperienceInterface;
import edu.touro.mco152.bm.persist.DiskRun;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.blockSequence;

public class WriteCommand implements BMOperationsCommand {
    private UserExperienceInterface userInterface;
    public WriteCommand(UserExperienceInterface userInterface)
    {
        this.userInterface = userInterface;
    }

    public WriteCommand(UserExperienceInterface userInterface, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence) {
        this.userInterface = userInterface;
    }

    @Override
    public void execute() {
        WriteOperation.readOperation(userInterface, numOfMarks, numOfBlocks, blockSizeKb, blockSequence);
    }
}
