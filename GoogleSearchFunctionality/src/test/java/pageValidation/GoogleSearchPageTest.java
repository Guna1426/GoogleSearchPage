package pageValidation;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.GoogleSearchPage;

public class GoogleSearchPageTest extends GoogleSearchPage {

	@Test(priority = 1)
	public void verifyTheUrl() {
		String current_URL = driver.getCurrentUrl();
		assertEquals(readExcelData.getStringData("GoogleSearch", 5, 0), current_URL);
		logger.log(Status.INFO, "URL Verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 2)
	public void verifySearchBox() {
		enterText1(readExcelData.getStringData("GoogleSearch", 1, 0), searchBox);
		logger.log(Status.INFO, "Search Box is verified and user able to enter the text in it.");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 3)
	public void verifySearchButton() {
		enterText1(readExcelData.getStringData("GoogleSearch", 2, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Search button is verified and user able to click on it.");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 4)
	public void verifyAutoSuggestions() {
		enterText1(readExcelData.getStringData("GoogleSearch", 2, 0), searchBox);
		enterText1(readExcelData.getStringData("GoogleSearch", 3, 0), searchBox);
		setImplicit(200000);
		printAllElements(auto_Suggestions);
		logger.log(Status.INFO, "Auto Suggestions is verified and user able to see it.");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 5)
	public void clickOnAutoSuggestions() {
		enterText1(readExcelData.getStringData("GoogleSearch", 2, 0), searchBox);
		enterText1(readExcelData.getStringData("GoogleSearch", 3, 0), searchBox);
		printAllElements(auto_Suggestions);
		clickEvent(auto_Suggestions_Select);
		logger.log(Status.INFO, "Auto Suggestions is selected");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 6)
	public void verifyTheRecentSearches() {
		clickEvent(searchBox);
		printAllElements(recent_searches);
		clickEvent(recent_searches); 
		logger.log(Status.INFO, "User able to see the recent searched and able to select it");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 7)
	public void verifyTheSearchResults() {
		enterText1(readExcelData.getStringData("GoogleSearch", 4, 0), searchBox);
		clickButton(searchButton);
		String search_Results = getAttributeValue(searchBox, "value");
		assertEquals(readExcelData.getStringData("GoogleSearch", 4, 0), search_Results);
		logger.log(Status.INFO, "Search Results are verfied and are found to be correct");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 8)
	public void verifyTheMicIsClickable() {
		clickButton(mic_Icon);
		logger.log(Status.INFO, "Mic Icon is clicked");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 9)
	public void verifyTheSearchByImageIsClickable() {
		clickButton(search_by_image);
		logger.log(Status.INFO, "Search by image icon is clicked");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 10)
	public void searchOnlyAlphabet() {

		enterText1(readExcelData.getStringData("GoogleSearch", 2, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with albhabetic is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 11)
	public void searchOnlyNumeric() {
		enterText1(Integer.toString(readExcelData.getNumbericData("GoogleSearch", 6, 0)), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with Numeric is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 12)
	public void searchOnlySpecialCharacters() {
		enterText1(readExcelData.getStringData("GoogleSearch", 7, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with Special Characters is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 13)
	public void searchOnlyAlbhaNumeric() {
		enterText1(readExcelData.getStringData("GoogleSearch", 8, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with AlbhaNumeric is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 14)
	public void searchOnlyNumericwithSpecialCharacters() {
		enterText1(readExcelData.getStringData("GoogleSearch", 9, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with Numeric with Special Characters is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 15)
	public void searchOnlyAlbhabeticwithSpecialCharacters() {
		enterText1(readExcelData.getStringData("GoogleSearch", 10, 0), searchBox);
		clickButton(searchButton);
		logger.log(Status.INFO, "Google Search only with Albhabetic with Special Characters is verified");
		logger.log(Status.PASS, "Test case verified successfully");
	}

	@Test(priority = 16)
	public void verifyFailedTestCase() {
		enterText1(readExcelData.getStringData("GoogleSearch", 4, 0), searchBox);
		clickButton(searchButton);
		String search_Results = getAttributeValue(searchBox, "value");
		assertEquals(search_Results, readExcelData.getStringData("GoogleSearch", 11, 0));
		logger.log(Status.INFO, "Search Results are verfied and are found to be correct");
		logger.log(Status.PASS, "Test case verified successfully");
	}

}
