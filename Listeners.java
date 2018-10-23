package com.core.framework.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
@SuppressWarnings("unused")
public class Listeners implements ITestListener {

    @Override
    public void onFinish(ITestContext arg0){

    }

    @Override
    public void onStart(ITestContext arg0){

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0){

    }

    @Override
    public void onTestFailure(ITestResult arg0){
        System.out.println("The Name of the test case failed is "+arg0.getName());
       // Log.info("The Name of the test case failed is "+arg0.getName());

    }



    @Override
    public void onTestSkipped(ITestResult arg0){
        System.out.println("The Name of the test case skipped is "+arg0.getName());
        // Log.info("The Name of the test case skipped is "+arg0.getName());

    }



    @Override
    public void onTestStart(ITestResult arg0){
        System.out.println("The test case is "+arg0.getName()+" started");
        // Log.info("The test case started is "+arg0.getName());

    }

    @Override
    public void onTestSuccess(ITestResult arg0){
        System.out.println("The Name of the test case passed is "+arg0.getName());
        // Log.info("The Name of the test case passed is "+arg0.getName());

    }


}
