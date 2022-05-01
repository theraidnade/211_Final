package scraperPackage;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonScraper {
	
	public static List<String> getItems(String keyword, int minPrice, int maxPrice){
		ArrayList<String> productList = new ArrayList<String>();
		keyword = keyword.replace(' ', '+');
		String productURL = "";
		if (minPrice == 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&page=", keyword);
		}
		else if (minPrice != 0 && maxPrice == 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&low-price=%s&page=", keyword, String.valueOf(minPrice));
		}
		else if (minPrice == 0 && maxPrice != 0) {
			productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&high-price=%s&page=", keyword, String.valueOf(maxPrice));
		}
		else {
			productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&low-price=%s&high-price=%s&page=", keyword, String.valueOf(minPrice), String.valueOf(maxPrice));
		}		for (int urlPage = 1; urlPage < 11; urlPage++) {
			try {
				Document amazonData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").ignoreHttpErrors(true).referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
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
				for (int index = 0; index < productList.size() - 3; index = index + 3) {
					System.out.println(productList.get(index) + ": " + productList.get(index + 1));
					System.out.println(productList.get(index + 2));
					System.out.println();
				}
				System.out.println("////////////////////////");
				System.out.println(productList.size());
			}
			catch (Exception scrapeFail) {
				scrapeFail.printStackTrace();
			}
		}
		System.out.println(productList);
		return productList;
	}

	public static void main(String[] args) {
		getItems("rtx 3090", 700, 2000);
	}

}
