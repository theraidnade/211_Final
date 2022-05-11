import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// imports from jsoup and other files

/**
 * Class to scrape from craigslist.com
 * @author Musa Hassan
 * @version 1.00
 */

public class CraigslistScraper {
	//returns the cheapest item in a String array
	
	/**
	 * Method to determine the cheapest item of every item found in the getItems method
	 * @param listings - array list of String values that contains items pulled from getItems 
	 * @return list of the cheapest item with name, price, and link in that order
	 */
	
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
	/**
	 * Method to determine the mean cost of every item found in the getItems method
	 * @param listings - array list of String values that contains items pulled from  getItems 
	 * @return double representing average of all craigslist items found
	 */
	public static double getAverage(List<String> listings) {//returns the average price for the item on this web-site
		if (listings.size() == 0 || listings == null) {//if the listings aren't there then just return negative one
			return -1;
		}
		DecimalFormat avgFormat = new DecimalFormat("#.00");// for the decimal formatting
		int number_of_items = listings.size()/3;//the list has 3 things 
		double Temporary_Total = 0.0; //used to calculate average
		double Average_Price = 0.0; //the average variable 
		for (int i = 0; i < listings.size(); i = i+3) { //to add the prices
			Temporary_Total += Double.parseDouble(listings.get(i+1)); //to add the prices
		}
		Average_Price = Temporary_Total/number_of_items; //average price
		return Double.parseDouble(avgFormat.format(Average_Price)); //return double average
	}
	
	/**
	 * acutal method for the scraping from craigslist
	 * @param keyword - String representing the item required
	 * @param minPrice - int representing the cheapest. Assumed to be 0 if nothing is passed
	 * @param maxPrice - int representing the maximum price for the item, assumed to be 0 then changed to Integer.MAX_VALUE
	 * @return List<String> -ArrayList of string with the information for each item in the order Name, Price, Link 
	 */
	
