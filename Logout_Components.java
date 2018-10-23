package Cobalt_BusinessComponents;

import PageObjectsAndMethods.Homepage;
import PageObjectsAndMethods.MyProfilePage;
import com.core.framework.utils.PublicVariables;
import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.NoSuchElementException;
import java.util.Properties;

public class Logout_Components {
    private static final Logger log = LogManager.getLogger(Logout_Components.class);

    MyProfilePage myProfilePage;
    Homepage homepage;

    public void logOutFromCobalt() throws Exception {
        try{
            myProfilePage = new MyProfilePage(PublicVariables.driver);
            homepage = new Homepage(PublicVariables.driver);
            System.out.println("Searching for MyProfile WebElement");
            Thread.sleep(1000);
            homepage.hoverClickOnMyProfile();

            myProfilePage.clickLogOut();

            Thread.sleep(1000);
            PublicVariables.driver.quit();
            PublicVariables.testStatus = "Passed";
            //homepage.clickLogOut();
        }catch (NoSuchElementException e){
            log.error("NoSuchElementException occured ", e.getMessage());
            PublicVariables.testStatus= "Failed";

        }
    }
}
