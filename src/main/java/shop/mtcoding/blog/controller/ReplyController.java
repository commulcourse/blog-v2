package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private HttpSession session;

    // controller에서는 principal 권한 인증과 값만 받고, service에서 조건과 Exception 처리.
    // 글쓰기 처럼 타입이 다양한것이 아니니 String으로 받으면 됨. return은 save된 BoardId.
    @PostMapping("/reply")
    public String save(ReplySaveReqDto replySaveReqDto) {
        // 일단. 인증해야댐.
        User principal = (User) session.getAttribute("principal");
        // 안증되면 다음~~
        if (principal == null) {
            throw new CustomException("인증되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomException("comment를 작성해주세요");
        }
        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomException("boardId가 필요합니다");
        }
        // if (replySaveReqDto.getComment().length() > 100) {
        // throw new CustomException("100자이내로 작성해주세요");
        // }
        replyService.댓글쓰기(replySaveReqDto, principal.getId());
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

    // @RestController, @ResponseBody, ResponseEntity 이 중 하나만 있어도 됨. 다 동일하게 데이터를
    // 응답하는 것임.
    @DeleteMapping("/reply/{id}")
    public @ResponseBody ResponseEntity<?> deleteReply(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증되지 않았습니다", HttpStatus.UNAUTHORIZED); // 401에러
        }
        replyService.댓글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "댓글삭제성공", null), HttpStatus.OK);
    }
}
// 1인증, 2댓글존재유무확인, 3권한OK - Api:어노테이션을 만들어 사용하는 기술