import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Craigslist {
	public static double averagePrice;
	public static String[] cheapestItem = new String[3];
	public static String[] getLowest(List<String> items) throws IOException {
		return cheapestItem;
	}
	public static double getAverage(List<String> items) {
		return averagePrice;
	}
	public static List<String> getItems(String keyword, int min, int max) throws IOException {
		double TotalPrice = 0;
		double number_of_listings = 0;
		ArrayList<String> productsListings = new ArrayList<String>();
		keyword = keyword.replace(" ", "+");
		String URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58", keyword);
		if(min == 0 && max == 0) {
		URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58", keyword);
		max = Integer.MAX_VALUE;
		}else if(min != 0 && max != 0){
			URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58&min_price=5555&max_price=15000", keyword);
			URL = URL.replaceFirst("5555", Integer.toString(min));
			URL = URL.replaceFirst("15000", Integer.toString(max));
		}
		else if (min == 0 && max != 0) {
			URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58&max_price=15000", keyword);
			URL = URL.replaceFirst("15000", Integer.toString(max));
		}else if (min != 0 && max == 0) {
			URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58&min_price=15000", keyword);
			URL = URL.replaceFirst("15000", Integer.toString(min));
		}
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			Elements item_price = eBayData.select(".result-info");
			for(Element price : item_price) {
				if(price.toString().contains("broken") || price.toString().toLowerCase().contains("for parts") || price.toString().toLowerCase().contains("parts")|| price.toString().toLowerCase().contains("defective")) {
					continue;
				}
				if(price.select(".result-price").text().replace("$", "").replace(",", "").equals("") || price.select(".result-price").text().replace("$", "").replace(",", "").equals("0") || price.select(".result-price").text().replace("$", "").replace(",", "").equals("1") || price.select(".result-price").text().replace("$", "").replace(",", "") == null) {
					continue;
				}
				if(Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) > max || Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) < min) {
					continue;
				}
				Element title = price.select("a").first();
				String name = title.select("a").first().text();
				productsListings.add(name);
				number_of_listings = number_of_listings + 1;
				productsListings.add(price.select(".result-price").text().replace("$", "").replace(",", ""));
				TotalPrice = TotalPrice + Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", ""));
				Element links = price.select("a").first();
				String url = links.attr("href");
				productsListings.add(url);
				
			}
			if(productsListings.size() > 0) {
			cheapestItem[0] = productsListings.get(0);
			cheapestItem[1] = productsListings.get(1);
			cheapestItem[2] = productsListings.get(2);
			}
			averagePrice = TotalPrice / number_of_listings;
			return productsListings;
		}
	
	}
