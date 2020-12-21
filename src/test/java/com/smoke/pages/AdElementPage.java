package com.smoke.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for the Ad Elements Page
 */
public class AdElementPage extends BasePage {

    @FindBy(css = "#outer_canvas > section > div.grid-container > div > div.contact-info > div:nth-child(2)" +
            " > div.phone-area > div")
    private WebElement phoneNumber;

    @FindBy(id = "to-form")
    private WebElement showPhoneNumberButton;

    /**
     * Constructor
     * @param driver: Webdriver
     */
    public AdElementPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks whether the phone of the ad is hidden
     * @return: boolean - true if hidden, false if visible
     */
    public boolean checkPhoneNumberHidden() {
        return !phoneNumber.isDisplayed();
    }


    /**
     * Clicks on the phone button that makes the phone number appear
     */
    public void clickOnButtonToShowPhoneNumber() {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.elementToBeClickable(showPhoneNumberButton));

        showPhoneNumberButton.click();
    }

}
