package pages;

import base.TestBase;

public class GoogleSearchPage extends TestBase {

	 

	public static final String searchBox = "//*[@name='q']";
	
	public static final String searchButton = "//*[@name='btnK']";
	
	public static final String auto_Suggestions = "//*[@class='wM6W7d']";
	public static final String auto_Suggestions_Select= "(//*[@role= 'option'])[4]";
	public static final String recent_searches= "(//*[@role= 'option'])[5]";
	public static final String mic_Icon= "//*[@class='goxjub']";
	public static final String search_by_image = "//*[@class='Gdd5U']";

}
