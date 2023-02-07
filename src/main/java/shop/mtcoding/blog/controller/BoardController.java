package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @PostMapping("/board/{id}/update")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, BoardUpdateReqDto boardUpdateDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않습니다", HttpStatus.UNAUTHORIZED);
        }
        if (boardUpdateDto.getTitle() == null || boardUpdateDto.getTitle().isEmpty()) {
            throw new CustomException("title을 작성해주세요");
        }
        if (boardUpdateDto.getContent() == null || boardUpdateDto.getContent().isEmpty()) {
            throw new CustomException("content를 작성해주세요");
        }
        boardService.글수정(boardUpdateDto, id, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "수정성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증되지 않았습니다", HttpStatus.UNAUTHORIZED); // HttpStatus이 인증되지 않았음.
        }
        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
        // return "redirect:/";
    }

    @PostMapping("/board")
    public String save(BoardSaveReqDto BoardSaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (BoardSaveReqDto.getTitle() == null || BoardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("title을 작성해주세요");
        }
        if (BoardSaveReqDto.getContent() == null || BoardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("content를 작성해주세요");
        }
        if (BoardSaveReqDto.getContent() == null || BoardSaveReqDto.getContent().isEmpty()
                || BoardSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("100자이내로 작성해주세요");
        }

        boardService.글쓰기(BoardSaveReqDto, principal.getId());
        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    public String main(Model model) {
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/updateForm";
    }
}
