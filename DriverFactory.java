package com.core.framework.utils;

import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unused", "Deprecation"})
public class DriverFactory extends PublicVariables{
    private static final Logger Log = LogManager.getLogger(DriverFactory.class);


    /**
     * Initializing the WebDriver of different browsers
     * @param browser
     * @param URL
     * @param nativeEventFlag
     * @return
     * @throws Exception
     *
     */

    public static WebDriver initializeDriver(String browser, String URL, boolean...nativeEventFlag) throws Exception {

        try {
            if (pageTimeOut == 0) {
                pageTimeOut = 30;
                System.out.println("No Page Time Out has been given in Application Project, so Framework default time out assigned for Page Time Out is 30");
            }

            if (objTimeOut == 0) {
                objTimeOut = 10;
                System.out.println("No Obj Time Out has been given in Application Project, so Framework default time out assigned for Obj Time Out is 10");
            }

            if (currentTestName == null) {
                currentTestName = "No Test Name Given";
                System.out.println("No Test Name has been given in Application Project, so Framework default test name assigned for Test case is 'No Name Given'");
            }

            String browserType = browser;
            Log.info("Launching " + browserType + " browser");

            //Firefox or IE Browser or Chrome Browser

            if (browserType.equalsIgnoreCase("Mozilla") || browserType.contains("firefox") || browserType.equalsIgnoreCase("ff") || browserType.equalsIgnoreCase("") || browserType == null) {
                //File file = new File(PublicVariables.BrowserPathFF);
                System.setProperty("webdriver.gecko.driver", BrowserPathFF);//file.getAbsolutePath());
                driver = new FirefoxDriver();
                Thread.sleep(2000);
                driver.manage().window().maximize();
                driver.get(URL);
                Log.info("Mozilla Browser Started ");
                HtmlReporter.Log("Launch Browser", browserType + " browser started", "Pass");

            } else if (browserType.equalsIgnoreCase("Internet") || browserType.equalsIgnoreCase("Internet Explorer") || browserType.equalsIgnoreCase("IE")) {
              // DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                //capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                //capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                //capabilities.setCapability("ignoreProtectedModeSettings", true);
                //capabilities.setCapability("ignoreZoomSetting", true);
                //capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                //capabilities.setBrowserName("internet explorer");
               /* if ((nativeEventFlag.length>0)){
                    capabilities.setCapability("nativeEvents", nativeEventFlag[0]);

                }else {
                    capabilities.setCapability("nativeEvents", false);
                }*/
               // capabilities.setCapability("enablePersistentHover", false);
                //capabilities.setCapability("requiredWindowFocus", true);

               // capabilities.setCapability("initialBrowserUrl", URL);

                //File file = new File("D:/IMSAssist_UI_TestAutomation/driverServers/IEDriverServer.exe");
                System.setProperty("webdriver.ie.driver",BrowserPathIE );  //file.getAbsolutePath());
                driver = new InternetExplorerDriver();
                Thread.sleep(2000);
                driver.manage().window().maximize();
                driver.get(URL);
                Log.info("Internet Explorer Started");

               //driver.get(URL);
                Thread.sleep(4000);
                HtmlReporter.Log("Launch Browser", browserType + " browser started ", "Pass");

                //driver.get(URL);


            } else if (browserType.equalsIgnoreCase("Google Chrome") || browserType.equalsIgnoreCase("Chrome") || browserType.equalsIgnoreCase("GC")) {
              DesiredCapabilities capabilities = DesiredCapabilities.chrome();
               capabilities.setBrowserName("google chrome");
               capabilities.setCapability("ignoreProtectedModeSettings", true);
               capabilities.setCapability("initialBrowserUrl", URL);

                //File file = new File(PublicVariables.BrowserPathGC);
                System.setProperty("webdriver.chrome.driver", BrowserPathGC); //file.getAbsolutePath());
                driver = new ChromeDriver();
                Log.info("Google Chrome browser started ");

                driver.get(URL);
                HtmlReporter.Log("Launch Browser", browserType + " browser started", "Pass");



            }else if (browserType.equalsIgnoreCase("GridMozilla")){

                java.net.URL hubUrl = new URL("http://localhost:4444/wd/hub");
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName("firefox");
                capabilities.setCapability("ignoreProtectedModeSettings", true);
                capabilities.setCapability("initialBrowserUrl", URL);
                //capabilities.setVersion("47.0");

                File file = new File(PublicVariables.BrowserPathFF);
                System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
                driver = new RemoteWebDriver(hubUrl, (Capabilities) capabilities);
                Log.info("Mozilla browser Started ");

                HtmlReporter.Log("Launch Browser", browserType+" Browser", "Pass");
                driver.get(URL);
            }else if (browserType.equalsIgnoreCase("GridIE")){
                java.net.URL hubURL = new URL("http://localhost:4444/wd/hub");
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName("iexplorer");
                //capabilities.setCapability("version", 11);
                //capabilities.setCapability("ignoreProtectedModeSettings", true);
                //capabilities.setCapability("initialBrowserUrl", URL);

                File file = new File(PublicVariables.BrowserPathIE);
                System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

                driver = new RemoteWebDriver(hubURL, (Capabilities) capabilities);
                Log.info("IE browser Started ");

                HtmlReporter.Log("Launch Browser", browserType+" browser started ", "Pass");
                driver.get(URL);
            }else if(browserType.equalsIgnoreCase("GridChrome")|| browserType.equalsIgnoreCase("GridGC")){

                java.net.URL hubUrl = new URL("http://localhost:4444/wd/hub");
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setBrowserName("Google Chrome");
                //capabilities.setCapability("ignoreProtectedModeSettings", true);
                //capabilities.setCapability("initialBrowserUrl", URL);

                File file = new File(PublicVariables.BrowserPathGC);
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                driver = new RemoteWebDriver(hubUrl, (Capabilities) capabilities);
                Log.info("Chrome browser started ");

                HtmlReporter.Log("Launch Browser", browserType+ " browser started ", "Pass");
                driver.get(URL);

            }else if (browserType.equals("html")){
                System.out.println("Browser Type "+browserType);
                driver = new HtmlUnitDriver();
                driver.get(htmlDriver);
            }else {
                Log.info("Browser Type " + browserType +"Unsupported ");
            }
            driver.manage().window().maximize();
            int implicitWaitTime = (16);
            driver.manage().timeouts().implicitlyWait(pageTimeOut, TimeUnit.SECONDS);




        }catch(IOException e){
            Log.info(" Not Able to open the browser ----" + e.getMessage());
            HtmlReporter.Log("Open Browser Driver", "Not Able to Open the Browser ---", "Fail");
            Assert.assertTrue(false, "Not Able to open the Browser---" + e.getMessage());
        }

        selenium = new WebDriverBackedSelenium(driver, URL);

        return driver;
    }


