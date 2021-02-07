//CS 3250 Showcase TR 34-53
//Jennifer Khuu (jtk2eh)

package showcase;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;     // for Firefox
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;

public class showcase {
    private WebDriver driver;
    private WebDriverWait wait;
    private String auth = "https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?redirect_uri=https%3A%2F%2Fdevelopers.google.com%2Foauthplayground&prompt=consent&response_type=code&client_id=407408718192.apps.googleusercontent.com&scope=email&access_type=offline&flowName=GeneralOAuthFlow";
    private String create = "https://calendar.google.com/calendar/u/0/r/eventedit";
    private long currentTimestamp;
    private String defaultstart;
    private String defaultend;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private int offset;
    
    @BeforeEach
    void setUp() throws Exception {
        // driver setup
        System.setProperty("webdriver.chrome.driver", "/Users/jenniferkhuu/Downloads/chromedriver");    // configure path to the driver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver,30);

        // login at first website (Google OAuth Playground)
        driver.get(auth);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("identifier")));
        driver.findElement(By.name("identifier")).sendKeys("megjennifer1@gmail.com");
        driver.findElement(By.name("identifier")).sendKeys(Keys.RETURN);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
        driver.findElement(By.name("password")).sendKeys("computerscienceisfun");
        driver.findElement(By.name("password")).sendKeys(Keys.RETURN);

        // Login at second website (Google Calendar) 
        driver.navigate().to(create);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("identifier")));
        driver.findElement(By.name("identifier")).sendKeys("megjennifer1@gmail.com");
        driver.findElement(By.name("identifier")).sendKeys(Keys.RETURN);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
        driver.findElement(By.name("password")).sendKeys("computerscienceisfun");
        driver.findElement(By.name("password")).sendKeys(Keys.RETURN);
        
        // for dates
        String current = Instant.now().toString();
        year = Integer.parseInt(current.substring(0, 4));
        month = Integer.parseInt(current.substring(5,7));
        day = Integer.parseInt(current.substring(8,10));
        hour =  Integer.parseInt(current.substring(11,13));
        // minutes is rounded up to nearest half hour
        if (Integer.parseInt(current.substring(14,16)) < 30) {
            minutes =  30;
            offset = 0;
        }
        else {
            minutes = 0;
            offset = 3600;
        }
        
        // these are the default start and end times GCal gives when there's no specified input:
        currentTimestamp = LocalDateTime.of(year, month, day, hour, minutes, 00).atZone(ZoneId.of("UTC")).toEpochSecond()+offset;
        defaultstart = (currentTimestamp)+"";
        defaultend = (currentTimestamp+3600)+"";
        
    }


    // xpath to the description box
    private String descrip = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[2]/div[2]";
    // xpath to save button
    private String save = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div[1]/div[3]/span";
    // xpath to title
    private String title = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div[1]/div[2]/label/div[1]/div/input";
    // xpath to bold
    private String bold = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[3]/span/span";
    // xpath to calendar event
    //private String event = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div/div[2]/div[2]";
    private String event = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div/div/div[2]";
    // xpath to delete button
    private String delete = "/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[2]/div[2]/div";
    private String delete2="//*[@id=\"xDetDlgDelBu\"]";
    // xpath to start date box
    private String start_date_box = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div[1]/div[1]/div/label/div[1]/div/input";
    // xpath to repeat button
    private String repeat = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[1]/div[2]/div[2]/div/div[2]/div/div/div/div/div[1]/div[1]/div[1]";
    // xpath to event saved notification
    private String eventsaved = "/html/body/div[23]/div/div[1]";
    // xpath to delete all events button
    private String deleteallevents = "/html/body/div[4]/div[2]/div/div[2]/span/div/div[2]/span/label[3]/div";
    //xpath to busy
    private String busy="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[1]/div";
   
    @Test
    public void TR34() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR34 Event!");
        
        //click busy
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(busy)));
        driver.findElement(By.xpath(busy)).click();
        
        //set busy to free
        String free = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[1]/div/div[2]/div[2]/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(free)));
        driver.findElement(By.xpath(free)).click();
        driver.findElement(By.xpath(free)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(free)));

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(descrip), "info"));
        
        // save
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath(title),"My Big TR34 Event!"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR34 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for free 
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String free2="//*[@id=\"xDtlDlgCt\"]/div[5]";
        assertTrue(driver.findElement(By.xpath(free2)).isDisplayed(), "free not found");
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            driver.findElement(By.xpath(delete)).sendKeys(Keys.DELETE);
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
//            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR35() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR35 Event!");
        
        //click default visibility
        String default_visibility="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[2]/div/div[1]/div[1]/div[1]/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(default_visibility)));
        driver.findElement(By.xpath(default_visibility)).click();
        
        //set default visibility to public
        String public_visibility = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[2]/div/div[2]/div[2]/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(public_visibility)));
        driver.findElement(By.xpath(public_visibility)).click();
        driver.findElement(By.xpath(public_visibility)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(public_visibility)));

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR35 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for public visibility
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"xDetDlgPrv\"]")));
        assertTrue(driver.findElement(By.xpath("//*[@id=\"xDetDlgPrv\"]")).isDisplayed(), "public not found");
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR36() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR36 Event!");
        
        //click default visibility
        String default_visibility="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[2]/div/div[1]/div[1]/div[1]/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(default_visibility)));
        driver.findElement(By.xpath(default_visibility)).click();
        
        //set default visibility to private
        String private_visibility = "/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[2]/div/div[2]/div[3]/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(private_visibility)));
        driver.findElement(By.xpath(private_visibility)).click();
        driver.findElement(By.xpath(private_visibility)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(private_visibility)));

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR36 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
      
      //check for private visibility
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"xDetDlgPrv\"]")));
        assertTrue(driver.findElement(By.xpath("//*[@id=\"xDetDlgPrv\"]")).isDisplayed(), "private not found");
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR37() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR37 Event!");
        
     // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click help button
        WebElement help=driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[5]/div[2]/div[3]/div/span"));
        String expectedUrl = help.getAttribute("href");  
        help.click();
        
     // check opened tab 
        ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        try{
            assertEquals(expectedUrl, driver.getCurrentUrl());
          }
          catch(Throwable pageNavigationError){
          }
        driver.close();
        
        //go back to original tab
        driver.switchTo().window(tabs2.get(0));
        
     // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR38() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR38 Event!");
         
     // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click attach button
        String attach="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[1]/div/span/span";
        WebElement attach_element=driver.findElement(By.xpath(attach));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(attach)));
        attach_element.click();
        
        //check if right popup appears
        String selectAFile="/html/body/div[23]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selectAFile)));
        assertTrue(driver.findElement(By.xpath(selectAFile)).isDisplayed(), "Select A File popup not found");
        
        driver.quit();
    }
    
    @Test
    public void TR39() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR39 Event!");
        
        // click italics
        String italics="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[4]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(italics)));
        driver.findElement(By.xpath(italics)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR39 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for italicized description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info="/html/body/div[4]/div/div/div[2]/span/div/div/div[3]/div[2]/div[2]/i";
        WebElement el=driver.findElement(By.xpath(info));
        String fontWeight = el.getCssValue("font-weight");
        assertTrue(!fontWeight.equals("bold") || !fontWeight.equals("700"));
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
        
        driver.quit();
    }
    
    @Test
    public void TR40() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR40 Event!");
        
        // click underline
        String underline="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[5]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(underline)));
        driver.findElement(By.xpath(underline)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR40 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for underlined description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info="//*[@id=\"xDetDlgDesc\"]";
        WebElement el=driver.findElement(By.xpath(info));
        String fontWeight = el.getCssValue("font-weight");
        assertTrue(!fontWeight.equals("bold") || !fontWeight.equals("700"));
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR41() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR41 Event!");
        
        // click number
        String number="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[7]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(number)));
        driver.findElement(By.xpath(number)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR41 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for numbered description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info="//*[@id=\"xDetDlgDesc\"]";
        WebElement el=driver.findElement(By.xpath(info));
        String fontWeight = el.getCssValue("font-weight");
        assertTrue(!fontWeight.equals("bold") || !fontWeight.equals("700"));
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR42() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR42 Event!");
        
        // click bullet
        String bullet="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[8]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bullet)));
        driver.findElement(By.xpath(bullet)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR42 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for bulleted description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info="//*[@id=\"xDetDlgDesc\"]";
        WebElement el=driver.findElement(By.xpath(info));
        String fontWeight = el.getCssValue("font-weight");
        assertTrue(!fontWeight.equals("bold") || !fontWeight.equals("700"));
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    
    @Test
    public void TR43() {
     // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR43 Event!");
        
        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        // valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        // insert link
        String insert_link="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[10]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(insert_link)));
        driver.findElement(By.xpath(insert_link)).click();
        
        // confirm that the correct popup opens
        String editlinkpopup = "/html/body/div[4]/div/div/div[2]";
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(editlinkpopup)));
        assertTrue(driver.findElement(By.xpath(editlinkpopup)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR44() {
        //valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR44 Event!");
        
        //click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();
        
        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(descrip), "info"));
        
        //click remove formatting
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).click();
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.HOME).build().perform();
        String info="info";
        actions.keyDown(Keys.LEFT_SHIFT);
        for (int i = 0; i < info.length(); i++){
            actions.sendKeys(Keys.ARROW_RIGHT);
            }
        actions.keyUp(Keys.LEFT_SHIFT);
        actions.build().perform();
        String remove_formatting="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[1]/div[6]/div[2]/div/div[1]/div[1]/div[11]/span/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(remove_formatting)));
        driver.findElement(By.xpath(remove_formatting)).click();
        
     // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR44 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
     
        //check for non-bold description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info_loc="//*[@id=\"xDetDlgDesc\"]";
        WebElement el=driver.findElement(By.xpath(info_loc));
        String fontWeight = el.getCssValue("font-weight");
        assertTrue(!fontWeight.equals("bold") || !fontWeight.equals("700"));
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    
    }
    
    @Test
    public void TR45() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR45 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(save)));
        
        // confirm correct page for testing
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(eventsaved)));
        if (driver.getCurrentUrl() != "https://calendar.google.com/calendar/u/0/r/agenda")
            driver.navigate().to("https://calendar.google.com/calendar/u/2/r/agenda");
        
        // once page loads, check page source for event
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(event)));
        assertTrue(driver.getPageSource().contains("My Big TR45 Event!"), "title not found");
        assertTrue(driver.getPageSource().contains(defaultstart), "start timestamp not found: " + defaultstart);
        assertTrue(driver.getPageSource().contains(defaultend), "end timestamp not found: " + defaultend);
        
        //check for no description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
        driver.findElement(By.xpath(event)).click();
        String info_loc="//*[@id=\"xDetDlgDesc\"]";
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(info_loc))); 
        
        String cancel="/html/body/div[4]/div/div/div[2]/span/div/div/div[1]/div/div/div[1]/div/span";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cancel)));
        driver.findElement(By.xpath(cancel)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cancel)));
        WebDriverWait wait = new WebDriverWait(driver,30);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(event)));
            driver.findElement(By.xpath(event)).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(delete2)));
            driver.findElement(By.xpath(delete2)).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(event)));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {}
            
        
        driver.quit();
    }
    
    @Test
    public void TR46() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR46 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        //wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(descrip), "info"));
        
        //invalid text in add guest input
        String guest="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/span/div[1]/div/span/div/div[1]/div[2]/div[1]/div/div[1]/input";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(guest)));
        driver.findElement(By.xpath(guest)).sendKeys("th1s...i5-#s&w0rds=that?r\\\\inva1id");
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath(guest), "th1s...i5-#s&w0rds=that?r\\\\inva1id"));

        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        
        //check to see if current page is still create event
       assertTrue(driver.getCurrentUrl().contentEquals(create));
        
        driver.quit();
    }
    
    @Test
    public void TR47() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR47 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //invalid text in add guest input
        String guest="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/span/div[1]/div/span/div/div[1]/div[2]/div[1]/div/div[1]/input";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(guest)));
        driver.findElement(By.xpath(guest)).sendKeys("jtk2eh@virginia.edu");
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath(guest), "jtk2eh@virginia.edu"));

        // save
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(save)));
        driver.findElement(By.xpath(save)).click();
        
        //check to see if correct popup appears
        //String guestPopup="//*[@id=\"b1w9Rc\"]";
        String guestPopup="//*[@id=\"yDmH0d\"]/div/div/div[2]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(guestPopup)));
        assertTrue(driver.findElement(By.xpath(guestPopup)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR48() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR48 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click Google Apps button
        String googleApps="/html/body/div[2]/div[1]/div[1]/header/div[2]/div[3]/div[1]/div[1]/div/div/a";
        driver.findElement(By.xpath(googleApps)).click();
        
        //check to see if correct popup appears
        String googleAppsPopup="/html/body/div[2]/div[1]/div[1]/header/div[2]/div[3]/div[1]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleAppsPopup)));
        assertTrue(driver.findElement(By.xpath(googleAppsPopup)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR49() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR49 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click profile button
        String profile="/html/body/div[2]/div[1]/div[1]/header/div[2]/div[3]/div[1]/div[2]/div/a/img";
        driver.findElement(By.xpath(profile)).click();
        
        //check to see if correct popup appears
        String profilePopup="/html/body/div[2]/div[1]/div[1]/header/div[2]/div[3]/div[1]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(profilePopup)));
        assertTrue(driver.findElement(By.xpath(profilePopup)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR50() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR50 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click Find a Time button
        String findATime="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div[2]/span/span";
        driver.findElement(By.xpath(findATime)).click();
        
        //check to see if correct popup appears
        String findATimePopup="/html/body/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/span[2]/div/div/div[2]/div/div/div[1]/div[2]/div[2]/div/div[2]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(findATimePopup)));
        assertTrue(driver.findElement(By.xpath(findATimePopup)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR51() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR51 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        
        //click hide side panel button
        String hideSide="/html/body/div[2]/div[1]/div[1]/div[2]/div[5]/div/div[2]\n";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(hideSide)));
        driver.findElement(By.xpath(hideSide)).click();
        
        //check to see if side panel is hidden
        String sidePanel="/html/body/div[2]/div[1]/div[1]/div[2]/div[4]/div[1]/div";
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(sidePanel)));
        assertFalse(driver.findElement(By.xpath(sidePanel)).isDisplayed());
        
        driver.quit();
    }
    
    @Test
    public void TR52() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR52 Event!");
        
        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(descrip), "info"));
        
        //click back button
        driver.navigate().back();
        
      //Wait for the alert to be displayed and store it in a variable
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        //Press the OK button
        alert.accept();
        
        //goes to correct url
        assertEquals(driver.getCurrentUrl(), "https://calendar.google.com/calendar/u/0/r/eventedit?pli=1");
        
        driver.quit();
    }
    
    @Test
    public void TR53() {
        // valid text in title
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(title)));
        driver.findElement(By.xpath(title)).sendKeys("My Big TR53 Event!");

        // click bold
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(bold)));
        driver.findElement(By.xpath(bold)).click();

        //valid text in description
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(descrip)));
        driver.findElement(By.xpath(descrip)).sendKeys("info");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(descrip), "info"));
        
        //click back button
        driver.navigate().back();
        
      //Wait for the alert to be displayed and store it in a variable
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        //Press the cancel button
        alert.dismiss();
        
        //goes to correct url
        assertEquals(driver.getCurrentUrl(), create);
        
        driver.quit();
    }
    
}
