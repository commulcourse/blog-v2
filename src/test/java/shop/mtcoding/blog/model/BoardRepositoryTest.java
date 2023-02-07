package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainResponseDto;

// f - ds - 컨트롤러Controller -서비스Service - 레파지토리Repository - 마이바티스Mybatis
//SRP 솔리드의 원칙 - 단일책임의 원칙 // 나

// F - DS - C
@MybatisTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper(); // Jackson라이브러리라 작동할때 Jackson은 json으로 바꾸어준다. (컨트롤러에서 )
        // when
        List<BoardMainResponseDto> boardMainResponseDto = boardRepository.findAllWithUser();
        // System.out.println("테스트: size: " + BoardMainRespDto.size());

        String responseBody = om.writeValueAsString(boardMainResponseDto);
        System.out.println("테스트" + responseBody);

        // than

        assertThat(boardMainResponseDto.get(5).getUsername()).isEqualTo("love");

    }
}