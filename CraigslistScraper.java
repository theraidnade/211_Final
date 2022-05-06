import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Craigslist {
	public static double average;
	public static ArrayList<String> cheapest = new ArrayList<String>();
	public static ArrayList<String> getLowest() throws IOException {
		return cheapest;
	}
	public static double getAverage() {
		return average;
	}
	public static void getItems(String keyword, int min, int max) throws IOException {
		double all = 0;
		double number = 0;
		ArrayList<String> products = new ArrayList<String>();
		keyword = keyword.replace(" ", "+");
		String URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58", keyword);
		if(min == 0 && max == 0) {
		URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58", keyword);

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
				if(price.select(".result-price").text().replace("$", "").replace(",", "").equals("")) {
					continue;
				}
				if(Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) > max || Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) < min) {
					continue;
				}
				Element title = price.select("a").first();
				String name = title.select("a").first().text();
				products.add(name);
				number = number + 1;
				products.add(price.select(".result-price").text().replace("$", "").replace(",", ""));
				all = all + Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", ""));
				Element links = price.select("a").first();
				String url = links.attr("href");
				products.add(url);
				
			}
			if(products.size() > 0) {
			cheapest.add(products.get(0));
			cheapest.add(products.get(1));
			cheapest.add(products.get(2));
			}
			average = all / number;	
		}
	
	}


