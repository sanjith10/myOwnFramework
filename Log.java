package com.core.framework.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
@SuppressWarnings("unused")
public class Log {
    //Initialize Log4j logs
    private static final Logger Log=LogManager.getLogger(Log.class);//(Log.class);

    //BasicConfigurator.configure();

    //This is to print log for the beginning of the test case, as we usually run so many test cases  as a test suite

    public static void startTestCase(String sTestCaseName){
        Log.info("*****************************************************************************");
        Log.info("*****************************************************************************");
        Log.info("$$$$$$$$$$$$$$$$$                   " + sTestCaseName+ "             $$$$$$$$$$$$$$$$$");
        Log.info("********************************************************************************");
        Log.info("*********************************************************************************");
    }

    //This is to print  log for the ending of the test case

    public static void endTestCase(String sTestCaseName){
        Log.info("XXXXXXXXXXXXXXXX                     " + "-E----N----D-"        + "               XXXXXXXXXXXXXXXXXXXX");

        Log.info("X");
        Log.info("X");
        Log.info("X");
        Log.info("X");
    }

    // This method is for logging info
    public static void info(String message){
        Log.info(message);
    }


    //This method is for logging warnings
    public static void warn(String message){
        Log.warn(message);
    }


    //This method is for logging the error details
    public static void error(String message){
        Log.error(message);
    }


    public static void fatal(String message){
        Log.fatal(message);
    }


  public static void debug(String message){
        Log.debug(message);
  }

}
