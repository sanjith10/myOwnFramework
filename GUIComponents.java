package com.core.framework.utils;

import com.google.common.base.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import static com.core.framework.utils.HtmlReporter.*;

@SuppressWarnings("unused")
public class GUIComponents extends PublicVariables {

    private static final Logger Log = LogManager.getLogger(DriverFactory.class);
    public static boolean bResultFlag = false;
    public static String sBCDescription = "***";
    public static boolean bObjectValidation = false;
    Utilities utils = new Utilities();


    public GUIComponents(WebDriver driver) {
        GUIComponents.driver = DriverFactory.driver;
        bResultFlag = HtmlReporter.bResultFlag;
        sBCDescription = HtmlReporter.sBCDescription;
    }


    @SuppressWarnings("static-access")
    public static void switchFrames(WebDriver currDriver, String[] frames) throws Exception {

        try {
            Thread.sleep(1000);
            currDriver.switchTo().defaultContent();

            if (frames.length == 0) {
                reporter.Log("switchFrames", "No frame to switch", "pass");
                return;
            }

            if (frames.length == 1) {
                currDriver.switchTo().frame(frames[0]);
            } else {
                for (String frm : frames) {
                    Thread.sleep(600);

                    try {
                        currDriver.switchTo().frame(frm);
                    } catch (NoSuchFrameException e) {
                        Log.error("NoSuchFrameException occured" + frm + " -- " + e.getMessage());
                    }
                }
            }

        } catch (NoSuchElementException e) {
            Log("switchFrames", "Switch Frames got an exception", "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "switch frames got failed");
        }

    }


    public static boolean waitForElementLoad(WebElement element, int timeout) throws Exception {

        boolean flag = false;

        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (NoSuchElementException e) {
            flag = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
            Log("waitForElementLoad", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }

        wait = null;
        return flag;
    }

    public static void setbObjectValidation(boolean bObjectValidation) {
        GUIComponents.bObjectValidation = bObjectValidation;
    }


    public boolean isExists(WebElement pageFactoryElement, int timeout) throws Exception {
        boolean isPresent = false;


        if (GUIComponents.waitForElementLoad(pageFactoryElement, timeout)) {

            if (pageFactoryElement.isDisplayed())
                isPresent = true;
        } else {
            isPresent = false;
        }
        return isPresent;


    }


    public static boolean waitForElementPresent(WebElement element, int timeout) {

        boolean flag = false;
        final String eleLocatorString = DriverFactory.getPageFactoryLocatorString(element);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(eleLocatorString)));
            flag = true;

        } catch (java.util.NoSuchElementException e) {
            flag = false;
            Log.error("NoSuchElementExcepetion occured" + e.getMessage());
        }

