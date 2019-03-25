package com.core.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GUIComponents {

    private static final Logger Log = LogManager.getLogger(DriverFactory.class);
    public static boolean bResultFlag = false;
    public static String sBCDescription = "***";
    public static boolean bObjectValidation = false;
    Utilities utils = new Utilities();


    public GUIComponents(WebDriver driver) {
        PublicVariables.driver = driver;
        bResultFlag = HtmlReporter.bResultFlag;
        sBCDescription = HtmlReporter.sBCDescription;
    }

    public static void switchFrames(WebDriver currDriver, String[] frames) throws Exception {
        try {
            Thread.sleep(1000);
            currDriver.switchTo().defaultContent();

            if (frames.length == 0) {
                HtmlReporter.reportLog("switchFrames", "No frame to switch", "pass");
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
            HtmlReporter.reportLog("switchFrames", "Switch Frames got an exception", "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "switch frames got failed");
        }
    }

    public static boolean waitForElementLoad(WebElement element, int timeout) throws Exception {
        boolean flag = false;

        PublicVariables.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(PublicVariables.driver, timeout);

        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (Exception e) {
            flag = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
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
        PublicVariables.driver.manage().timeouts().implicitlyWait(PublicVariables.objTimeOut, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(PublicVariables.driver, timeout);

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

    public static void sendKeys(WebElement element, String text) {
        element.sendKeys(text);
    }

    public boolean isExists(WebElement pageFactoryElement) throws Exception {
        boolean isPresent = false;

        if (GUIComponents.waitForElementLoad(pageFactoryElement, PublicVariables.pageTimeOut)) {
            if (pageFactoryElement.isDisplayed())
                isPresent = true;
        } else {
            isPresent = false;
        }
        return isPresent;
    }


    public void validateTextEquals(WebElement element, String ExpectedText) throws Exception {
        HtmlReporter.StartBC();
        waitForElementLoad(element,PublicVariables.pageTimeOut);
        String textVal = element.getText();
        System.out.println(textVal);

        try {
            HtmlReporter.verify(textVal, ExpectedText, "Validate if text displays equals the expected value");
            HtmlReporter.reportLog("ValidateTextEquals", "Validate if text displays equals the expected value", "pass");
        } catch (NoSuchElementException e) {
           HtmlReporter.reportLog("ValidateTextEquals", "Validate if text displays equals the expected value", "Fail");
        }
    }

    public void validateTextContains(WebElement element, String ExpectedText) throws Exception {
        HtmlReporter.StartBC();

        String textVal = element.getText();
        System.out.println(textVal);

        try {
            HtmlReporter.verify(textVal.contains(ExpectedText),
                    true,
                    "Validate text displayed contains the expected value");
            HtmlReporter.reportLog("ValidateTextContains",
                    "Validate if text displays contains the expected value", "pass");
        } catch (Exception e) {
            HtmlReporter.reportLog("ValidateTextContains", "Validate if text displays does not contains the expected value", "Fail");
            Log.error("NoSuchElementExcepetion occured" + e.getMessage());
        }
    }

    public void verifyControlExists(WebElement element, boolean ExpectedText) throws Exception {
        HtmlReporter.StartBC();

        try {
            HtmlReporter.verify(isExists(element), ExpectedText,
                    "Validate if control exists");
            HtmlReporter.reportLog("verifyControlExist", "Validate if control exists", "Pass");
        } catch (Exception e) {
            HtmlReporter.reportLog("verifyControlExist", "Validate if control does not exist", "Fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
        }
    }

    public int getRowCount(WebElement pageFactory_webTable) throws Exception {
        final String webTable1 = DriverFactory.getPageFactoryLocatorString(pageFactory_webTable);
        int output = -1;

        try {
            if (webTable1.contains("//")) {
                if (PublicVariables.driver.findElement(By.xpath(webTable1)).isDisplayed()) {
                    List<WebElement> rows = PublicVariables.driver.findElements(By.xpath(webTable1 + "/tbody/tr"));
                    if ((rows.size()) != 0) {
                        output = rows.size() - 1;
                    } else {
                        output = -1;
                    }
                } else {
                    if (PublicVariables.driver.findElement(By.id(webTable1)).isDisplayed()) {
                        String convToXpath = "//table[@id='" + webTable1 + "']";
                        List<WebElement> rows = PublicVariables.driver.findElements(By.xpath(convToXpath + "/tbody/tr"));
                        if ((rows.size()) != 0) {
                            output = rows.size() - 1;

                        }
                    } else {
                        output = -1;
                    }
                }
            }
        } catch (NoSuchElementException e) {

            HtmlReporter.reportLog("getRowCount", "Get Row Count from WebTable: " + pageFactory_webTable.toString(), "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Get Row Count of WebTable got failed");
        }
        return output;
    }


    public static void removeFocus(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) PublicVariables.driver;
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
            HtmlReporter.reportLog("getRowCount", "Get Row Count from WebTable: " + webTable.toString(), "fail");
            Log.error("NoSuchElementException occured " + e.getMessage());
            Assert.assertTrue(false, "Get Row Count of WebTable got failed");
            return -1;
        }
    }

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
                HtmlReporter.reportLog("clickWebTableColumnHeader", "Failed to click WebTable Header column of table: " + webTable.toString(), "fail");
                Assert.assertTrue(false, "Failed to click webTable header column");
                return false;
            }
        } catch (IOException e) {
            Log.info("Failed in clickWebTableColumnHeader--- " + e.getMessage());
            HtmlReporter.reportLog("clickWebTableColumnHeader", "Failed to click WebTable Header column of table: " + webTable.toString(), "fail");
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
                HtmlReporter.reportLog("Set", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", Set, " + input);
                try {
                    if (isExists(webElement)) {

                        //selenium.type(elementIdentifierString, input);
                        //webElement.sendKeys(op);
                        //webElement.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT, Keys.END), input);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("Set", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("Set", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.reportLog("Set", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }

        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.reportLog("Set", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
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
    public void setSecurePassword(WebElement webElement, String encryptedPassword) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.reportLog("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                try {
                    if (isExists(webElement)) {
                        //byte[] decryptedPassword =
                        Utilities.decryptEncodedPassword(encryptedPassword);
                        //String op = new String(decryptedPassword);
                        //selenium.type(elementIdentifierString, op);
                        //webElement.sendKeys(op);
                        //                        //webElement.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT, Keys.END), op);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.reportLog("setSecurePassword", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }
        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.reportLog("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here");

            } else {
                try {
                    if (isExists(webElement)) {
                        byte[] decryptedPassword = Utilities.decryptEncodedPassword(encryptedPassword);
                        String op = new String(decryptedPassword);

                        //webElement.sendKeys(op);
                        webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), op);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("setSecurePassword", "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "'", "Fail");
                        Assert.assertTrue(false, "Enter the Value 'It's Password' in the field '" + elementIdentifierString + "' >>>Error");
                    }
                } catch (java.util.NoSuchElementException e) {
                    HtmlReporter.reportLog("setSecurePassword", "Set the value '" + "It's a Password" + "' in the field '" + elementIdentifierString + "' >>Error ", e.getMessage());
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "SetThe Value '" + "It's a Password' in the field " + elementIdentifierString + " >>Error  " + e.getMessage());
                }
            }
        } catch (java.util.NoSuchElementException e) {
            HtmlReporter.reportLog("setSecurePassword", "Validating Object" + elementIdentifierString + " existence in application, object does not exists, throwing exception here", "Fail");
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
    public void type(WebElement webElement, String input) throws Exception {
        String elementIdentifierString = DriverFactory.getPageFactoryLocatorString(webElement);

        try {
            if (!waitForElementLoad(webElement, PublicVariables.objTimeOut)) {
                HtmlReporter.reportLog("type", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", type, " + input);

                try {
                    if (isExists((webElement))) {
                        webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), input);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("type", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("type", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("type", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("type", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("textAreaType", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", textAreaType, " + input);
                try {
                    if (isExists((webElement))) {
                        webElement.click();
                        webElement.sendKeys(input);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("textAreaType", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("textAreaType", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("textAreaType", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("textAreaType", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("typeNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", typeNotNull, " + input);

                try {
                    if (isExists((webElement))) {
                        if (webElement.getText().isEmpty()) {
                            //webElement.click();
                            webElement.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), input);
                            if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.reportLog("typeNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("typeNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("typeNotNull", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("typeNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("select", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", select, " + input);
                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);
                        dropdown.selectByVisibleText(input);
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("select", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("select", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("select", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("select", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("selectNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectNotNull, " + input);
                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);
                        WebElement option = dropdown.getFirstSelectedOption();
                        if (option.getText().isEmpty()) {
                            dropdown.selectByVisibleText(input);
                            if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.reportLog("selectNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("selectNotNull", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("selectNotNull", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("selectNotNull", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("selectByIndex", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectByIndex, " + input);
                try {
                    if (isExists((webElement))) {
                        Select dropdown = new Select(webElement);
                        dropdown.selectByIndex(Integer.parseInt(input));
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("selectByIndex", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("selectByIndex", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("selectByIndex", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("selectByIndex", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("selectByInstring", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Pass");
                                }
                                blnFlag = true;
                                break;
                            }
                        }
                        if (!blnFlag) {
                            HtmlReporter.reportLog("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                            Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                        }
                    } else {
                        HtmlReporter.reportLog("selectByInstring", "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Enter the Value '" + input + "' in the field '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("selectByInstring", "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "typing the Value '" + input + "' in the field '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("selectByInstring", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("checkBoxOnOff", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", selectByIndex, " + input);
                try {
                    if (isExists((webElement))) {
                        if (input.toLowerCase().equalsIgnoreCase("on")) {

                            if (!webElement.isSelected()) {

                                webElement.click();

                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("checkBox On", "CheckBox on is Successful'" + elementIdentifierString + "' ", "Pass");
                                }
                            }
                        } else if (input.toLowerCase().equalsIgnoreCase("off")) {

                            if (webElement.isSelected()) {
                                webElement.click();
                                HtmlReporter.reportLog("checkBox Off ", "CheckBox off is Successful '" + elementIdentifierString + "' ", "Pass");
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("checkBoxOnOff", "Object doesn't exists  -->'" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists -->'" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("checkBoxOnOff", "Object click operation failed >>'" + elementIdentifierString + "' >>Error: ", "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object click operation failed >>'" + elementIdentifierString + "' >>Error: ");
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("checkBoxOnOff", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("click", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", click, ");
                try {
                    if (isExists((webElement))) {
                        webElement.click();
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("click", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("click", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("click", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("click", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("actionClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", actionClick, ");
                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(PublicVariables.driver).click(element).perform();
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("actionClick", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("actionClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("actionClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("actionClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("actionDoubleClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", actionDoubleClick, ");
                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(PublicVariables.driver).doubleClick(element).perform();

                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("actionDoubleClick", "Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("actionDoubleClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("actionDoubleClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("actionDoubleClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("hoverClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", hoverClick, " + input);
                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(PublicVariables.driver).moveToElement(element).build().perform();
                        PublicVariables.objWait.until(ExpectedConditions.visibilityOf(PublicVariables.driver.findElement(By.linkText(input))));
                        PublicVariables.driver.findElement(By.linkText(input)).click();
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("hoverClick", "hover Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("hoverClick", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("hoverClick", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("hoverClick", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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
                HtmlReporter.reportLog("hover", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
                Assert.assertTrue(false, "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here");
            } else {
                Log.info("Start - PerformAction - Arguments (" + elementIdentifierString + ", " + webElement + ", hover, " + input);
                try {
                    if (isExists((webElement))) {
                        WebElement element = webElement;
                        new Actions(PublicVariables.driver).moveToElement(element).build().perform();
                        PublicVariables.objWait.until(ExpectedConditions.visibilityOf(PublicVariables.driver.findElement(By.linkText(input))));
                        PublicVariables.driver.findElement(By.linkText(input)).click();

                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("hover", "hover Clicking the Object is Successful '" + elementIdentifierString + "' ", "Pass");
                        }
                    } else {
                        HtmlReporter.reportLog("hover", "Object doesn't exists '" + elementIdentifierString + "' ", "Fail");
                        Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' ");
                    }
                } catch (NoSuchElementException e) {
                    HtmlReporter.reportLog("hover", "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage(), "Fail");
                    Log.error("NoSuchElementException occured " + e.getMessage());
                    Assert.assertTrue(false, "Object doesn't exists '" + elementIdentifierString + "' >>Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            HtmlReporter.reportLog("hover", "Validating Object " + elementIdentifierString + " existence in application, object doesn't exists, throwing exception here", "Fail");
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

                    if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                        HtmlReporter.reportLog("getColumnIndexFromWebTable", "column index :" + colPosition + " -->from webTable " + webTable.toString() + " based on Column Name : " + columnName, "Pass");
                    }
                    Log.info("Column position of '" + columnName + "' is: " + colPosition);
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("getColumnIndexFromWebTable", "failed to get column index :" + colPosition + " -->from webTable " + webTable.toString() + " based on Column Name : " + columnName, "Fail");
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
            PublicVariables.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            sPropertyToVerify = sPropertyToVerify.toUpperCase();
            switch (sPropertyToVerify) {
                case "CLICK_PRECEDING":
                    try {
                        element = PublicVariables.driver.findElement(By.xpath(".//table[contains(@id,'" + sTableUniqueName + "')]//td[starts-with(text(),'" + sRowName + "')]/preceding-sibling::td[" + sColIndex + "]/a"));
                        element.click();
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("performAction", "Click the Object '" + objectNameOnUi + "'", "Pass");
                        }
                    } catch (NoSuchElementException e) {
                        Log.error("NoSuchElementException occured " + e.getMessage());
                        HtmlReporter.reportLog("performAction", "Click the Object '" + objectNameOnUi + "'", "Fail");
                        Assert.assertTrue(false, "Click the Object '" + objectNameOnUi + "' is failed");
                    }
                    break;
                case "CLICK_FOLLOWING":
                    try {
                        element = PublicVariables.driver.findElement(By.xpath(".//table[contains(@id,'" + sTableUniqueName + "')]//td[starts-with(text(),'" + sRowName + "')]/following-sibling::td[" + sColIndex + "]/a"));
                        element.click();
                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("performAction", "Click the Object '" + objectNameOnUi + "'", "Pass");
                        }
                    } catch (NoSuchElementException e) {
                        Log.error("NoSuchElementException occured " + e.getMessage());
                        HtmlReporter.reportLog("performAction", "Click the Object '" + objectNameOnUi + "'", "Fail");
                        Assert.assertTrue(false, "Click the Object '" + objectNameOnUi + "' is failed");

                    }
                    break;
                default:
                    HtmlReporter.reportLog("performAction", "Option not Available  '" + objectNameOnUi + "'", "Fail");
                    break;
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("performAction", "Validating the Object '" + objectNameOnUi + "' is" + sPropertyToVerify + " failed, Error Message: " + e.getMessage() + "<br><br>" + sListReportDesc + "</table>", "Fail");
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
            HtmlReporter.reportLog("getSelectedListValue", "Failed to capture the selected value from the list box", "Fail");
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
            HtmlReporter.reportLog("getColumnValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
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
            HtmlReporter.reportLog("validateColumnNamesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
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
            HtmlReporter.reportLog("getCellValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
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
                //reportLog.info("Dependent Cell Value: '"+dependentColName+"' has no cell with value: '"+dependentColVal+"'");
                return cellValue;
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("getCellValuesFromWebTable", "Failed to capture the column values from the Web table :" + webTable.toString(), "Fail");
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
            HtmlReporter.reportLog("getRowValuesFromWebTable", "Failed to capture the Row values from the Web table :" + webTable.toString(), "Fail");
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
            HtmlReporter.reportLog("getRowNoFromWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
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
            HtmlReporter.reportLog("getRowNoFromWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row No from the Web table: " + webTable.toString());
        }
        return rowNo;
    }


    public void setCellValueInWebTable(WebElement webTable, String findRowByValue, String findColByValue, String cellInput) throws Exception {
        int colNum = GUIComponents.getColumnIndexFromWebTable(webTable, findColByValue);
        int rowNum = GUIComponents.getRowNoFromWebTable(webTable, findRowByValue);
        final String webTable1 = DriverFactory.getPageFactoryLocatorString(webTable);
        try {
            PublicVariables.driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).click();
            PublicVariables.driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).sendKeys(cellInput);
            PublicVariables.driver.findElement(By.xpath(webTable1 + "/tbody[1]/tr[" + rowNum + "]/td[" + colNum + "]//input")).sendKeys(Keys.TAB);

        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("setCellValueInWebTable", "Failed to capture the Row No from the Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to get the Row No from the Web table: " + webTable.toString());
        }
    }

    public static boolean findElementTypeInWebTable(WebElement webTable, String objType, int row, int col) throws Exception {
        try {
            List<WebElement> trs = webTable.findElements(By.tagName("tr"));
            boolean flag = false;
            List<WebElement> tds = trs.get(row - 1).findElements(By.tagName("td"));
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
                                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                            HtmlReporter.reportLog("Checkbox Type", "Checkbox clicked successfully", "Pass");
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
                                    if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                        HtmlReporter.reportLog("Link Type", "Link clicked successfully", "Pass");
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
                            if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.reportLog("Image Type", "Link in Image clicked successfully", "Pass");
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
            HtmlReporter.reportLog("findElementTypeInWebTable", "Link in Image clicked successfully", "Fail");
            Assert.assertTrue(false, "Failed to find Web element");
            return false;
        }
    }


    public static WebElement getElementByTypeInWebTable(WebElement webTable, String objType, int row, int col) throws Exception {
        WebElement actualElement = null;
        try {
            List<WebElement> trs = webTable.findElements(By.tagName("tr"));
            //boolean flag = false;
            List<WebElement> tds = trs.get(row - 1).findElements(By.tagName("td"));
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
                                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                            HtmlReporter.reportLog("Checkbox Type", "Checkbox clicked successfully", "Pass");
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
                                    if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                        HtmlReporter.reportLog("Link Type", "Link clicked successfully", "Pass");
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
                            if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                HtmlReporter.reportLog("Image Type", "Link in Image clicked successfully", "Pass");
                            }
                            break;

                        }
                        break;

                    case "span":
                        WebElement innerSpaceElement = ele.findElement(By.tagName("span"));
                        actualElement = innerSpaceElement;

                        if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                            HtmlReporter.reportLog("Space Type", "Link in Image clicked successfully", "Pass");
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
            HtmlReporter.reportLog("getElementByTypeInWebTable", "Link in Image clicked successfully", "Fail");
            Assert.assertTrue(false, "Failed to find Web element");
        }
        return actualElement;
    }


    public static void waitForPageLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, PublicVariables.pageTimeOut);
        wait.until(pageLoadCondition);
    }


    public void acceptAlert() throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(PublicVariables.driver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = PublicVariables.driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.reportLog("acceptAlert", "Failed to Accept Alert on Browser :", "Fail");
            Assert.assertTrue(false, "Failed to Accept Alert on Browser");
        }
    }

    public void acceptAlert(String alertDecision) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(PublicVariables.driver, 2);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = PublicVariables.driver.switchTo().alert();
            if (alertDecision.equals("accept")) {
                alert.accept();
            } else if (alertDecision.equals("dismiss")) {
                alert.dismiss();
            }
        } catch (NoAlertPresentException e) {
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.reportLog("acceptAlert", "Failed to Accept Alert on Browser :", "Fail");
            Assert.assertTrue(false, "Failed to Accept Alert on Browser");
        }
    }


    public boolean alertIsPresent() throws Exception {
        try {
            PublicVariables.driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            Log.error("NoAlertPresentException occured", e.getMessage());
            HtmlReporter.reportLog("alertIsPresent", "Alert is not present", "Fail");
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
                return;
            }

            WebElement row = allRows.get(0);
            int colNo = 0;
            List<WebElement> columnsRow = row.findElements(By.tagName("td"));
            int columnsCount = columnsRow.size();

            System.out.println("Total Columns: " + columnsCount + " " + allRows.size());
            for (int column = 0; column < columnsCount; column++) {
                String cellText = columnsRow.get(column).getText();
                System.out.println(cellText);
                if (cellText.equalsIgnoreCase(strColumnName)) {
                    System.out.println(strColumnName);
                    colNo = column;
                    break;
                }
            }
            if (allRow == true) {
                for (int i = 1; i <= allRows.size() - 2; i++) {
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")) {
                        if (strCondition.equalsIgnoreCase("Check")) {
                            if (!els.isSelected()) {
                                els.click();
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        } else {
                            if (els.isSelected()) {
                                els.click();
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName + " is not a checkbox", "Fail");
                    }
                }
            } else {
                if (endRow < 0 || endRow >= allRows.size()) {
                    Log.info("Webtable has '" + allRows.size() + "' rows, whereas end RowIndex is " + endRow);
                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Webtable has '" + allRows.size() + "' rows, whereas end RowIndex is " + endRow, "Fail");
                    return;
                }

                for (int i = startRow; i <= endRow; i++) {
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")) {
                        if (strCondition.equalsIgnoreCase("Check")) {
                            if (!els.isSelected()) {
                                els.click();
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        } else {
                            if (els.isSelected()) {
                                els.click();
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName + " is not a checkbox", "Fail");
                    }
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Failed to check a checkbox in a Web table :" + webTable.toString(), "Fail");
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
                return;
            }

            WebElement row = allRows.get(0);
            int colNo = 0;
            List<WebElement> columnsRow = row.findElements(By.tagName("td"));
            int columnsCount = columnsRow.size();

            System.out.println("Total Columns: " + columnsCount + " " + allRows.size());
            for (int column = 0; column < columnsCount; column++) {
                String cellText = columnsRow.get(column).getText();
                System.out.println(cellText);
                if (cellText.equalsIgnoreCase(strColumnName)) {
                    System.out.println(strColumnName);
                    colNo = column;
                    break;
                }
            }
            if (allRow == true) {
                for (int i = 1; i <= allRows.size() - 2; i++) {
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")) {
                        if (strCondition.equalsIgnoreCase("Check")) {
                            if (!els.isSelected()) {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        } else {
                            if (els.isSelected()) {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName + " is not a checkbox", "Fail");
                    }
                }
            } else {
                if (endRow < 0 || endRow >= allRows.size()) {
                    Log.info("Webtable has '" + allRows.size() + "' rows, whereas end RowIndex is " + endRow);
                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Webtable has '" + allRows.size() + "' rows, whereas end RowIndex is " + endRow, "Fail");
                    return;
                }

                for (int i = startRow; i <= endRow; i++) {
                    WebElement els = allRows.get(i).findElements(By.tagName("td")).get(colNo).findElements(By.tagName("input")).get(0);
                    if (els.getAttribute("type").equalsIgnoreCase("checkbox")) {
                        if (strCondition.equalsIgnoreCase("Check")) {
                            if (!els.isSelected()) {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        } else {
                            if (els.isSelected()) {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            } else {
                                if (PublicVariables.detailedReport.equalsIgnoreCase("yes")) {
                                    HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Checkbox is checked for Row " + i + " and Column " + strColumnName, "Pass");
                                }
                            }
                        }
                    } else {
                        HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Row " + i + " and Column " + strColumnName + " is not a checkbox", "Fail");
                    }
                }
            }
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("check_unCheck_checkboxesInATable", "Failed to check a checkbox in a Web table :" + webTable.toString(), "Fail");
            Assert.assertTrue(false, "Failed to check a checkbox in a Web table: " + webTable.toString());
        }
    }

    public static void scrollToView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) PublicVariables.driver;
        long screenHeight = (long) js.executeScript("return window.innerHeight");
        js.executeScript(String.format("window.scrollTo(%d,%d)", element.getLocation().x, element.getLocation().y - (screenHeight / 3)));
    }

    public static void setAttribute(WebElement element, String attributeName, String value) {
        JavascriptExecutor js = (JavascriptExecutor) PublicVariables.driver;
        js.executeScript(String.format("arguments[0].setAttribute(%s,%s);", attributeName, value), element);
    }


    public void scroll(String direction) {
        JavascriptExecutor js = (JavascriptExecutor) PublicVariables.driver;
        switch (direction.toLowerCase()) {
            case "up":
                js.executeScript("window.scrollBy(0,250)", "");
                break;
            case "down":
                js.executeScript("window.scrollBy(0,-250)", "");
                break;
        }
    }

    public static String getColorControlByCssClass(String cssString, String category) {
        DriverFactory.waitForPageLoad();
        JavascriptExecutor js = (JavascriptExecutor) PublicVariables.driver;
        String script = "";
        if (category.toLowerCase().equals("color")) {
            script = "return $(\'" + cssString + "\').css('color')";
        } else {
            script = "return $(\'" + cssString + "\').css('background-color')";
        }
        try {
            return (String) js.executeScript(script);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static boolean waitForElementLoad(List<WebElement> element, int timeout) throws Exception {

        boolean flag = false;

        PublicVariables.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(PublicVariables.driver, timeout);

        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(element));
            flag = true;
        } catch (NoSuchElementException e) {
            flag = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("waitForElementLoad", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }

        wait = null;
        return flag;
    }


    /**
     * Function    :   waitforAmountOfElementPresence
     * Description :    Wait for Amount of elements present
     *
     * @param element
     * @param elementNumberExpected
     * @param timeout
     * @return
     * @throws Exception
     */
    public static boolean waitforAmountOfElementPresence(List<WebElement> element, int elementNumberExpected, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        boolean flag = false;
        try {
            flag = wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    int elementCount = element.size();
                    System.out.println("Count " + elementCount);
                    return elementCount >= elementNumberExpected;
                }
            });
        } catch (NoSuchElementException e) {
            flag = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("waitforAmountOfElementPresence", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
        return flag;
    }

    /**
     * Function    :   isElementDisplay
     * Description :    Check element display or not
     *
     * @param element
     * @param elementName
     * @param timeout
     * @return
     * @throws Exception
     */
    public boolean isElementDisplay(WebElement element, String elementName, int timeout) throws Exception {
        Boolean check = false;
        try {
            check = isExists(element, timeout);
        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("isElementDisplay", "Unable to find " + elementName, "Fail");
            PublicVariables.failureCause = e.getMessage();
        }
        return check;
    }

    /**
     * Function    :   getCssValue
     * Description :    Check Css Value
     *
     * @param element
     * @param cssValue
     * @param timeout
     * @return
     * @throws Exception
     */
    public String getCssValue(WebElement element, String cssValue, int timeout) throws Exception {
        String value = "";
        try {
            waitForElementLoad(element, timeout);
            value = element.getCssValue(cssValue);
        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("getCssValue", "Unable to find Css" + cssValue, "Fail");
            PublicVariables.failureCause = e.getMessage();
        }
        return value;
    }

    /**
     * Function    :   checkColorElement
     * Description :    Check  css color of element
     *
     * @param element
     * @param color
     * @param timeout
     * @return
     * @throws Exception
     */

    public boolean checkColorElement(WebElement element, String color, int timeout) throws Exception {
        boolean check = false;
        try {
            waitForElementLoad(element, timeout);
            check = getCssValue(element, "color", timeout).equals(color);

        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("checkColorElement", "Unable to find element" + element.toString(), "Fail");
            PublicVariables.failureCause = e.getMessage();
        }
        return check;
    }

    /**
     * Function    :   checkTextElement
     * Description :    Check text of element
     *
     * @param element
     * @param text_expected
     * @param timeout
     * @return
     * @throws Exception
     */
    public boolean checkTextElement(WebElement element, String text_expected, int timeout) throws Exception {
        boolean check = false;
        try {
            waitForElementLoad(element, timeout);
            check = element.getText().equals(text_expected);

        } catch (NoSuchElementException e) {
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("checkTextElement", "Unable to find element" + element.toString(), "Fail");
            PublicVariables.failureCause = e.getMessage();
        }
        return check;
    }


    /**
     * Click on element by javascript
     *
     * @param element
     * @author Duy Ngo
     */
    public static void clickJS(WebElement element) throws Exception {

        try {
            JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
            js.executeScript("arguments[0].click();", element);

        } catch (WebDriverException e) {
            System.out.println(String.format("Error while click on '%s' element by javascript", element));

        }
    }

    /**
     *
     * @param locator
     * @param timeout
     * @throws Exception
     */
    public boolean waitForElementClickable(By locator, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        boolean check = false;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            check = true;
        } catch (NoSuchElementException e) {
            check = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("waitForElementClickable", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
        wait = null;
        return check;
    }

    /**
     *
     * @param element
     * @param timeout
     * @throws Exception
     */
    public void waitForElementClickable(WebElement element, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("waitForElementClickable", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
    }

    /**
     *
     * @param element
     * @param timeout
     * @throws Exception
     */
    public void clickElement(WebElement element, int timeout) throws Exception{
        try {
            waitForElementClickable(element, timeout);
            element.click();
        } catch (Exception e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("clickElement", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
    }

    /**
     *
     * @param element
     * @param stringSend
     * @param timeout
     * @throws Exception
     */
    public void sendkeyToElement(WebElement element,String stringSend, int timeout) throws Exception{
        try {
            waitForElementClickable(element, timeout);
            element.sendKeys(stringSend);
        } catch (NoSuchElementException e) {
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("clickElement", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
    }

    /**
     *
     * @param webElement
     * @param timeout
     */
    @SuppressWarnings({"resource", "unused"})
    public void waitUntilElementNotDisplayed(WebElement webElement, int timeout) {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        ExpectedCondition<Boolean> elementIsDisplayed = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver arg0) {
                try {
                    webElement.isDisplayed();
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                } catch (StaleElementReferenceException f) {
                    return true;
                }
            }
        };
        final Boolean until = wait.until(elementIsDisplayed);
    }

    /**
     *
     * @param locator
     * @param timeout
     * @return
     * @throws Exception
     */
    public boolean waitforElementVisibility(By locator, int timeout)throws Exception{
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        boolean check = false;
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            check = true;
        }catch (NoSuchElementException e){
            check = false;
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("waitforElementVisibility", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
        return check;
    }

    public Point getLocationOfControl(WebElement element)throws Exception{
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), PublicVariables.pageTimeOut);
        Point className = null;
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            className = element.getLocation();
        }
        catch (NoSuchElementException e){
            Log.error("NoSuchElementException occured " + e.getMessage());
            HtmlReporter.reportLog("getLocationOfControl", "No Such Element Exception occured :" + e.getMessage(), "fail");
        }
        return className;
    }
    public void handleMulipleTabWindow() {
        String MainWindow = PublicVariables.driver.getWindowHandle();

        Set<String> s1 = PublicVariables.driver.getWindowHandles();

        Iterator<String> i1 = s1.iterator();

        while (i1.hasNext()) {
            String ChildWindow = i1.next();

            if (!MainWindow.equalsIgnoreCase(ChildWindow)) {
                PublicVariables.driver.switchTo().window(ChildWindow);
            }
        }
    }

    public boolean isElementDisplay(By locator, String elementName, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        boolean check = false;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            check = true;
        } catch (NoSuchElementException e) {
            check = false;
            Log.info("NoSuchElementException occured", e.getMessage());
            HtmlReporter.reportLog("isElementDisplay", "Unable to find " + elementName, "Fail");
            PublicVariables.failureCause = e.getMessage();
        }
        return check;
    }


    public void waitforElementHasStop(WebElement element, int timeout) throws Exception {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), timeout);
        try {
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    Point oldPoint = element.getLocation();
                    DriverFactory.sleep(500);
                    Point newPoint = element.getLocation();
                    return oldPoint.equals(newPoint);
                }
            });
        } catch (Exception e) {
            Log.error("Exception occured " + e.getMessage());
            }
    }
}