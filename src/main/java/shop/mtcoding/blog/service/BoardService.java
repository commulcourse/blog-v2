package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional // 서비스에서 Exception 예외잡아내기, Where 절에 걸리는 파라미터를 앞에 받아야 한다고 했음!
    public void 글수정(BoardUpdateReqDto boardUpdateDto, int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("글이 존재 하지않아 수정이 불가합니다.");
        }
        if (boardPS.getUserId() != userId) {
            throw new CustomApiException("게시물의 수정 권한이 없습니다.");
        }
        try { // !!!!!!!!!!물어볼것!!!!!!!!!!! 왜 boardRepository에서만 id를 들고오는지?
              // boardUpdateReqDto의 id를 가지고 오면 안되는지!!!!???
            boardRepository.updateById(id, boardUpdateDto.getTitle(), boardUpdateDto.getContent());
        } catch (Exception e) {
            throw new CustomApiException("서버의 문제로 글수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return 1;
    }

    // where 절에 걸리는 파라미터를 앞에 받기
    @Transactional
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {
        int result = boardRepository.insert(
                boardSaveReqDto.getTitle(),
                boardSaveReqDto.getContent(),
                userId);
        if (result != 1) {
            throw new CustomException("회원가입실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public void 게시글삭제(int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글은 삭제할 수 없습니다.");
        }
        if (boardPS.getUserId() != userId) {
            throw new CustomApiException("해당게시물을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        try {
            boardRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            // 터진 후 로그를 남겨야 함(DB or File)
            // CustomApiHandlerException 을 하나 더 만들어서..
        }
    }

}
