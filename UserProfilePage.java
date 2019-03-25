package PageObjectsAndMethods;

import com.core.framework.utils.GUIComponents;
import com.core.framework.utils.PublicVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.core.framework.utils.DriverFactory;

public class UserProfilePage extends GUIComponents {

    public UserProfilePage(WebDriver driver){
        super((driver));
        PageFactory.initElements(driver, this);
    }
    //div[contains(@class,"user-profile")]//a[contains(.,'Immigration Documents')]
    @FindBy(xpath = "//div[contains(@class,'user-profile')]//a[contains(.,'Immigration Documents')]")
    private WebElement tabUserProfileImmigrationDocuments;

    @FindBy(xpath="//div[contains(@class,\"user-profile\")]//a[contains(.,'Profile')]")
    public WebElement tabProfile;

    @FindBy(xpath = "//button[@type='button' and contains(text(),'New Immigration Document')]")
    private WebElement btnNewImmigrationDocument;

    @FindBy(xpath = "//label[contains(text(),'Issued To')]/following-sibling::div[1]")
    private WebElement slbIssueTo;

    @FindBy(xpath = "//label[contains(text(),'Country')]/following-sibling::div[1]")
    private WebElement slbCountry;

    @FindBy(xpath = "//label[contains(text(),'Classification')]/following-sibling::div[1]")
    private WebElement slbClassification;

    @FindBy(xpath = "//label[contains(text(),'Immigration Document Type')]/following-sibling::div[1]")
    private WebElement slbImmigrationDocumentType;

    @FindBy(xpath = "//label[contains(text(),'Immigration Document Assistant Link to Case')]/following-sibling::div[1]")
    private WebElement slbImmigrationDocumentAssistantLinktoCase;

    @FindBy(xpath = "//input[@name='isInactive']")
    private WebElement chkboxInactive;

    @FindBy(xpath = "//button[starts-with(@ng-click,'uploadDocument()')]")
    private WebElement btnAddDocuments;

    @FindBy(xpath = "//button[starts-with(@ng-click-async,'submitDocument()')]")
    private WebElement btnSubmit;

    @FindBy(xpath = "//div[@id='DocumentTypeAndUpload']")
    private WebElement txtDocumentTypeAndUpload;

    @FindBy(xpath = "//body[starts-with(@class,'modal-open')]//span[contains(text(),'Browse Files')]")
    private WebElement btnBrowseFiles;

    @FindBy(xpath = "//button[@type='submit' and text()='Next']")
    private WebElement btnNextinDocument;

    @FindBy(xpath = "//input[@ng-model='documentToUpload.name']")
    private WebElement txtDocumentName;

    @FindBy(xpath = "//body[starts-with(@class,'modal-open')]//button[@type='submit']")
    private WebElement btnUpload;

    @FindBy(xpath = "//ul[starts-with(@class,'nav navbar-nav')]//a[contains(.,'Immigration Documents')]")
    private WebElement tabImmigrationDocuments;

    @FindBy(xpath = "//button[@type='submit' and contains(.,'OK')]")
    private WebElement btnPopupOK;

    @FindBy(xpath = "//i[starts-with(@class,'fa fa-angle-down fa-lg light')]")
    private WebElement btnshowedDetail;

    @FindBy(xpath = "//body[starts-with(@class,'modal-open')]//section[@class='inplaceLoader']")
    private WebElement frameLoader;

    public WebElement getTabUserProfileImmigrationDocuments() { return tabUserProfileImmigrationDocuments; }

    public WebElement getbtnNewImmigrationDocument(){ return btnNewImmigrationDocument; }

    public WebElement getslbIssueTo(){ return slbIssueTo; }

    public WebElement getslbCountry(){ return slbCountry;}

    public WebElement getslbClassification() {  return slbClassification;  }

    public WebElement getslbImmigrationDocumentType(){ return slbImmigrationDocumentType; }

    public WebElement getslbImmigrationDocumentAssistantLinktoCase() { return slbImmigrationDocumentAssistantLinktoCase; }

    public WebElement getchkboxInactive(){ return chkboxInactive; }

    public WebElement getbtnAddDocuments(){ return btnAddDocuments; }

    public WebElement getbtnSubmit() { return btnSubmit;  }

    public WebElement gettxtDocumentTypeAndUpload() { return txtDocumentTypeAndUpload;  }

    public WebElement getbtnUpload() {return btnUpload;}

    public WebElement getTabImmigrationDocuments(){return tabImmigrationDocuments;}

    public WebElement getBtnPopupOK(){return btnPopupOK;}

