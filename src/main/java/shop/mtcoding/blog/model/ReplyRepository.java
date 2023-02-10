package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;

@Mapper
public interface ReplyRepository {

        public List<Reply> findAll();

        public Reply findById(int id);

        public int insert(@Param("content") String content,
                        @Param("userId") int userId,
                        @Param("boardId") int boardId);

        public int updateById(@Param("id") int id, @Param("content") String content);

        public int deleteById(int id);
}