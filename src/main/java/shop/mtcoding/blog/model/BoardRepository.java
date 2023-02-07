package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailResponseDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResponseDto;

@Mapper
public interface BoardRepository {

        public BoardDetailResponseDto findByIdWithUser(int id);

        public List<BoardMainResponseDto> findAllWithUser();

        public List<Board> findAll();

        public Board findById(int id);

        public int insert(@Param("title") String title, @Param("content") String content,
                        @Param("userId") int userId);

        public int updateById(@Param("id") int id, @Param("title") String title,
                        @Param("content") String content);

        public int deleteById(int id);
}