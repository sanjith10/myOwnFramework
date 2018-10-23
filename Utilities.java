package com.core.framework.utils;

import autoitx4java.AutoItX;
import com.jacob.com.LibraryLoader;
import com.sun.jna.platform.WindowUtils;
import org.apache.commons.net.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.os.WindowsUtils;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unused")
public class Utilities extends PublicVariables {

    private static final Logger Log = LogManager.getLogger(DriverFactory.class);
    //public  String strDataFilePath;

public  Properties loadingPropertiesFile(String strDataFilePath) {
    //Load data from appropriate TestData.txt file
    File file = new File(strDataFilePath);
    FileInputStream fileInput = null;
    {
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }
    Properties prop = new Properties();
    //load properties file

        try {
            prop.load(fileInput);
            return prop;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }

}
    public String ParseUsernameAndPassword(Properties prop, String sUserRole) {
        String strUsernameAndPassword;
        String strUsername;
        String strPassword;
        String strUserFullName = "N/A";
        String strUserContactId = "N/A";

        //loadingPropertiesFile(strDataFilePath);

        switch (sUserRole) {
            case "SuperAdmin":
                strUsername = prop.getProperty("sUsernameSuperAdmin");
                strPassword = prop.getProperty("sPasswordSuperAdmin");
                strUserFullName = prop.getProperty("SuperAdminName");
                //strUserContactId = prop.getProperty("SuperAdminContactId");
                break;
            case "Admin":
                strUsername = prop.getProperty("sUsernameAdmin");
                strPassword = prop.getProperty("sPasswordAdmin");
                strUserFullName = prop.getProperty("AdminName");
                strUserContactId = prop.getProperty("AdminContactId");
                break;
            case "KMAdmin":
                strUsername = prop.getProperty("sUsernameKMAdmin");
                strPassword = prop.getProperty("sPasswordKMAdmin");
                strUserFullName = prop.getProperty("KMAdminName");
                strUserContactId = prop.getProperty("KMAdminContactId");
                break;
            case "Attorney":
                strUsername = prop.getProperty("sUsernameAtty");
                strPassword = prop.getProperty("sPasswordAtty");
                strUserFullName = prop.getProperty("BALAttorneyName");
                strUserContactId = prop.getProperty("BALAttorneyContactId");
                break;
            case "Assistant":
                strUsername = prop.getProperty("sUsernameAsst");
                strPassword = prop.getProperty("sPasswordAsst");
                strUserFullName = prop.getProperty("BALAssistantName");
                strUserContactId = prop.getProperty("BALAssistantContactId");
                break;
            case "CountryCaseManagerElevated":
                strUsername = prop.getProperty("sUsernameCCMElevated");
                strPassword = prop.getProperty("sPasswordCCMElevated");
                strUserFullName = prop.getProperty("CCMElevatedName");
                strUserContactId = prop.getProperty("CCMElevatedContactId");
                break;
            case "CountryCaseManager_USOnly":
                strUsername = prop.getProperty("sUsernameCCMUSAOnly");
                strPassword = prop.getProperty("sPasswordCCMEUSAOnly");
                strUserFullName = prop.getProperty("CCMUSOnlyName");
                strUserContactId = prop.getProperty("CCMUSOnlyContactId");
                break;
            case "CountryCaseManagerNonUS":
                strUsername = prop.getProperty("sUsernameCCMNonUS");
                strPassword = prop.getProperty("sPasswordCCMENonUS");
                strUserFullName = prop.getProperty("CCMNonUSName");
                strUserContactId = prop.getProperty("CCMNonUSContactId");
                break;
            case "HRContact":
                strUsername = prop.getProperty("sUsernameHRContact");
                strPassword = prop.getProperty("sPasswordHRContact");
                strUserFullName = prop.getProperty("HRContactName");
                strUserContactId = prop.getProperty("HRContactId");
                break;
            case "HRLead":
                strUsername = prop.getProperty("sUsernameHRLead");
                strPassword = prop.getProperty("sPasswordHRLead");
                strUserFullName = prop.getProperty("HRLeadName");
                strUserContactId = prop.getProperty("HRLeadContactId");
                break;
            case "Manager":
                strUsername = prop.getProperty("sUsernameMgr");
                strPassword = prop.getProperty("sPasswordMgr");
                strUserFullName = prop.getProperty("HRManagerName");
                strUserContactId = prop.getProperty("HRManagerContactId");
                break;
            case "Beneficiary":
                strUsername = prop.getProperty("sUsernameBene");
                strPassword = prop.getProperty("sPasswordBene");
                strUserFullName = prop.getProperty("BeneName");
                strUserContactId = prop.getProperty("BeneContactId");
                break;
            case "NetworkPartner1":
                strUsername = prop.getProperty("sUsernameNP1");
                strPassword = prop.getProperty("sPasswordNP1");
                strUserFullName = prop.getProperty("NP1ContactName");
                strUserContactId = prop.getProperty("NP1ContactId");
                break;
            case "NetworkPartner2":
                strUsername = prop.getProperty("sUsernameNP2");
                strPassword = prop.getProperty("sPasswordNP2");
                strUserFullName = prop.getProperty("NP2ContactName");
                strUserContactId = prop.getProperty("NP2ContactId");
                break;
            case "NetworkPartner3":
                strUsername = prop.getProperty("sUsernameNP3");
                strPassword = prop.getProperty("sPasswordNP3");
                strUserFullName = prop.getProperty("NP3ContactName");
                strUserContactId = prop.getProperty("NP3ContactId");
                break;
            case "HRC_CompanyScope":
                strUsername = prop.getProperty("sUsernameHRC_Company");
                strPassword = prop.getProperty("sPasswordHRC_Company");
                strUserFullName = prop.getProperty("HRC_CompanyName");
                strUserContactId = prop.getProperty("HRC_CompanyContactId");
                break;
            case "HRC_CountryScope":
                strUsername = prop.getProperty("sUsernameHRC_Country");
                strPassword = prop.getProperty("sPasswordHRC_Country");
                strUserFullName = prop.getProperty("HRC_CountryName");
                strUserContactId = prop.getProperty("HRC_CountryContactId");
                break;
            case "HRC_BusinessUnitScope":
                strUsername = prop.getProperty("sUsernameHRC_BusUnit");
                strPassword = prop.getProperty("sPasswordHRC_BusUnit");
                strUserFullName = prop.getProperty("HRC_BusUnitName");
                strUserContactId = prop.getProperty("HRC_BusUnitContactId");
                break;
            case "HRC_EntityScope":
                strUsername = prop.getProperty("sUsernameHRC_Entity");
                strPassword = prop.getProperty("sPasswordHRC_Entity");
                strUserFullName = prop.getProperty("HRC_EntityName");
                strUserContactId = prop.getProperty("HRC_EntityContactId");
                break;
            case "Oracle_HRContact":
                strUsername = prop.getProperty("sUsername_Oracle_HRContact");
                strPassword = prop.getProperty("sPassword_Oracle_HRContact");
                strUserFullName = prop.getProperty("Oracle_HRContactFullName");
                strUserContactId = prop.getProperty("Oracle_HRContactId");
                break;
            case "Oracle_HRLead":
                strUsername = prop.getProperty("sUsername_Oracle_HRLead");
                strPassword = prop.getProperty("sPassword_Oracle_HRLead");
                strUserFullName = prop.getProperty("Oracle_HRLeadFullName");
                strUserContactId = prop.getProperty("Oracle_HRLeadId");
                break;
            case "ProfileBeneficiary":
                strUsername = prop.getProperty("ProfileDocsBeneUsername");
                strPassword = prop.getProperty("ProfileDocsBenePassword");
                strUserFullName = prop.getProperty("ProfileDocsBeneName");
                strUserContactId = prop.getProperty("ProfileDocsCompanyContactId");
                break;
            default:
                strUsername = "None";
                strPassword = "None";
                break;
        }

        strUsernameAndPassword = strUsername + ";" + strPassword+";"+strUserFullName+";"+strUserContactId;

        return strUsernameAndPassword;
    }




