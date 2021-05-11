package com.flipkarttest.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResultPage extends CommonHeader{
	
	private WebDriver driver;
	private Actions action;
	private WebDriverWait wait;
	private JavascriptExecutor jsDriver;
	
	public SearchResultPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.action = new Actions(driver);
		this.jsDriver = (JavascriptExecutor)driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//a[@title='Mobiles']")
	private WebElement mobilesFilter;
	
	@FindBy(xpath = "(//select)[2]")
	private WebElement maxPriceSelect;
	
	@FindBy(xpath = "//section[contains(.,'Availability')]")
	private WebElement availabilitySection;
	
	@FindBy(xpath = "//section/div[contains(.,'Availability')]/following-sibling::div//input")
	private WebElement excludeOutOfStockCheckBox;
	
	@FindBy(xpath = "//a[@class='_1fQZEK']")
	private List<WebElement> searchResult;
	
	public void applyMobileFilter() {
		mobilesFilter.click();
	}
	
	public void applyPriceFilter(int inr) {
		Select maxPriceSelector = new Select(maxPriceSelect);
		List<WebElement> allOptions= maxPriceSelector.getOptions();
		Iterator<WebElement> itr = allOptions.iterator();

		while(itr.hasNext()) {
			WebElement option = itr.next();
			String justTheNumberPart = option.getText().replaceAll("[^\\dA-Za-z ]", ""); //"[^\\dA-Za-z ]" "[\u20B9+]" 
			Integer priceInOption = Integer.parseInt(justTheNumberPart); 
			if(priceInOption>=inr) { 
				maxPriceSelector.selectByValue(priceInOption.toString());
				break;
			}
		}
	}
	
	public void appplyAvailabilityFilter() {
		wait = new WebDriverWait(driver, 3);
		jsDriver.executeScript("arguments[0].scrollIntoView();",availabilitySection);//window.scrollBy(0,-50);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='_1XXPTY _1v2cG7']"))); //--loader
		wait.until(ExpectedConditions.elementToBeClickable(availabilitySection));
		availabilitySection.click();
		jsDriver.executeScript("arguments[0].click();",excludeOutOfStockCheckBox);
		jsDriver.executeScript("window.scrollTo(0, 30);");
	}
	
	public List<WebElement> getResultListOnCurrentPage() {
		return searchResult;
	}

	public int getProductPrice(WebElement mobile) {
		return Integer.parseInt(mobile.findElement(By.xpath("//div[@class='_30jeq3 _1_WHN1']")).getText().replaceAll("[^\\dA-Za-z ]", ""));
	}
	
	public List<String[]> getResultDetails() {
		wait = new WebDriverWait(driver, 3);
		int totaldevices = Integer.parseInt(driver.findElement(By.xpath("//span[@class='_10Ermr']")).getText().split(" ")[5]);
		
		List<WebElement> mobiles;
		List<String[]> allDetails= new ArrayList<String[]>();
		Boolean nextPageExists =false;
		
		allDetails.add(new String[]{"Device Details","Price","Ratings"}); 
		do {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='_10Ermr']")));
			mobiles= driver.findElements(By.xpath("//a[@class='_1fQZEK']"));
			System.out.println(mobiles.size()+ " devices displayed on this page");
			for(int i=0;i<mobiles.size();i++) {
				String priceToCompare = driver.findElement(By.xpath("(//a[@class='_1fQZEK']//div[@class='_30jeq3 _1_WHN1'])["+(i+1)+"]")).getText().replaceAll("[^\\dA-Za-z ]", "");
				if(Integer.parseInt(priceToCompare)<=40000) {
					String deviceDetails = driver.findElement(By.xpath("(//a[@class='_1fQZEK']//div[@class='_4rR01T'])["+(i+1)+"]")).getText();
					String price = driver.findElement(By.xpath("(//a[@class='_1fQZEK']//div[@class='_30jeq3 _1_WHN1'])["+(i+1)+"]")).getText();
					String reviews = driver.findElement(By.xpath("(//a[@class='_1fQZEK']//span[@class='_2_R_DZ'])["+(i+1)+"]")).getText().split(" ")[0];
					System.out.println((i+1)+" "+deviceDetails+" "+price+" "+reviews);
					
					allDetails.add(new String[]{deviceDetails, price, reviews});	
				}
			}
			
			//Check Next Page
			if(driver.findElements(By.xpath("//a[contains(.,'Next')]")).size()>0) { //"//a[contains(.,'Next')]")
				nextPageExists = true;
				driver.findElement(By.xpath("//a[contains(.,'Next')]")).click();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='_1XXPTY _1v2cG7']")));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Next page Found");
			} else {
				nextPageExists = false;
			}
		}while(nextPageExists);
		
		return allDetails;
	}
}