        wait = null;
        return flag;
    }


    /**
     * Validates the element displayed or not
     *
     * @param pageFactoryElement
     * @param timeout
     * @return
     */

    public static boolean isDisplayed(WebElement pageFactoryElement, int timeout) {
        boolean isPresent = false;

        if (waitForElementPresent(pageFactoryElement, timeout)) {
            if (pageFactoryElement.isDisplayed()) isPresent = true;
        } else {
            isPresent = false;
        }

        return isPresent;
    }


    public boolean isExists(WebElement pageFactoryElement) throws Exception {
        boolean isPresent = false;


        if (GUIComponents.waitForElementLoad(pageFactoryElement, 2)) {

            if (pageFactoryElement.isDisplayed()) isPresent = true;

        } else {
            isPresent = false;
        }

        return isPresent;
    }


    public static void validateTextEquals(WebElement element, String ExpectedText) throws Exception {
        StartBC();

        String textVal = element.getText();
        System.out.println(textVal);

        if (textVal.contentEquals(ExpectedText)) {
            Log("ValidateTextEquals", "Validate if text displays equals the expected value", "pass");
        } else {
            Log("ValidateTextEquals", "Validate if text displays equals the expected value", "Fail");
            Assert.assertTrue(false, "Validate text displayed does not equals the expected value which is not expected");

        }

    }


    public int getRowCount(WebElement pageFactory_webTable) throws Exception {
        final String webTable1 = DriverFactory.getPageFactoryLocatorString(pageFactory_webTable);
        int output = -1;

        try {
            if (webTable1.contains("//")) {
                if (driver.findElement(By.xpath(webTable1)).isDisplayed()) {
                    List<WebElement> rows = driver.findElements(By.xpath(webTable1 + "/tbody/tr"));
                    if ((rows.size()) != 0) {
                        output = rows.size() - 1;

                    } else {
                        output = -1;
                    }

                } else {
                    if (driver.findElement(By.id(webTable1)).isDisplayed()) {
                        String convToXpath = "//table[@id='" + webTable1 + "']";
                        List<WebElement> rows = driver.findElements(By.xpath(convToXpath + "/tbody/tr"));
                        if ((rows.size()) != 0) {
                            output = rows.size() - 1;

                        }
                    } else {
                        output = -1;
                    }
                }
            }
        } catch (NoSuchElementException e) {

            Log("getRowCount", "Get Row Count from WebTable: " + pageFactory_webTable.toString(), "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Get Row Count of WebTable got failed");
        }
        return output;

    }


    public static void removeFocus(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].focus();arguments[0].blur(); return true", element);

    }


    public int getRowCount(WebElement webTable, int... tbody) throws Exception {

        try {
            final WebElement webTable1 = webTable;
            int output;
            if (webTable1.isDisplayed()) {
                if (tbody.length == 0) {
                    output = webTable1.findElements(By.xpath("//tbody[1]/tr")).size();
                } else {
                    output = webTable1.findElements(By.xpath("//tbody[" + tbody[0] + "]/tr")).size();
                }
            } else {
                output = -1;
            }
            return output;


        } catch (NoSuchElementException e) {
            Log("getRowCount", "Get Row Count from WebTable: " + webTable.toString(), "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Get Row Count of WebTable got failed");
            return -1;
        }


    }

    /*public boolean clickWebTableColumnHeader(WebElement webTable, String colName){

        try{


        }
        }*/

    public boolean clickWebTableColumnHeader(WebElement webTable, String colName) throws Exception {


        try {

            int colIndex = GUIComponents.getColumnIndexFromWebTable(webTable, colName);

            if (colIndex == 0) {

                Log.info("Failed in clickWebTableColumnHeader--- Column Index returned 0");
                return false;


            }

            try {
                WebElement cell = webTable.findElement(By.xpath("./tbody[1]/tr[" + 1 + "]")).findElement(By.xpath("./th[" + colIndex + "]"));
                utils.highlightMe(cell);
                Thread.sleep(400);
                cell.click();
                Thread.sleep(400);
                return true;
            } catch (InterruptedException e) {
                Log.info("Failed in clickWebTableColumnHeader--- " + e.getMessage());
                HtmlReporter.Log("clickWebTableColumnHeader", "Failed to click WebTable Header column of table: " + webTable.toString(), "fail");
                Assert.assertTrue(false, "Failed to click webTable header column");
                return false;
            }

        } catch (IOException e) {
            Log.info("Failed in clickWebTableColumnHeader--- " + e.getMessage());
            HtmlReporter.Log("clickWebTableColumnHeader", "Failed to click WebTable Header column of table: " + webTable.toString(), "fail");
            Assert.assertTrue(false, "Failed to click webTable header column");
            return false;
        }


    }


    /**
     * setting the given input value using selenium type method
     *
     * @param webElement
     * @param input
     * @throws Exception
     */
    public void set(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("Set", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", Set, " + input);
                try {
                    if (isExists(webElement)) {

                        selenium.type(elementIdentifierString, input);
                        //webElement.sendKeys(op);
                        //webElement.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT, Keys.END), input);
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("Set", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.Log("Set", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.Log("Set", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }

        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.Log("Set", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");
        }


    }

    /**
     * Setting Secure Password using selenium type method
     *
     * @param webElement
     * @param encryptedPassword
     * @throws Exception
     */

    @SuppressWarnings({"static-access", "deprecation"})
    public void setSecurePassword(WebElement webElement, String encryptedPassword) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                try {
                    if (isExists(webElement)) {
                        byte[] decryptedPassword = utils.decryptEncodedPassword(encryptedPassword);
                        String op = new String(decryptedPassword);
                        selenium.type(elementIdentifierString, op);
                        //webElement.sendKeys(op);
                        //                        //webElement.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT, Keys.END), op);
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.Log("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.Log("setSecurePassword", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }

        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.Log("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");
        }

    }

    /**
     * Entering the encrypted password using senKeys method
     *
     * @param webElement
     * @param encryptedPassword
     * @throws Exception
     */
    public void typeSecurePassword(WebElement webElement, String encryptedPassword) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                try {
                    if (isExists(webElement)) {
                        byte[] decryptedPassword = utils.decryptEncodedPassword(encryptedPassword);
                        String op = new String(decryptedPassword);

                        //webElement.sendKeys(op);
                        webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), op);
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.Log("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.Log("setSecurePassword", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }

        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.Log("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");
        }


    }

    /**
     * Entering the input using Selenium sendKeys method
     *
     * @param webElement
     * @param input
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public void type(WebElement webElement, String input) throws Exception {


        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("type", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", type, " + input);

                try {
                    if (isExists((webElement))) {
                        webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), input);
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("type", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("type", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("type", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("type", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void textAreaType(WebElement webElement, String input) throws Exception {

        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("textAreaType", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", textAreaType, " + input);

                try {
                    if (isExists((webElement))) {
                        webElement.click();
                        webElement.sendKeys(input);
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("textAreaType", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("textAreaType", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("textAreaType", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("textAreaType", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }


    /**
     * @param webElement
     * @param input
     * @throws Exception
     */
    public void typeNotNull(WebElement webElement, String input) throws Exception {

        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("typeNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", typeNotNull, " + input);

                try {
                    if (isExists((webElement))) {
                        if (webElement.getText().isEmpty()) {
                            //webElement.click();
                            webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), input);
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("typeNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                            }
                        }


                    } else {
                        HtmlReporter.Log("typeNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("typeNotNull", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("typeNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }
    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void select(WebElement webElement, String input) throws Exception {

        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("select", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", select, " + input);

                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);
                        dropdown.selectByVisibleText(input);


                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("select", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("select", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("select", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("select", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }
    }


    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void selectNotNull(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("selectNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectNotNull, " + input);

                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);
                        WebElement option = dropdown.getFirstSelectedOption();
                        if (option.getText().isEmpty()) {
                            dropdown.selectByVisibleText(input);


                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("selectNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                            }

                        }

                    } else {
                        HtmlReporter.Log("selectNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("selectNotNull", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("selectNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void selectByIndex(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("selectByIndex", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectByIndex, " + input);

                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);

                        dropdown.selectByIndex(Integer.parseInt(input));


                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("selectByIndex", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("selectByIndex", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("selectByIndex", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("selectByIndex", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }


    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */
    public void selectByInstring(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("selectByInstring", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectByInstring, " + input);

                try {
                    if (isExists((webElement))) {

                        String sListOption;
                        Boolean blnFlag = false;
                        Select dropdown = new Select(webElement);
                        List<WebElement> allOptions = webElement.findElements(By.tagName("option"));
                        int j = allOptions.size();
                        for (int i = 0; i < j; i++) {
                            sListOption = allOptions.get(i).getText();
                            if (sListOption.contains(input)) {
                                dropdown.selectByVisibleText(sListOption);
                                if (detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.Log("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                                }
                                blnFlag = true;
                                break;
                            }
                        }


                        if (!blnFlag) {
                            HtmlReporter.Log("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                            Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                        }


                    } else {
                        HtmlReporter.Log("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("selectByInstring", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("selectByInstring", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void checkBoxOnOff(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("checkBoxOnOff", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectByIndex, " + input);

                try {
                    if (isExists((webElement))) {
                        if (input.toString().toLowerCase().equalsIgnoreCase("on")) {

                            if (webElement.isSelected()) {

                                webElement.click();

                                if (detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.Log("checkBox On", "CheckBox on is Successful'" + elementIdentifierString + "' ", "Pass");
                                }

                            }

                        } else if (input.toString().toLowerCase().equalsIgnoreCase("off")) {

                            if (webElement.isSelected()) {

                                webElement.click();
                                HtmlReporter.Log("checkBox Off ", "CheckBox off is Successful '" + elementIdentifierString + "' ", "Pass");
                            }
                        }
                    } else {

                        HtmlReporter.Log("checkBoxOnOff", "Object doesn't exists  -->'" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists -->'" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("checkBoxOnOff", "Object click operation failed >>'" + elementIdentifierString + "' >>Error: ", "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object click operation failed >>'" + elementIdentifierString + "' >>Error: ");
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("checkBoxOnOff", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }

    /**
     * @param webElement
     * @throws Exception
     */

    public void click(WebElement webElement) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("click", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", click, ");

                try {
                    if (isExists((webElement))) {
                        webElement.click();


                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("click", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("click", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("click", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("click", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }

    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */


    public void actionClick(WebElement webElement, String[]... input) throws Exception {

        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("actionClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", actionClick, ");

                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(driver).click(element).perform();

                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("actionClick", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("actionClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("actionClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("actionClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }


    }

    /**
     * @param webElement
     * @throws Exception
     */

    public void actionDoubleClick(WebElement webElement) throws Exception {

        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("actionDoubleClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", actionDoubleClick, ");

                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(driver).doubleClick(element).perform();

                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("actionDoubleClick", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("actionDoubleClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("actionDoubleClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("actionDoubleClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }


    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void hoverClick(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("hoverClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", hoverClick, " + input);

                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(driver).moveToElement(element).build().perform();
                        objWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText(input))));
                        driver.findElement(By.linkText(input)).click();

                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("hoverClick", "hover Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("hoverClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("hoverClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("hoverClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }


    }

    /**
     * @param webElement
     * @param input
     * @throws Exception
     */

    public void hover(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.Log("hover", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");


            } else {

                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", hover, " + input);

                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(driver).moveToElement(element).build().perform();
                        objWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText(input))));
                        driver.findElement(By.linkText(input)).click();

                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("hover", "hover Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }


                    } else {
                        HtmlReporter.Log("hover", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.Log("hover", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.Log("hover", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
        }


    }


    public void PerformAction(String objectNameInUi, WebElement oWebElement, String sAction, String sData) {

        //GUIComponents.waitFor
    }

    /**
     * Getting Column Position from web Table using Column Name
     *
     * @param webTable
     * @param columnName
     * @return
     * @throws Exception
     */

    public static Integer getColumnIndexFromWebTable(WebElement webTable, String columnName) throws Exception {
        Integer colPosition = 0;
        try {
            List<WebElement> th = webTable.findElements(By.xpath("./*/tr/th"));

            for (int column = 0; column < th.size(); column++) {
                if (columnName.equalsIgnoreCase(th.get(column).getText().trim())) {
                    colPosition = column + 1;

                    if (detailedReport.equalsIgnoreCase("yes")) {
                        HtmlReporter.Log("getColumnIndexFromWebTable", "column index :" + colPosition + " -->from webTable " + webTable.toString() + " based on Column Name : " + columnName, "Pass");
                    }
                    Log.info("Column position of '" + columnName + "' is: " + colPosition);
                    break;
                }
            }


        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getColumnIndexFromWebTable", "failed to get column index :" + colPosition + " -->from webTable " + webTable.toString() + " based on Column Name : " + columnName, "Fail");
            Assert.assertTrue(false, "Failed to get column index from webTable :" + webTable.toString());
        }

        return colPosition;
    }

    /**
     * @param objectNameOnUi
     * @param sTableUniqueName
     * @param sRowName
     * @param sColIndex
     * @param sPropertyToVerify
     * @param sExpectedValue
     * @throws Exception
     */


    public void performActionTableObjects(String objectNameOnUi, String sTableUniqueName, String sRowName, String sColIndex, String sPropertyToVerify, String sExpectedValue) throws Exception {
        String sListReportDesc = "";
        WebElement element = null;

        try {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            sPropertyToVerify = sPropertyToVerify.toUpperCase();

            switch (sPropertyToVerify) {
                case "CLICK_PRECEDING":
                    try {
                        element = driver.findElement(By.xpath(".//table[contains(@id,'" + sTableUniqueName + "')]//td[starts-with(text(),'" + sRowName + "')]/preceding-sibling::td[" + sColIndex + "]/a"));
                        element.click();
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("performAction", "Click the Object '" + objectNameOnUi + "'", "Pass");
                        }
                    } catch (NoSuchElementException e) {
                        Log.error("NoSuchElementException occured " + e.getMessage());
                        HtmlReporter.Log("performAction", "Click the Object '" + objectNameOnUi + "'", "Fail");
                        Assert.assertTrue(false, "Click the Object '" + objectNameOnUi + "' is failed");

                    }

                    break;

                case "CLICK_FOLLOWING":

                    try {
                        element = driver.findElement(By.xpath(".//table[contains(@id,'" + sTableUniqueName + "')]//td[starts-with(text(),'" + sRowName + "')]/following-sibling::td[" + sColIndex + "]/a"));
                        element.click();
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("performAction", "Click the Object '" + objectNameOnUi + "'", "Pass");
                        }
                    } catch (NoSuchElementException e) {
                        Log.error("NoSuchElementException occured " + e.getMessage());
                        HtmlReporter.Log("performAction", "Click the Object '" + objectNameOnUi + "'", "Fail");
                        Assert.assertTrue(false, "Click the Object '" + objectNameOnUi + "' is failed");

                    }

                    break;

                default:
                    HtmlReporter.Log("performAction", "Option not Available  '" + objectNameOnUi + "'", "Fail");
                    break;

            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("performAction", "Validating the Object '" + objectNameOnUi + "' is" + sPropertyToVerify + " failed, Error Message: " + e.getMessage() + "<br><br>" + sListReportDesc + "</table>", "Fail");
            Assert.assertTrue(false, "Exception Occured, Click on '" + objectNameOnUi + "' is failed");
        }
    }

    /**
     * @param webElement
     * @return
     * @throws Exception
     */


    public String getSelectedListValue(WebElement webElement) throws Exception {
        try {
            Select select = new Select(webElement);
            String selectedValue = select.getFirstSelectedOption().getText();
            return selectedValue;
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getSelectedListValue", "Failed to capture the selected value from the list box", "Fail");
            Assert.assertTrue(false, "Failed to capture the selected value from the list box");

        }
        return null;
    }

    /**
     * @param webTable
     * @param columnName
     * @return
     * @throws Exception
     */

    public static List<String> getColumnValuesFromWebTable(WebElement webTable, String columnName) throws Exception {
        List<String> returnColumnData = new ArrayList<>();
        WebElement table = null;
        int colPosition = 0;
        try {
            colPosition = getColumnIndexFromWebTable(webTable, columnName);
            if (colPosition == 0) {
                Log.info("No Column with Name: '" + columnName + "' found in the Web Table " + webTable);
                return returnColumnData;
            }

            List<WebElement> tBodies = webTable.findElements(By.tagName("tbody"));
            if (tBodies != null) {
                table = tBodies.get(0);
            } else {
                table = webTable;

            }

            List<WebElement> columnsData = table.findElements(By.xpath("./tr/td[" + colPosition + "]"));
            for (WebElement e : columnsData) {
                returnColumnData.add(e.getText());
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getColumnValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to capture the column values from the Web table: " + webTable.toString());
        }
        Log.info("Column Data: " + returnColumnData);
        return returnColumnData;
    }

    /**
     * @param webTable
     * @param colNames
     * @return
     * @throws Exception
     */

    public boolean validateColumnNamesInWebTable(WebElement webTable, List<String> colNames) throws Exception {
        boolean actOutput = false;
        String colValue = null;
        try {
            ListIterator<String> iterator = colNames.listIterator();

            while (iterator.hasNext()) {
                colValue = iterator.next();
                if (GUIComponents.getColumnIndexFromWebTable(webTable, iterator.next()) != 0) {
                    actOutput = true;
                } else {
                    Log.info("Column Name :" + colValue + " is not available in webTable");
                    actOutput = false;
                    break;
                }

            }
            return actOutput;


        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("validateColumnNamesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to validate the column Names from the Web table: " + webTable.toString());
        }
        return actOutput;
    }


    /**
     * @param webTable
     * @param returnColName
     * @param dependentColName
     * @param dependentColVal
     * @return
     * @throws Exception
     */


    public static String getCellValueFromWebTable(WebElement webTable, String returnColName, String dependentColName, String dependentColVal) throws Exception {

        String cellValue = null;
        List<String> dependentColData = new ArrayList<>();
        List<String> returnColData = new ArrayList<>();
        int rowIndex = 0;

        try {
            dependentColData = getColumnValuesFromWebTable(webTable, dependentColName);
            returnColData = getColumnValuesFromWebTable(webTable, returnColName);

            rowIndex = dependentColData.indexOf(dependentColVal);
            if (rowIndex >= 0) {
                cellValue = returnColData.get(rowIndex);
                return cellValue;
            } else {
                Log.info("Failed in getCellValueFromWebTable---");
                Log.info("Dependent Cell Value: '" + dependentColName + "' has no cell with value: '" + dependentColVal + "'");
                return cellValue;
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getCellValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Cell Values from the Web table: " + webTable.toString());
        }
        return cellValue;
    }


    /**
     * @param webTable
     * @param returnColName
     * @param row
     * @return
     * @throws Exception
     */


    public static String getCellValueFromWebTable(WebElement webTable, String returnColName, int row) throws Exception {
        String cellValue = null;
        int rowIndex = 0;
        List<String> returnColumnData = new ArrayList<>();
        try {

            returnColumnData = getColumnValuesFromWebTable(webTable, returnColName);
            rowIndex = row;
            if (rowIndex >= 0) {
                cellValue = returnColumnData.get(rowIndex);
                return cellValue;
            } else {
                Log.info("Failed in getCellValueFromWebTable---");
                //Log.info("Dependent Cell Value: '"+dependentColName+"' has no cell with value: '"+dependentColVal+"'");
                return cellValue;
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getCellValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Cell Values from the Web table: " + webTable.toString());
        }
        return cellValue;

    }

    /**
     * @param webTable
     * @param rowIndex
     * @return
     * @throws Exception
     */


    public static List<String> getRowValuesFromWebTable(WebElement webTable, Integer rowIndex) throws Exception {
        List<String> returnRowData = new ArrayList<>();
        List<WebElement> allRows = null;
        WebElement table = null;

        try {
            List<WebElement> tbodies = webTable.findElements(By.tagName("tbody"));
            if (tbodies != null) {
                table = tbodies.get(0);
            } else {
                table = webTable;
            }
            allRows = table.findElements(By.tagName("tr"));
            Log.info("Total Rows: " + allRows.size());

            if (allRows.size() == 0) {
                Log.info("No Rows found in the WebTable");
                return returnRowData;
            } else {
                if (rowIndex < 0 || rowIndex >= allRows.size()) {
                    Log.info("WebTable has '" + allRows.size() + "' rows, whereas passed rowIndex is " + rowIndex);
                    return returnRowData;
                }
            }

            WebElement row = allRows.get(rowIndex - 1);
            List<WebElement> columnsRow = row.findElements(By.tagName("td"));
            int columnsCount = columnsRow.size();
            for (int column = 0; column < columnsCount; column++) {
                String cellText = columnsRow.get(column).getText();
                returnRowData.add(cellText);
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getRowValuesFromWebTable", "Failed to capture the Row values from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row Values from the Web table: " + webTable.toString());
        }
        return returnRowData;
    }


    public static Integer getRowNoFromWebTable(WebElement webTable, String Value) throws Exception {

        int rowNo = 0;
        try {
            List<WebElement> rows = webTable.findElements(By.xpath("./tbody[1]/tr"));
            for (int i = 0; i < rows.size(); i++) {

                WebElement row = webTable.findElement(By.xpath("./tbody[1]/tr[" + (i + 1) + "]"));
                if (row.getText().trim().contains(Value)) {
                    rowNo = i + 1;
                    Log.info("Row Number Containing '" + Value + "' is :" + rowNo);
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getRowNoFromWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row No from the Web table: " + webTable.toString());
        }
        return rowNo;
    }


    public static Integer getRowNoFromWebTable(WebElement webTable, int colNum, String Value) throws Exception {

        int rowNo = 0;
        try {
            Utilities ut = new Utilities();
            ut.highlightMe(webTable);

            List<WebElement> tbody = webTable.findElements(By.tagName("tbody"));
            List<WebElement> rows = tbody.get(0).findElements(By.tagName("tr"));
            for (int i = 1; i < rows.size(); i++) {
                ut.highlightMe(rows.get(i));
                List<WebElement> tds = rows.get(i).findElements(By.tagName("td"));
                if (tds.get(colNum - 1).getText().trim().equalsIgnoreCase(Value)) {
                    ut.highlightMe(tds.get(colNum - 1));
                    rowNo = i + 1;
                    Log.info("Row Number containing '" + Value + "' is :" + rowNo);
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("getRowNoFromWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row No from the Web table: " + webTable.toString());
        }
        return rowNo;
    }


    public void setCellValueInWebTable(WebElement webTable, String findRowByValue, String findColByValue, String cellInput) throws Exception {

        int colNum = GUIComponents.getColumnIndexFromWebTable(webTable, findColByValue);
        int rowNum = GUIComponents.getRowNoFromWebTable(webTable, findRowByValue);
        final String webTable1 = DriverFactory.getPageFactoryLocatorString(webTable);


        try {
            driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).click();
            driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).sendKeys(cellInput);
            driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).sendKeys(Keys.TAB);

        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.Log("setCellValueInWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row No from the Web table: " + webTable.toString());
        }
    }

@SuppressWarnings("static-access")
    public static boolean findElementTypeInWebTable(WebElement webTable, String objType, int row, int col) throws Exception {
        try {
            List<WebElement> trs = webTable.findElements(By.tagName("tr"));
            boolean flag = false;
            List<WebElement> tds = trs.get(row-1).findElements(By.tagName("td"));
            if (tds.size() > 0) {
                WebElement ele = tds.get(col - 1);

                switch (objType.toLowerCase().trim()) {
                    case "checkbox":
                        List<WebElement> innerElements = ele.findElements(By.tagName("input"));
                        if (innerElements.size() > 0) {
                            for (WebElement in : innerElements) {
                                if (in.getAttribute("type").equals("checkbox") || in.getAttribute("class").contains("checkbox")) {
                                    if (in.isEnabled() && !(in.isSelected())) {
                                        flag = true;
                                        if (detailedReport.equalsIgnoreCase("yes")) {
                                            HtmlReporter.Log("Checkbox Type", "Checkbox clicked successfully", "Pass");
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        break;
                    case "link":

                        List<WebElement> innerLinkElements = ele.findElements(By.tagName("a"));
                        if (innerLinkElements.size() > 0) {
                            for (WebElement in : innerLinkElements) {
                                if (!(in.getAttribute("type").equals("hidden"))) {
                                    flag = true;
                                    if (detailedReport.equalsIgnoreCase("yes")) {
                                        HtmlReporter.Log("Link Type", "Link clicked successfully", "Pass");
                                    }
                                    break;
                                }
                            }

                        }
                        break;
                    case "image":
                        WebElement innerImgElement = ele.findElement(By.tagName("img"));

                        if (Integer.parseInt(innerImgElement.getAttribute("width")) > 0) {
                            flag = true;
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("Image Type", "Link in Image clicked successfully", "Pass");
                            }
                            break;

                        }
                        break;
                    default:
                        Log.info("Given Type Not Defined ");
                        break;
                }

            }


            return flag;


        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException", e.getMessage());
            HtmlReporter.Log("findElementTypeInWebTable", "Link in Image clicked successfully", "Fail");
            Assert.assertTrue(false, "Failed to find Web element");
            return false;
        }

    }



public static WebElement getElementByTypeInWebTable(WebElement webTable, String objType, int row, int col) throws Exception {
        WebElement actualElement = null;

    try {
        List<WebElement> trs = webTable.findElements(By.tagName("tr"));
        boolean flag = false;
        List<WebElement> tds = trs.get(row-1).findElements(By.tagName("td"));
        if (tds.size() > 0) {
            WebElement ele = tds.get(col - 1);

            switch (objType.toLowerCase().trim()) {
                case "checkbox":
                    List<WebElement> innerElements = ele.findElements(By.tagName("input"));
                    if (innerElements.size() > 0) {
                        for (WebElement in : innerElements) {
                            if (in.getAttribute("type").equals("checkbox") || in.getAttribute("class").contains("checkbox")) {
                                if (in.isEnabled() && !(in.isSelected())) {
                                    actualElement = in;
                                    if (detailedReport.equalsIgnoreCase("yes")) {
                                        HtmlReporter.Log("Checkbox Type", "Checkbox clicked successfully", "Pass");
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    break;
                case "link":

                    List<WebElement> innerLinkElements = ele.findElements(By.tagName("a"));
                    if (innerLinkElements.size() > 0) {
                        for (WebElement in : innerLinkElements) {
                            if (!(in.getAttribute("type").equals("hidden"))) {
                                actualElement = in;
                                if (detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.Log("Link Type", "Link clicked successfully", "Pass");
                                }
                                break;
                            }
                        }

                    }
                    break;
                case "image":
                    WebElement innerImgElement = ele.findElement(By.tagName("img"));

                    if (Integer.parseInt(innerImgElement.getAttribute("width")) > 0) {
                        actualElement = innerImgElement;
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("Image Type", "Link in Image clicked successfully", "Pass");
                        }
                        break;

                    }
                    break;

                case "span":
                    WebElement innerSpaceElement = ele.findElement(By.tagName("span"));
                    actualElement = innerSpaceElement;

                    if (detailedReport.equalsIgnoreCase("yes")) {
                        HtmlReporter.Log("Space Type", "Link in Image clicked successfully", "Pass");
                    }
                    break;

                default:
                    Log.info("Given Type Not Defined ");
                    break;
            }

        }


        return actualElement;


    } catch (NoSuchElementException e) {
        Log.error("NoSuchElementException", e.getMessage());
        HtmlReporter.Log("getElementByTypeInWebTable", "Link in Image clicked successfully", "Fail");
        Assert.assertTrue(false, "Failed to find Web element");

    }
    return actualElement;

}



public static void waitForPageLoad(WebDriver driver){
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>(){
            public Boolean apply(WebDriver driver){
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, PublicVariables.pageTimeOut);
        wait.until(pageLoadCondition);
}



public void acceptAlert() throws Exception {
        try{
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }catch (NoAlertPresentException e){
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.Log("acceptAlert", "Failed to Accept Alert on Browser :", "Fail");
            Assert.assertTrue(false, "Failed to Accept Alert on Browser");
        }
}



public void acceptAlert(String alertDecision) throws Exception {
        try{
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            if (alertDecision.equals("accept")){
                alert.accept();
            }else if (alertDecision.equals("dismiss")){
                alert.dismiss();
            }
        }catch (NoAlertPresentException e){
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.Log("acceptAlert", "Failed to Accept Alert on Browser :", "Fail");
            Assert.assertTrue(false, "Failed to Accept Alert on Browser");
        }
}


public boolean alertIsPresent() throws Exception {
        try {
            driver.switchTo().alert();
            return true;
        }catch (NoAlertPresentException e){
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.Log("alertIsPresent", "Alert is not present", "Fail");
            Assert.assertTrue(false, "Alert is not present");
            return false;
        }
}

public void check_unCheck_checkboxesInATable(WebElement webTable, String strColumnName, int startRow, int endRow, String strCondition, boolean allRow) throws Exception {

        List<WebElement> allRows = null;
        WebElement table = null;
        try {
            List<WebElement> tbodies = webTable.findElements(By.tagName("tbody"));
            if (tbodies != null) {
                table = tbodies.get(0);
            } else {
                table = webTable;
            }
            allRows = table.findElements(By.tagName("tr"));
            Log.info("Total Rows: " + allRows.size());

            if (allRows.size() == 0) {
                Log.info("No Rows found in the WebTable");
                return ;
            }

            WebElement row = allRows.get(0);
            int colNo=0;
            List<WebElement> columnsRow = row.findElements(By.tagName("td"));
            int columnsCount = columnsRow.size();

            System.out.println("Total Columns: "+columnsCount +" "+allRows.size());
            for (int column = 0; column < columnsCount; column++) {
                String cellText = columnsRow.get(column).getText();
                System.out.println(cellText);
                if (cellText.equalsIgnoreCase(strColumnName)){
                    System.out.println(strColumnName);
                    colNo = column;
                    break;
                }
            }

            if (allRow == true){
                for (int i=1; i<=allRows.size()-2;i++){
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")){
                        if (strCondition.equalsIgnoreCase("Check")){
                            if (!els.isSelected()){
                                els.click();
                                if (detailedReport.equalsIgnoreCase("yes")){
                                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                                }
                            }else {
                                if (detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }else {
                            if (els.isSelected()){
                                els.click();
                                if (detailedReport.equalsIgnoreCase("yes")){
                                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                                }
                            }else {
                                if (detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }
                    }
                    else {
                        HtmlReporter.Log("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName+" is not a checkbox", "Fail");
                    }
                }
            }else {
                if (endRow < 0 || endRow >= allRows.size()){
                    Log.info("Webtable has '"+allRows.size() + "' rows, whereas end RowIndex is "+ endRow);
                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Webtable has '"+allRows.size() + "' rows, whereas end RowIndex is "+ endRow, "Fail");
return;
                }

                for (int i = startRow; i<=endRow; i++){
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")){
                        if (strCondition.equalsIgnoreCase("Check")){
                    if (!els.isSelected()){
                        els.click();
                        if (detailedReport.equalsIgnoreCase("yes")){
                            HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                        }
                    }else {
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                        }
                    }
                }else {
                    if (els.isSelected()){
                        els.click();
                        if (detailedReport.equalsIgnoreCase("yes")){
                            HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                        }
                    }else {
                        if (detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                        }
                    }
                }
            }
                    else {
                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName+" is not a checkbox", "Fail");
            }
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.Log("check_unCheck_checkboxesInATable", "Failed to check a checkbox in a Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to check a checkbox in a Web table: " + webTable.toString());
        }

}




public void verifyCheck_unCheck_checkboxInATable(WebElement webTable, String strColumnName, int startRow, int endRow, String strCondition, boolean allRow) throws Exception {
    List<WebElement> allRows = null;
    WebElement table = null;
    try {
        List<WebElement> tbodies = webTable.findElements(By.tagName("tbody"));
        if (tbodies != null) {
            table = tbodies.get(0);
        } else {
            table = webTable;
        }
        allRows = table.findElements(By.tagName("tr"));
        Log.info("Total Rows: " + allRows.size());

        if (allRows.size() == 0) {
            Log.info("No Rows found in the WebTable");
            return ;
        }

        WebElement row = allRows.get(0);
        int colNo=0;
        List<WebElement> columnsRow = row.findElements(By.tagName("td"));
        int columnsCount = columnsRow.size();

        System.out.println("Total Columns: "+columnsCount +" "+allRows.size());
        for (int column = 0; column < columnsCount; column++) {
            String cellText = columnsRow.get(column).getText();
            System.out.println(cellText);
            if (cellText.equalsIgnoreCase(strColumnName)){
                System.out.println(strColumnName);
                colNo = column;
                break;
            }
        }

        if (allRow == true){
            for (int i=1; i<=allRows.size()-2;i++){
                WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                if (els.getAttribute("type").equalsIgnoreCase("checkbox")){
                    if (strCondition.equalsIgnoreCase("Check")){
                        if (!els.isSelected()){
                            if (detailedReport.equalsIgnoreCase("yes")){
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                            }
                        }else {
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                            }
                        }
                    }else {
                        if (els.isSelected()){
                            if (detailedReport.equalsIgnoreCase("yes")){
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                            }
                        }else {
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                            }
                        }
                    }
                }
                else {
                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName+" is not a checkbox", "Fail");
                }
            }
        }else {
            if (endRow < 0 || endRow >= allRows.size()){
                Log.info("Webtable has '"+allRows.size() + "' rows, whereas end RowIndex is "+ endRow);
                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Webtable has '"+allRows.size() + "' rows, whereas end RowIndex is "+ endRow, "Fail");
                return;
            }

            for (int i = startRow; i<=endRow; i++){
                WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                if (els.getAttribute("type").equalsIgnoreCase("checkbox")){
                    if (strCondition.equalsIgnoreCase("Check")){
                        if (!els.isSelected()){
                            if (detailedReport.equalsIgnoreCase("yes")){
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                            }
                        }else {
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                            }
                        }
                    }else {
                        if (els.isSelected()){
                            if (detailedReport.equalsIgnoreCase("yes")){
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row "+i+" and Column "+strColumnName, "Pass");
                            }
                        }else {
                            if (detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.Log("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                            }
                        }
                    }
                }
                else {
                    HtmlReporter.Log("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName+" is not a checkbox", "Fail");
                }
            }
        }
    } catch (NoSuchElementException e) {
        Log.error("NoSuchElementException occured " + e.getMessage());
        HtmlReporter.Log("check_unCheck_checkboxesInATable", "Failed to check a checkbox in a Web table :" + webTable.toString(), "Fail");
        Assert.assertTrue(false, "Failed to check a checkbox in a Web table: " + webTable.toString());
    }

}




public void scroll(String direction){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        switch (direction.toLowerCase()){
            case "up":
                js.executeScript("window.scrollBy(0,250)","");
                break;
            case "down":
                js.executeScript("window.scrollBy(0,-250)", "");
                break;
        }
}






}