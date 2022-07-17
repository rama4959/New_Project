package TestClasses;
import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import BaseClasses.BaseClass1;
import PomClasses.Home_Page;
import PomClasses.Login_Page;
import PomClasses.ProductDetails_page;
public class VerifyUserCanBuyProduct {
	
	WebDriver driver;
	Login_Page lp;
	Home_Page hp;
	ProductDetails_page pdp;
	
	ExtentHtmlReporter htmlReporter;
	ExtentReports report;
	ExtentTest test;
	
	@Parameters("browser")
	@BeforeClass
	public void beforeClass(String k)
	{
		
		htmlReporter = new ExtentHtmlReporter("ExtentReporter.html");
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		test = report.createTest("VerifyUserCanBuyProduct");
		
		driver = BaseClass1.getDriver(k);
	}
	
	@BeforeMethod
	public void beforeMethod()
	{
		lp = new Login_Page(driver);
		hp = new Home_Page(driver);
		pdp = new ProductDetails_page(driver);
	}
	
	@Test(priority=1)
	public void verifyUserCanLogin()
	{
		lp.putEmail();
		lp.putPassword();
		lp.clickLoginBtn();
		SoftAssert soft = new SoftAssert();
		soft.assertTrue(hp.isProfileNameVisible());
		soft.assertAll();
	}
	
	@Test(priority=2)
	public void VerifyUserCanSearchProduct() throws EncryptedDocumentException, IOException
	{
		String data = hp.getDataFromExcelSheet("Sheet1", 0, 0);
		hp.enterSearchText(data);
		hp.clickSearchButton();
		Assert.assertTrue(hp.isProductListAppeared());
	}
	@Test(priority=3)
	public void verifyUserCanSelectProductAndMoveToProductDetailsPage()
	{
		hp.clickOnFirstProduct();
		Assert.assertTrue(pdp.isBuyNowButtonIsAppeared());
	}
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException
	{
		if(result.getStatus() == ITestResult.SUCCESS)
		{
			test.log(Status.PASS, "Test is passed " + result.getName());
		}
		else
		{
			String path = hp.getScreenShot(driver, result.getName());
			test.log(Status.FAIL, "Test is failed " + result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			
			
		}
	}
	@AfterClass
	public void afterClass()
	{
		report.flush();
	}
	

}
