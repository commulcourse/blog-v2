package shop.mtcoding.blog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

import shop.mtcoding.blog.model.User;

/*
 * AutoConfigureMockMvc : Moc 컨테이너 환경 (가짜IOC환경 )
 * @SpringBootTest : 통합테스트(모두다띄워서 실행하는것과 비슷하게 한다.)
 */
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper om;

    // @BeforeAll //이건 일단 몰라도됨
    // public static void 테이블차리기() {};

    // @AfterEach
    // public void teardown() {
    // //데이터 삭제 후 다시 인서트됨. 이것도 일단 몰라도됨.
    // }

    @BeforeEach // Test메서드 실행진전마다 호출됨
    public void setUp() {
        // 임시로 세션 생성
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
    public void deleteReply_test() throws Exception {
        // given
        int id = 1;

        // when
        // delete이기 때문에 session만 있으면 됨.
        ResultActions resultActions = mvc.perform(
                delete("/reply/" + id)
                        .session(mockSession)); // session이 주입된 채로 요청
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);

        // then

    }

    @Test
    public void save_test() throws Exception {
        // given
        String comment = "hello";
        int boardId = 4;
        String requestBody = "comment=" + comment + "&boardId=" + boardId;

        // when
        // post.put이 아니면 전송할 데이터가 없기 때문에 content, contetnType를 삭제해야한다.
        // delete를 확인하면 됨.
        ResultActions resultActions = mvc.perform(
                post("/reply")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)); // session이 주입되어 요청함.

        resultActions.andExpect(status().is3xxRedirection());
    }

}