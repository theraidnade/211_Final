package scraperPackage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WalmartScraper {
	
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
			productURL = String.format("https://www.walmart.com/search?q=%s&page=", keyword);
		}
		else if (minPrice != 0 && maxPrice == 0) {
			productURL = String.format("https://www.walmart.com/search?q=%s&min_price=%s&page=", keyword, String.valueOf(minPrice));
		}
		else if (minPrice == 0 && maxPrice != 0) {
			productURL = String.format("https://www.walmart.com/search?q=%s&max_price=%s&page=", keyword, String.valueOf(maxPrice));
		}
		else {
			productURL = String.format("https://www.walmart.com/search?q=%s&min_price=%s&max_price=%s&page=", keyword, String.valueOf(minPrice), String.valueOf(maxPrice));
		}
		for (int urlPage = 1; urlPage < 10; urlPage++) {
			try {
				Document walmartData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productsInfo = walmartData.select(".mb1.ph1.pa0-xl.bb.b--near-white.w-25");
				for (Element listingInfo : productsInfo) {
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
								if (actualPrice < maxPrice && actualPrice > minPrice) {
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
									if (basePrice < maxPrice && basePrice > minPrice) {
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


	public static void main(String[] args) {
		List<String> testList = getItems("vibrators", 10, 200);
		System.out.println(getAverage(testList));
		String[] tempString = getLowest(testList);
		for (String part: tempString) {
			System.out.println(part);
		}
	}

}