	public static List<String> getItems(String keyword, int min, int max, String location) throws IOException {
		ArrayList<String> productsListings = new ArrayList<String>(); //to hold the product listings
		keyword = keyword.replace(" ", "+"); //so that spaces are turned into plus signs for the URL
		String URL = String.format("https://washingtondc.craigslist.org/search/springfield-va/sss?query=%s&lat=38.74500&lon=-77.23300&sort=priceasc&search_distance=58", keyword);
		if(min == 0 && max == 0) {//^ for the URL
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
		} //the above if else statements and such are for the max prices. If they are both 0, then it doesn't matter. 
		//if ones is 0 and one a number then the one that is a number is used while the other is ignored.
		
		if(location.replaceAll(" ", "").equalsIgnoreCase("location not listed")) {//location URL
			;
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("montgomery")) {
			URL = URL.replace("washingtondc", "montgomery");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("anchorage")) {
			URL = URL.replace("washingtondc", "anchorage");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("flagstaff")) {
			URL = URL.replace("washingtondc", "flagstaff");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("phoenix")) {
			URL = URL.replace("washingtondc", "phoenix");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("tucson")) {
			URL = URL.replace("washingtondc", "tucson");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("fayar")) {
			URL = URL.replace("washingtondc", "fayar");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("littlerock")) {
			URL = URL.replace("washingtondc", "littlerock");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("fortsmith")) {
			URL = URL.replace("washingtondc", "fortsmith");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("santamaria")) {
			URL = URL.replace("washingtondc", "santamaria");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("santabarbara")) {
			URL = URL.replace("washingtondc", "santabarbara");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("sanfranciscobay")) {
			URL = URL.replace("washingtondc", "sfbay");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("sacramento")) {
			URL = URL.replace("washingtondc", "sacramento");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("sandiego")) {
			URL = URL.replace("washingtondc", "sandiego");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("palmsprings")) {
			URL = URL.replace("washingtondc", "palmsprings");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("orangecounty")) {
			URL = URL.replace("washingtondc", "orangecounty");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("boulder")) {
			URL = URL.replace("washingtondc", "boulder");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("denver")) {
			URL = URL.replace("washingtondc", "denver");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("cosprings")) {
			URL = URL.replace("washingtondc", "cosprings");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("hartford")) {
			URL = URL.replace("washingtondc", "hartford");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("newhaven")) {
			URL = URL.replace("washingtondc", "newhaven");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("newlondon")) {
			URL = URL.replace("washingtondc", "newlondon");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("northwesternconnecticut")) {
			URL = URL.replace("washingtondc", "nwct");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("delaware")) {
			URL = URL.replace("washingtondc", "delaware");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("daytona")) {
			URL = URL.replace("washingtondc", "daytona");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("keys")) {
			URL = URL.replace("washingtondc", "keys");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("miami")) {
			URL = URL.replace("washingtondc", "miami");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("orlando")) {
			URL = URL.replace("washingtondc", "orlando");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("tallahassee")) {
			URL = URL.replace("washingtondc", "tallahassee");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("tampa")) {
			URL = URL.replace("washingtondc", "tampa");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("augusta")) {
			URL = URL.replace("washingtondc", "augusta");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("savannah")) {
			URL = URL.replace("washingtondc", "savannah");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("atlanta")) {
			URL = URL.replace("washingtondc", "atlanta");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("honolulu")) {
			URL = URL.replace("washingtondc", "honolulu");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("boise")) {
			URL = URL.replace("washingtondc", "boise");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("eastidaho")) {
			URL = URL.replace("washingtondc", "eastidaho");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("bloomington")) {
			URL = URL.replace("washingtondc", "bn");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("champaignurbana")) {
			URL = URL.replace("washingtondc", "chambana");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("chicago")) {
			URL = URL.replace("washingtondc", "chicago");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("rockford")) {
			URL = URL.replace("washingtondc", "rockford");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("springfieldillinois")) {
			URL = URL.replace("washingtondc", "springfieldil");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("iowacity")) {
			URL = URL.replace("washingtondc", "iowacity");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("siouxcity")) {
			URL = URL.replace("washingtondc", "siouxcity");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("cedarrapids")) {
			URL = URL.replace("washingtondc", "cedarrapids");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("desmoines")) {
			URL = URL.replace("washingtondc", "desmoines");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("topeka")) {
			URL = URL.replace("washingtondc", "topeka");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("wichita")) {
			URL = URL.replace("washingtondc", "wichita");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("eastky")) {
			URL = URL.replace("washingtondc", "eastky");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("louisville")) {
			URL = URL.replace("washingtondc", "louisville");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("batonrouge")) {
			URL = URL.replace("washingtondc", "batonrouge");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("centrallouisiana")) {
			URL = URL.replace("washingtondc", "cenla");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("neworleans")) {
			URL = URL.replace("washingtondc", "neworleans");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("maine")) {
			URL = URL.replace("washingtondc", "maine");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("annapolis")) {
			URL = URL.replace("washingtondc", "annapolis");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("baltimore")) {
			URL = URL.replace("washingtondc", "baltimore");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("frederick")) {
			URL = URL.replace("washingtondc", "frederick");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("boston")) {
			URL = URL.replace("washingtondc", "boston");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("lansing")) {
			URL = URL.replace("washingtondc", "lansing");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("upperpeninsula")) {
			URL = URL.replace("washingtondc", "up");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("annarbor")) {
			URL = URL.replace("washingtondc", "annarbor");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("minneapolis")) {
			URL = URL.replace("washingtondc", "minneapolis");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("rochesterminnesota")) {
			URL = URL.replace("washingtondc", "rmn");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("jackson")) {
			URL = URL.replace("washingtondc", "jackson");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("columbiamo")) {
			URL = URL.replace("washingtondc", "columbiamo");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("kansascity")) {
			URL = URL.replace("washingtondc", "kansascity");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("southeastmissouri")) {
			URL = URL.replace("washingtondc", "semo");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("springfield")) {
			URL = URL.replace("washingtondc", "springfield");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("stlouis")) {
			URL = URL.replace("washingtondc", "stlouis");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("lasvegas")) {
			URL = URL.replace("washingtondc", "lasvegas");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("reno")) {
			URL = URL.replace("washingtondc", "reno");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("santafe")) {
			URL = URL.replace("washingtondc", "santafe");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("albany")) {
			URL = URL.replace("washingtondc", "albany");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("buffalo")) {
			URL = URL.replace("washingtondc", "buffalo");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("newyork")) {
			URL = URL.replace("washingtondc", "newyork");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("rochesternewyork")) {
			URL = URL.replace("washingtondc", "rochester");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("asheville")) {
			URL = URL.replace("washingtondc", "asheville");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("greensboro")) {
			URL = URL.replace("washingtondc", "greensboro");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("raleigh")) {
			URL = URL.replace("washingtondc", "raleigh");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("akroncanton")) {
			URL = URL.replace("washingtondc", "akroncanton");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("cincinnati")) {
			URL = URL.replace("washingtondc", "cincinnati");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("cleveland")) {
			URL = URL.replace("washingtondc", "cleveland");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("amarillo")) {
			URL = URL.replace("washingtondc", "amarillo");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("wichitafalls")) {
			URL = URL.replace("washingtondc", "wichitafalls");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("lubbock")) {
			URL = URL.replace("washingtondc", "lubbock");
		}
		else 	if(location.replaceAll(" ", "").equalsIgnoreCase("sanantonio")) {
			URL = URL.replace("washingtondc", "sanantonio");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("dallas")) {
			URL = URL.replace("washingtondc", "dallas");
		}
		else if(location.replaceAll(" ", "").equalsIgnoreCase("austin")) {
			URL = URL.replace("washingtondc", "austin");
		}
		
			Document eBayData = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
			//above line searches for the product
			Elements item_price = eBayData.select(".result-info");//set the item_price to the html part for info 
			for(Element price : item_price) {//searches through the elements
				if(price.toString().contains("broken") || price.toString().toLowerCase().contains("for parts") || price.toString().toLowerCase().contains("parts")|| price.toString().toLowerCase().contains("defective")) {
					continue; //if the products are broken, defective, or anything else like that ust continue
				}
				if(price.select(".result-price").text().replace("$", "").replace(",", "").equals("") || price.select(".result-price").text().replace("$", "").replace(",", "").equals("0") || price.select(".result-price").text().replace("$", "").replace(",", "").equals("1") || price.select(".result-price").text().replace("$", "").replace(",", "") == null) {
					continue; //if the prices are not real, like 0, or no listed price, or 1, or null just continue 
				}
				if(Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) > max || Double.parseDouble(price.select(".result-price").text().replace("$", "").replace(",", "")) < min) {
					continue;//if the price is greater than max or less than min just move on
				}
				Element title = price.select("a").first();// the title is hidden in the href, so we have to dig through that
				String name = title.select("a").first().text();//and the title is qeual to the text
				productsListings.add(name);//add the name
				productsListings.add(price.select(".result-price").text().replace("$", "").replace(",", ""));//add the price but as a string
				Element links = price.select("a").first();//same steps for the link
				String url = links.attr("href");//again same thing
				productsListings.add(url);// and the link for the listing 
			
			}
			return productsListings;
		}
	
	}