    public String parseBrowserEnvProperties(Properties prop, String strTestDataFile){
    String strEnvAndBrowserType;
    //loadingPropertiesFile(strTestDataFile);
    String sEnvironment = prop.getProperty("sEnvironment");
    String sBrowserType = prop.getProperty("sBrowserType");
    strEnvAndBrowserType = sEnvironment+ ";"+ sBrowserType;
    return strEnvAndBrowserType;

}












    /**
     * Getting current date in required format
     *
     * @param format
     * @return
     */
    public String getTodayDate(String format) {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(format);
        System.out.println(ft.format(date));
        return ft.format(date);
    }

    /**
     * Getting Random Number of given length
     *
     * @param length
     * @return
     */
    public int getRandomNumber(int length) {
        Random random = new Random();
        int randomNumber = 0;
        boolean loop = true;

        while (loop) {
            randomNumber = random.nextInt();
            if (Integer.toString(randomNumber).length() == length && !Integer.toString(randomNumber).startsWith("-")) {
                loop = false;
            }
        }
        return randomNumber;
    }

    /**
     * Encrypting original Password
     *
     * @param strOrgPW
     * @return
     */

    public static String encryptPassword(String strOrgPW) {

       byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(strOrgPW.getBytes());

       //byte[] encoded = Base64.encodeBase64(strOrgPW.getBytes());

        System.out.println("\n"+ encoded);
        return new String(encoded);
    }





