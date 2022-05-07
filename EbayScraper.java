
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {
	public static double averagePrice;
	public static String[] cheapestItem = new String[3];
	public static String[] getLowest(List<String> listings) throws IOException {
		if (listings.size() == 0 || listings == null) {
			return null;
		}
		double lowVal = Double.MAX_VALUE;
		int minimumIndex = 0;
		for (int j = 0; j < listings.size(); j = j+3) {
			if (Double.parseDouble(listings.get(j+1)) < lowVal) {
				minimumIndex = j;
				lowVal = Double.parseDouble(listings.get(j+1));
			}
		}
		String[] returnArray = {listings.get(minimumIndex), listings.get(minimumIndex+1), listings.get(minimumIndex+2)};
		return returnArray;
	}
	public static double getAverage(List<String> listings) {
		if (listings.size() == 0 || listings == null) {
			return -1;
		}
		DecimalFormat avgFormat = new DecimalFormat("#.00");
		int number_of_items = listings.size()/3;
		double Temporary_Total = 0.0;
		double Average_Price = 0.0;
		for (int i = 0; i < listings.size(); i = i+3) {
			Temporary_Total += Double.parseDouble(listings.get(i+1));
		}
		Average_Price = Temporary_Total/number_of_items;
		return Double.parseDouble(avgFormat.format(Average_Price));
	}
	
	public static ArrayList<String> getItems(String keyword, int min, int max) throws IOException{
		double TotalPrice = 0;
		String delete_string;
		String delete_string2;
		double number_of_listings = 0;
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&rt=nc&LH_BIN=1", keyword);
		ArrayList<String> productsListings = new ArrayList<String>();
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
				
				productsListings.add(price.select(".s-item__title").text());
				number_of_listings = number_of_listings + 1;
				if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("Free shipping")) {
					productsListings.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					if(price.select(".s-item__price").text().replace("$", "").replace(",", "").contains(" ")) {
						delete_string = price.select(".s-item__price").text().replace("$", "").replace(",", "").substring(0, price.select(".s-item__price").text().replace("$", "").replace(",", "").indexOf(" "));
						TotalPrice = TotalPrice + Double.parseDouble(delete_string);
					}
					else {
						TotalPrice = TotalPrice + Double.parseDouble(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					}
				}
				else if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("")) {
					productsListings.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
					if(price.select(".s-item__price").first().text().replace("$", "").replace(",", "").contains(" "))
					{
						delete_string2 = price.select(".s-item__price").text().replace("$", "").replace(",", "");
						TotalPrice = TotalPrice + Double.parseDouble(delete_string2.substring(0, delete_string2.indexOf(" ")));
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
					productsListings.add(totalString);
					TotalPrice = TotalPrice + Double.parseDouble(totalString);

				}
				Element links = price.select("a").first();
				String url = links.attr("href");
				productsListings.add(url);
			}
			if(productsListings.size() > 0) {
			cheapestItem[0] = productsListings.get(0);
			cheapestItem[1] = productsListings.get(1);
			cheapestItem[2] = productsListings.get(2);
			averagePrice = TotalPrice / number_of_listings;
			}
			System.out.println(TotalPrice);
			return productsListings;
	}
	public static void main(String[] args) throws IOException {
		System.out.println(getItems("s10", 150, 215));
	}
	
		}
