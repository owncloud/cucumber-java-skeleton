package android;

import org.openqa.selenium.By;

import java.util.logging.Level;

import utils.log.Log;

public class ChromeCustomTabPage extends CommonPage {

    private String username_xpath = "//input[@id='user']";
    private String password_xpath = "//input[@id='password']";
    private String submit_xpath = "//input[@id='submit']";
    private String authorize_xpath = "//body[@id=\"body-login\"]/div[1]/div/span/form/button";
    private String switch_xpath = "//body[@id=\"body-login\"]/div[1]/div/span/a/button";
    private String icon_xpath = "//android.webkit.WebView[@content-desc=\"ownCloud\"]/android.view.View[1]/android.view.View";

    public ChromeCustomTabPage(){
    }

    public void enterCredentials(String username, String password){
        Log.log(Level.FINE, "Starts: enter OAuth2 credentials");

        //waitForWebContext();
        waitByXpath(10, icon_xpath);
        driver.context("WEBVIEW_chrome");

        //switch button to go back to credentials
        if (!driver.findElements(By.xpath(switch_xpath)).isEmpty())
            driver.findElementByXPath(switch_xpath).click();

        waitByXpath(5,username_xpath);
        driver.findElement(By.xpath(username_xpath)).sendKeys(username);
        driver.findElement(By.xpath(password_xpath)).sendKeys(password);
        driver.findElement(By.xpath(submit_xpath)).click();

    }

    public void authorize(){
        Log.log(Level.FINE, "Starts: Authorize OAuth2");

        //Valid from device with chrome with context change
        waitByXpath(5, authorize_xpath);
        driver.findElement(By.xpath(authorize_xpath)).click();
        driver.context("NATIVE_APP");


    }
}