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
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyDetailRespDto;
import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트 (실제환경과 동일하게 Bean이 생성됨, 포트도 랜덤으로 만들어짐)
 * @AutoConfigureMockMvc는 Mock 환경의 IoC컨테이너에 MockMvc Bean이 생성됨
*/

@Transactional // 메서드 실행직후 롤백!! //auto_increment 초기화
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

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
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoardUpdateReqDto boardUpdateReqDto = new BoardUpdateReqDto();
        boardUpdateReqDto.setTitle("제목-수정");
        boardUpdateReqDto.setContent("제목-수정");

        String requestBody = om.writeValueAsString(boardUpdateReqDto);
        System.out.println("테스트: " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(put("/board/" + id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));
        // then
        // resultActions.andExpect(status().isOk());
        // resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;
        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id)
                        .session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트" + responseBody);
        /*
         * jsonPath
         * 최상위:$
         * 객체 탐색: 닷(.)
         * 배열: [0]
         */
        // then
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());
    }

    // @Test
    // public void detail_test() throws Exception {
    // // given
    // int id = 1;
    // // when
    // ResultActions resultActions = mvc.perform(
    // get("/board/" + id));

    // Map<String, Object> map =
    // resultActions.andReturn().getModelAndView().getModel();
    // BoardDetailRespDto dto = (BoardDetailRespDto) map.get("dto");
    // String model = om.writeValueAsString(dto);
    // System.out.println("테스트:" + model);

    // // then
    // resultActions.andExpect(status().isOk());
    // assertThat(dto.getUsername()).isEqualTo("ssar");

    // }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto boardDto = (BoardDetailRespDto) map.get("boardDto");
        List<ReplyDetailRespDto> replyDtos = (List<ReplyDetailRespDto>) map.get("replyDtos");
        String boardJson = om.writeValueAsString(boardDto);
        String replyListJson = om.writeValueAsString(replyDtos);
        System.out.println("테스트 : " + boardJson);

        // then
        // resultActions.andExpect(status().isOk());
        // assertThat(boardDto.getUsername()).isEqualTo("ssar");
        // assertThat(boardDto.getuserId()).isEqualTo("1");
        // assertThat(boardDto.getTitle()).isEqualTo("1번째 제목");
        // assertThat(replyDtos.getComment(1)).isEqualTo("댓글");
        // assertThat(replyDtos.getUsername()).isEqualTo("");
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(
                get("/"));
        // then
        // resultActions.andExpect(status().isOk());
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardResp.BoardMainRespDto> dtos = (List<BoardResp.BoardMainRespDto>) map.get("dtos");
        String model = om.writeValueAsString(dtos);
        System.out.println("테스트:" + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.size()).isEqualTo(6);
        assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");

    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("제목");
        boardSaveReqDto.setContent("내용");

        String requestBody = om.writeValueAsString(boardSaveReqDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession));

        System.out.println("save_test : ");
        // then
        resultActions.andExpect(status().isCreated());
    }

}