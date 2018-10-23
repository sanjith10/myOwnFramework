package PageObjectsAndMethods;

import Cobalt_BusinessComponents.Logout_Components;
import com.core.framework.utils.GUIComponents;
import com.core.framework.utils.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;



public class Homepage {

    /**
     * Common Object properties for all user Roles
     */
GUIComponents guiComponents;
    @FindBy(linkText = "Homepage")
    WebElement linkHomePage;

    @FindBy(linkText = "Reporting")
    WebElement linkReporting;

    @FindBy(linkText = "News")
    WebElement linkNews;

    @FindBy(xpath = "//button[@text()='   Recent Searches ']")
    WebElement btnRecentSearches;

    @FindBy(xpath = "//input[@placeholder='Search']")
    WebElement tbSearch;

    @FindBy(xpath = "//i[@class='fa fa-search']")
    WebElement labelSearch;

    @FindBy(xpath = "//div[@class='user-avatar ngImg ng-isolate-scope']")
    WebElement imgMyProfile;


    @FindBy(xpath= "//button[@href='#/dashboard/initiatecase']")
    WebElement btn_Initiate_A_Case;


    @FindBy(xpath = "//span[@has-user-type='BAL_ADMIN SUPER_ADMIN']")
    WebElement tabSuperAdmin;


    @FindBy(xpath = "//user-preference-card[@id='userProfile']/child::div[1]")
    WebElement iconMyProfile;
    //xpath = "//div[@class='user-avatar ngImg ng-isolate-scope' and ng-style='ngImgStyle()']", "//ng-img[@class='user-avatar large center-block' and src='user.avatar]//preceding::div[1]
   //List<WebElement> ele = iconMyProfile.findElements(By.tagName("button"));

    @FindBy(xpath = "/html/body/div[2]/nav/div/div[2]/div")//This Xpath works
    WebElement userIcon;



    //@FindBy(xpath = "//user-preference-card[@id='userProfile'")
            //WebElement iconMyProfile;

    private static final Logger log = LogManager.getLogger(Logout_Components.class);

   WebDriver driver;

    //WebElement userIcon1 = driver.findElement(By.cssSelector("div[src=user.avatar]"));
    public Homepage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void hoverOnWebElement(WebElement webElement) throws Exception {
        try {

                //WebElement element = webElement;
                ut.highlightMe(userIcon);
            new Actions(driver).moveToElement(userIcon).build().perform();
                //new Actions(driver).moveToElement(element).build().perform();
                //PublicVariables.objWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText(input))));

        }catch (NoSuchElementException e){
            log.error("NoSuchElementException occured ", e.getMessage());
        }
    }

    Utilities ut = new Utilities();


    public void hoverClickOnMyProfile() throws Exception {
        //ut.highlightMe(iconMyProfile);
        Thread.sleep(2000);
        hoverOnWebElement(userIcon);
        //hoverOnWebElement(iconMyProfile);

        //Thread.sleep(3000);
        //PublicVariables.objWait.until(ExpectedConditions.visibilityOf(iconMyProfile));
        iconMyProfile.click();

    }

    public void clickOnAdminTab(){
        tabSuperAdmin.click();
    }

    //WebElement element = iconMyProfile.findElement(By.xpath("//user-preference-card[@id='userProfile']/child::div[1]/loading-aware-panel[1]/div[2]/div[1]/div[5]/div[1]/button[1]"));




}
