package shop.mtcoding.blog.model;

import java.security.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reply {
    private int id;
    private String comment;
    private int boardId;
    private int userId;
    private Timestamp createdAt;
}
