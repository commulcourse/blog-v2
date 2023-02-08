package shop.mtcoding.blog.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import shop.mtcoding.blog.util.ThumbParse;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void 글수정(int id, int principalId, BoardUpdateReqDto boardUpdateReqDto) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("글이 존재 하지않아 수정이 불가합니다.");
        }
        if (boardPS.getUserId() != principalId) {
            throw new CustomApiException("게시물의 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        String thumbnail = ThumbParse.Thumbnail(boardUpdateReqDto.getContent());
        try {
            int result = boardRepository.updateById(id, boardUpdateReqDto.getTitle(), boardUpdateReqDto.getContent(),
                    thumbnail);
        } catch (Exception e) {
            throw new CustomApiException("서버의 문제로 글수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // return 1;
    }

    // where 절에 걸리는 파라미터를 앞에 받기
    @Transactional
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {

        // 1. content 내용을 Document로 받고, img 찾아내서(0,1,2) src를 찾아서 thumbnail추가

        String html = boardSaveReqDto.getContent();
        String thumbnail = "";
        Document doc = Jsoup.parse(html);
        // System.out.println(doc);
        Elements els = doc.select("img");
        if (els.size() == 0) {
            thumbnail = "/images/dora.png";
        } else {
            Element el = els.get(0);
            thumbnail = el.attr("src");
            // System.out.print(img);
        }
        System.out.println(els);
        int result = boardRepository.insert(
                boardSaveReqDto.getTitle(),
                boardSaveReqDto.getContent(),
                thumbnail,
                userId);
        if (result != 1) {
            throw new CustomException("회원가입실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /// images/dora.png
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