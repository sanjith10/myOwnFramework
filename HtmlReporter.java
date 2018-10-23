package com.core.framework.utils;

import com.sun.jna.platform.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

@SuppressWarnings("unused")
public class HtmlReporter extends PublicVariables {
    private static final Logger Log = LogManager.getLogger(DriverFactory.class);
    public static int iStepCounter = 0;
    public static String strNewHTMLLocation;
    public static String fileName;
    public static String SSFileName;
    public static String SFileName;
    public static String sBCDescription = "***";
    //public static String strDescription;
    static Hashtable<String, String> RunTime_hash = new Hashtable<>();
    public static Boolean bResultFlag = true;

    public static String getHTMLLocation() throws IOException {
        if (strNewHTMLLocation == null) {
            CreateReportFolder();
            return strNewHTMLLocation;
        } else {
            return strNewHTMLLocation;
        }
    }

    /**
     * The CreateReportFolder method creates the folder for a module
     *
     * @author akongara
     * @Category Generic
     */
    public static String CreateReportFolder() throws IOException {
        String strHTMLLocation = html_ReportPath;  //System.getProperty("user.dir")+"\\reports\\mb\\servicing\\solomon\\html\\";
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        String sDateStart = dateFormat.format(date);

        new File(strHTMLLocation + "\\" + sDateStart + "\\screenshots").mkdirs();
        strNewHTMLLocation = strHTMLLocation + sDateStart;
        //strNewHTMLLocation = strHTMLLocation + appPublicVariables.testSuiteName+ " "+sDateStart;
        return strNewHTMLLocation;
    }

    /**
     * Capture ScreenShots Using Robot
     *
     * @author akongara
     * @Category Generic
     * @Description Capture Screenshot of entire Desktop and store in classpath using Robot
     */
    @SuppressWarnings("unused")
    public static void captureScreen() throws IOException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String sDateStart = dateFormat.format(date);

            Robot robot = new Robot();
            String format = "png";
            strNewHTMLLocation=getHTMLLocation();
            String fileName = strNewHTMLLocation + "\\screenshots\\" + sDateStart + ".png";

            /* Capture screenshots with Robot entire desktop screes*/
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, format, new File(fileName));
            try {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                org.apache.commons.io.FileUtils.copyFile(scrFile, new File(fileName));
                //org.apache.commons.io.FileUtils.copyFile(scrFile, new File(strNewHTMLLocation + "\\screenshots\\"+ sDateStart + ".png"));
            } catch (NullPointerException ne) {
                System.err.println(ne);
                Log.info("Unable to capture screenshot of Driver due to Null Pointer -Exception is : " + ne);
            }
            SFileName = sDateStart + ".png";
            currentScreenShot = fileName.toString();
            //System.out.println("A Full Screenshot Saved");

        } catch(AWTException ex){
            System.err.println(ex);
            Log.info("AWTException occured"+ ex.getMessage());
        }
    }

    /**
     * This CreateScenarioHTMLReport program creates the HTML file for a Test
     */
