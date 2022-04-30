package scraperPackage;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AmazonPackagezero {

	public static void main(String[] args) {
		ArrayList<String> priceList = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> linkList = new ArrayList<String>();
		String searchKeyword = "shampoo";
		searchKeyword = searchKeyword.replace(' ', '+');
		String productURL = String.format("https://www.amazon.com/s?k=%s&s=price-asc-rank&page=", searchKeyword);
		for (int urlPage = 1; urlPage < 5; urlPage++) {
			try {
				Document amazonData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").ignoreHttpErrors(true).referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productPrices = amazonData.select(".sg-col-inner");
				for (Element listingPrice : productPrices) {
					if (!(listingPrice.select(".a-size-medium.a-color-base.a-text-normal").text().equals(""))) {
						if (listingPrice.select(".a-size-medium.a-color-base.a-text-normal").text().length() < 300 && listingPrice.select(".a-offscreen").text().length() < 10 && listingPrice.select(".a-offscreen").text().length() > 0 && (!(nameList.contains(listingPrice.select(".a-size-medium.a-color-base.a-text-normal").text())))) {
							nameList.add(listingPrice.select(".a-size-medium.a-color-base.a-text-normal").text());
							priceList.add(listingPrice.select(".a-offscreen").text());
							linkList.add(listingPrice.select(".a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal").attr("abs:href"));
						}
					}
				}
				for (int index = 0; index < priceList.size(); index++) {
					System.out.println(nameList.get(index) + ": " + priceList.get(index));
					System.out.println(linkList.get(index));
					System.out.println();
				}
				System.out.println("////////////////////////");
				System.out.println(priceList.size());
			}
			catch (Exception scrapeFail) {
				scrapeFail.printStackTrace();
			}
		}
		System.out.println(nameList);
		System.out.println(priceList);
		System.out.println(linkList);
	}

}
