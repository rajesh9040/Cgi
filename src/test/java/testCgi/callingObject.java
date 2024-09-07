package testCgi;


import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Cgi.cgi;



public class callingObject  {
	
  cgi c;
  WebDriver driver;
		// TODO Auto-generated method stub
    
	@Test
	public void launch() throws IOException, InterruptedException {
		
		c = new cgi(driver);
		c.launchbrowser();
		
		
		

	}

}
