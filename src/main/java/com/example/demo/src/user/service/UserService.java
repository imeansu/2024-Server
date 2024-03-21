package com.example.demo.src.user.service;


import com.example.demo.common.Constant;
import com.example.demo.common.Constant.*;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.exceptions.business.TermsNotAgreedException;
import com.example.demo.common.history.DataHistoryService;
import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.common.service.OtpService;
import com.example.demo.src.user.dto.OtpReqDto;
import com.example.demo.src.user.dto.PostUserDto;
import com.example.demo.src.user.entity.TermsAgreement;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.repository.TermsAgreementRepository;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.repository.UserRepositorySupport;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final TermsAgreementRepository termsAgreementRepository;
    private final DataHistoryService dataHistoryService;
    private final JwtService jwtService;
    private final OtpService otpService;


    //POST
    public PostUserRes createUser(PostUserDto postUserDto) {
        //중복 체크
        Optional<User> checkLoginIdUser = userRepository.findByLoginIdAndState(postUserDto.getLoginId(), ACTIVE);
        if(checkLoginIdUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_LOGIN_ID);
        }

        // 정책 추가: PhoneNumber 중복 불가
        Optional<User> checkPhoneNumberUser = userRepository.findByPhoneNumberAndState(postUserDto.getPhoneNumber(), ACTIVE);
        if(checkPhoneNumberUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_LOGIN_ID);
        }

        // 소셜 로그인 중복 체크
        if (postUserDto.isOAuth()) {
            Optional<User> checkSocialLoginUser = userRepository.findBySocialLoginTypeAndOauthIdAndState(postUserDto.getSocialLoginType(), postUserDto.getOauthId(), ACTIVE);
            if(checkSocialLoginUser.isPresent()){
                throw new BaseException(POST_SOCIAL_LOGIN_USERS_EXISTS);
            }
        }

        // 약관 동의 체크
        if(postUserDto.getTerms() == null || !TermsAgreement.checkAllRequiredTerms(postUserDto.getTerms())){
            Set<TermsType> notAgreedTerms = new HashSet<>(TermsAgreement.requiredTermsTypes);
            if (postUserDto.getTerms() != null) {
                notAgreedTerms.removeAll(postUserDto.getTerms());
            }
            throw new TermsNotAgreedException(INVALID_TERMS_AGREEMENT, notAgreedTerms);
        }

        String encryptPwd;
        try {
            encryptPwd = SHA256.encrypt(postUserDto.getPassword());
            postUserDto.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserDto.toEntity());
        logHistory(saveUser.getId(), EventType.USER, DataEvent.SIGN_UP, saveUser, "sign up");

        postUserDto.getTerms().forEach( termsType -> {
            TermsAgreement termsAgreement = new TermsAgreement(saveUser, termsType);
            termsAgreementRepository.save(termsAgreement);
            logHistory(termsAgreement.getUser().getId(), EventType.USER, DataEvent.AGREE_TERMS, termsAgreement, "agree terms");
        });

        return new PostUserRes(saveUser.getId());
    }

    public void modifyUserName(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updateName(patchUserReq.getName());
        logHistory(userId, EventType.USER, DataEvent.MODIFY_USER_NAME, user, "modify user name");
    }

    public void resetPassword(OtpInfo otpInfo, String password) {
        User user = userRepository.findByIdAndState(otpInfo.getUserId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String encryptPwd;
        try {
            encryptPwd = SHA256.encrypt(password);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        user.resetPassword(encryptPwd);
        logHistory(otpInfo.getUserId(), EventType.USER, DataEvent.RESET_PASSWORD, user, "reset password");
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
        logHistory(userId, EventType.USER, DataEvent.DEACTIVATE_USER_BY_ADMIN, user, "deactivate user by admin");
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        List<GetUserRes> getUserResList = userRepository.findAllByLoginIdAndState(email, ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByEmail(String email) {
        Optional<User> result = userRepository.findByLoginIdAndState(email, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    @Transactional(readOnly = true)
    public boolean checkUserByOauthId(SocialLoginType socialLoginType, String oauthId) {
        Optional<User> result = userRepository.findBySocialLoginTypeAndOauthIdAndState(socialLoginType, oauthId, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByPhoneNumberAndName(String phoneNumber, String name) {
        return userRepository.findByPhoneNumberAndNameAndState(phoneNumber, name, ACTIVE);
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByLoginIdAndState(postLoginReq.getLoginId(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(!user.getPasswordHash().equals(encryptPwd)){
            throw new BaseException(FAILED_TO_LOGIN);
        }

        user.canLogin();

        Long userId = user.getId();
        String jwt = jwtService.createJwt(userId);
        user.updateLastLoginAt();
        logHistory(userId, EventType.USER, DataEvent.LOGIN, user, "login");
        return new PostLoginRes(userId,jwt);
    }

    public List<GetUserRes> searchUsers(UserSearchCriteria c, PageRequest pageRequest) {
        UserStatus status = Constant.valueOfOrNull(UserStatus.class, c.getUserStatus());

        Page<User> users = userRepositorySupport.searchUsers(c.getName(), c.getLoginId(), c.getCreatedAt(), status, pageRequest);
        return users.stream().map(GetUserRes::new).collect(Collectors.toList());
    }

    public GetUserRes getUserByLoginId(String loginId) {
        User user = userRepository.findByLoginIdAndState(loginId, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    public GetUserRes getUserByOauthId(SocialLoginType socialLoginType, String oauthId) {
        User user = userRepository.findBySocialLoginTypeAndOauthIdAndState(socialLoginType, oauthId, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    public OtpInfo createOtp(OtpReqDto otpReqDto) {
        OtpInfo otpInfo = otpService.generateOtp(otpReqDto);
        logHistory(null, EventType.USER, DataEvent.CREATE_OTP, otpInfo, "create otp for password reset");
        return otpInfo;
    }

    public OtpInfo verifyOtp(OtpVerityReq otpVerityReq, OtpInfo otpInfo) {
        OtpInfo cacheOtp = otpService.getCacheOtp(otpInfo.getPhoneNumber());
        if (cacheOtp.getOtp().equals(otpVerityReq.getOtp())) {
            otpService.clearOtp(otpInfo.getPhoneNumber());
            cacheOtp.setSuccess(true);
            logHistory(null, EventType.USER, DataEvent.VERIFY_OTP_SUCESS, otpInfo, "verify otp for password reset success");
            return cacheOtp;
        }

        logHistory(null, EventType.USER, DataEvent.VERIFY_OTP_FAIL, otpInfo, "verify otp for password reset fail");
        return cacheOtp;
    }

    public List<User> getNeedLawNotifyUsers(LocalDateTime now) {
        return userRepositorySupport.getLastLawNotifiedUsersBefore(now);
    }

    private void logHistory(Long userId, EventType eventType, DataEvent dataEvent, Object data, String reason) {
        dataHistoryService.save(DataHistory.builder()
                .userId(userId)
                .eventType(eventType)
                .dataEvent(dataEvent)
                .data(data)
                .reason(reason)
                .build());
    }
}
