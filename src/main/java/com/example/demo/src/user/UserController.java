package com.example.demo.src.user;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.dto.OtpReqDto;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.service.UserService;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;

    private final JwtService jwtService;


    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@Valid @RequestBody PostUserReq postUserReq) {

        PostUserRes postUserRes = userService.createUser(postUserReq.toDto());
        return new BaseResponse<>(postUserRes);
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /app/users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String loginId) {
        if(loginId == null){
            List<GetUserRes> getUsersRes = userService.getUsers();
            return new BaseResponse<>(getUsersRes);
        }
        // Get Users
        List<GetUserRes> getUsersRes = userService.getUsersByEmail(loginId);
        return new BaseResponse<>(getUsersRes);
    }

    /**
     * 회원 1명 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") Long userId) {
        GetUserRes getUserRes = userService.getUser(userId);
        return new BaseResponse<>(getUserRes);
    }

    /**
     * 회원 1명 조회 API With loginId
     * 아이디 중복 확인 API
     * [GET] /app/users/loginId/:loginId
     * @return BaseResponse
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/checkLoginId/{loginId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse checkLoginId(@PathVariable("loginId") String loginId) {
        if (userService.checkUserByEmail(loginId)) {
            throw new BaseException(POST_USERS_EXISTS_LOGIN_ID);
        }
        // 일단 그냥 SUCCESS 로 리턴
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 유저정보변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq){

        Long jwtUserId = jwtService.getUserId();

        userService.modifyUserName(userId, patchUserReq);

        String result = "수정 완료!!";
        return new BaseResponse<>(result);
    }

    /**
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}/deactivate")
    public BaseResponse<String> deactivateUser(@PathVariable("userId") Long userId){
        Long jwtUserId = jwtService.getUserId();

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }

    /**
     * 비번 찾기 OTP 발급 API
     * [POST] /app/users/password/otp
     * @return BaseResponse<OtpRes>
     */
    @ResponseBody
    @PostMapping("/password/otp")
    public BaseResponse<String> createOtp(@RequestBody OtpReq otpReq, HttpServletRequest request){
        User user = userService.getUserByPhoneNumberAndName(otpReq.getPhoneNumber(), otpReq.getName()).orElseThrow(
                () -> new BaseException(NOT_FIND_USER)
        );
        OtpInfo otpInfo = userService.createOtp(otpReq.toDto(user.getId()));
        String result = "OTP 발송 완료!!";
        request.getSession().setAttribute("otpInfo", otpInfo);
        return new BaseResponse<>(result);
    }

    /**
     * 비번 찾기 OTP 확인 API
     * [POST] /app/users/password/otp/verify
     * @return BaseResponse<OtpVerifyRes>
     */
    @ResponseBody
    @PostMapping("/password/otp/verify")
    public BaseResponse<String> verifyOtp(@RequestBody OtpVerityReq otpVerityReq, HttpServletRequest request){
        OtpInfo otpInfo = (OtpInfo) request.getSession().getAttribute("otpInfo");
        if (otpInfo == null) {
            throw new BaseException(INVALID_OTP);
        }

        OtpInfo result = userService.verifyOtp(otpVerityReq, otpInfo);
        request.getSession().setAttribute("otpInfo", result);
        if (result.isSuccess()) {
            return new BaseResponse<>(SUCCESS);
        }
        return new BaseResponse<>(INVALID_OTP);
    }

    /**
     * 비번 변경 API
     * [POST] /app/users/password/reset
     * @return BaseResponse<OtpVerifyRes>
     */
    @ResponseBody
    @PostMapping("/password/reset")
    public BaseResponse<String> resetPassword(@RequestBody PasswordResetReq passwordResetReq, HttpServletRequest request){
        OtpInfo otpInfo = (OtpInfo) request.getSession().getAttribute("otpInfo");
        if (otpInfo == null) {
            throw new BaseException(INVALID_OTP);
        }

        userService.resetPassword(otpInfo, passwordResetReq.getPassword());
        request.getSession().removeAttribute("otpInfo");

        String result = "비밀번호 변경 완료!!";
        return new BaseResponse<>(result);
    }
}
