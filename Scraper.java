import org.jsoup.nodes.Document;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {

	public static void getItems(String keyword, int min, int max) throws IOException {
		ArrayList<String> products = new ArrayList<String>();
		keyword = keyword.replace(" ", "+");
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&_sop=15&rt=nc&LH_BIN=1", keyword);
		ArrayList<String> prices = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			Elements item_price = eBayData.select(".s-item__info.clearfix");
			for(Element price : item_price) {
				products.add(price.select(".s-item__title").text());
				if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("Free shipping")) {
					products.add(price.select(".s-item__price").text().replace("$", ""));
				}
				else if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("")) {
					continue;
				}
				else {
					double pre = Double.parseDouble(price.select(".s-item__price").text().replace("$", ""));
					double post = Double.parseDouble(price.select(".s-item__shipping.s-item__logisticsCost").text().replace("+$", ""));
//fix string thing shipping space.

				}
			}
			System.out.println(products);
			for (String product: products) {
			System.out.println(product);
			}
		}
	public static void main(String[] args) throws IOException {
		Scraper.getItems("rtx 3060", 10, 11);
	}
	
		}