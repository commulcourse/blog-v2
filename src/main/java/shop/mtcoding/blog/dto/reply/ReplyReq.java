package shop.mtcoding.blog.dto.reply;

import lombok.Getter;
import lombok.Setter;

public class ReplyReq {

    @Setter
    @Getter
    public static class ReplySaveReqDto {
        private String comment;
        private int userId;
        private int boardId;
    }
}