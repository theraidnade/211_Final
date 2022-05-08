import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// imports from jsoup and other files
public class CraigslistScraper {
	//returns the cheapest item in a String array
	public static String[] getLowest(List<String> listings) throws IOException {
		if (listings.size() == 0 || listings == null) { //if the listings return as an empty list then just return null because their isn't a cheapest
			return null;
		}
		double lowVal = Double.MAX_VALUE; //this way whatever the first listing is it would be the cheapest current one
		int minimumIndex = 0; // the index of the cheapest one
		for (int j = 0; j < listings.size(); j = j+3) { //iterate through the listings
			if (Double.parseDouble(listings.get(j+1)) < lowVal) {// if the price of the current one is cheaper then set the values as such
				minimumIndex = j;
				lowVal = Double.parseDouble(listings.get(j+1));
			}
		}
		String[] returnArray = {listings.get(minimumIndex), listings.get(minimumIndex+1), listings.get(minimumIndex+2)}; //return String[] with everything
		return returnArray;
	}
	public static double getAverage(List<String> listings) {//returns the average price for the item on this web-site
		if (listings.size() == 0 || listings == null) {//if the listings aren't there then just return negative one
			return -1;
		}
		DecimalFormat avgFormat = new DecimalFormat("#.00");// for the decimal formatting
		int number_of_items = listings.size()/3;//the list has 3 things 
		double Temporary_Total = 0.0;
		double Average_Price = 0.0;
		for (int i = 0; i < listings.size(); i = i+3) {
			Temporary_Total += Double.parseDouble(listings.get(i+1));
		}
		Average_Price = Temporary_Total/number_of_items;
		return Double.parseDouble(avgFormat.format(Average_Price));
	}
	public static List<String> getItems(String keyword, int min, int max, String location) throws IOException {
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
		if(location.equals("montgomery")) {
			URL = URL.replace("washingtondc", "montgomery");
		}
		if(location.equals("anchorage")) {
			URL = URL.replace("washingtondc", "anchorage");
		}
		if(location.equals("flagstaff")) {
			URL = URL.replace("washingtondc", "flagstaff");
		}
		if(location.equals("phoenix")) {
			URL = URL.replace("washingtondc", "phoenix");
		}
		if(location.equals("tucson")) {
			URL = URL.replace("washingtondc", "tucson");
		}
		if(location.equals("fayar")) {
			URL = URL.replace("washingtondc", "fayar");
		}
		if(location.equals("littlerock")) {
			URL = URL.replace("washingtondc", "littlerock");
		}
		if(location.equals("fortsmith")) {
			URL = URL.replace("washingtondc", "fortsmith");
		}
		if(location.equals("santamaria")) {
			URL = URL.replace("washingtondc", "santamaria");
		}
		if(location.equals("santabarbara")) {
			URL = URL.replace("washingtondc", "santabarbara");
		}
		if(location.equals("sfbay")) {
			URL = URL.replace("washingtondc", "sfbay");
		}
		if(location.equals("sacramento")) {
			URL = URL.replace("washingtondc", "sacramento");
		}
		if(location.equals("sandiego")) {
			URL = URL.replace("washingtondc", "sandiego");
		}
		if(location.equals("palmsprings")) {
			URL = URL.replace("washingtondc", "palmsprings");
		}
		if(location.equals("orangecounty")) {
			URL = URL.replace("washingtondc", "orangecounty");
		}
		if(location.equals("boulder")) {
			URL = URL.replace("washingtondc", "boulder");
		}
		if(location.equals("denver")) {
			URL = URL.replace("washingtondc", "denver");
		}
		if(location.equals("cosprings")) {
			URL = URL.replace("washingtondc", "cosprings");
		}
		if(location.equals("hartford")) {
			URL = URL.replace("washingtondc", "hartford");
		}
		if(location.equals("newhaven")) {
			URL = URL.replace("washingtondc", "newhaven");
		}
		if(location.equals("newlondon")) {
			URL = URL.replace("washingtondc", "newlondon");
		}
		if(location.equals("nwct")) {
			URL = URL.replace("washingtondc", "nwct");
		}
		if(location.equals("delaware")) {
			URL = URL.replace("washingtondc", "delaware");
		}
		if(location.equals("daytona")) {
			URL = URL.replace("washingtondc", "daytona");
		}
		if(location.equals("keys")) {
			URL = URL.replace("washingtondc", "keys");
		}
		if(location.equals("miami")) {
			URL = URL.replace("washingtondc", "miami");
		}
		if(location.equals("orlando")) {
			URL = URL.replace("washingtondc", "orlando");
		}
		if(location.equals("tallahassee")) {
			URL = URL.replace("washingtondc", "tallahassee");
		}
		if(location.equals("tampa")) {
			URL = URL.replace("washingtondc", "tampa");
		}
		if(location.equals("augusta")) {
			URL = URL.replace("washingtondc", "augusta");
		}
		if(location.equals("savannah")) {
			URL = URL.replace("washingtondc", "savannah");
		}
		if(location.equals("atlanta")) {
			URL = URL.replace("washingtondc", "atlanta");
		}
		if(location.equals("honolulu")) {
			URL = URL.replace("washingtondc", "honolulu");
		}
		if(location.equals("boise")) {
			URL = URL.replace("washingtondc", "boise");
		}
		if(location.equals("eastidaho")) {
			URL = URL.replace("washingtondc", "eastidaho");
		}
		if(location.equals("bn")) {
			URL = URL.replace("washingtondc", "bn");
		}
		if(location.equals("chambana")) {
			URL = URL.replace("washingtondc", "chambana");
		}
		if(location.equals("chicago")) {
			URL = URL.replace("washingtondc", "chicago");
		}
		if(location.equals("rockford")) {
			URL = URL.replace("washingtondc", "rockford");
		}
		if(location.equals("springfieldil")) {
			URL = URL.replace("washingtondc", "springfieldil");
		}
		if(location.equals("iowacity")) {
			URL = URL.replace("washingtondc", "iowacity");
		}
		if(location.equals("siouxcity")) {
			URL = URL.replace("washingtondc", "siouxcity");
		}
		if(location.equals("cedarrapids")) {
			URL = URL.replace("washingtondc", "cedarrapids");
		}
		if(location.equals("desmoines")) {
			URL = URL.replace("washingtondc", "desmoines");
		}
		if(location.equals("topeka")) {
			URL = URL.replace("washingtondc", "topeka");
		}
		if(location.equals("wichita")) {
			URL = URL.replace("washingtondc", "wichita");
		}
		if(location.equals("eastky")) {
			URL = URL.replace("washingtondc", "eastky");
		}
		if(location.equals("louisville")) {
			URL = URL.replace("washingtondc", "louisville");
		}
		if(location.equals("batonrouge")) {
			URL = URL.replace("washingtondc", "batonrouge");
		}
		if(location.equals("cenla")) {
			URL = URL.replace("washingtondc", "cenla");
		}
		if(location.equals("neworleans")) {
			URL = URL.replace("washingtondc", "neworleans");
		}
		if(location.equals("maine")) {
			URL = URL.replace("washingtondc", "maine");
		}
		if(location.equals("annapolis")) {
			URL = URL.replace("washingtondc", "annapolis");
		}
		if(location.equals("baltimore")) {
			URL = URL.replace("washingtondc", "baltimore");
		}
		if(location.equals("frederick")) {
			URL = URL.replace("washingtondc", "frederick");
		}
		if(location.equals("boston")) {
			URL = URL.replace("washingtondc", "boston");
		}
		if(location.equals("lansing")) {
			URL = URL.replace("washingtondc", "lansing");
		}
		if(location.equals("up")) {
			URL = URL.replace("washingtondc", "up");
		}
		if(location.equals("annarbor")) {
			URL = URL.replace("washingtondc", "annarbor");
		}
		if(location.equals("minneapolis")) {
			URL = URL.replace("washingtondc", "minneapolis");
		}
		if(location.equals("rmn")) {
			URL = URL.replace("washingtondc", "rmn");
		}
		if(location.equals("jackson")) {
			URL = URL.replace("washingtondc", "jackson");
		}
		if(location.equals("columbiamo")) {
			URL = URL.replace("washingtondc", "columbiamo");
		}
		if(location.equals("kansascity")) {
			URL = URL.replace("washingtondc", "kansascity");
		}
		if(location.equals("semo")) {
			URL = URL.replace("washingtondc", "semo");
		}
		if(location.equals("springfield")) {
			URL = URL.replace("washingtondc", "springfield");
		}
		if(location.equals("stlouis")) {
			URL = URL.replace("washingtondc", "stlouis");
		}
		if(location.equals("lasvegas")) {
			URL = URL.replace("washingtondc", "lasvegas");
		}
		if(location.equals("reno")) {
			URL = URL.replace("washingtondc", "reno");
		}
		if(location.equals("santafe")) {
			URL = URL.replace("washingtondc", "santafe");
		}
		if(location.equals("albany")) {
			URL = URL.replace("washingtondc", "albany");
		}
		if(location.equals("buffalo")) {
			URL = URL.replace("washingtondc", "buffalo");
		}
		if(location.equals("newyork")) {
			URL = URL.replace("washingtondc", "newyork");
		}
		if(location.equals("rochester")) {
			URL = URL.replace("washingtondc", "rochester");
		}
		if(location.equals("asheville")) {
			URL = URL.replace("washingtondc", "asheville");
		}
		if(location.equals("greensboro")) {
			URL = URL.replace("washingtondc", "greensboro");
		}
		if(location.equals("raleigh")) {
			URL = URL.replace("washingtondc", "raleigh");
		}
		if(location.equals("akroncanton")) {
			URL = URL.replace("washingtondc", "akroncanton");
		}
		if(location.equals("cincinnati")) {
			URL = URL.replace("washingtondc", "cincinnati");
		}
		if(location.equals("cleveland")) {
			URL = URL.replace("washingtondc", "cleveland");
		}
		if(location.equals("amarillo")) {
			URL = URL.replace("washingtondc", "amarillo");
		}
		if(location.equals("wichitafalls")) {
			URL = URL.replace("washingtondc", "wichitafalls");
		}
		if(location.equals("lubbock")) {
			URL = URL.replace("washingtondc", "lubbock");
		}
		if(location.equals("sanantonio")) {
			URL = URL.replace("washingtondc", "sanantonio");
		}
		if(location.equals("dallas")) {
			URL = URL.replace("washingtondc", "dallas");
		}
		if(location.equals("austin")) {
			URL = URL.replace("washingtondc", "austin");
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
				productsListings.add(price.select(".result-price").text().replace("$", "").replace(",", ""));
				Element links = price.select("a").first();
				String url = links.attr("href");
				productsListings.add(url);
				//personal
				int i = 0;
				while(i < productsListings.size() - 2) {
					System.out.println(productsListings.get(i));
					System.out.println(productsListings.get(i + 1));
					System.out.println(productsListings.get(i + 2));
					i = i + 1;
				}
				//personal
			}
			return productsListings;
		}
	
	}
