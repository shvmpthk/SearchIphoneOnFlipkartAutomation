package com.flipkarttest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends CommonHeader{
	
	private WebDriver driver;
	private Actions action;
	private WebDriverWait wait;
	
	public HomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.action = new Actions(driver);
		PageFactory.initElements(driver, this);
	}
	
	/*Check Login Popup*/
	public boolean loginPopUpExists() {
		wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@tabindex='-1']/div"))));
		}catch(TimeoutException | NoSuchElementException te) {
			return false;
		}
		return true;
	}
	
	/*Close Login Pup up*/
	public boolean closeLoginPopup() {
		try {		
			driver.findElement(By.xpath("//div[@tabindex='-1']/div/button")).click();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
}