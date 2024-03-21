package com.example.demo.src.user.repository;

import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndState(Long id, State state);
    Optional<User> findByLoginIdAndState(String email, State state);
    Optional<User> findByPhoneNumberAndState(String PhoneNumber, State state);
    Optional<User> findBySocialLoginTypeAndOauthIdAndState(SocialLoginType socialLoginType, String oauthId, State state);
    Optional<User> findByPhoneNumberAndNameAndState(String phoneNumber, String name, State state);
    List<User> findAllByLoginIdAndState(String email, State state);
    List<User> findAllByState(State state);

}
