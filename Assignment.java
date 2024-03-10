package interview;

import java.time.Duration;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;

public class Assignment {
	String prdouctDetailsPageProductName;
	WebDriver driver;

	@Test(priority = 0, description = "Setup Selenium WebDriver")
	public void prerequist() {
		String browserType = "chrome"; // Change this to "firefox" or "edge" as needed
		driver = getDriver(browserType);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	private static WebDriver getDriver(String browserType) {
		WebDriver driver;

		switch (browserType.toLowerCase()) {
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "G:/geckodriver.exe");
			FirefoxOptions ops = new FirefoxOptions();
			ops.addArguments("--remote-allow-origins=*");
			ops.setCapability("acceptInsecureCerts", true);
			driver = new FirefoxDriver(ops);
			break;
		case "edge":
			System.setProperty("webdriver.edge.driver", "G:/msedgedriver.exe");
			EdgeOptions ops1 = new EdgeOptions();
			ops1.addArguments("--remote-allow-origins=*");
			ops1.setCapability("acceptInsecureCerts", true);
			driver = new EdgeDriver(ops1);

			break;
		case "chrome":
		default:
			System.setProperty("webdriver.chrome.driver", "G:/chromedriver.exe");
			ChromeOptions ops2 = new ChromeOptions();
			ops2.addArguments("--remote-allow-origins=*");
			ops2.setCapability("acceptInsecureCerts", true);
			driver = new ChromeDriver(ops2);
		}
		return driver;

	}

	@Test(priority = 1, description = "Verify that the homepage loads successfully")
	public void verifyHomePage() {
		String url = "https://www.flipkart.com/";
		driver.get(url);
		String pageLoadStatus = null;

		do {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			pageLoadStatus = (String) js.executeScript("return document.readyState");
		} while (!pageLoadStatus.equals("complete")); // to verify the page load successfully

		Assert.assertEquals(pageLoadStatus, "complete");

		System.out.println("Page Loaded.");
		driver.manage().window().maximize();
	}

	@Test(priority = 2, description = "verify user is able to Search and Add to Cart")
	public void searchAndAddToCart() throws InterruptedException {
		driver.findElement(By.name("q")).sendKeys("laptop", Keys.ENTER);
		driver.findElements(By.className("_4rR01T")).get(2).click();

		String mainPage = driver.getWindowHandle();
		Set<String> allPages = driver.getWindowHandles();
		for (String page : allPages) {
			if (!page.equals(mainPage))
				driver.switchTo().window(page);
		}
		prdouctDetailsPageProductName = driver.findElement(By.className("B_NuCI")).getText();
		System.out.println("prdouctDetailsPageProductName --" + prdouctDetailsPageProductName);

		driver.findElement(By.xpath("//input[@placeholder='Enter Delivery Pincode']")).sendKeys("411057", Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Add to cart']")).click();

	}

	@Test(priority = 3, description = "verify user is able to Proceed to Checkout")
	public void ProceedToCheckout() throws InterruptedException {
		Thread.sleep(5000);
		String viewCartsub1ProdName = driver.findElement(By.xpath("//div[@class='_3fSRat']/div/a")).getText();

		String viewCartsub2ProdName = driver.findElement(By.xpath("//div[@class='_20RCA6']")).getText();
		driver.findElement(By.xpath("//*[text()='Place Order']")).click();
		String viewCartProductName = viewCartsub1ProdName + "  (" + viewCartsub2ProdName + ")";
		System.out.println("ProdName----" + viewCartProductName);
		Assert.assertEquals(prdouctDetailsPageProductName, viewCartProductName);

	}

	@Test(priority = 4, description = "Verify that the user is successfully logged in.")
	public void UserAuthentication() throws InterruptedException {
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("9876543210");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys("123456");
		driver.findElement(By.xpath("//*[text()='Login']")).click();

		String userName = driver.findElement(By.xpath("//span[@class='npYOZI']")).getText();
		Assert.assertEquals(userName, "Arpit Pawar");

	}

	 @Test(priority = 5, description = "verify user is able to Enter valid shipping information")
	public void ShippingInformation() throws InterruptedException {

		driver.findElement(By.className("_1P2-0f")).click(); // Add new address
		driver.findElement(By.name("name")).sendKeys("Arpit Pawar");
		driver.findElement(By.name("phone")).sendKeys("9876543210");
		driver.findElement(By.name("pincode")).sendKeys("411057");
		driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys("Megapolise");
		driver.findElement(By.name("addressLine1")).sendKeys("Phase 3 , pune");

		driver.findElement(By.name("city")).sendKeys("Pune");

		WebElement state = driver.findElement(By.name("state"));
		Select dropdown = new Select(state);
		dropdown.selectByValue("Maharashtra");

		driver.findElement(By.xpath("//button[@type='button']")).click();
	
	}

	 @Test(priority = 6, description = "Verify the order summary")
	public void ReviewOrder() throws InterruptedException {
		String OrderSummarysub1ProductName = driver.findElement(By.className("_2Kn22P")).getText();
		String OrderSummarysub2ProductName = driver.findElement(By.className("_20RCA6")).getText();

		String OrderSummaryProductName = OrderSummarysub1ProductName + "  (" + OrderSummarysub2ProductName + ")";
		System.out.println("OrderSummaryProductName----" + OrderSummaryProductName);
		Assert.assertEquals(OrderSummaryProductName, prdouctDetailsPageProductName);
		driver.findElement(By.xpath("//*[text()='CONTINUE']")).click();
		driver.findElement(By.xpath("//*[text()='Accept & Continue']")).click();

	}

	 @Test(priority = 7, description = "verify user is able to Choose a payment method")
	public void PaymentInformation() throws InterruptedException {
		driver.findElement(By.xpath("//label[@for='CREDIT']")).click();
		driver.findElement(By.name("cardNumber")).sendKeys("161356785678");

		WebElement month = driver.findElement(By.name("month"));
		Select dropdown2 = new Select(month);
		dropdown2.selectByValue("02");

		WebElement year = driver.findElement(By.name("year"));
		Select dropdown3 = new Select(year);
		dropdown3.selectByValue("24");

		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("123"); //CVV
		driver.findElement(By.xpath("//button[@type='button']")).click();

		Thread.sleep(50000);
		driver.quit();
	}

}
