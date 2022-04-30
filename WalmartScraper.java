package scraperPackage;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WalmartScraper {

	public static void main(String[] args) {
		ArrayList<String> priceList = new ArrayList<String>();
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> linkList = new ArrayList<String>();
		String searchKeyword = "shampoo";
		searchKeyword = searchKeyword.replace(' ', '+');
		String productURL = String.format("https://www.walmart.com/search?q=%s&sort=price_low&page=", searchKeyword);
		for (int urlPage = 1; urlPage < 11; urlPage++) {
			try {
				Document walmartData = Jsoup.connect(productURL.concat(String.valueOf(urlPage))).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").referrer("http://www.google.com").timeout(20000000).followRedirects(true).get();
				Elements productPrices = walmartData.select(".mb1.ph1.pa0-xl.bb.b--near-white.w-25");
				for (Element listingPrice : productPrices) {
					if (!(listingPrice.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text().equals(""))) {
						if (listingPrice.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text().length() < 300 && listingPrice.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() < 10 && listingPrice.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text().length() > 0 && (!(nameList.contains(listingPrice.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text())))) {
							nameList.add(listingPrice.select(".f6.f5-l.normal.dark-gray.mb0.mt1.lh-title").text());
							priceList.add(listingPrice.select(".b.black.f5.mr1.mr2-xl.lh-copy.f4-l").text());
							linkList.add(listingPrice.select(".absolute.w-100.h-100.z-1").attr("abs:href"));
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