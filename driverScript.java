package executeEngine;

import Cobalt_BusinessComponents.Logout_Components;
import com.core.framework.utils.DataTable;
import com.core.framework.utils.PublicVariables;
import com.core.framework.utils.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class driverScript {


    private static final Logger log = LogManager.getLogger(Logout_Components.class);

    public static testSuiteForExecution testSuite;


    public static String sActionKeyword;
    public static Method method[];

    public static String sRunMode;

    public static DataTable dt;
    public static String excelFilePath;

    public static Utilities ut;
    public static String strTestDataFile, strUserRole;


    public driverScript() throws Exception {
        testSuite = new testSuiteForExecution();

        method = testSuite.getClass().getMethods();
        dt = new DataTable();
        ut = new Utilities();
    }
    @Test(priority = 1)
    @Parameters({"sTestExecution", "sTestDataFile", "userRole"})
    public void mainScriptExecutor(String sTestExecution, String sTestDataFile,String userRole) throws Exception {

strTestDataFile = sTestDataFile;
strUserRole = userRole;
excelFilePath = sTestExecution;

        dt.setExcelFile(excelFilePath);

        Properties prop = ut.loadingPropertiesFile(strTestDataFile);

        driverScript startEngine = new driverScript();
        startEngine.executeTestCase(prop, strUserRole);


    }

    private void executeTestCase(Properties prop, String strUserRole) throws InvocationTargetException, IllegalAccessException, IOException {
        int iTotalTestCases = dt.getRowCountOfTestExcel(PublicVariables.sheet_TestCases);

        System.out.println(iTotalTestCases);
        for (int iTestCase = 1; iTestCase<iTotalTestCases; iTestCase++){

            sRunMode = dt.getCellData(iTestCase,PublicVariables.col_RunMode, PublicVariables.sheet_TestCases);
            if (sRunMode.equalsIgnoreCase("Yes")){
                sActionKeyword = dt.getCellData(iTestCase, PublicVariables.col_TestCaseName, PublicVariables.sheet_TestCases);
                //executeActions(strTestDataFile, strUserRole, strBrowserType, strEnvironment);
                executeActions(prop, strUserRole);
                dt.setCellDataToExcel(iTestCase+1, PublicVariables.col_Result+1, PublicVariables.testStatus, excelFilePath);
            }else {
                PublicVariables.testStatus = "Skipped";
                dt.setCellDataToExcel(iTestCase+1, PublicVariables.col_Result+1, PublicVariables.testStatus, excelFilePath);
            }
        }
    }


private static void executeActions(Properties prop, String strUserRole) throws InvocationTargetException, IllegalAccessException {
        //String strTestDataFile, String strUserRole, String strBrowserType, String strEnvironment
        for (int i=0; i<method.length;i++){
            if (method[i].getName().equals(sActionKeyword)){
                method[i].invoke(testSuite, prop, strUserRole);


            }
        }
}


}
