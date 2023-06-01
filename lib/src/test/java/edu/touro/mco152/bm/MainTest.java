package edu.touro.mco152.bm;


import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that show the benchmark can be run independent of
 * the SwingUI. Proven using a very limited version of
 * the benchmark being run using a stripped down UIInterface
 * that has just enough to pass the tests
 *
 * @implement UIInterface
 */
public class MainTest implements UserExperienceInterface{
    public static int currentPercentComplete;

    public MainTest() {
        setupDefaultAsPerProperties();

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
            App.worker = new MainTest();
        }
    }



        @Override
        public void setCallable(Callable userCallable) {

        }




        @Override
        public boolean isCancelledUI() {
            return false;
        }

        @Override
        public void process(List<DiskMark> markList) {

        }

        @Override
        public void done() {

        }


        @Override
        public void cancelUI(boolean b) {
        }

        @Override
        public void addPropertyChangeListenerUI(PropertyChangeListener pcl) {

        }

        @Test
        @Override
        public void executeUI() {
            try {
                DiskWorker test = new DiskWorker(new MainTest());
                test.call();

                assertEquals(100, currentPercentComplete);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public void setProgressUI(int percentComplete) {
            assertTrue(percentComplete >= 0 && percentComplete <= 100);
            currentPercentComplete = percentComplete;
        }
    @Override
    public void interfacePublish (DiskMark wMark) {
        assertNotNull(wMark);
    }
    }
