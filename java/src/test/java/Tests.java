import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
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

	@Test
	public void signUpTest() {
		driver.get("http://localhost:8088/litecart");
		driver.findElement(By.cssSelector("[name=login_form] a")).click();
		driver.findElement(By.name("firstname")).sendKeys("firstname");
		driver.findElement(By.name("lastname")).sendKeys("lastname");
		driver.findElement(By.name("address1")).sendKeys("address1");
		driver.findElement(By.name("postcode")).sendKeys("12345");
		driver.findElement(By.name("city")).sendKeys("city");
		Select select = new Select(driver.findElement(By.cssSelector("select[name=country_code]")));
		((JavascriptExecutor)driver).executeScript("arguments[0].value = \"US\"; arguments[0].dispatchEvent(new Event('change'))", select);
		String email = System.currentTimeMillis() + "@test.ru";
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("phone")).sendKeys("205-297-5716");
		driver.findElement(By.name("password")).sendKeys("password");
		driver.findElement(By.name("confirmed_password")).sendKeys("password");
		driver.findElement(By.name("create_account")).click();
		driver.findElement(By.xpath("//a[contains(., 'Logout')]")).click();
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys("password");
		driver.findElement(By.name("login")).click();
		driver.findElement(By.xpath("//a[contains(., 'Logout')]")).click();
	}

	@Test
	public void productCreationTest() {
		driver.get("http://localhost:8088/litecart/admin/");
		driver.findElement(By.name("username")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("login")).click();
		driver.findElement(By.xpath("//span[contains(., 'Catalog')]")).click();
		driver.findElement(By.xpath("//a[contains(., ' Add New Product')]")).click();
		driver.findElement(By.xpath("//label[contains(., ' Enabled')]/input[@value=1]")).click();
		driver.findElement(By.name("name[en]")).sendKeys("Skeleton Duck");
		driver.findElement(By.name("code")).sendKeys("rd006");
		driver.findElement(By.cssSelector("input[name='categories[]'][data-name='Root']")).click();
		driver.findElement(By.cssSelector("input[name='categories[]'][data-name='Rubber Ducks']")).click();
		driver.findElement(By.cssSelector("input[name='product_groups[]'][value='1-3']")).click();
		driver.findElement(By.name("quantity")).sendKeys("3");;
		Select select = new Select(driver.findElement(By.name("sold_out_status_id")));
		((JavascriptExecutor)driver).executeScript("arguments[0].value = 2; arguments[0].dispatchEvent(new Event('change'))", select);
		File file = new File("skeleton-rubber-duck.jpg");
		driver.findElement(By.name("new_images[]")).sendKeys(file.getAbsolutePath());
		driver.findElement(By.name("date_valid_from")).sendKeys("22022019");
		driver.findElement(By.name("date_valid_to")).sendKeys("22022020");
		driver.findElement(By.xpath("//a[contains(., 'Information')]")).click();
		select = new Select(driver.findElement(By.name("manufacturer_id")));
		((JavascriptExecutor)driver).executeScript("arguments[0].value = 1; arguments[0].dispatchEvent(new Event('change'))", select);
		String shortDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sollicitudin ante massa, eget ornare libero porta congue.";
		String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse sollicitudin ante massa, eget ornare libero porta congue. Cras scelerisque dui non consequat sollicitudin. Sed pretium tortor ac auctor molestie. Nulla facilisi. Maecenas pulvinar nibh vitae lectus vehicula semper. Donec et aliquet velit. Curabitur non ullamcorper mauris. In hac habitasse platea dictumst. Phasellus ut pretium justo, sit amet bibendum urna. Maecenas sit amet arcu pulvinar, facilisis quam at, viverra nisi. Morbi sit amet adipiscing ante. Integer imperdiet volutpat ante, sed venenatis urna volutpat a. Proin justo massa, convallis vitae consectetur sit amet, facilisis id libero.";
		driver.findElement(By.name("short_description[en]")).sendKeys(shortDescription);
		driver.findElement(By.className("trumbowyg-editor")).sendKeys(description);
		driver.findElement(By.xpath("//a[contains(., 'Prices')]")).click();
		driver.findElement(By.name("purchase_price")).sendKeys("1");
		driver.findElement(By.name("prices[USD]")).sendKeys("20.0000");
		driver.findElement(By.name("prices[EUR]")).sendKeys("0.0000");
		driver.findElement(By.name("save")).click();
	}

	@Test
	public void basicCartTest() {
		for (int i = 0; i < 3; i++) {
			driver.get("http://localhost:8088/litecart");
			driver.findElement(By.className("product")).click();
			if(driver.findElements(By.name("options[Size]")).size() > 0) {
				Select select = new Select(driver.findElement(By.name("options[Size]")));
				((JavascriptExecutor)driver).executeScript("arguments[0].value = 'Medium'; arguments[0].dispatchEvent(new Event('change'))", select);
			}
			WebElement currentQuantity = driver.findElement(By.className("quantity"));
			int fixedQuantity = Integer.parseInt(currentQuantity.getText());
			driver.findElement(By.name("add_cart_product")).click();
			wait.until(d -> Integer.parseInt(currentQuantity.getText()) > fixedQuantity);
			driver.findElement(By.id("logotype-wrapper")).click();
		}
		driver.findElement(By.xpath("//a[contains(., 'Checkout')]")).click();
		List<WebElement> rows;
		while((rows = driver.findElements(By.cssSelector(".dataTable td"))).size() > 0) {
			driver.findElement(By.name("remove_cart_item")).click();
			wait.until(ExpectedConditions.stalenessOf(rows.get(1)));
		}
	}

	@Test
	public void externalLinksTest() {
		driver.get("http://localhost:8088/litecart/admin");
		driver.findElement(By.name("username")).sendKeys("admin");
		driver.findElement(By.name("password")).sendKeys("admin");
		driver.findElement(By.name("login")).click();
		driver.findElement(By.xpath("//span[@class='name' and contains(., 'Countries')]")).click();
		driver.findElement(By.xpath("//a[contains(., ' Add New Country')]")).click();
		Set<String> initialWindows = driver.getWindowHandles();
		List<WebElement> links = driver.findElements(By.cssSelector(".fa.fa-external-link"));
		for(WebElement element : links) {
			element.click();
			wait.until(ExpectedConditions.numberOfWindowsToBe(initialWindows.size() + 1));
			Set<String> currentWindows = driver.getWindowHandles();
			currentWindows.removeAll(initialWindows);
			driver.switchTo().window(currentWindows.iterator().next());
			driver.close();
			driver.switchTo().window(initialWindows.iterator().next());
		}
		driver.quit();
	}

	@After
	public void stop() {
		driver.quit();
	}
}
