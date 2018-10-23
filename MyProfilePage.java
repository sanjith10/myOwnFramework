package PageObjectsAndMethods;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class MyProfilePage {
    @FindBy(xpath = "/html/body/div[2]/nav/div/user-preference-card/div/loading-aware-panel/div[2]/div[1]/div[6]/div/button[1]") //This Xpath works
    WebElement btnLogOut;



    WebDriver driver;
    public MyProfilePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void clickLogOut(){

        btnLogOut.click();
    }


}
