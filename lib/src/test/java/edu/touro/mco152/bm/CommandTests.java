package edu.touro.mco152.bm;

import edu.touro.mco152.bm.commands.BMInvoker;
import edu.touro.mco152.bm.commands.ReadCommand;
import edu.touro.mco152.bm.commands.WriteCommand;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import static edu.touro.mco152.bm.App.blockSequence;
import static edu.touro.mco152.bm.persist.DiskRun.BlockSequence.SEQUENTIAL;
import static org.junit.jupiter.api.Assertions.*;

public class CommandTests {
    @Test
    void writeTest()
    {
        setupDefaultAsPerProperties();
        BMInvoker testInvoker = new BMInvoker();
        WriteCommand writeCommand = new WriteCommand(new MainTest(), 128, 2048, SEQUENTIAL, 25);
        testInvoker.setCommand(writeCommand);
        assertTrue(testInvoker.runCommand());

    }
    @Test
    void ReadTest()
    {
        setupDefaultAsPerProperties();
        BMInvoker testInvoker = new BMInvoker();
        ReadCommand readTest = new ReadCommand(new MainTest(), 128, 2048, SEQUENTIAL, 25);
        testInvoker.setCommand(readTest);
        assertTrue(testInvoker.runCommand());

    }
    public void setupDefaultAsPerProperties()
    {
        /**
         * Bruteforce setup of static classes/fields to allow DiskWorker to run.
         *
         * @author lcmcohen
         */

        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath() + File.separator + App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        } else {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }
}
