import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
public class Scraper {

	public static void main(String[] args) {
		final String URL = "https://web.archive.org/web/20190104110157/http://shares.telegraph.co.uk/indices/?index=MCX";
		
		try {
			final Document dc = (Document) Jsoup.connect(URL);
			System.out.println(dc.outerHtml());
			System.out.println(dc.outerHtml());
			
			for(Element row : dc.select("table.tablesorter.full tr")) {
				
			}
		
		
		
		}catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
