package edu.touro.mco152.bm;

import edu.touro.mco152.bm.Commands.*;
import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;

/**
 * Run the disk benchmarking as a Swing-compliant thread (only one of these threads can run at
 * once.) Can cooperate with Swing and other forms of user interface to provide and make use of interim and final progress and
 * information, which is also recorded as needed to the persistence store, and log.
 * <p>
 * Depends on static values that describe the benchmark to be done having been set in App and Gui classes.
 * The DiskRun class is used to keep track of and persist info about each benchmark at a higher level (a run),
 * while the DiskMark class described each iteration's result, which is displayed by the UI as the benchmark run
 * progresses.
 * <p>
 * This class only knows how to do 'read' or 'write' disk benchmarks, all of which is done in doInBackground(). It is instantiated by the
 * startBenchmark() method.
 * <p>
 * To be compliant with many types of user interfaces, including Swing, our class implements the Callable interface
 * (As suggested on our course's Slack) This means that our DoInBackground method, which prior required
 * direct dependency on Swing, can be replaced by passing the DW class to whichever interface we please
 * and having said interface call our Call method to execute DiskWorker's logic.
 */

public class DiskWorker implements Callable {
    private UserExperienceInterface userInterface;

    public DiskWorker(UserExperienceInterface userInterface)
    {
        this.userInterface = userInterface;
    }

    // Record any success or failure status returned from SwingWorker (might be us or super)
    Boolean lastStatus = null;  // so far unknown
    //callable copy/paste
    //
    @Override
    public Object call() throws Exception {

        /*
          We 'got here' because: 1: End-user clicked 'Start' on the benchmark UI,
          which triggered the start-benchmark event associated with the App::startBenchmark()
          method.  2: startBenchmark() then instantiated a DiskWorker, and called
          its (super class's) execute() method, causing our user interface to eventually
          call this doInBackground() method.
         */
        Logger.getLogger(App.class.getName()).log(Level.INFO, "*** New worker thread started ***");
        msg("Running readTest " + App.readTest + "   writeTest " + App.writeTest);
        msg("num files: " + App.numOfMarks + ", num blks: " + App.numOfBlocks
                + ", blk size (kb): " + App.blockSizeKb + ", blockSequence: " + App.blockSequence);
        BMOperatonInvoker invoker = new BMOperatonInvoker();
        ReadCommand readCommand = new ReadCommand(userInterface, numOfMarks, numOfBlocks, blockSizeKb, blockSequence );
        WriteCommand writeCommand = new WriteCommand(userInterface, numOfMarks, numOfBlocks, blockSizeKb, blockSequence );


        /*
          init local vars that keep track of benchmarks, and a large read/write buffer
         */
        int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete;
        int wUnitsTotal = App.writeTest ? numOfBlocks * numOfMarks : 0;
        int rUnitsTotal = App.readTest ? numOfBlocks * numOfMarks : 0;
        int unitsTotal = wUnitsTotal + rUnitsTotal;
        float percentComplete;

        int blockSize = blockSizeKb * KILOBYTE;
        byte[] blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }


        Gui.updateLegend();  // init chart legend info

        if (App.autoReset) {
            App.resetTestData();
            Gui.resetTestData();
        }

        int startFileNum = App.nextMarkNumber;

        /*
          The GUI allows a Write, Read, or both types of BMs to be started. They are done serially.
         */
        if (App.writeTest) {
            invoker.setCommand(writeCommand);
            invoker.runCommand();
            // END outer loop for specified duration (number of 'marks') for WRITE benchmark
        /*
          Most benchmarking systems will try to do some cleanup in between 2 benchmark operations to
          make it more 'fair'. For example a networking benchmark might close and re-open sockets,
          a memory benchmark might clear or invalidate the Op Systems TLB or other caches, etc.
         */

            // try renaming all files to clear catch
        }
        if (App.readTest && App.writeTest && !userInterface.isCancelledUI()) {
            JOptionPane.showMessageDialog(Gui.mainFrame,
                    """
                            For valid READ measurements please clear the disk cache by
                            using the included RAMMap.exe or flushmem.exe utilities.
                            Removable drives can be disconnected and reconnected.
                            For system drives use the WRITE and READ operations\s
                            independantly by doing a cold reboot after the WRITE""",
                    "Clear Disk Cache Now", JOptionPane.PLAIN_MESSAGE);
        }

        // Same as above, just for Read operations instead of Writes.
        if (App.readTest) {
            invoker.setCommand(readCommand);
            invoker.runCommand();
        }
        App.nextMarkNumber += App.numOfMarks;
        return true;
    }




    public Boolean getLastStatus() {
        return lastStatus;
    }



}
