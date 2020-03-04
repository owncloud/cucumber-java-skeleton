package android;

import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import utils.LocProperties;
import utils.entities.OCFile;

public class FileListPage extends CommonPage{
    private String headertext_xpath = "//*[@text='ownCloud']";
    private String closeselection_id = "com.owncloud.android:id/action_mode_close_button";
    private String fab_id = "fab_expand_menu_button";
    private String createfolder_id = "fab_mkdir";
    private String uploadfab_id = "fab_upload";
    private String uploadoption_id = "files_linear_layout";
    private String downloaded_id = "com.owncloud.android:id/localFileIndicator";
    private String avoffline_id = "com.owncloud.android:id/localFileIndicator";


    private String shareoption_id = "com.owncloud.android:id/action_share_file";
    private String renameoption_id = "com.owncloud.android:id/action_rename_file";
    private String moveoption_id = "com.owncloud.android:id/action_move";
    private String copyoption_id = "com.owncloud.android:id/copy_file";
    private String removeoption_id = "com.owncloud.android:id/action_remove_file";
    private String avofflineoption_id = "com.owncloud.android:id/action_set_available_offline";
    private String syncoption_id = "com.owncloud.android:id/action_sync_account";
    private String syncoption_text = "Refresh account";

    private HashMap<String, String> operationsMap = new HashMap<String, String>();

    public FileListPage() {
        super();
        //Filling up the operations to have the key-value with name and id
        operationsMap.put("Share", shareoption_id);
        operationsMap.put("Rename", renameoption_id);
        operationsMap.put("Move", moveoption_id);
        operationsMap.put("Copy", copyoption_id);
        operationsMap.put("Remove", removeoption_id);
        operationsMap.put("Set as available offline", avofflineoption_id);
    }

    public void createFolder(){
        waitById(5, fab_id);
        driver.findElement(MobileBy.id(fab_id)).click();
        driver.findElement(MobileBy.id(createfolder_id)).click();
    }

    public void upload(){
        waitById(5, fab_id);
        driver.findElement(MobileBy.id(fab_id)).click();
        driver.findElement(MobileBy.id(uploadfab_id)).click();
        driver.findElement(MobileBy.id(uploadoption_id)).click();
    }

    public void pushFile(String itemName){
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath, LocProperties.getProperties().getProperty("testResourcesPath"));
        File app = new File(appDir, "io/cucumber/example-files/AAA.txt");

        try {
            driver.pushFile("/mnt/sdcard/Download/aaa.txt", app);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void executeOperation(String operation, String itemName){
        selectItemList(itemName);
        selectOperation(operation);
    }

    public void downloadAction(String itemName) {
        actions.click(matchByText(itemName)).perform();
    }

    public boolean isItemInList (String itemName) {
        return !driver.findElementsByAndroidUIAutomator("new UiSelector().text(\"" + itemName + "\");").isEmpty();
    }

    public boolean isHeader(){
        return !driver.findElements(By.xpath(headertext_xpath)).isEmpty();
    }

    public void selectItemList(String itemName) {
        matchByText(itemName);
        actions.clickAndHold(matchByText(itemName)).perform();
    }

    public void selectOperation(String operationName) {
        if (driver.findElementsByAndroidUIAutomator("new UiSelector().resourceId(\"" + operationsMap.get(operationName) + "\");").isEmpty()){
            //Operation inside menu, matching by name
            selectOperationMenu(operationName);
        } else {
            //Operation in toolbar, matching by id
            actions.click(matchById(operationsMap.get(operationName))).perform();
        }
    }

    public void browse(String folderName){
        actions.click(matchByText(folderName)).perform();
    }

    public void closeSelectionMode(){
        MobileElement backArrow = (MobileElement)
                driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\""+closeselection_id+"\");");
        actions.click(backArrow).perform();
    }

    public boolean fileIsDownloaded(String fileName)  {
        //Checking file is downloaded inside the device
        try {
            byte[] downloadedFile = driver.pullFile("/sdcard/owncloud/" +
                    LocProperties.getProperties().getProperty("userName1") +
                    "@" +
                    URLEncoder.encode(LocProperties.getProperties().getProperty("hostName"), "UTF-8") + "/" +
                    fileName);
            return downloadedFile!=null && downloadedFile.length>0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fileIsMarkedAsDownloaded(String itemName){
        //Enforce this.. downloaded file must fit the itemName
        return driver.findElement(By.id(downloaded_id)).isDisplayed();
    }

    public boolean fileIsMarkedAsAvOffline(String itemName){
        //Enforce this.. av offline file must fit the itemName and distinguish from downloaded
        waitById(3, avoffline_id);
        return driver.findElement(By.id(avoffline_id)).isDisplayed();
    }

    private void selectOperationMenu(String operationName){
        MobileElement threeDotButton = (MobileElement)
                driver.findElementByAndroidUIAutomator("new UiSelector().description(\"More options\");");
        actions.click(threeDotButton).perform();
        actions.click(matchByText(operationName)).perform();
    }

    public void selectFileUpload(String itemName){
        /*MobileElement hamburger = (MobileElement)
                driver.findElementByAndroidUIAutomator("new UiSelector().description(\"Show roots\");");
        actions.click(hamburger).perform();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"Show roots\");")).click();
        //MobileElement downloadOption = (MobileElement)
        //driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Show roots\");")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Downloads\");")).click();
                //driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Downloads\");");
        //actions.click(downloadOption).perform();
        AndroidElement itemToUpload = (AndroidElement)
                driver.findElementByAndroidUIAutomator("new UiSelector().text(\""+ itemName +"\");");
        actions.click(itemToUpload).perform();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\""+ itemName +"\");")).click();
        //Give some seconds to perform the action to return to the file list
        //waitByXpath(8, headertext_xpath);*/
    }

    public boolean displayedList(ArrayList<OCFile> listServer){
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().description(\"More options\");")).click();
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"" + syncoption_text + "\");").click();
        Iterator iterator = listServer.iterator();
        while (iterator.hasNext()){
            OCFile ocfile = (OCFile) iterator.next();
            if(ocfile.getName().equals(LocProperties.getProperties().getProperty("userName1"))) {
                continue;
            } else if (!isItemInList(ocfile.getName())){
                return false;
            }
        }
        return true;
    }

}