public static void CreateScenarioHTMLReport(String sTestCaseName) throws IOException{
    iStepCounter= 0;
    stepCount=0;
    RunTime_hash.put("htmlReport", "BC");
    fileName = strNewHTMLLocation + "\\" +sTestCaseName + " .html";
    uploadHTMLReportPath = fileName.toString();
    try {
        File tcHTMLReport = new File(fileName);
        if (!tcHTMLReport.exists()) {
            tcHTMLReport.createNewFile();
        }
        //The name of the file to open.
    }catch (NoSuchFileException e){
        System.out.println(e.getMessage());
        Log.info("NoSuchFileException occured "+ e.getMessage());
    }
    try {
        if (applicationName == null){
            applicationName = "No Application Name given in appPublicVariables.java in maven project";

        }
        System.out.println("Application Name : "+ PublicVariables.applicationName);
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("<HTML><HEAD>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<title> "+ PublicVariables.applicationName+ "UI Test Automation Report </title>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("</HEAD><BODY>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<P><B><U><CENTER><FONT face=Verdana color=#0033CC  size=4>"+ PublicVariables.applicationName+ " </FONT></CENTER></U></B></P>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TABLE height=10 width='100%' borderColorLight=#008080 border=2>&nbsp");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TR>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TR>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TD vAlign=Left  align=Left  width='3%' bgColor=#e1e1e1 height=20>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("FONT face=Verdana color=#0033CC size= 2><B>Execution Date :  ");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("</TR>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("</B></FONT></TD>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("</TABLE");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<br>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TABLE height=10 width='100%' borderColorLight=#008080 border=2>&nbsp");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("<TD vAlign=middle align=middle width='5%' bgColor=#e1e1e1 height=24><FONT face=Verdana color=#0033CC size=3><B>TC Step No </B></FONT></TD>");
        bufferedWriter.write("<TD vAlign=middle align=middle width='15%' bgColor=#e1e1e1 height=24><FONT face=Verdana color=#0033CC size=3><B>Function Name </B></FONT></TD>");
        bufferedWriter.write("<TD vAlign=middle align=middle width='75%' bgColor=#e1e1e1 height=24><FONT face=Verdana color=#0033CC size=3><B>Description </B></FONT></TD>");
        bufferedWriter.write("<TD vAlign=middle align=middle width='10%' bgColor=#e1e1e1 height=24><FONT face=Verdana color=#0033CC size=3><B>Status </B></FONT></TD>");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.close();


    }catch (IOException ex){
        System.out.println("Error writing to file '"+ fileName+ "'");
        Log.info("IOException Occured"+ ex.getMessage());
    }
}
/**
 * Below method takes screen shots
 */
public static String takeScreenShots(WebDriver driver, String sfileName) throws Exception{
    if(driver == null){
        return null;
    }
    try {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date= new Date();
        String sDateStart = dateFormat.format(date);
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        org.apache.commons.io.FileUtils.copyFile(scrFile, new File(sfileName + "\\screenshots\\"+ sDateStart+ ".png"));
        SSFileName = sDateStart + ".png";
        return SSFileName;
    }catch (FileNotFoundException e){
        Log.error("ClassUtils | Method takeScreenShot |Exception occured while capturing Screenshot : "+ e.getMessage());
        throw new Exception();
    }
    // return sfileName;
}

//This method implements print HTML report
    public static void EndBC(String strBusComName, String strStatus) throws Exception {
    if(testStatus==null|| testStatus.equalsIgnoreCase("warning")|| testStatus.equalsIgnoreCase("pass")|| testStatus.equalsIgnoreCase("passed")|| testStatus.equalsIgnoreCase("true")){
      if(strStatus.toLowerCase().contains("pass")|| strStatus.toLowerCase().contains("warning")){
          testStatus = "Passed";

            }else if(strStatus.toLowerCase().contains("fail)")){

          testStatus ="Failed";

            }

        } else if(strStatus.toLowerCase().contains("fail")){
        testStatus = "Failed";

        }
iStepCounter= iStepCounter+1;
    String ColorVal;
    HtmlReporter.takeScreenShots(driver, strNewHTMLLocation);
    if (screenShotsOnEveryStep.equalsIgnoreCase("yes")){
        captureScreen();
    }else if (strStatus.toLowerCase().contains("fail")){
        captureScreen();
    }

    System.out.println(strBusComName + "<<<<" + strStatus);
    if (strStatus.toLowerCase().contains("pass")){
        ColorVal= "009966";
        bResultFlag = true;
    }else{
        ColorVal = "FF3300";
        bResultFlag=false;
       // Assert.fail(strDescription);
    }

    try {
        //Assume default Encoding
        FileWriter fileWriter= new FileWriter(fileName, true);
        //Always wrap FileWriter in BufferedWriter
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        //Print Step Columns
        bufferedWriter.write("<TR><TD align=center width='2%' bgColor=#ffffe1 height=19>");
        bufferedWriter.write("<FONT face=Verdana size=2><b> "+ iStepCounter + "</b></FONT></TD>");
        bufferedWriter.write(System.lineSeparator());

        //Print Function/Business Component Column
        bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19>");
        bufferedWriter.write("<FONT face=Verdana size=2><b> "+ strBusComName + " </b></FONT></TD>");
        bufferedWriter.write(System.lineSeparator());

        //Print Description Column
        bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19> <FONT face=Verdana size=2><b> "+ sBCDescription + " </b></FONT></TD>");
        bufferedWriter.write(("<TD align=center width='2%' bgColor=#ffffe1 height=19>"));
        bufferedWriter.write(System.lineSeparator());

        //Print Status & Attach Screenshot
        if (currentScreenShot!=null){
            bufferedWriter.write("<a href= .\\screenshots\\" + SFileName+ " > <FONT face=Verdana color= "+ ColorVal+ " size= 2><b> "+ strStatus + " </b></FONT></a></TD></TR>");
            currentScreenShot = null;
        }else{
            bufferedWriter.write(" <FONT face=Verdana color= "+ ColorVal+ " size=2><b> "+ strStatus + "</b></FONT></TD></TR>");

        }
        bufferedWriter.write(System.lineSeparator());
        //Always Close files
        bufferedWriter.close();
        RunTime_hash.put("htmlReport", "End BC");
        sBCDescription = "";
    }catch (IOException ex){
        System.out.println("Error Writing to File "+ fileName+ " ");
        Log.error("IOException Occured "+ ex.getMessage());
        RunTime_hash.put("htmlReport", "End BC");
        sBCDescription = "";
    }

    }

    public static void EndBC(String strBusComName, String strStartDesc, String strEndDesc, String strStatus ) throws Exception {
        if (testStatus == null || testStatus.equalsIgnoreCase("warning") || testStatus.equalsIgnoreCase("pass") || testStatus.equalsIgnoreCase("passed") || testStatus.equalsIgnoreCase("true")) {
            if (strStatus.toLowerCase().contains("pass") || strStatus.toLowerCase().contains("warning")) {
                testStatus = "Passed";

            } else if (strStatus.toLowerCase().contains("fail)")) {

                testStatus = "Failed";

            }

        } else if (strStatus.toLowerCase().contains("fail")) {
            testStatus = "Failed";

        }
        iStepCounter = iStepCounter + 1;
        String ColorVal;
        HtmlReporter.takeScreenShots(driver, strNewHTMLLocation);
        if (screenShotsOnEveryStep.equalsIgnoreCase("yes")) {
            captureScreen();
        } else if (strStatus.toLowerCase().contains("fail")) {
            captureScreen();
        }

        System.out.println(strBusComName + "<<<<" + strStatus);
        if (strStatus.toLowerCase().contains("pass")) {
            ColorVal = "009966";
            bResultFlag = true;
        } else {
            ColorVal = "FF3300";
            bResultFlag = false;

        }
        try {
            //Assume default Encoding
            FileWriter fileWriter = new FileWriter(fileName, true);
            //Always wrap FileWriter in BufferedWriter
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Print Step Columns
            bufferedWriter.write("<TR><TD align=center width='2%' bgColor=#ffffe1 height=19>");
            bufferedWriter.write("<FONT face=Verdana size=2><b> " + iStepCounter + "</b></FONT></TD>");
            bufferedWriter.write(System.lineSeparator());

            //Print Function/Business Component Column
            bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19>");
            bufferedWriter.write("<FONT face=Verdana size=2><b> " + strBusComName + " </b></FONT></TD>");
            bufferedWriter.write(System.lineSeparator());

            //Print Description Column
            bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19> <FONT face=Verdana size=2><b> " + strStartDesc + sBCDescription + strEndDesc + " </b></FONT></TD>");
            bufferedWriter.write(("<TD align=center width='2%' bgColor=#ffffe1 height=19>"));
            bufferedWriter.write(System.lineSeparator());

            //Print Status & Attach Screenshot
            if (currentScreenShot != null) {
                bufferedWriter.write("<a href= .\\screenshots\\" + SFileName + " > <FONT face=Verdana color= " + ColorVal + " size= 2><b> " + strStatus + " </b></FONT></a></TD></TR>");
                currentScreenShot = null;
            } else {
                bufferedWriter.write(" <FONT face=Verdana color= " + ColorVal + " size=2><b> " + strStatus + "</b></FONT></TD></TR>");
            }
            bufferedWriter.write(System.lineSeparator());
            //Always Close files
            bufferedWriter.close();
            RunTime_hash.put("htmlReport", "End BC");
            sBCDescription = "";
        } catch (IOException ex) {
            System.out.println("Error Writing to File " + fileName + " ");
            Log.error("IOException Occured " + ex.getMessage());
            RunTime_hash.put("htmlReport", "End BC");
            sBCDescription = "";
        }
    }
        //The StartBC method implements invoking of HTML Report for Business Components node
        /**
         * @category : Generic
         * @author : akongara(akongara@balglobal.com)
         */
    public static void StartBC(){
        RunTime_hash.put("htmlReport", "Start BC");
        sBCDescription = "";
        }


    /**
     * The Log Method implements printing of HTML Report for Business Components node & Individual Generic/Customized
     * @param sFunctionName
     * @param strDescription
     * @param strStatus
     * @throws Exception
     */

    public static void Log(String sFunctionName, String strDescription, String strStatus) throws Exception {
        stepCount = stepCount +1;
        if(testStatus==null || testStatus.equalsIgnoreCase("warning")||testStatus.equalsIgnoreCase("pass")||testStatus.equalsIgnoreCase("passed")||testStatus.equalsIgnoreCase("true")){
            if(strStatus.toLowerCase().contains("pass")||strStatus.toLowerCase().contains("warning")){
                testStatus= "Passed";
            }else if (strStatus.toLowerCase().contains("fail")){
                testStatus= "Failed";
            }
        }else if (strStatus.toLowerCase().contains("fail")){
            testStatus= "Failed";
        }
        String ColorVal, strHtmlDesc;
        //takeScreenShots(driver, strNewHTMLLocation);
        if (screenShotsOnEveryStep.equalsIgnoreCase("yes")) {
            captureScreen();
        } else if (strStatus.toLowerCase().contains("fail")) {
            captureScreen();
        }

        if (strStatus.toLowerCase().contains("pass")){
            ColorVal = "009966";
            testSteps.put("Status"+ stepCount, "Passed");
            if(currentScreenShot!=null){
                screenShotSteps.put("Screenshot"+ stepCount, currentScreenShot);
            }
            //bResultFlag= true;

        }else if(strStatus.toLowerCase().contains("fail")){
            ColorVal= "FF3300";
            testSteps.put("Status"+ stepCount, "Failed");
            if (currentScreenShot!=null){
                screenShotSteps.put("Screenshot"+ stepCount, currentScreenShot);
            }
            HtmlReporter.bResultFlag=false;
            Assert.fail(strDescription);
        }else if(strStatus.toLowerCase().contains("warning")){
            ColorVal = "FFA500";
            testSteps.put("Status"+ stepCount, "Passed");
            if (currentScreenShot!=null){
                screenShotSteps.put("Screenshot"+ stepCount, currentScreenShot);
            }
            HtmlReporter.bResultFlag= true;
        }else {
            ColorVal= "FFFF00";
        }
        testSteps.put("Description"+ stepCount, strDescription);

        try{
            if (RunTime_hash.get("htmlReport")=="End BC") {
                //Assume Default Encoding
                FileWriter fileWriter = new FileWriter(fileName, true);
                //Always wrap fileWriter in bufferedWriter
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("<TR><TD align=center width='2%' bgColor=#ffffe1 height=19>");
                bufferedWriter.write("<FONT face=Verdana size=2><b> " + iStepCounter + " </b></FONT></TD>");
                bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19>");
                bufferedWriter.write("<FONT face=Verdana size=2><b> " + sFunctionName + "</b></FONT></TD>");
                bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19>");
                strHtmlDesc = "<FONT face=Verdana size=2> color= " + ColorVal + "> <b> " + strDescription + " </b></FONT>";
                bufferedWriter.write(strHtmlDesc);
                bufferedWriter.write("<TD align=center width='2%' bgColor=#ffffe1 height=19>");
                if (currentScreenShot != null) {
                    bufferedWriter.write("<a href=.\\screenshots\\" + SFileName + " > <FONT face=Verdana color= " + ColorVal + " size=2> <b> " + strStatus + " </b></FONT></TD></TR>");

                } else {
                    bufferedWriter.write(" <FONT face=Verdana colr= " + ColorVal + " size=2><b> " + strStatus + " </b></FONT></TD></TR>");
                }
                bufferedWriter.write(" <a href= .\\screenshots\\" + SFileName + "'TARGET= '_new'><img src=.\\screenshots\\" + SFileName + "'border='no' height='80' width='120' alt='Click to maximize'></a></TD></TR>");
                //Always close the file
                bufferedWriter.close();

            }else {
                if(!(strDescription== "")){
                    strHtmlDesc= "<FONT face=Verdana size=2 color= "+ ColorVal+ "> <b> "+ strDescription+ " </b><FONT>";
                    sBCDescription= sBCDescription+ "<br> <br>"+ strHtmlDesc;
                }
            }
        }catch (IOException ex){
            System.out.println("Error writing to file '"+ fileName + "'");
            Log.info("IOException Occured"+ ex.getMessage());
        }

    }
    /**
     * EndTC method implements closing of the HTML Report for a Test
     */
public static void EndTC() throws IOException {
    try {
        //Assume Default Encoding
        FileWriter fileWriter = new FileWriter(fileName, true);

        //Always wrap fileWriter in bufferedWriter
        BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);

        bufferedWriter.write("</Body>");
        bufferedWriter.write("</HTML>");

        //Always close files
        bufferedWriter.close();

    }catch (IOException ex){
        System.out.println("Error writing to file '"+ fileName+ "'");
        Log.info("IOException occured"+ ex.getMessage());

    }
}




}
