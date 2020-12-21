package com.smoke.pages;

import com.smoke.tests.SmokeTests;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the Rent Results Page
 */
public class RentResultsPage extends BasePage {

    private final static Logger logger = LogManager.getLogger(RentResultsPage.class);

    @FindBy(id = "price_range_container")
    private WebElement priceRangeContainer;

    @FindBy(id = "priceFrom")
    private WebElement priceFromField;

    @FindBy(id = "priceTo")
    private WebElement priceToField;

    @FindBy(id = "area_range_container")
    private WebElement areaRangeContainer;

    @FindBy(id = "areaFrom")
    private WebElement areaFromField;

    @FindBy(id = "areaTo")
    private WebElement areaToField;

    @FindBy(id = "submit_search")
    private WebElement submitSearchButton;

    @FindBy(id = "r_sorting_link")
    private WebElement sortingLink;

    @FindBy(css = "#sorting_selection > ul > li:nth-child(2)")
    private WebElement descendingSortingOption;

    /**
     * Constructor
     * @param driver: webdriver
     */
    public RentResultsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checking if next pages exists
     * @param firstPage: condition so that we know if this is the first page of the results
     * @return: boolean - true if next page exists
     */
    public boolean nextPageExists(boolean firstPage) {


                List<WebElement> arrowButtons = driver.findElements(By.className("page_btn"));

                // if we are on the first page there are two page buttons
                // one for the last page, one for the next page
                if (firstPage) {
                    String nextPageLink = arrowButtons.get(1).getAttribute("href");
                    driver.navigate().to(nextPageLink);

                    return true;
                }

                // here there are 3 page buttons
                // one for the last page, one for the previous and one for the next
                else if (arrowButtons.size() > 1) {
                    String nextPageLink = arrowButtons.get(2).getAttribute("href");
                    driver.navigate().to(nextPageLink);

                    return true;
                }

                // there are no more next page buttons - meaning we are at the last page
                else {
                    return false;
                }
    }

