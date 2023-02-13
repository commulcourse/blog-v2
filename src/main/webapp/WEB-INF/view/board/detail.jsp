<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %>


<div class="container my-3">
    <c:if test="${boardDto.userId == principal.id}">
        <div class="mb-3">
            <a href="/board/${boardDto.id}/updateForm" class="btn btn-warning">수정</a>
            <button onclick="deleteById(${boardDto.id})" class="btn btn-danger">삭제</button>
        </div>
    </c:if>


    <div class="mb-2">
        글 번호 :
        <span id="id">
            <i>${boardDto.id} </i>
        </span>
        작성자 :
        <span class="me-3">
            <i>${boardDto.username} </i>
        </span>

    </div>

    <div>
        <h2><b>${boardDto.title}</b></h2>
    </div>
    <hr/>
    <div>
        <div>${boardDto.content}</div>
    </div>
    <hr/>

        <i id="heart" class="fa-regular fa-heart my-xl my-cursor" value="no"></i>&nbsp;

    <div class="card">
        <form action="/reply" method="post">
                <input type="hidden" name="boardId" value="${boardDto.id}">
            <div class="card-body">
                <textarea id="reply-comment" name="comment" class="form-control" rows="1"></textarea>
            </div>
            <div class="card-footer">
                <button type="submit" id="btn-reply-save" class="btn btn-primary">등록</button>
            </div>
        </form>
    </div>
    <br/>
    <div class="card">
        <div class="card-header">댓글 리스트</div>
        <ul id="reply-box" class="list-group">
            <c:forEach items="${replyDtos}" var="reply">
                <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                    <div>${reply.comment}</div>
                    <div class="d-flex">
                        <div class="font-italic">작성자 : ${reply.username}</div>
                        <c:if test="${principal.id == reply.userId}" > 
                        <button onClick="deleteByReplyId(${reply.id})" class="badge bg-secondary">삭제</button>
                        </c:if>
                    </div>
                </li>
            </c:forEach>

        </ul>
    </div>
</div>

<script>
    function deleteByReplyId(id){
        $.ajax({
            type: "delete",
            url: "/reply/" + id,
            dataType:"json"
        }).done((res) => {
            alert(res.msg);
            // location.reload(); //새로고침 데이터수정이나 삭제가 1건이라면 문제가 없지만
            $("#reply-"+id).remove(); //많은경우라면 reload를 하지 않아야한다. 가령 100개라면 reload로 페이지를 전체적으로 100번 받아버리는 셈이다.
        }).fail((err) => {
            alert(err.responseJSON.msg);
        });
    }
</script>
<script>
    function deleteById(id) {
        $.ajax({
            type: "delete",
            url: "/board/" + id,
            dataType: "json"
        }).done((res) => { // 20X일때
            alert(res.msg);
            location.href = "/";
        }).fail((err) => { // 40X, 50X일때
            alert(err.responseJSON.msg);
        });
    }
</script>

<%@ include file="../layout/footer.jsp" %>