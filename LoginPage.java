package PageObjectsAndMethods;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public WebDriver driver1;
    //object properties for login Page
    @FindBy(xpath = "//input[@name='username']") //"html/body/div[2]/div/div[1]/div/form/div[3]/input")
            WebElement tbUserName;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/form/div[2]")
    WebElement tbErrorMsgForUN;

    @FindBy(xpath ="//input[@name='password']")  //"html/body/div[2]/div/div[1]/div/form/div[4]/input")
            WebElement tbPassWord;

    @FindBy(xpath = "//span[text()='Sign In']") //"html/body/div[2]/div/div[1]/div/form/button")
            WebElement btnSignIn;

    @FindBy(linkText ="Forgot Password?" )
    WebElement linkForgotPassword;

    public LoginPage(WebDriver driver2){

        this.driver1= driver2;

        PageFactory.initElements(driver1, this);

    }




    public  void enterUn(String userName){
        tbUserName.sendKeys(userName);
    }

    public void enterPw(String passWord){
        tbPassWord.sendKeys(passWord);
    }

    public String getErrorMsgOnUn(){
        String actualErrorMsg = tbErrorMsgForUN.getText();
        return actualErrorMsg;
    }


    public void clickSignIn(){
        btnSignIn.click();
    }







}