    public void selectFieldBox(WebElement element, String field)throws Exception{
        if(!field.equals("")) {
            clickElement(element, PublicVariables.objTimeOut);
            By txtIssueTo = By.xpath("//div[contains(text(),'" + field + "')]");
            waitForElementClickable(txtIssueTo, PublicVariables.objTimeOut);
            DriverFactory.getDriver().findElement(txtIssueTo).click();
        }
    }

    public void clickoptionDocument(String classification, String documentType, String option) throws Exception{
        By gearIcon = By.xpath("//p[contains(text(),'" + classification + "')]/../following-sibling::div/p[contains(text(),'" + documentType + "')]/../following-sibling::div//i[@id='document-options']");
        waitForElementClickable(gearIcon, PublicVariables.objTimeOut);
        DriverFactory.getDriver().findElement(gearIcon).click();
//p[contains(text(),'Advance Parole (AP)')]/../following-sibling::div/p[contains(text(),'Entry Visa')]/../following-sibling::div//i[@id='document-options']/following-sibling::ul/li[text()='Edit']
        By optionbutton = By.xpath("//p[contains(text(),'" + classification + "')]/../following-sibling::div/p[contains(text(),'" + documentType + "')]/../following-sibling::div//i[@id='document-options' and @aria-expanded='true']/following-sibling::ul/li[contains(text(),'" + option + "')]");
        waitForElementClickable(optionbutton, PublicVariables.objTimeOut);
        DriverFactory.getDriver().findElement(optionbutton).click();
    }

    public void uploadDocument(String filePath, String documentName) throws Exception{
        clickElement(btnAddDocuments,PublicVariables.objTimeOut);
        waitForElementClickable(btnBrowseFiles,PublicVariables.objTimeOut);
        waitforElementHasStop(btnBrowseFiles,PublicVariables.objTimeOut);
        clickElement(btnBrowseFiles,PublicVariables.objTimeOut);
//        Utilities.uploadFileDocument(filePath);
        Runtime.getRuntime().exec(PublicVariables.AUTOIT_PATH + "fileupload.exe " + PublicVariables.DATA_PATH + filePath);
        clickElement(btnNextinDocument,PublicVariables.objTimeOut);
        sendkeyToElement(txtDocumentName,documentName,PublicVariables.objTimeOut);
        clickElement(btnUpload,PublicVariables.objTimeOut);
    }

    public void clickbtnShowImmigrationDetail(String classification, String documentType)throws Exception{
        By showDetail = By.xpath("//p[contains(text(),'" + classification + "')]/../following-sibling::div/p[contains(text(),'" + documentType + "')]/../following-sibling::div/i[starts-with(@ng-click,'toggleImmigrationDetail(immigrationDocument)')]");
        waitForElementClickable(showDetail,PublicVariables.objTimeOut);
        DriverFactory.getDriver().findElement(showDetail).click();
    }

    public boolean isImmigrationDocumentDisplay(String documentName)throws Exception{
        By docName = By.xpath("//span[starts-with(@class,'ng-binding ng-scope') and contains(.,'" + documentName + "')]");
        waitForElementLoad(btnshowedDetail,PublicVariables.objTimeOut);
        if(DriverFactory.getDriver().findElements(docName).size()!=0) return true;
        else return false;
    }

    public void clickOptionDocumentUpload(String documentName, String option)throws Exception{
        By gearIcon = By.xpath("//div[@id='DocumentTypeAndUpload']//li[starts-with(@class,'list-group-item ng-scope')]//div[contains(text(),'" + documentName + "')]/following-sibling::div//button[starts-with(@type,'button')]");
        waitForElementClickable(gearIcon,PublicVariables.objTimeOut);
        DriverFactory.getDriver().findElement(gearIcon).click();

        By editButon = By.xpath("//div[@id='DocumentTypeAndUpload']//li[starts-with(@class,'list-group-item ng-scope')]//div[contains(text(),'TestC6180')]/following-sibling::div//button[@type='button' and @aria-expanded='true']/following-sibling::ul/li[contains(text(),'" + option + "')]");
        waitForElementClickable(editButon,PublicVariables.objTimeOut);
        DriverFactory.getDriver().findElement(editButon).click();
    }

    public void selectChkboxDocumentVisibility(String userType)throws Exception{
        if(!userType.equals("")) {
            By checkBox = By.xpath("//body[starts-with(@class,'modal-open')]//label[contains(.,'" + userType + "')]/input[@type='checkbox']");
            waitUntilElementNotDisplayed(frameLoader, 10);
            waitForElementClickable(checkBox, PublicVariables.objTimeOut);
            DriverFactory.getDriver().findElement(checkBox).click();
        }
    }

}
