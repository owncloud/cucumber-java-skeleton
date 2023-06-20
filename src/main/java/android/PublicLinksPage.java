/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.support.PageFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.date.DateUtils;
import utils.log.Log;

public class PublicLinksPage extends CommonPage {

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkNameValue")
    private MobileElement namePublicLink;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadOnly")
    private MobileElement downloadViewOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionReadAndWrite")
    private MobileElement downloadViewUploadOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkEditPermissionUploadFiles")
    private MobileElement uploadOnlyOption;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkPasswordSwitch")
    private MobileElement passwordSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkPasswordValue")
    private MobileElement textPassword;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkExpirationSwitch")
    private MobileElement expirationSwitch;

    @AndroidFindBy(id = "com.owncloud.android:id/shareViaLinkExpirationValue")
    private MobileElement expirationDate;

    @AndroidFindBy(id = "android:id/button1")
    private MobileElement okButton;

    @AndroidFindBy(id = "android:id/next")
    private MobileElement nextButton;

    @AndroidFindBy(id = "com.owncloud.android:id/cancelButton")
    private MobileElement cancelButton;

    @AndroidFindBy(id = "com.owncloud.android:id/saveButton")
    private MobileElement saveButton;

    public PublicLinksPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void addLinkName(String linkName) {
        Log.log(Level.FINE, "Starts: Add link name: " + linkName);
        namePublicLink.clear();
        namePublicLink.sendKeys(linkName);
    }

    public void setPermission(String permission) {
        Log.log(Level.FINE, "Starts: Set link permission: " + permission);
        switch (permission) {
            case ("1"): {
                downloadViewOption.click();
                break;
            }
            case ("15"): {
                downloadViewUploadOption.click();
                break;
            }
            case ("4"): {
                uploadOnlyOption.click();
                break;
            }
        }
    }

    public void selectDownloadView() {
        Log.log(Level.FINE, "Starts: Select Download / View");
        downloadViewOption.click();
    }

    public void selectDownloadViewUpload() {
        Log.log(Level.FINE, "Starts: Select Download / View / Upload");
        downloadViewUploadOption.click();
    }

    public void selectUploadOnly() {
        Log.log(Level.FINE, "Starts: Select Upload Only (File drop)");
        uploadOnlyOption.click();
    }

    public boolean checkPermissions(String permissions) {
        Log.log(Level.FINE, "Starts: Check permissions: " + permissions);
        switch (permissions) {
            case ("1"): {
                if (parseIntBool(downloadViewOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Download / View is selected");
                    return true;
                }
            }
            case ("15"): {
                if (parseIntBool(downloadViewUploadOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Download / View / Upload is selected");
                    return true;
                }
            }
            case ("4"): {
                if (parseIntBool(uploadOnlyOption.getAttribute("checked"))) {
                    Log.log(Level.FINE, "Upload only is selected");
                    return true;
                }
            }
        }
        return false;
    }

    public void addPassword(String itemName, String password) throws IOException, SAXException, ParserConfigurationException {
        Log.log(Level.FINE, "Starts: Add link password: " + password);
        //To avoid password keyboard to appear
        driver.hideKeyboard();
        passwordSwitch.click();
        textPassword.sendKeys(password);
    }

    public boolean isPasswordEnabled(String itemName) {
        boolean switchEnabled = true;
        boolean passVisible;
        switchEnabled = parseIntBool(passwordSwitch.getAttribute("checked"));
        passVisible = textPassword.isDisplayed();
        return switchEnabled && passVisible;
    }

    public void setExpiration(String days) {
        Log.log(Level.FINE, "Starts: Set Expiration date in days: " + days);
        expirationSwitch.click();
        int defaultExpiration = Integer.valueOf(days);
        String dateToSet = DateUtils.dateInDaysAndroidFormat(Integer.toString(defaultExpiration));
        Log.log(Level.FINE, "Days: " + days + ". Days to set: " + defaultExpiration + " Date to set: " + dateToSet);
        if (findListAccesibility(dateToSet).isEmpty()) {
            Log.log(Level.FINE, "Date not found, next page");
            nextButton.click();
        }
        findAccesibility(dateToSet).click();
        okButton.click();
    }

    public boolean checkExpiration(String days) {
        Log.log(Level.FINE, "Starts: Check expiration in days: " + days);
        boolean switchEnabled;
        boolean dateCorrect = false;
        int expiration = Integer.parseInt(days);
        String shortDate = DateUtils.shortDate(Integer.toString(expiration));
        Log.log(Level.FINE, "Date to check: " + shortDate + " Expiration: " + expiration);
        switchEnabled = parseIntBool(expirationSwitch.getAttribute("checked"));
        Log.log(Level.FINE, "SwitchEnabled -> " + switchEnabled);
        if (switchEnabled) {
            dateCorrect = expirationDate.getText().equals(shortDate);
        }
        Log.log(Level.FINE, "Date Correct -> " + dateCorrect);
        return switchEnabled && dateCorrect;
    }

    public void close() {
        Log.log(Level.FINE, "Starts: Cancel public link view");
        cancelButton.click();
    }

    public void submitLink() {
        Log.log(Level.FINE, "Starts: Submit public link");
        saveButton.click();
    }
}