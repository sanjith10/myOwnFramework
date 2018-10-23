package Cobalt_BusinessComponents;


import PageObjectsAndMethods.LoginPage;
import com.core.framework.utils.*;
import com.core.framework.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.util.NoSuchElementException;

public class Login_Components extends DriverFactory{
    private static final Logger log = LogManager.getLogger(Login_Components.class);

    LoginPage loginPage;
    DataTable dt1 = new DataTable();
    Database db1 = new Database("SQLServer", "jdbc:sqlserver://dev1-sqldev1.baldev.local:1433;databaseName=IMS;user=service.ims;password=imsassist_123;","dbo", "service.ims","imsassist_123");


    //"jdbc:sqlserver://<server>:<port>;databaseName=AdventureWorks;user=<user>;password=<password>";
    public Login_Components() throws Exception {

    }

    public void loginToCobalt(String userName, String passWord, String browserType, String uRl) throws Exception {
        try {
            DriverFactory.initializeDriver(browserType, uRl, true);
            loginPage = new LoginPage(PublicVariables.driver);

            Thread.sleep(4000);
            loginPage.enterUn(userName);
            //HtmlReporter.Log("enterUn", userName+" User name has been entered", "Pass");
            loginPage.clickSignIn();
            HtmlReporter.Log("clickSignIn", " Entered Username and Clicked on SignIn button", "Pass");
            Thread.sleep(4000);

            String expErrorMessage = "Please check your credentials!";
            String actualMessage =loginPage.getErrorMsgOnUn();
            System.out.println(actualMessage);

            //Assert.assertEquals(actualMessage.trim(), expErrorMessage);
            if (actualMessage.equalsIgnoreCase(expErrorMessage)) {
                log.info("'" + userName + "' Username doesn't exist in database, please provide valid one");
                PublicVariables.testStatus= "Failed";
            }else {
                loginPage.enterPw(passWord);
                //HtmlReporter.Log("enterPw", passWord+" Password has been entered", "Pass");
                loginPage.clickSignIn();
                HtmlReporter.Log("clickSignIn", " Entered Password and clicked on SignIn again ", "Pass");
                Thread.sleep(4000);
                // HtmlReporter.Log("clickSignIn", " User successfully logged into Cobalt ", "Pass");
                //Thread.sleep(2000);
                PublicVariables.testStatus = "Passed";
            }

        }catch (NoSuchElementException e){
            log.info("NoSuchElementException occured", e.getMessage());

            //Assert.assertTrue(false,"Please check your credentials!");
            PublicVariables.testStatus= "Failed";

        }
    }



    public void loginUsingInvalidUn(String userName,String browserType, String uRl) throws Exception {

        try {
            DriverFactory.initializeDriver(browserType, uRl, true);
            loginPage = new LoginPage(PublicVariables.driver);
            Thread.sleep(2000);
          loginPage.enterUn(userName);
            Thread.sleep(1000);
            HtmlReporter.Log("enterUn", userName+" User name has been entered", "Pass");
            loginPage.clickSignIn();
            Thread.sleep(2000);


            String expErrorMessage = "Please check your credentials!";
            String actualMessage =loginPage.getErrorMsgOnUn();

            System.out.println(actualMessage);
            Assert.assertEquals(actualMessage.trim(), expErrorMessage);
            log.info("'"+userName+"' Username doesn't exist in database, please provide valid one");

            //HtmlReporter.Log("clickSignIn", " Entered Username and Clicked on SignIn button", "Pass");
            //Thread.sleep(4000);

        }catch (NoSuchElementException e){
            log.info("NoSuchElementException occured", e.getMessage());

        }
    }


    //?user=service.ims&password=imsassist_123&useUnicode=true&characterEncoding=UTF-8
    //"jdbc:mysql://127.0.0.1:3306/IMS"
    //:3306, 13.0.1745.2

    public Database getDb1() {
        return db1;

    }

   @Test(priority = 1)
    public void eq1() throws Exception {

         db1.executeQuery("Select CompanyId  from dbo.Company where CompanyName like 'Google%' and CompanyNumber = 1");
    }

    /**public void getdt1() throws IOException, InvalidFormatException {
        dt1.createWorkBook("D:\\wb5.xlsx", "mySheetAnu");
    }*/
Utilities ut = new Utilities();
@Test(priority = 1)
   public void encryptPW(){
       ut.encryptPassword("");
   }

    //[B@71238fc2
    @Test(priority = 1)
   public void decryptPW(){
    ut.decryptEncodedPassword("");
    }
}
