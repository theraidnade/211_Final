import org.jsoup.nodes.Document;//needed imports
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Scraper {
	public static String[] getLowest(List<String> listings) throws IOException {//does the same exact things as the craigslist one
		if (listings.size() == 0 || listings == null) {//return nothing if the list is empty
			return null;
		}
		double lowVal = Double.MAX_VALUE; //sets to max so the first listing is equal to 0
		int minimumIndex = 0; //min is 0 
		for (int j = 0; j < listings.size(); j = j+3) {//for loop to find the cheapest one 
			if (Double.parseDouble(listings.get(j+1)) < lowVal) {
				minimumIndex = j;
				lowVal = Double.parseDouble(listings.get(j+1)); //sets the lowest value for this
			}
		}
		String[] returnArray = {listings.get(minimumIndex), listings.get(minimumIndex+1), listings.get(minimumIndex+2)};
		return returnArray; //return
	}
	public static double getAverage(List<String> listings) {//same average method as the craigslist one 
		if (listings.size() == 0 || listings == null) {// return null for unimportant lists
			return -1;
		}
		DecimalFormat avgFormat = new DecimalFormat("#.00"); //decimal format etc
		String place_holder_for_delete;
		int number_of_items = listings.size()/3;
		double Temporary_Total = 0.0;
		double Average_Price = 0.0;
		for (int i = 0; i < listings.size(); i = i+3) {
			if(listings.get(i+1).contains(" ")) {
				place_holder_for_delete = listings.get(i+1);
				Temporary_Total += Double.parseDouble(place_holder_for_delete.substring(0, place_holder_for_delete.indexOf(" ")));
			}else {
			Temporary_Total += Double.parseDouble(listings.get(i+1));
			}
		}
		Average_Price = Temporary_Total/number_of_items;
		return Double.parseDouble(avgFormat.format(Average_Price));
	}
	
	public static ArrayList<String> getItems(String keyword, int min, int max) throws IOException{//main scraper
		ArrayList<String> productsListings = new ArrayList<String>();//product listings
		keyword = keyword.replace(" ", "+");//replace the word space with plus
		String URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&rt=nc&LH_BIN=1", keyword);//set the URL
		if(min == 0 && max == 0) {//max and min are 0 so just ignore them
			URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2380057.m570.l1313&_nkw=%s&_sacat=0&_ipg=240&rt=nc&LH_BIN=1", keyword);
			max = Integer.MAX_VALUE;
			}
		else if(min != 0 && max != 0){//if neither are 0 then change the URL for the min and maxes
			URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&_udlo=150&rt=nc&_udhi=15000", keyword);
			URL = URL.replaceFirst("150", Integer.toString(min));
			URL = URL.replaceFirst("15000", Integer.toString(max));
			}
			else if (min == 0 && max != 0) {//if the max is the only non zero the set just the min
				URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&rt=nc&_udhi=15000", keyword);
				URL = URL.replaceFirst("15000", Integer.toString(max));
			}else if (min != 0 && max == 0) {//vice versa ^^
				URL = String.format("https://www.ebay.com/sch/i.html?_from=R40&_nkw=%s&_sacat=0&LH_TitleDesc=0&_udlo=150&rt=nc", keyword);
				URL = URL.replaceFirst("150", Integer.toString(min));
			}
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			Elements item_price = eBayData.select(".s-item__info.clearfix");//above line does the search and this line has the data
			for(Element price : item_price) {//search through
				if(price.toString().contains("Shop on eBay")) {
					continue;// if it has that on it skip it because it is filler every time
				}
				productsListings.add(price.select(".s-item__title").text());//find the title
				if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("Free shipping")) {//if shipping is free
					productsListings.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));//delete the commmas and such and add the price
				}
				else if(price.select(".s-item__shipping.s-item__logisticsCost").text().equals("")) {//if it is an empty shipping cost then add the price
					productsListings.add(price.select(".s-item__price").text().replace("$", "").replace(",", ""));
				}
				else {//if there is a shipping cost then add it but make sure to change a lot of stuff like commas. dollar signs, etc
					String post = price.select(".s-item__shipping.s-item__logisticsCost").text().replace("+$", "").replace(",", "");
					int indexof = post.indexOf(" ");
					post = post.substring(0, indexof);
					String preDouble = price.select(".s-item__price").text().replace("$", "").replace(",", "");
					if(preDouble.contains(" ")) { 
						preDouble = preDouble.substring(0, preDouble.indexOf(" "));
					}
					double pre = Double.parseDouble(preDouble); //set the previous one to a double
					double dpost = Double.parseDouble(post); //set post to a double
					double total = pre + dpost; //add the two prices so you get shipping + cost
					String totalString = String.valueOf(total);	//and add the total
					productsListings.add(totalString);

				}
				Element links = price.select("a").first(); //to add the complicated URL thing
				String url = links.attr("href");
				productsListings.add(url);
			}	
			return productsListings; //return list
	}
	
}