    /**
     * Getting Element
     * @param element
     * @return
     */
    public static String getPageFactoryLocatorString(WebElement element){
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 1);

        final String op = element.toString();
        if (op.contains("Proxy element for : org.openqa.selenium.support.pagefactory.DefaultElementLocator")){
            return null;
        }

        String[] vals = op.split("->");
        String lastVal = vals[1].trim().split(": ")[1].toString().trim();
        driver.manage().timeouts().implicitlyWait(PublicVariables.pageTimeOut, TimeUnit.SECONDS);
        return lastVal.trim().substring(0, lastVal.trim().length()-1).trim().toString();

    }

    /**
     * Getting Web element
     * @param element
     * @param timeOut
     * @return
     */
    public static String getPageFactoryLocatorString(WebElement element, int timeOut){
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver, timeOut);

    final String op = element.toString();

    if(op.contains("Proxy element for : org.openqa.selenium.support.pagefactory.DefaultElementLocator")){
        return  null;
    }

    String[] vals = op.toString().split("->");
    String lastVal = vals[1].trim().split(": ")[1].toString().trim();

    driver.manage().timeouts().implicitlyWait(PublicVariables.pageTimeOut, TimeUnit.SECONDS);
    return lastVal.trim().substring(0, lastVal.trim().length()-1).trim().toString();

}


    /**
     * Getting Identifier from Web Element
     * @param element
     * @return
     */
    public static String getPageFactoryLocatorIdentifier(WebElement element){
        String[] vals = element.toString().split(": ");
        String identifier = vals[1].split("->")[1].trim();

        return identifier;
}


}
