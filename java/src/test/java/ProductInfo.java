import org.openqa.selenium.WebDriver;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ProductInfo {
  String name;
  String regularPrice;
  String campaignPrice;
  boolean isRegularPriceCrossedOut;
  int campaignPriceFontWeight;
  String regularPriceColor;
  String campaignPriceColor;
  float regularPriceFontSize;
  float campaignPriceFontSize;

  public void setRegularPriceFontSize(String fs) {
    regularPriceFontSize = Float.parseFloat(fs.substring(0, fs.indexOf("px")));
  }

  public void setCampaignPriceFontSize(String fs) {
    campaignPriceFontSize = Float.parseFloat(fs.substring(0, fs.indexOf("px")));
  }

  public boolean isRegularPriceGrey() {
    Pattern pattern = Pattern.compile("(?!=rgba\\()([0-9]*), ([0-9]*), ([0-9]*)");
    Matcher matcher = pattern.matcher(this.regularPriceColor);
    matcher.find();
    int red = Integer.parseInt(matcher.group(1));
    int green = Integer.parseInt(matcher.group(2));
    int blue = Integer.parseInt(matcher.group(3));
    return Arrays.asList(red, green, blue).stream().distinct().count() == 1;
  }

  public boolean isCampaignPriceRed() {
    Pattern pattern = Pattern.compile("(?!=rgba\\()([0-9]*), ([0-9]*), ([0-9]*)");
    Matcher matcher = pattern.matcher(this.campaignPriceColor);
    matcher.find();
    int red = Integer.parseInt(matcher.group(1));
    int green = Integer.parseInt(matcher.group(2));
    int blue = Integer.parseInt(matcher.group(3));
    return green == 0 && blue == 0;
  }

  public abstract boolean isCampaignPriceBold(WebDriver driver);
}
