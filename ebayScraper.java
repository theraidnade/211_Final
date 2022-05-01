import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {
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
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&_sop=15&rt=nc&LH_BIN=1", keyword);
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			Elements item_price = eBayData.select(".s-item__info.clearfix");
			for(Element price : item_price) {
				if(price.toString().contains("Shop on eBay") || price.toString().toLowerCase().contains("for parts") || price.toString().toLowerCase().contains("parts")|| price.toString().toLowerCase().contains("defective")) {
					continue;
				}
				if(Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", "")) > max || Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", "")) < min) {
					continue;
				}
				products.add(price.select(".s-item__title").text());
				number = number + 1;
				Element links = price.select("a").first();
				String url = links.attr("href");
				products.add(url);
				if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("Free shipping")) {
					products.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					all = all + Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
				}
				else if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("")) {
					products.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					all = all + Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
				}
				else {
					String post = price.select(".s-item__shipping.s-item__logisticsCost").text().replace("+$", "").replace(",", "");
					int indexof = post.indexOf(" ");
					post = post.substring(0, indexof);
					double pre = Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					double dpost = Double.parseDouble(post);
					double total = pre + dpost;
					String totalString = String.valueOf(total);	
					products.add(totalString);
					all = all + Double.parseDouble(totalString);

				}
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
	
	
		}
