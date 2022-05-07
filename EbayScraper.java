package scraperPackage;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class EbayScraper {
	public static double average;
	public static String[] cheapest = new String[3];
	public static String[] getLowest(List<String> items) throws IOException {
		return cheapest;
	}
	public static double getAverage(List<String> items) {
		return average;
	}
	public static ArrayList<String> getItems(String keyword, int min, int max) throws IOException{
		double all = 0;
		String temp1;
		String temp;
		double number = 0;
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&rt=nc&LH_BIN=1", keyword);
		ArrayList<String> products = new ArrayList<String>();
		keyword = keyword.replace(" ", "+");
		if(min == 0 && max == 0) {
			URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&rt=nc&LH_BIN=1", keyword);
			max = Integer.MAX_VALUE;
			}
		else if(min != 0 && max != 0){
			URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&_udlo=150&rt=nc&_udhi=15000", keyword);
			URL = URL.replaceFirst("150", Integer.toString(min));
			URL = URL.replaceFirst("15000", Integer.toString(max));
			}
			else if (min == 0 && max != 0) {//5&max_price=15000
				URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&rt=nc&_udhi=15000", keyword);
				URL = URL.replaceFirst("15000", Integer.toString(max));
			}else if (min != 0 && max == 0) {//5&max_price=15000
				URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&_udlo=150&rt=nc", keyword);
				URL = URL.replaceFirst("150", Integer.toString(min));
			}
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			Elements item_price = eBayData.select(".s-item__info.clearfix");
			for(Element price : item_price) {
				if(price.toString().contains("Shop on eBay")) {
					continue;
				}
				
				products.add(price.select(".s-item__title").text());
				number = number + 1;
				if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("Free shipping")) {
					products.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					if(price.select(".s-item__price").text().replace("$", "").replace(",", "").contains(" ")) {
						temp1 = price.select(".s-item__price").text().replace("$", "").replace(",", "").substring(0, price.select(".s-item__price").text().replace("$", "").replace(",", "").indexOf(" "));
						all = all + Double.parseDouble(temp1);
					}
					else {
						all = all + Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					}
				}
				else if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("")) {
					products.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					if(price.select(".s-item__price").first().text().replace("$", "").replace(",", "").contains(" "))
					{
						temp = price.select(".s-item__price").text().replace("$", "").replace(",", "");
						all = all + Double.parseDouble(temp.substring(0, temp.indexOf(" ")));
					}
				}
				else {
					String post = price.select(".s-item__shipping.s-item__logisticsCost").text().replace("+$", "").replace(",", "");
					int indexof = post.indexOf(" ");
					post = post.substring(0, indexof);
					String preDouble = price.select(".s-item__price").text().replace("$", "").replace(",", ""); //preDouble.substring(0, preDouble.indexOf(" "))
					if(preDouble.contains(" ")) {
						preDouble = preDouble.substring(0, preDouble.indexOf(" "));
					}
					double pre = Double.parseDouble(preDouble);
					double dpost = Double.parseDouble(post);
					double total = pre + dpost;
					String totalString = String.valueOf(total);	
					products.add(totalString);
					all = all + Double.parseDouble(totalString);

				}
				Element links = price.select("a").first();
				String url = links.attr("href");
				products.add(url);
			}
			if(products.size() > 0) {
			cheapest[0] = products.get(0);
			cheapest[1] = products.get(1);
			cheapest[2] = products.get(2);
			average = all / number;
			}
			return products;
	}
	
		}
