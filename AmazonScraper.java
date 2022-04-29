package scraperPackage;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonScraper {

	public static void main(String[] args) {
		ArrayList<String> priceList = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		Scanner userInput = new Scanner(System.in);
		System.out.println("Please provide the name of the product that you are searching for.");
		String searchKeyword = userInput.nextLine();
		searchKeyword = searchKeyword.replace(' ', '+');
		String productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&page=", searchKeyword);
		for (int urlPage = 0; urlPage < 10; urlPage++) {
			try {
				Document amazonData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productPrices = amazonData.select(".sg-col-inner");
				for (Element listingPrice : productPrices) {
					if (!(listingPrice.select(".a-size-medium.a-color-base.a-text-normal").text().equals(""))) {
						nameList.add(listingPrice.select(".a-text-normal").text());
						priceList.add(listingPrice.select(".a-offscreen").text());
					}
				}
				for (int index = 0; index < priceList.size(); index++) {
					System.out.println(nameList.get(index) + ": " + priceList.get(index));
				}
				System.out.println("////////////////////////");
				System.out.println(priceList.size());
			}
			catch (Exception scrapeFail) {
				scrapeFail.printStackTrace();
			}
	}
	}

}
