package com.smoke.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Class for the home property page
 */
public class PropertyPage extends BasePage {

    @FindBy(xpath = "(//button[@type='button'])[8]")
    private WebElement propertyTransactionDropdown;

    @FindBy(xpath = "(//button[@type='button'])[9]")
    private WebElement rentButton;

    @FindBy(xpath = "(//button[@type='button'])[14]")
    private WebElement propertyTypeDropdown;

    @FindBy(xpath = "(//button[@type='button'])[15]")
    private WebElement homeTypeButton;

    @FindBy(id = "areas")
    private WebElement searchAreas;

    @FindBy(css = "body > main > div.home-search-container > div > div.cinema-container > div > div > " +
            "div.cinema-main-content.property-tab > div.search-form > form > div.cell.small-12.locatio" +
            "n-input.relative-wrapper > div.selected-items.grid-x.closed")
    private WebElement searchAreasAfterFirstChoice;

    @FindBy(css = "body > main > div.home-search-container > div > div.cinema-container > div > div > " +
            "div.cinema-main-content.property-tab > div.search-form > form > div.cell.small-12.locatio" +
            "n-input.relative-wrapper.active > div.areas-dropdown-container > div.dropdown-panel > lab" +
            "el:nth-child(1) > p")
    private WebElement pagkratiArea1;

    @FindBy(css = "body > main > div.home-search-container > div > div.cinema-container > div > div > " +
            "div.cinema-main-content.property-tab > div.search-form > form > div.cell.small-12.locatio" +
            "n-input.relative-wrapper.active > div.areas-dropdown-container > div.dropdown-panel > lab" +
            "el:nth-child(2) > p")
    private WebElement pagkratiArea2;


    @FindBy(css = "body > main > div.home-search-container > div > div.cinema-container > div > div > " +
            "div.cinema-main-content.property-tab > div.search-form > form > div.cell.small-12.locatio" +
            "n-input.relative-wrapper.active > div.areas-dropdown-container > div.dropdown-panel > lab" +
            "el:nth-child(3) > p")
    private WebElement pagkratiArea3;

    @FindBy(className = "button-property")
    private WebElement seachButton;

    /**
     * Constructor
     * @param driver: Webdriver
     */
    public PropertyPage(WebDriver driver) {
        super(driver);
    }

    public void chooseRentTypeOptions() {
        propertyTransactionDropdown.click();
        rentButton.click();
        propertyTypeDropdown.click();
        homeTypeButton.click();
    }

    /**
     * Selects the suggested Pagkrati Areas
     */
    public void chooseSuggestedPagkratiAreas() {
        // choose the first suggested area
        searchAreas.sendKeys("Pagkrati");
        pagkratiArea1.click();

        // choose the second suggested area
        searchAreasAfterFirstChoice.click();
        searchAreas.sendKeys("Pagkrati");
        pagkratiArea2.click();

        // choose the third suggested area
        searchAreasAfterFirstChoice.click();
        searchAreas.sendKeys("Pagkrati");
        pagkratiArea3.click();
    }

    /**
     * Clicks on the Search Button to go to the Results page
     */
    public void pressSearchButton() {
        seachButton.click();
    }


}
