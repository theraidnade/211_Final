package scraperPackage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class used to scrape data directly from walmart.com
 * @author Phillip Dickey
 * @version 1.00
 *
 */
public class WalmartScraper {
	
	/**
	 * Method used to determine the average price of all of the items found in the getItems() method
	 * @param items - List of String objects that contains items pulled from the getItems() method
	 * @return double value representing the average price of all items found by the scraper
	 */
	public static double getAverage(List<String> items) {
		if (items.size() == 0 || items == null) {
			return -1;
		}
		//DecimalFormat used to ensure numerical cleanliness in GUI environment
		DecimalFormat avgFormat = new DecimalFormat("#.00");
		//Gets size of list and divides by three due to items having three values associated with them
		int numItems = items.size()/3;
		double tempPrice = 0.0;
		double averageCost = 0.0;
		//Adds all prices into the tempPrice variable
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
		//Checks every item and its price for being the lowest
		for (int i = 0; i < items.size(); i = i+3) {
			if (Double.parseDouble(items.get(i+1)) < lowVal) {
				minIndex = i;
				lowVal = Double.parseDouble(items.get(i+1));
			}
		}
		//Adds [Name, price, link] to String array
		String[] returnArray = {items.get(minIndex), items.get(minIndex+1), items.get(minIndex+2)};
		return returnArray;
	}
	
	/**
	 * Method used to grab products from Walmart using the jsoup api
	 * @param keyword - String value representing keywords that are passed to Walmart as search terms
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
		//These conditionals determine whether the url needs to include min and max price parameters on Walmart
		//Zero is the default for both minPrice and maxPrice so if it is detected it will use a URL with no min or max
		//It will then set maxPrice equal to the max Integer value so it does not cause problems later on
		if (minPrice == 0 && maxPrice == 0) {
			productURL = String.format("https://www.walmart.com/search?q=%s&page=", keyword);
			maxPrice = Integer.MAX_VALUE;
		}
		else if (minPrice != 0 && maxPrice == 0) {
			productURL = String.format("https://www.walmart.com/search?q=%s&min_price=%s&page=", keyword, String.valueOf(minPrice));
			maxPrice = Integer.MAX_VALUE;
		}
		else if (minPrice == 0 && maxPrice != 0) {
			productURL = String.format("https://www.walmart.com/search?q=%s&max_price=%s&page=", keyword, String.valueOf(maxPrice));
		}
		else {
			productURL = String.format("https://www.walmart.com/search?q=%s&min_price=%s&max_price=%s&page=", keyword, String.valueOf(minPrice), String.valueOf(maxPrice));
		}
		//This for loop increments through the first 10 pages of results
		for (int urlPage = 1; urlPage < 10; urlPage++) {
			//Try catch in the event that Jsoup throws an exception when connecting to Amazon
			try {
				//HTML Document object of data found on the specified walmart page
				Document walmartData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				//Elements object of the product panels on the Document
				Elements productsInfo = walmartData.select(".mb1.ph1.pa0-xl.bb.b--near-white.w-25");
				//Enhanced for loop used to grab individual products and then manipulate their data
				for (Element listingInfo : productsInfo) {
					//Conditionals to check that the Element contains usable data
					if (!(listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text().equals(""))) {
						if (listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text().length() < 300 && listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() < 10 && listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() > 0 && (!(productList.contains(listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text())))) {
							if (listingInfo.select(".f7.f6-l.gray").text().contains("shipping")) {
								double basePrice = 0;
								if (listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() <= 7) {
									basePrice = Double.parseDouble(listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().replace("$", ""));
								}
								else {
									basePrice = Double.parseDouble(listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().replace("$", "").replace(",", ""));
								}
								double shippingCost = 0;
								if (listingInfo.select(".f7.f6-l.gray:contains(shipping)").text().split(" ")[0].length() <= 7) {
									shippingCost = Double.parseDouble(listingInfo.select(".f7.f6-l.gray:contains(shipping)").text().split(" ")[0].replace("+$", ""));
								}
								else {
									shippingCost = Double.parseDouble(listingInfo.select(".f7.f6-l.gray:contains(shipping)").text().split(" ")[0].replace("+$", "").replace(",", ""));
								}
								double actualPrice = basePrice + shippingCost;
								//Conditional to ensure final price is within user-provided parameters
								if (actualPrice < maxPrice && actualPrice > minPrice) {
									//Adds data in the order <name, price, link>
									productList.add(listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text());
									productList.add(String.valueOf(actualPrice));
									productList.add(listingInfo.select(".absolute.w-100.h-100.z-1").attr("abs:href"));
								}
							}
							else {
								double basePrice = 0.0;
								if (listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() <= 7) {
									basePrice = Double.parseDouble(listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().replace("$", ""));
									if (basePrice < maxPrice && basePrice > minPrice) {
										productList.add(listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text());
										productList.add(String.valueOf(basePrice));
										productList.add(listingInfo.select(".absolute.w-100.h-100.z-1").attr("abs:href"));
									}
								}
								else {
									basePrice = Double.parseDouble(listingInfo.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().replace("$", "").replace(",", ""));
									//Conditional to ensure final price is within user-provided parameters
									if (basePrice < maxPrice && basePrice > minPrice) {
										//Adds data in the order <name, price, link>
										productList.add(listingInfo.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text());
										productList.add(String.valueOf(basePrice));
										productList.add(listingInfo.select(".absolute.w-100.h-100.z-1").attr("abs:href"));
									}
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

//  Main method used for testing
//	public static void main(String[] args) {
//		List<String> testList = getItems("slave", 0, 10000);
//		System.out.println(testList.size());
//		System.out.println(getAverage(testList));
//		String[] tempString = getLowest(testList);
//		for (String part: tempString) {
//			System.out.println(part);
//		}
//	}

}
