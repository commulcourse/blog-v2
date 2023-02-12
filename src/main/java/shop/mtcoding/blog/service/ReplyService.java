package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.ReplyRepository;

@Transactional(readOnly = true)
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    /*
     * @Transactional
     * public void 글수정(int id, int principalId, ReplyUpdateReqDto replyUpdateReqDto)
     * {
     * Board boardPS = replyRepository.findById(id);
     * if (boardPS == null) {
     * throw new CustomApiException("글이 존재 하지않아 수정이 불가합니다.");
     * }
     * if (boardPS.getUserId() != principalId) {
     * throw new CustomApiException("게시물의 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
     * }
     * String thumbnail = HtmlParser.getThumbnail(replyUpdateReqDto.getContent());
     * 
     * try {
     * int result = replyRepository.updateById(id, replyUpdateReqDto.getTitle(),
     * replyUpdateReqDto.getContent(),
     * thumbnail);
     * } catch (Exception e) {
     * throw new CustomApiException("서버의 문제로 글수정에 실패하였습니다.",
     * HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * // return 1;
     * }
     */

    // where 절에 걸리는 파라미터를 앞에 받기
    @Transactional
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {

        int result = replyRepository.insert(
                replySaveReqDto.getComment(),
                // replySaveReqDto.getUserId(),
                replySaveReqDto.getBoardId(),
                principalId);

        if (result != 1) {
            throw new CustomException("댓글쓰기실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /// images/dora.png
    }
    /*
     * @Transactional
     * public void 게시글삭제(int id, int userId) {
     * Reply boardPS = replyRepository.findById(id);
     * if (boardPS == null) {
     * throw new CustomApiException("없는 게시글은 삭제할 수 없습니다.");
     * }
     * if (boardPS.getUserId() != userId) {
     * throw new CustomApiException("해당게시물을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
     * }
     * 
     * try {
     * replyRepository.deleteById(id);
     * } catch (Exception e) {
     * throw new CustomApiException("서버에 일시적인 문제가 생겼습니다.",
     * HttpStatus.INTERNAL_SERVER_ERROR);
     * // 터진 후 로그를 남겨야 함(DB or File)
     * // CustomApiHandlerException 을 하나 더 만들어서..
     * }
     * }
     */

}