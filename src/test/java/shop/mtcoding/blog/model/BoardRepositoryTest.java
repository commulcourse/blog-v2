package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResponseDto;

@MybatisTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper();
        // when
        List<BoardMainResponseDto> boardMainResponseDto = boardRepository.findAllWithUser();
        // System.out.println("테스트: size: " + BoardMainRespDto.size());
        String responseBody = om.writeValueAsString(boardMainResponseDto);
        System.out.println("테스트" + responseBody);
        // than

        assertThat(boardMainResponseDto.get(5).getUsername()).isEqualTo("love");

    }
}
