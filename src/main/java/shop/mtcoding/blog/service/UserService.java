package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional // insert에는 transaction이 붙는다. read는 동시 가능하다. 하지만 insert가 동시에 되지 않기 때문에 시간이 많이
                   // 걸린다고 생각하면된다.
    public void 회원가입(JoinReqDto joinReqDto) {
        User sameUser = userRepository.findByUsername(joinReqDto.getUsername());
        if (sameUser != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        // 동시에 하다가 변경코드가 발행시 Rock걸린다.
        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());
        if (result != 1) {
            throw new CustomException("회원가입실패");
        }

    }

    // select를 하지 않으니깐 @Transactioanl을 하지 않아도 된다. 실시간 변경되어야 하는 곳에는 넣으면된다.
    // 이렇게 @Transactional 걸었을 경우, 변경되는 값이 아니라
    // 현재에 보는 값으로만 연산이 가능하다는 것을 의미한다. 로그인같은경우에는 무조건 걸지 않아도 된다.
    @Transactional(readOnly = true)
    public User 로그인(LoginReqDto loginReqDto) {
        User principal = userRepository.findByUsernameAndPassword(
                loginReqDto.getUsername(), loginReqDto.getPassword());

        if (principal == null) {
            throw new CustomException("유저네임 혹은 패스워드가 잘못되었습니다.");
        }
        // User user = new User();
        // user.setId(1);
        // user.setUsername("ssar");
        // user.setPassword("1234");
        // user.setEmail("ssar@nate.com");

        return principal;
    }
}
