package com.flipkarttest.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

abstract class CommonHeader {
	private WebDriver driver;
	
	@FindBy(css = "input[name='q']")
	private WebElement searchBox;
	
	@FindBy(xpath = "//button[@type='submit']")
	private WebElement searchBtn;
	
	protected CommonHeader(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/*Search a Term in SearchBox*/
	public void search(String query) {
		searchBox.sendKeys(query);
		searchBtn.click();
	}
}