    /**
     * Decrypting the Encoded Password
     *
     * @param encodedPassword
     * @return
     */
    public static byte[] decryptEncodedPassword(String encodedPassword) {
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(encodedPassword);
        //byte[] decoded = Base64.decodeBase64(encodedPassword);
                //getBytes());
        System.out.println("\n"+ new String(decoded));
        return decoded;
    }


    public void highlightMe(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: Red");
        js.executeScript("arguments[0].style.border= '2px groove green'", element);


    }


    public static String jvmBitVersion() {
        return System.getProperty("sun.arch.data.model");
    }


    public void killProcess(String processName) throws IOException {
        String line, totalString = "";
        Process proc = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        while ((line = in.readLine()) != null) {
            totalString = totalString + "-->" + line;

        }

        in.close();
        String[] process = totalString.split("-->");
        List<String> processNames = new ArrayList<String>();

        for (String s : process) {
            String[] n = s.split(" ");
            for (String nb : n) {
                if (nb.contains(" exe")) {
                    processNames.add(nb);
                }
            }
        }

        for (String procName : processNames) {
            if (procName.contains(processName)) {
                try {
                    WindowsUtils.killByName(procName);
                    break;
                } catch (Exception e) {
                    System.out.println("No such Process " + procName + " to Kill");
                    Log.error("Exception Occured " + e.getMessage());
                }
            }
        }

    }

