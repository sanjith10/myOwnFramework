package com.core.framework.utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;
@SuppressWarnings("unused")
public class TestSuite_Listeners implements ISuiteListener {

    Long startTime;
    @Override
    public void onStart(ISuite iSuite){
      startTime = System.currentTimeMillis();
    }

    @Override
    public void onFinish(ISuite iSuite){
        Long endTime = System.currentTimeMillis();
        Long totalExecutionTime = endTime- startTime;
        Long totalExecutionTimeInSeconds = totalExecutionTime/1000;

        System.out.println("\nSuite Execution Time = " + totalExecutionTimeInSeconds + " seconds");
    }
}
