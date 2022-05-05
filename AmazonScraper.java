package scraperPackage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonScraper {
	
	public static double getAverage(List<String> items) {
		if (items.size() == 0 || items == null) {
			return -1;
		}
		DecimalFormat avgFormat = new DecimalFormat("#.00");
		int numItems = items.size()/3;
		double tempPrice = 0.0;
		double averageCost = 0.0;
		for (int i = 0; i < items.size(); i = i+3) {
			tempPrice += Double.parseDouble(items.get(i+1));
		}
		averageCost = tempPrice/numItems;
		return Double.parseDouble(avgFormat.format(averageCost));
	}
	
	public static String[] getLowest(List<String> items) {
		if (items.size() == 0 || items == null) {
			return null;
		}
		double lowVal = Double.MAX_VALUE;
		int minIndex = 0;
		for (int i = 0; i < items.size(); i = i+3) {
			if (Double.parseDouble(items.get(i+1)) < lowVal) {
				minIndex = i;
				lowVal = Double.parseDouble(items.get(i+1));
			}
		}
		String[] returnArray = {items.get(minIndex), items.get(minIndex+1), items.get(minIndex+2)};
		return returnArray;
	}
	
	public static List<String> getItems(String keyword, int minPrice, int maxPrice){
		ArrayList<String> productList = new ArrayList<String>();
		keyword = keyword.replace(' ', '+');
		String productURL = "";
		if (minPrice == 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&page=", keyword);
		}
		else if (minPrice != 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&low-price=%s&page=", keyword, String.valueOf(minPrice));
		}
		else if (minPrice == 0 && maxPrice != 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&high-price=%s&page=", keyword, String.valueOf(maxPrice));
		}
		else {
			productURL = String.format("https://www.amazon.com/s?k=%s&low-price=%s&high-price=%s&page=", keyword, String.valueOf(minPrice), String.valueOf(maxPrice));
		}		for (int urlPage = 1; urlPage < 11; urlPage++) {
			try {
				Document amazonData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36").ignoreHttpErrors(true).referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productsInfo = amazonData.select(".sg-col-inner");
				for (Element listingInfo : productsInfo) {
					if (!(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text().equals(""))) {
						if (listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text().length() < 300 && listingInfo.select(".a-offscreen").text().length() < 10 && listingInfo.select(".a-offscreen").text().length() > 0 && (!(productList.contains(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text())))) {
							if (listingInfo.select(".a-color-base").text().contains("FREE")) {
								double basePrice = 0.0;
								if (listingInfo.select(".a-offscreen").text().length() <= 7) {
									basePrice = Double.parseDouble(listingInfo.select(".a-offscreen").text().replace("$", ""));
								}
								else {
									basePrice = Double.parseDouble(listingInfo.select(".a-offscreen").text().replace("$", "").replace(",", ""));
								}
								if (basePrice < maxPrice && basePrice > minPrice) {
									productList.add(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text());
									productList.add(String.valueOf(basePrice));
									productList.add(listingInfo.select(".a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal").attr("abs:href"));
								}
							}
							else {
								double basePrice = 0.0;
								if (listingInfo.select(".a-offscreen").text().length() <= 7) {
									basePrice = Double.parseDouble(listingInfo.select(".a-offscreen").text().replace("$", ""));
								}
								else {
									basePrice = Double.parseDouble(listingInfo.select(".a-offscreen").text().replace("$", "").replace(",", ""));
								}					
								double shippingCost = 0.0;
								if (listingInfo.select("span .a-color-base:contains($)").text().split(" ")[0].length() <= 7) {
									if (!(listingInfo.select("span .a-color-base:contains($)").text().equals(""))){
										shippingCost = Double.parseDouble(listingInfo.select("span .a-color-base:contains($)").text().split(" ")[0].replace("$", ""));
									}
								}
								else {
									shippingCost = Double.parseDouble(listingInfo.select("span .a-color-base:contains($)").text().split(" ")[0].replace("$", "").replace(",",""));
								}
								double actualPrice = basePrice + shippingCost;
								if (actualPrice < maxPrice && actualPrice > minPrice) {
									productList.add(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text());
									productList.add(String.valueOf(actualPrice));
									productList.add(listingInfo.select(".a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal").attr("abs:href"));
								}
							}
						}
					}
				}
			}
			catch (Exception scrapeFail) {
				scrapeFail.printStackTrace();
			}
		}
		return productList;
	}

	public static void main(String[] args) {
		List<String> tempList = getItems("cheese", 2, 2000);
		System.out.println(tempList);
		System.out.println(getAverage(tempList));
		String[] tempStrings = getLowest(tempList);
		for (String string: tempStrings) {
			System.out.println(string);
		}
	}

}
