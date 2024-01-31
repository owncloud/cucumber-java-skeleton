/**
 * ownCloud Android Scenario Tests
 *
 * @author Jesús Recio Rincón (@jesmrec)
 */

package android;

import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utils.LocProperties;
import utils.entities.OCFile;
import utils.log.Log;

public class FileListPage extends CommonPage {

    private final String shareoption_id = "com.owncloud.android:id/action_share_file";
    private final String avofflineoption_id = "com.owncloud.android:id/action_set_available_offline";
    private final String unavofflineoption_id = "com.owncloud.android:id/action_set_unavailable_offline";
    private final String downloadoption_id = "com.owncloud.android:id/action_download_file";
    private final String syncoption_id = "com.owncloud.android:id/action_sync_file";
    private final String footer_id = "com.owncloud.android:id/footerText";

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.owncloud.android:id/action_mode_close_button\");")
    private MobileElement closeSelectionMode;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_expand_menu_button")
    private MobileElement fabButton;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar_left_icon")
    private List<MobileElement> hamburgerButton;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_mkdir")
    private MobileElement createFolder;

    @AndroidFindBy(id = "com.owncloud.android:id/fab_upload")
    private MobileElement uploadOption;

    @AndroidFindBy(id = "com.owncloud.android:id/upload_from_files_item_view")
    private MobileElement uploadFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/root_toolbar")
    private List<MobileElement> toolbar;

    @AndroidFindBy (id = "com.owncloud.android:id/bottom_nav_view")
    MobileElement bottomBar;

    @AndroidFindBy(id = "com.owncloud.android:id/list_root")
    private MobileElement listFiles;

    @AndroidFindBy(id = "com.owncloud.android:id/file_list_constraint_layout")
    private MobileElement fileCell;

    @AndroidFindBy(id = "com.owncloud.android:id/Filename")
    private MobileElement fileName;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_keep_both")
    private MobileElement keepBoth;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_replace")
    private MobileElement replace;

    @AndroidFindBy(id = "com.owncloud.android:id/dialog_file_already_exists_skip")
    private MobileElement skip;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_shared_by_link_files")
    private MobileElement linksShortcut;

    @AndroidFindBy(id = "com.owncloud.android:id/nav_available_offline_files")
    private MobileElement avOffShortcut;

    @AndroidFindBy (id = "nav_spaces")
    MobileElement spacesTab;

    @AndroidFindBy (id = "nav_uploads")
    MobileElement uploads;

    @AndroidFindBy (id = "com.owncloud.android:id/list_empty_dataset_title")
    MobileElement emptyMessage;

    @AndroidFindBy (id = "com.owncloud.android:id/dialog_file_already_exists_title")
    MobileElement conflictTitle;

    public FileListPage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void refreshList() {
        Log.log(Level.FINE, "Refresh list");
        waitById(5, bottomBar);
        swipe(0.50, 0.70, 0.50, 0.95);
    }

    public void waitToload(String itemName) {
        Log.log(Level.FINE, "Waiting to load");
        String listFiles_id = "com.owncloud.android:id/list_root";
        try {
            //if list of files is not loaded, we should swipe to get the file list
            waitById(5, listFiles_id);
        } catch (Exception e) {
            Log.log(Level.FINE, "Swipe needed to get the list");
            refreshList();
        }
        Log.log(Level.FINE, "Loaded");
    }

    public void createFolder() {
        Log.log(Level.FINE, "Starts: create folder");
        refreshList();
        fabButton.click();
        createFolder.click();
    }

    public void uploadFiles() {
        Log.log(Level.FINE, "Starts: upload");
        fabButton.click();
        uploadOption.click();
        uploadFiles.click();
    }

    public void pushFile(String itemName) {
        Log.log(Level.FINE, "Starts: push file: " + itemName);
        File rootPath = new File(System.getProperty("user.dir"));
        File appDir = new File(rootPath,"src/test/resources");
        File app = new File(appDir, "io/cucumber/example-files/" + itemName);
        try {
            driver.pushFile("/sdcard/Download/" + itemName, app);
            Log.log(Level.FINE, "File " + itemName + " pushed");
        } catch (IOException e) {
            Log.log(Level.SEVERE, "IO Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeOperation(String operation, String itemName) {
        Log.log(Level.FINE, "Starts: execute operation: " + operation + " " + itemName);
        waitToload(itemName);
        selectItemList(itemName);
        selectOperation(operation);
    }

    public void downloadAction(String itemName) {
        Log.log(Level.FINE, "Starts: download action: " + itemName);
        if (!isItemInList(itemName)) {
            Log.log(Level.FINE, "Searching item... swiping: " + itemName);
            refreshList();
        }
        findUIAutomatorText(itemName).click();
    }

    public boolean isItemInList(String itemName) {
        Log.log(Level.FINE, "Starts: Check if item is in list: " + itemName);
        waitById(5, bottomBar);
        return !findListUIAutomatorText(itemName).isEmpty();
    }

    public boolean isFileListVisible() {
        Log.log(Level.FINE, "Starts: is File list Visible");
        return !toolbar.isEmpty();
    }

    public boolean errorDisplayed(String error) {
        Log.log(Level.FINE, "Starts: Error displayed: " + error);
        return findUIAutomatorSubText(error).isDisplayed();
    }

    public void selectItemList(String path) {
        Log.log(Level.FINE, "Starts: select item from list: " + path);
        String fileName = path.contains("/") ? browseToFile(path) : path;
        MobileElement element = getElementFromFileList(fileName);
        waitByTextVisible(5, fileName);
        longPress(element);
    }

    public void selectOperation(String operationName) {
        if (operationName.equals("share")) {  //placed in toolbar
            findId(shareoption_id).click();
        } else {
            Log.log(Level.FINE, "Operation: " + operationName + " placed in menu");
            selectOperationMenu(operationName);
        }
    }

    public void longPress(String itemName) {
        longPress(findUIAutomatorText(itemName));
    }

    //Select once multiselection mode is on
    public void selectItem(String itemName) {
        Log.log(Level.FINE, "Starts: select item: " + itemName);
        findUIAutomatorText(itemName).click();
    }

    public void openLinkShortcut() {
        Log.log(Level.FINE, "Starts: open link shortcut");
        linksShortcut.click();
    }

    public void openAvOffhortcut() {
        Log.log(Level.FINE, "Starts: open av offline shortcut");
        waitByTextInvisible(5, "Download enqueued");
        avOffShortcut.click();
    }

    public void openSpaces(){
        spacesTab.click();
    }

    public void openUploadsView(){
        uploads.click();
    }

    public void closeSelectionMode() {
        Log.log(Level.FINE, "Starts: close selection mode");
        closeSelectionMode.click();
    }

    public boolean fileIsMarkedAsDownloaded(String path) {
        Log.log(Level.FINE, "Check if file is downloaded: " + path);
        selectItemList(path);
        List<MobileElement> downloadOptions = findListId(downloadoption_id);
        List<MobileElement> syncOptions = findListId(syncoption_id);
        return downloadOptions.isEmpty() && !syncOptions.isEmpty();
    }

    public boolean itemIsMarkedAsAvOffline(String path) {
        selectItemList(path);
        findUIAutomatorDescription("More options").click();
        return findListId(avofflineoption_id).isEmpty();
    }

    public boolean itemIsMarkedAsUnAvOffline(String path) {
        selectItemList(path);
        findUIAutomatorDescription("More options").click();
        return findListId(unavofflineoption_id).isEmpty();
    }

    private void openMenuActions(){
        findUIAutomatorDescription("More options").click();
    }

    private void selectOperationMenu(String operationName) {
        Log.log(Level.FINE, "Starts: Select operation from the menu: " + operationName);
        openMenuActions();
        findUIAutomatorText(operationName).click();
    }

    public boolean operationAvailable(String operationName){
        Log.log(Level.FINE, "Starts: Check if operation is available: " + operationName);
        openMenuActions();
        return !findListUIAutomatorText(operationName).isEmpty();
    }

    public boolean conflictDisplayed(){
        Log.log(Level.FINE, "Starts: Conflict displayed");
        return  conflictTitle.isDisplayed();
    }

    public void fixConflict(String option){
        Log.log(Level.FINE, "Starts: Fix Conflict: " + option);
        if (option.equals("replace")){
            replace.click();
        } else {
            keepBoth.click();
        }
    }

    public boolean displayedList(String path, ArrayList<OCFile> listServer) {
        browseToFolder(path); // Mover a la carpeta
        String userName1 = LocProperties.getProperties().getProperty("userName1");
        return listServer.stream().filter(
            ocfile -> !ocfile.getName().equalsIgnoreCase(userName1) && ocfile.getName().length() <= 15)
            .allMatch(ocfile -> {
                while (!isItemInList(ocfile.getName()) && !endList(listServer.size())) {
                    refreshList();
                }
                return isItemInList(ocfile.getName());
            });
    }

    private boolean endList(int numberItems) {
        return !findListUIAutomatorText(Integer.toString(numberItems - 1) + " files")
                .isEmpty();
    }

    private MobileElement getElementFromFileList (String itemName) {
        Log.log(Level.FINE, "Starts: searching item in list: " + itemName);
        if (isItemInList(itemName)) {
            Log.log(Level.FINE, "Item found: " + itemName);
            return findUIAutomatorText(itemName);
        } else {
            Log.log(Level.FINE, "Item not found: " + itemName);
            return null;
        }
    }

    public String getPrivateLink(String scheme, String linkOriginal) {
        Log.log(Level.FINE, "Starts: Create private link: " + scheme + " " + linkOriginal);
        String originalScheme = getScheme(linkOriginal);
        Log.log(Level.FINE, "Original scheme: " + originalScheme);
        String linkToOpen = linkOriginal.replace(originalScheme, scheme);
        String linkToOpen2 = linkToOpen.replace("%21", "!");
        Log.log(Level.FINE, "Link to open: " + linkToOpen + " " + linkToOpen2);
        return linkToOpen2;
    }

    private String getScheme(String originalURL){
        return originalURL.split("://")[0];
    }

    public void openPrivateLink(String privateLink) throws MalformedURLException {
        Log.log(Level.FINE, "Starts: Open private link: " + privateLink);
        driver.closeApp();
        wait (5);
        driver.get(privateLink);
        //Let some time to load... did not found a reliable condition to avoid this ugly wait
        wait (5);
    }

    public void openFakePrivateLink() {
        Log.log(Level.FINE, "Starts: Open fake private link");
        String originalScheme = getScheme(System.getProperty("server"));
        String fakeURL = System.getProperty("server").replace(originalScheme, "owncloud") + "/f/11111111111";
        Log.log(Level.FINE, "Fake URL: " + fakeURL);
        driver.get(fakeURL);
        //Let some time to load... did not found a reliable condition to avoid this ugly wait
        wait (5);
    }

    public boolean itemOpened(String itemType, String itemName) {
        Log.log(Level.FINE, "Starts: checking if item is opened: " + itemType + " " + itemName);
        if (itemType.equals("file")) {
            Log.log(Level.FINE, "Opening file");
            boolean fileNameVisible = findUIAutomatorText(itemName).isDisplayed();
            boolean fileTypeIconVisible = findId("com.owncloud.android:id/fdImageDetailFile").isDisplayed();
            return fileNameVisible && fileTypeIconVisible;
        } else if (itemType.equals("folder")) {
            Log.log(Level.FINE, "Opening folder");
            boolean folderNameVisible = findUIAutomatorText(itemName).isDisplayed();
            boolean hamburgerButtonVisible = hamburgerButton.size() > 0;
            return folderNameVisible && !hamburgerButtonVisible;
        }
        return false;
    }

}
