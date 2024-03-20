package com.example.demo.src.user;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.exceptions.business.SocialLoginUserNotFoundException;
import com.example.demo.common.exceptions.business.SocialLoginUserNotFoundException.UnsignedUpSocialLoginUser;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.user.dto.PostUserDto;
import com.example.demo.src.user.model.GetSocialOAuthRes;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.src.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SocialLoginController {

    private final OAuthService oAuthService;

    private final UserService userService;

    private final String UNSIGNEDUP_SOCIAL_LOGIN_USER_SESSION_KEY = "unsignedUpSocialLoginUserCode";

    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /login/:socialLoginType
     * @return void
     */
    @GetMapping("/loginUrl/{socialLoginType}")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.accessRequest(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     * @throws SocialLoginUserNotFoundException 소셜 로그인 사용자가 가입한 적이 없는 경우
     * Exception 안에 있는 socialLoginType, oauthId 로 소셜 로그인과 연결하여 가입을 진행할 수 있도록 한다.
     */
    @ResponseBody
    @GetMapping(value = "/login/{socialLoginType}")
    public BaseResponse<GetSocialOAuthRes> socialLoginCallback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code,
            HttpServletRequest request
    ) throws IOException, BaseException {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code : {}", code);
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        try {
            GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(socialLoginType,code);
            return new BaseResponse<>(getSocialOAuthRes);
        } catch (SocialLoginUserNotFoundException e) {
            HttpSession session = request.getSession();
            session.setAttribute(UNSIGNEDUP_SOCIAL_LOGIN_USER_SESSION_KEY, e.getResult());
            throw e;
        }
    }

    /**
     * 소셜로그인 회원가입 API
     * [POST] /signup/{socialLoginType}
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/signup/{socialLoginType}")
    public BaseResponse<PostUserRes> createSocialLoginUser(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @Valid @RequestBody PostUserReq postUserReq,
            @RequestParam(name = "code") String code,
//            @SessionAttribute(name = UNSIGNEDUP_SOCIAL_LOGIN_USER_SESSION_KEY, required = false) UnsignedUpSocialLoginUser unsignedUpSocialLoginUser,
            HttpServletRequest request
    ) {
        UnsignedUpSocialLoginUser unsignedUpSocialLoginUser = (UnsignedUpSocialLoginUser) request.getSession().getAttribute(UNSIGNEDUP_SOCIAL_LOGIN_USER_SESSION_KEY);

        // 소셜 로그인은 성공했지만, 미가입으로 세션에 저장된 정보와 비교하여 같은 정보인지 확인한다.
        PostUserDto postUserDto = unsignedUpSocialLoginUserCheck(unsignedUpSocialLoginUser, postUserReq, socialLoginPath, code);

        PostUserRes postUserRes = userService.createUser(postUserDto);
        return new BaseResponse<>(postUserRes);
    }

    private PostUserDto unsignedUpSocialLoginUserCheck(UnsignedUpSocialLoginUser unsignedUpSocialLoginUser, PostUserReq postUserReq, String socialLoginPath, String code) {
        if (unsignedUpSocialLoginUser != null &&
                unsignedUpSocialLoginUser.getSocialLoginType().equals(Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase())) &&
                unsignedUpSocialLoginUser.getCode().equals(code)) {
            PostUserDto postUserDto = postUserReq.toDto();
            postUserDto.setOAuth(true);
            postUserDto.setSocialLoginType(unsignedUpSocialLoginUser.getSocialLoginType());
            postUserDto.setOauthId(unsignedUpSocialLoginUser.getOauthId());
            return postUserDto;
        }
        throw new BaseException(BaseResponseStatus.INVALID_SOCIAL_LOGIN_TOKEN);
    }
}
