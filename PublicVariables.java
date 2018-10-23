package com.core.framework.utils;

import autoitx4java.AutoItX;
import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unused", "deprecation" })
public class PublicVariables {
    /**
     * File System Paths Related Global Variables
     */
    public static String Browser = "IE";
    //public static String[] Browser = {"IE", "Mozilla", "GoogleChrome"};
    public static String Environment = "IMS-STG2-Done";
    public static String BrowserPathIE ="D:\\IMSAssist_UI_TestAutomation\\driverServers\\IEDriverServer.exe";   //System.getProperty("user.dir") + "//IEDriverServer.exe";
    public static String BrowserPathFF = "D:\\IMSAssist_UI_TestAutomation\\driverServers\\geckodriver.exe";    // System.getProperty("user.dir") + "//geckodriver.exe";
    public static String BrowserPathGC = "D:\\IMSAssist_UI_TestAutomation\\driverServers\\chromedriver.exe" ; //System.getProperty("user.dir") + "//chromedriver.exe";
    public static String URL;
    public static String Path_TestData = System.getProperty("user.dir") + "\\src\\test\\resources\\testData\\";
    public static String html_ReportPath = "D:\\IMSAssist_UI_TestAutomation\\TestScreenshots\\reports\\html\\";//System.getProperty("user.dir") + "\\reports\\html\\";


    /**
     * DataTable Accessing and Assignment Global Variables
     */
    public static String xlDataFileName;
    public static String xlDataFilePath;
    public static String xlSheetName;
    public static DataTable dataTable;
    public static Map<String, String> tcRowData = new HashMap<String, String>();


    public static final int col_TestCaseName = 0;
    public static final int col_Description =1;
   // public static final int col_PageObjects = 3;
    //public static final int col_ActionKeyword = 4;
    public static final int col_RunMode = 2;

    public static final String sheet_TestCases = "Test Cases";
    public static final int col_Result = 3;





    /**
     * HTML Reporter Global Variables
     */
    public static HtmlReporter reporter;
/**
 * Selenium WebDriver Global Variables
 */
public static WebDriver driver = null;
public static WebDriver childDriver = null;
public static Selenium selenium =null;
public static AutoItX autoIT = null;
public static String htmlDriver = null;

/**
 * Synchronization Timeout Objects
 */
public static WebDriverWait wait;
public static WebDriverWait objWait;
public static WebDriverWait pageWait;

/**
 * GUI Components Global Objects
 */
    //This should be commented until creation of GUI Components class public static GUIComponents obj = new GUIComponents(driver);


    /**
     * Application Related Global Variables
     */
public static String applicationName;
public static int pageTimeOut;
public static int objTimeOut;
public static String currentTestName;

/**
 * Rally Related Global Variables
 */
    public static String rallySync;
    public static String rallyURL;
   // public static String rallyDomain;
    public static String rallyProject;
    public static String rallyUser;
    public static String rallyEncryptedPassword;
    public static HashMap<String, String> rallyInputs = new HashMap<String, String>();

    /**
     * Test Results Variables
     */
public static String testStatus = null;
public static String uploadHTMLReportPath;
public static HashMap<String, String> testSteps = new HashMap<String, String>();
public static HashMap<String, String> screenShotSteps = new HashMap<String, String>();
public static int stepCount = 0;
public static String currentScreenShot= null;
public static String screenShotsOnEveryStep= "Yes";
public static String detailedReport= "Yes";

}

