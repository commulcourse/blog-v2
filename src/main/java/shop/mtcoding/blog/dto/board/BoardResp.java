package shop.mtcoding.blog.dto.board;

import javax.swing.text.AbstractDocument.Content;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {

    @Setter
    @Getter
    public static class BoardMainResponseDto {
        private int id;
        private String title;
        private String username;
    }

    @Setter
    @Getter
    public static class BoardDetailResponseDto {
        private int id;
        private String title;
        private String Content;
        private int userId;
        private String username;

    }

}