    /**
     * Sets a price range for a filtered search
     */
    public void setPriceRange(String priceFrom, String priceTo) {

        // wait until element is ready
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("price_range_container")));

        // set price range
        priceRangeContainer.click();
        priceFromField.sendKeys(priceFrom);
        priceToField.sendKeys(priceTo);
    }

    /**
     * Sets an area range for a filtered search
     */
    public void setAreaRange(String areaFrom, String areaTo) {
        areaRangeContainer.click();
        areaFromField.sendKeys(areaFrom);
        areaToField.sendKeys(areaTo);
    }

    /**
     * Submit search
     */
    public void submitSearch() {
        submitSearchButton.click();
    }

    /**
     * Sets the sorting preference as descending
     */
    public void setDescendingSortOrder() {
        // wait until element is ready
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("r_sorting_link")));

        sortingLink.click();
        descendingSortingOption.click();
    }

    /**
     * Gets the home price - to be used to verify descending order
     * @param element: Result webElement
     * @return: The price
     */
    public int getHomePrice(WebElement element) {

        String elementXPath = "//*[@id='"
                + element.getAttribute("id")
                + "']/div/h1";

        String properties = driver.findElement(By.xpath(elementXPath)).getText();

        // extract the price from the text
        properties = properties.replaceAll("[^0-9|]", "");
        String[] tokens = properties.split("\\|");

        return Integer.parseInt(tokens[1]);
    }

    /**
     * Gets the all the results of the current page
     * @return: Number of results
     */
    public List<WebElement> getAllResults() {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[starts-with(@id,'r_')]")));

        return driver.findElements(By.xpath("//*[starts-with(@id,'r_')]"));
    }

    /**
     * Counts the images of a specific result element
     * @param element: Result element
     * @return: boolean - true if ok, false if more than 10 photos
     */
    public boolean countImages(WebElement element) throws RuntimeException {

        int count = 0;

        // mouse hover over result so that the image rotator arrow appears
        Actions mouseHover = new Actions(driver);
        mouseHover.moveToElement(element).build().perform();

        // using try-catch here because if the result element has only one photo
        // the image rotator will not appear and an exception will be thrown
        try {
            String elementXPath = "//article[@id='"
                    + element.getAttribute("id")
                    + "']/figure/div/span[2]";

            WebElement imageWheel = driver.findElement(By.xpath(elementXPath));

            // the image wheel will not be displayed if we arrive in the last photo
            while (imageWheel.isDisplayed()) {
                imageWheel.click();
                count++;

                // this sleep ensures that the image is ready and the rotator can move to
                // the next one. Otherwise it is clicked too fast and might not move to the
                // next image, losing the image count. wait.until is not working properly here
                // In my case 700ms could do the job, but because once it failed, I gave it 1 sec
                // so as to make sure it will not fail
                Thread.sleep(1000);
            }

            if (count > 9) {
                logger.error("Element with id = " + element.getAttribute("id") + " has more than 10 photos");
                return false;
            }

        }
        catch (Exception e) {

            // if the exception is not NoSuchElement then it is probably a runtime exception
            // not related to an element that has only one photo
            if (!e.getClass().getName().equals("org.openqa.selenium.NoSuchElementException")) {
                throw new RuntimeException();
            }
        }

        return true;
    }

    /**
     * Checks if the price and the square footage of an element are within specified range
     * @param element: Result element
     * @return: boolean - true if within range
     */
    public boolean checkIfWithinRange(WebElement element) {

            String elementXPath = "//*[@id='"
                    + element.getAttribute("id")
                    + "']/div/h1";

            String properties = driver.findElement(By.xpath(elementXPath)).getText();

            // extract the square footage and price from the text
            properties = properties.replaceAll("[^0-9|]", "");
            String[] tokens = properties.split("\\|");
            int squareFootage = Integer.parseInt(tokens[0]);
            int price = Integer.parseInt(tokens[1]);

            // check that squareFootage and price are within specified range
            if(squareFootage < 75 || squareFootage > 150
                    || price < 200 || price > 700 ) {
                logger.error("Element with id: "
                        + element.getAttribute("id")
                        + " is not within squareFootage/price range!");
                return false;
            }

        return true;
    }

    /**
     * Checks that phone number is visible after clicking on the button
     * @param element: Result element
     * @return: boolean - true if visible after clicking
     */
    public boolean checkPhoneNumber(WebElement element) {

        // open the link in new tab, Keys.Chord string passed to sendKeys
        String clicklnk = Keys.chord(Keys.CONTROL, Keys.ENTER);


        // we need to try all possible areas to find the link to navigate to
        List<String> areas = new ArrayList<>();
        areas.add("neo-pagkrati");
        areas.add("pagkrati");
        areas.add("profitis-ilias-pagkrati");

        for (String area : areas) {
            try {

                driver.findElement(By.xpath("//a[contains(@href,\"/property/enoikiaseis|katoikies|" +
                        area +
                        "|" +
                        element.getAttribute("id").replaceFirst("^r_", "") +
                        ".html\")]"))
                        .sendKeys(clicklnk);
                break;
            }
            catch (Exception ignored) {

            }
        }


        // switch to the new tab that contains each specific ad
        ArrayList<String> tabs2 = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));

        AdElementPage adElementPage = new AdElementPage(driver);

        // check that phone number is hidden
        if(!adElementPage.checkPhoneNumberHidden()) {
            logger.error("Phone number should be hidden before clicking!");
            driver.close();
            driver.switchTo().window(tabs2.get(0));
            return false;
        }

        adElementPage.clickOnButtonToShowPhoneNumber();

        // check that phone number appears after clicking
        if(adElementPage.checkPhoneNumberHidden()) {
            logger.error("Phone number should be visible after clicking!");
            driver.close();
            driver.switchTo().window(tabs2.get(0));
            return false;
        }

        driver.close();
        driver.switchTo().window(tabs2.get(0));

        return true;

    }

}
