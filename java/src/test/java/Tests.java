import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;

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

	@Test
	public void countriesOrderTest() {
		driver.get("http://localhost:8088/litecart/admin/");
		driver.findElement(By.name("username")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("login")).click();
		driver.get("http://localhost:8088/litecart/admin/?app=countries&doc=countries");

		ArrayList<String> countries = new ArrayList<>();
		for(WebElement element : driver.findElements(By.className("row"))) {
			countries.add(element.findElement(By.tagName("a")).getText());
		}
		ArrayList sortedCountries = new ArrayList(countries);
		Collections.sort(sortedCountries);
		Assert.assertTrue(countries.equals(sortedCountries));

		for(String s : countries) {
			WebElement row = driver.findElement(By.xpath("//tr[@class='row'][.//a[contains(., \"" + 	s + "\")]]"));
			if(Integer.parseInt(row.findElements(By.tagName("td")).get(5).getText()) > 0) {
				row.findElements(By.tagName("a")).get(1).click();
				ArrayList<String> zones = new ArrayList<String>();
				for(WebElement elem : driver.findElements(By.xpath("//table[@id='table-zones']//tr[.//a[@id='remove-zone']]"))) {
					zones.add(elem.findElement(By.xpath("//td[.//input[contains(@name, 'name')]]")).getText());
				}
				ArrayList sortedZones = new ArrayList(zones);
				Collections.sort(sortedZones);
				Assert.assertTrue(zones.equals(sortedZones));
				driver.navigate().back();
			}
		}
	}

	@Test
	public void geoZonesOrderTest() {
		driver.get("http://localhost:8088/litecart/admin/");
		driver.findElement(By.name("username")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("login")).click();
		driver.get("http://localhost:8088/litecart/admin/?app=geo_zones&doc=geo_zones");

		int geoZonesCount = driver.findElements(By.className("row")).size();
		for(int i = 0; i < geoZonesCount; i++) {
			WebElement row = driver.findElements(By.className("row")).get(i);
			row.findElement(By.cssSelector("a[title=Edit")).click();

			ArrayList<String> zones = new ArrayList<String>();
			for(WebElement elem : driver.findElements(By.xpath("//table[@id='table-zones']//tr[.//a[@id='remove-zone']]"))) {
				zones.add(elem.findElement(By.cssSelector("select[name*=zone_code] option[selected]")).getText());
				System.out.println(elem.findElement(By.cssSelector("select[name*=zone_code] option[selected]")).getText());
			}
			ArrayList sortedZones = new ArrayList(zones);
			Collections.sort(sortedZones);
			Assert.assertTrue(zones.equals(sortedZones));
			driver.navigate().back();
		}
	}

	@Test
	public void productInfoTest() {

		driver.get("http://localhost:8088/litecart");
		WebElement element = driver.findElement(By.id("box-campaigns")).findElement(By.tagName("li"));
		ArrayList<ProductInfo> infos = new ArrayList<>();
		ProductInfoCard card = new ProductInfoCard();
		card.name = element.findElement(By.className("name")).getText();
		card.regularPrice = element.findElement(By.className("regular-price")).getText();
		card.campaignPrice = element.findElement(By.className("campaign-price")).getText();
		card.isRegularPriceCrossedOut = element.findElements(By.cssSelector("s.regular-price")).size() > 0;
		card.regularPriceColor = element.findElement(By.className("regular-price")).getCssValue("color");
		card.campaignPriceFontWeight = Integer.parseInt(element.findElement(By.className("campaign-price")).getCssValue("font-weight"));
		card.campaignPriceColor = element.findElement(By.className("campaign-price")).getCssValue("color");
		card.setRegularPriceFontSize(element.findElement(By.className("regular-price")).getCssValue("font-size"));
		card.setCampaignPriceFontSize(element.findElement(By.className("campaign-price")).getCssValue("font-size"));
		infos.add(card);

		element.click();

		ProductInfoPage page = new ProductInfoPage();
		page.name = driver.findElement(By.tagName("h1")).getText();
		page.regularPrice = driver.findElement(By.className("regular-price")).getText();
		page.campaignPrice = driver.findElement(By.className("campaign-price")).getText();
		page.isRegularPriceCrossedOut = driver.findElements(By.cssSelector("s.regular-price")).size() > 0;
		page.regularPriceColor = driver.findElement(By.className("regular-price")).getCssValue("color");
		page.campaignPriceFontWeight = Integer.parseInt(driver.findElement(By.className("campaign-price")).getCssValue("font-weight"));
		page.campaignPriceColor = driver.findElement(By.className("campaign-price")).getCssValue("color");
		page.setRegularPriceFontSize(driver.findElement(By.className("regular-price")).getCssValue("font-size"));
		page.setCampaignPriceFontSize(driver.findElement(By.className("campaign-price")).getCssValue("font-size"));
		infos.add(page);

		Assert.assertTrue(card.name.equals(page.name));
		Assert.assertTrue(card.regularPrice.equals(page.regularPrice));
		Assert.assertTrue(card.campaignPrice.equals(page.campaignPrice));
		for(ProductInfo info : infos) {
			Assert.assertTrue(info.isRegularPriceCrossedOut);
			Assert.assertTrue(info.isRegularPriceGrey());
			Assert.assertTrue(info.isCampaignPriceRed());
			Assert.assertTrue(info.isCampaignPriceBold(driver));
			Assert.assertTrue(info.campaignPriceFontSize > info.regularPriceFontSize);
		}
	}

	@After
	public void stop() {
		driver.quit();
	}
}
