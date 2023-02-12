package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
    public static String getThumbnail(String content) {
        String thumbnail;
        Document doc = Jsoup.parse(content);
        // System.out.println(doc);
        Elements els = doc.select("img");
        if (els.size() == 0) {
            thumbnail = "/images/dora.png";
        } else {
            Element el = els.get(0);
            thumbnail = el.attr("src");
            // System.out.print(img);
        }
        // System.out.println(els);
        return thumbnail;
    }

}
