package com.smoke.tests;

import com.smoke.pages.PropertyPage;
import com.smoke.pages.RentResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertTrue;

/**
 * Smoke Tests suite
 */
public class SmokeTests {

    public static WebDriver driver = null;
    private final static Logger logger = LogManager.getLogger(SmokeTests.class);


    // in case of parallel execution the driver might be needed to be setup in a different way
    @BeforeClass
    void setup() {
        System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        logger.info("Starting SmokeTests Suite...");
    }

    @AfterClass
    void tearDown() {
        driver.close();
        driver.quit();
        logger.info("Finished SmokeTests Suite ...\n");
    }

    @Test
    void PagkratiSearchAdsTest() {
        logger.info("Starting PagkratiSearchAdsTest...");

        // navigate to the xe property page
        driver.navigate().to("https://www.xe.gr/property/");

        // PROPERTY PAGE
        PropertyPage propertyPage = new PropertyPage(driver);

        // select rent/type options, choose search areas and press search button
        propertyPage.chooseRentTypeOptions();
        propertyPage.chooseSuggestedPagkratiAreas();
        propertyPage.pressSearchButton();

        // RENT RESULTS PAGE
        RentResultsPage rentResultsPage = new RentResultsPage(driver);

        // set price and area range for a filtered search and submit search
        rentResultsPage.setPriceRange("200", "700");
        rentResultsPage.setAreaRange("75", "150");
        rentResultsPage.submitSearch();

        // set descending sort order
        rentResultsPage.setDescendingSortOrder();

        // setting up some variables
        boolean nextPageExistsCondition = true, firstPage = true;
        int countPages = 0;
        AtomicInteger countElements = new AtomicInteger();

        // for each page that has results
        while (nextPageExistsCondition) {

            // get all elements of the page
            List<WebElement> elements = rentResultsPage.getAllResults();
            assertTrue(elements.size() > 0);

            AtomicInteger currentElementPrice = new AtomicInteger(0);
            AtomicInteger previousElementPrice = new AtomicInteger(0);

            // for each element of the page
            elements.forEach(element -> {

                // exclude r_sorting_link which is not a result
                if (!element.getAttribute("id").equals("r_sorting_link")) {

                    // keep track of current and previous price to validate descending order
                    currentElementPrice.set(rentResultsPage.getHomePrice(element));

                    if (previousElementPrice.get() == 0) {
                        previousElementPrice.set(currentElementPrice.get());
                    } else if (currentElementPrice.get() > previousElementPrice.get()) {
                        logger.error("Descending sorting error");
                        Assert.fail();
                    } else {
                        previousElementPrice.set(currentElementPrice.get());
                    }

                    // check that phone number appears after clicking at the phone button
                    // - making this check first because when rolling to count images xpath changes -
                assertTrue(rentResultsPage.checkPhoneNumber(element));

                    // assert each element has no more than 10 photos
                assertTrue(rentResultsPage.countImages(element));

                    // verify square meters and price within range
                assertTrue(rentResultsPage.checkIfWithinRange(element));

                countElements.getAndIncrement();
                }
            });

            // check if there is a next page
            nextPageExistsCondition = rentResultsPage.nextPageExists(firstPage);
            if (firstPage) {
                firstPage = false;
            }

            countPages++;
        }

        logger.info("Total pages: " + countPages);
        logger.info("Total results parsed: " + countElements);
        logger.info("Finished PagkratiSearchAdsTest ...");
    }

}
