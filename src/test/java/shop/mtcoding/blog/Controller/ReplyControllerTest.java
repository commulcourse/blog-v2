package shop.mtcoding.blog.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.dto.board.BoardResp;
import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트 (실제환경과 동일하게 Bean이 생성됨, 포트도 랜덤으로 만들어짐)
 * @AutoConfigureMockMvc는 Mock 환경의 IoC컨테이너에 MockMvc Bean이 생성됨
*/

@Transactional // 메서드 실행직후 롤백!! //auto_increment 초기화
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

    // @BeforeAll
    // public static void 테이블차리기(){
    // }

    // @AfterEach
    // public void teardown(){ //데이터 다 날리고 다시 인서트
    // }
    //

    @BeforeEach // @Test메서드 실행 직전 마다 호출됨
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");

        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void save_test() throws Exception {
        // given
        String comment = "댓글1";
        int boardId = 1;

        String requestBody = "comment" + comment + "&boardId=" + boardId;
        // when
        ResultActions resultActions = mvc.perform(
                post("/reply")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession));

        // System.out.println("save_test : ");
        // then
        resultActions.andExpect(status().isCreated());
    }

}