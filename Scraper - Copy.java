import org.jsoup.nodes.Document;
import java.util.Scanner;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {

	public static void main(String[] args) {
		Scanner inp = new Scanner(System.in);
		System.out.println("Input your desired material wants");
		String keyword = inp.nextLine();
		keyword = keyword.replace(" ", "+");
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0", keyword);
		ArrayList<String> prices = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			System.out.println(eBayData.outerHtml());
			Elements item_price = eBayData.select("s-item__detail s-item__detail--primary");
			for(Element listPrice : item_price) {
				if(listPrice.select("s-item__price").text().equals("") || listPrice.select("s-item__price").text().length() > 20) {
					continue;
				}else {
					String p$ = listPrice.select("s-item__price").text();
					prices.add(p$);
				}
			}
		Elements Name = eBayData.select("s-item__info clearfix");
		for(Element LName : PPrice) {
			if(LName.select("s-item__title").text().equals("")) {
				continue;
			}else {
				String PName = LName.select("s-item__title").text();
				nameList.add(PName);
			}
		}
		for (int index = 0; index < prices.size(); index++) {
			System.out.println(nameList.get(index) + ": " + prices.get(index));
		}
		}
		}

	


