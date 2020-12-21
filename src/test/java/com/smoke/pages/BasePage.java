package com.smoke.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for the web pages
 * Contains some basic functions that each page might use
 */
public class BasePage {

    protected final WebDriver driver;

    /**
     * Constructor
     * @param driver: webdriver
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    /**
     * Gets the title of the page
     * @return: String with the title
     */
    public String getTitle() { return driver.getTitle(); }

}
