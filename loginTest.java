package LoginAndLogoutTests;

import Cobalt_BusinessComponents.Login_Components;
import PageObjectsAndMethods.LoginPage;
import com.core.framework.utils.DriverFactory;
import com.core.framework.utils.HtmlReporter;
import com.core.framework.utils.PublicVariables;
import com.core.framework.utils.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.Properties;

public class loginTest{


    Login_Components login_components = new Login_Components();
    Utilities ut = new Utilities();
    String userName, password, sEnvironment, browserType;


    public loginTest() throws Exception {
    }

    private static final Logger log = LogManager.getLogger(loginTest.class);


    public void verifyLogin(Properties prop, String userRole) throws Exception {
        String strUserNameAndPassword = ut.ParseUsernameAndPassword(prop, userRole);
        String[] arrayUserNameAndPassword = strUserNameAndPassword.split(";");

         userName = arrayUserNameAndPassword[0];
         password = arrayUserNameAndPassword[1];

         String strEnvAndBrowser = ut.parseBrowserEnvProperties(prop, userRole);
         String[] arrayEnvAndBrowserType = strEnvAndBrowser.split(";");
         sEnvironment = arrayEnvAndBrowserType[0];
         browserType = arrayEnvAndBrowserType[1];

        System.out.println("\n========== Starting Login Verification Test for '" + userRole + "' ==========");
        System.out.println("Environment = " + sEnvironment);
        System.out.println("Username = " + userName);
        System.out.println("User Role = " + userRole);
        login_components.loginToCobalt(userName, password, browserType, sEnvironment);
        System.out.println("========== Ending Login Verification Test for '" + userRole + "' ==========");


    }






    @Test(priority = 1)
    @Parameters({"userName", "browserType", "uRl"})
    public void verifyLoginUsingInvalidUn(Properties prop, String userRole) throws Exception {
        String strUserNameAndPassword = ut.ParseUsernameAndPassword(prop, userRole);
        String[] arrayUserNameAndPassword = strUserNameAndPassword.split(";");

        userName = arrayUserNameAndPassword[0];
        password = arrayUserNameAndPassword[1];

        String strEnvAndBrowser = ut.parseBrowserEnvProperties(prop, userRole);
        String[] arrayEnvAndBrowserType = strEnvAndBrowser.split(";");
        sEnvironment = arrayEnvAndBrowserType[0];
        browserType = arrayEnvAndBrowserType[1];

        System.out.println("\n========== Starting Login Verification Test for '" + userRole + "' ==========");
        System.out.println("Environment = " + sEnvironment);
        System.out.println("Username = " + userName);
        System.out.println("User Role = " + userRole);
        login_components.loginUsingInvalidUn(userName, browserType, sEnvironment);
    }


}
