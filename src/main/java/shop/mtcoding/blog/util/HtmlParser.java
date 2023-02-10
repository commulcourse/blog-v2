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
// 브랜치 dev 하나 만들고 dev에서 기본세팅해놓고, 뷰까지 해놓고
// 토필하나 만들어서 회원가입 기능완료, 회원가입 테스트 완료, dev 에 머지
// 로ㄱㄴ기능 완료 로긴 기능 테스트완료 머지
// dev머지 다되고 나서 github에 푸쉬 하고
// 토픽도 올라가고 dev? 아 헷갈려 머라구여
//

/*
 * dev 브랜치 만들고, git으로 commit 시키기
 * 그러고 토픽도계속올리고, 머지도 계속 올려놔.
 * 
 * commit 된것을 master가 승인할 때 Confirm스쿼시 머지로 해서 깔끔하게 한개만하게 해줌
 */