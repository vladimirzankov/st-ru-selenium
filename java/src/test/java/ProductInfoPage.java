import org.openqa.selenium.WebDriver;

public class ProductInfoPage extends ProductInfo {

  public boolean isCampaignPriceBold(WebDriver driver) {
    return this.campaignPriceFontWeight == 700;
  }
}
