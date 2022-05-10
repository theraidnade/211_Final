package scraperPackage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class used to scrape data directly from Amazon.com
 * @author Phillip Dickey
 * @version 1.00
 */
public class AmazonScraper {
	
	/**
	 * Method used to determine the average price of all of the items found in the getItems() method
	 * @param items - List of String objects that contains items pulled from the getItems() method
	 * @return double value representing the average price of all items found by the scraper
	 */
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
	
	/**
	 * Method used to determine the lowest priced item among all of the items in the getItems() method
	 * @param items - List of String objects that contains items pulled from the getItems() method
	 * @return String array containing the [name, price, link] of the lowest priced item
	 */
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
	
	/**
	 * Method used to grab products from Amazon
	 * @param keyword - String value representing keywords that are passed to Amazon as search terms
	 * @param minPrice - int value representing the minimum price that a product can be
	 * @param maxPrice - int value representing the maximum price that a product can be
	 * @return List<String> - List of string objects that represent the <...,name, price, link, ...> of the products found by the scraper
	 */
	public static List<String> getItems(String keyword, int minPrice, int maxPrice){
		//List of String objects that will be filled with product information and then returned
		ArrayList<String> productList = new ArrayList<String>();
		//Replaces any spaces in the parameter with pluses to make it URL friendly
		keyword = keyword.replace(' ', '+');
		String productURL = "";
		//These conditionals determine whether the url needs to include min and max price parameters on Amazon
		if (minPrice == 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&page=", keyword);
			maxPrice = Integer.MAX_VALUE;
		}
		else if (minPrice != 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&low-price=%s&page=", keyword, String.valueOf(minPrice));
			maxPrice = Integer.MAX_VALUE;
		}
		else if (minPrice == 0 && maxPrice != 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&high-price=%s&page=", keyword, String.valueOf(maxPrice));
		}
		else {
			productURL = String.format("https://www.amazon.com/s?k=%s&low-price=%s&high-price=%s&page=", keyword, String.valueOf(minPrice), String.valueOf(maxPrice));
		}
		//This for loop increments through the first 5 pages of results
		for (int urlPage = 1; urlPage < 7; urlPage++) {
			//Try catch in the event that Jsoup throws an exception when connecting to Amazon
			try {
				Document amazonData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").ignoreHttpErrors(true).referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productsInfo = amazonData.select(".sg-col-inner");
				for (Element listingInfo : productsInfo) {
					//Conditionals to check that the Element contains usable data
					if (!(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text().equals(""))) {
						if (listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text().length() < 300 && listingInfo.select(".a-offscreen").text().length() < 10 && listingInfo.select(".a-offscreen").text().length() > 0 && (!(productList.contains(listingInfo.select(".a-size-medium.a-color-base.a-text-normal").text())))) {
							//Conditional to determine whether shipping cost needs to be calculated and added or not
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
								//Conditional to ensure final price is within user-provided parameters
								if (actualPrice < maxPrice && actualPrice > minPrice) {
									//Adds data in the order <name, price, link>
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
	
//	Main method used for personal testing.
//	public static void main(String[] args) {
//		List<String> tempList = getItems("cheese", 2, 2000);
//		System.out.println(tempList);
//		System.out.println(getAverage(tempList));
//		String[] tempStrings = getLowest(tempList);
//		for (String string: tempStrings) {
//			System.out.println(string);
//		}
//	}

}