    public static void initializeJacobs() {
        String jacobDllVersionToUse;
        if (jvmBitVersion().toString() == "32") {
            jacobDllVersionToUse = "jacob-1.18-x86.dll";
        } else {
            jacobDllVersionToUse = "jacob-1.18-x64.dll";
        }
        File file = new File("lib", jacobDllVersionToUse);
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());

    }


    public static String dateFormatted(String myDate, String returnFormat, String myFormat) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(returnFormat);
        Date date = null;
        String returnValue = "";

        try {
            date = new SimpleDateFormat(myFormat, Locale.ENGLISH).parse(myDate);
            returnValue = dateFormat.format(date);
        } catch (ParseException e) {
            returnValue = null;
            e.printStackTrace();
            Log.error("Parse Exception occured " + e.getMessage());
        }
        return returnValue;
    }


    public String dateAddSub(String myDate, String returnFormat, String myFormat, int noOfDays) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(returnFormat);
        String sourceDate = myDate;
        Date mDate;

        try {
            mDate = new SimpleDateFormat(myFormat, Locale.ENGLISH).parse(sourceDate);
            String returnValue = dateFormat.format(mDate);
            System.out.println(returnValue);

            Calendar cal = Calendar.getInstance();
            cal.setTime(mDate);
            cal.add(Calendar.DATE, noOfDays);

            String myF = dateFormat.format(cal.getTime());
            return myF;


        } catch (ParseException e) {
            e.printStackTrace();
            Log.error("Parse Exception occured " + e.getMessage());
            return null;
        }

    }


    public static void ieDownloadPopupFile(String downloadPath) {
        initializeJacobs();

        autoIT = new AutoItX();
        autoIT.sleep(600);
        autoIT.winWait("Internet Explorer", "", 5000);
        autoIT.winActivate("Internet Explorer", "");

        String IEWindow = autoIT.winGetHandle("Internet Explorer", "");
        System.out.println(IEWindow);
        autoIT.winActivate(IEWindow, "");
        autoIT.sleep(2000);
        autoIT.controlFocus("Internet Explorer", "", "[TEXT:Save &as]");
        autoIT.controlClick("Internet Explorer", "", "[TEXT:Save &as]");
        autoIT.winWait("Save As");
        autoIT.winActivate("Save As");
        autoIT.controlFocus("Save As", "", "[CLASS:Edit;INSTANCE:1]");
        autoIT.sleep(1000);
        autoIT.send(downloadPath);
        autoIT.sleep(2000);
        autoIT.controlClick("Save As", "", "[TEXT:&Save]");
        autoIT.sleep(3000);

        if (autoIT.winExists("[CLASS:#32770]")) {
            autoIT.winActivate("[CLASS:#32770]");
            autoIT.send("{TAB}", false);
            autoIT.sleep(400);
            autoIT.controlClick("[CLASS:#32770]", "", "[TEXT:&Yes]");
            autoIT.sleep(1000);
        }

        String wndHandle = autoIT.winGetHandle("[Class:IEFrame]", "");
        String ieDownloadWndTitle = "[HANDLE:" + wndHandle + "]";

        autoIT.sleep(1000);
        autoIT.winActivate(ieDownloadWndTitle, "");
        autoIT.send("{F6}", false);
        autoIT.sleep(300);
        autoIT.send("{TAB}", false);
        autoIT.sleep(300);
        autoIT.send("{TAB}", false);
        autoIT.sleep(300);
        autoIT.send("{TAB}", false);


    }


    public static void ieDownloadBarFile(String downloadPath){

        initializeJacobs();

        AutoItX x = new AutoItX();
        x.sleep(600);
        String wndHandle = x.winGetHandle("[Class:IEFrame]", "");
        String ieDownloadWndTitle = "[HANDLE:" + wndHandle + "]";
        int ctrlPosX = x.controlGetPosX(ieDownloadWndTitle, "", "[Class:DirectUIHWND;INSTANCE:1]");
        int ctrlPosY = x.controlGetPosY(ieDownloadWndTitle, "", "[Class:DirectUIHWND;INSTANCE:1]");

        float color = x.pixelGetColor(ctrlPosX, ctrlPosY);
        int loopCnt = 0;

        do{
            x.winActivate(ieDownloadWndTitle, "");
            x.sleep(200);
            x.send("{TAB}");
            x.winActivate("[Class:IEFrame]", "");
            x.sleep(300);

            ctrlPosX = x.controlGetPosX(ieDownloadWndTitle, "", "[Class:DirectUIHWND;INSTANCE:1]");
            ctrlPosY = x.controlGetPosY(ieDownloadWndTitle, "", "[Class:DirectUIHWND;INSTANCE:1]");
            color = x.pixelGetColor(ctrlPosX, ctrlPosY);
            loopCnt = loopCnt+1;
            if (loopCnt>=10){
                break;
            }
        }while ((color == 0) || (loopCnt >= 10));

        x.winActivate(ieDownloadWndTitle);
        x.send("{F6}", false);
        x.sleep(500);
        x.send("{TAB}", false);
        x.sleep(500);
        x.send("{DOWN}", false);
        x.sleep(500);
        x.send("a", false);


        x.winWait("Save As");
        x.winActivate("Save As");
        x.controlFocus("Save As", "", "[Class:Edit;INSTANCE:1]");
        x.sleep(1000);
        x.send(downloadPath);
        x.sleep(2000);
        x.controlClick("Save As", "", "[TEXT:&Save]");
        x.sleep(3000);

        if(x.winExists("[Class:#32770]")){
            x.winActivate("[Class:#32770]");
            x.send("{TAB}", false);
            x.sleep(400);
            x.controlClick("[Class:#32770]", "", "[TEXT:&Yes]");
            x.sleep(1000);
        }

        x.sleep(1000);
        x.winActivate(ieDownloadWndTitle, "");
        x.send("{F6}", false);
        x.sleep(300);
        x.send("{TAB}", false);
        x.sleep(300);
        x.send("{TAB}", false);
        x.sleep(300);
        x.send("{TAB}", false);

    }



}
