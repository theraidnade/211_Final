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
				else {
					products.add(price.select(".result-price").text().replace("$", "").replace(",", ""));
				}
				products.add(price.select(".postid_7477795394").text());
				number = number + 1;
				Element links = price.select("a").first();
				String url = links.attr("href");
				products.add(url);
				
			}
			if(products.size() > 0) {
			cheapest.add(products.get(0));
			cheapest.add(products.get(1));
			cheapest.add(products.get(2));
			average = all / number;
			}
			for (int i = 0; i < products.size() - 3; i++) {
				System.out.println(products.get(i));
				System.out.println(products.get(i + 1));
				System.out.println(products.get(i + 2));
				}
			
		}
	public static void main(String[] args) throws IOException {
		Craigslist.getItems("rtx 3090", 0, 2000);
		System.out.println("||||||||||||||||||END||||||||||||||||||");
		System.out.println(getLowest());
		System.out.println(getAverage());
		
		
	}
}
