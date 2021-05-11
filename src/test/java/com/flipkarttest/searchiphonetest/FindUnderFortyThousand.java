package com.flipkarttest.searchiphonetest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.flipkarttest.pages.HomePage;
import com.flipkarttest.pages.SearchResultPage;
import com.opencsv.CSVWriter;

public class FindUnderFortyThousand {
	
	protected WebDriver driver;
	protected String url = null; 
	protected int underPrice = 40000;
	
	@BeforeSuite
	public void beforeSuite() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver(new ChromeOptions().addArguments("start-maximized"));
		url="https://www.flipkart.com";
	}
	
	@BeforeTest
	public void beforeTest() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
	}
	
	@Test
	public void search() {
		HomePage homePage = new HomePage(driver);
		
		if(homePage.loginPopUpExists()) {
			homePage.closeLoginPopup();
		}
		
		homePage.search("iphones");
		
		Assert.assertEquals(true, true,"");
		
		SearchResultPage searchPage = new SearchResultPage(driver);
		searchPage.applyMobileFilter();
		searchPage.applyPriceFilter(underPrice);
		searchPage.appplyAvailabilityFilter();
		
		
		List<String[]> iphones= searchPage.getResultDetails();
		
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("iphonesUnder40k.csv"));
			writer.writeAll(iphones);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@AfterSuite
	public void afterSuite() {
		 driver.quit();
	}

}
