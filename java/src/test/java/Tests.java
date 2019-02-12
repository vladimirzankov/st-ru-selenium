import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tests {

	public WebDriver driver;
	public WebDriverWait wait;

	@Before
	public void start() {
			driver = new ChromeDriver();
			wait = new WebDriverWait(driver, 10);
		}

	@Test
	public void sidebarTest() {
		driver.get("http://localhost:8088/litecart/admin/");
		driver.findElement(By.name("username")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("login")).click();

		int appsCount = driver.findElements(By.id("app-")).size();
		for(int i = 0; i < appsCount; i++) {
			driver.findElements(By.id("app-")).get(i).click();
			Assert.assertTrue(driver.findElements(By.tagName("h1")).size() > 0);
			int docsCount = driver.findElements(By.cssSelector(".docs li")).size();
			if (docsCount > 0) {
				for(int x = 0; x < docsCount; x++) {
					driver.findElements(By.cssSelector(".docs li")).get(x).click();
					Assert.assertTrue(driver.findElements(By.tagName("h1")).size() > 0);
				}
			}
		}
	}

	@Test
	public void stickersTest() {
		driver.get("http://localhost:8088/litecart");
		for(WebElement e : driver.findElements(By.className("product"))) {
			Assert.assertTrue(e.findElements(By.className("sticker")).size() == 1);
		}
	}

	@After
	public void stop() {
		driver.quit();
	}
}
