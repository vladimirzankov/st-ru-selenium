import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ProductInfoCard extends ProductInfo {

  public boolean isCampaignPriceBold(WebDriver driver) {
    if (driver instanceof ChromeDriver) {
      return this.campaignPriceFontWeight == 700;
    } else {
      return this.campaignPriceFontWeight == 900;
    }
  }
}
