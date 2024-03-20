package com.example.demo.src.user.entity;

import com.example.demo.common.Constant.TermsType;
import com.example.demo.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "terms_agrement")
public class TermsAgreement extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TermsType termsType;

    public TermsAgreement(User user, TermsType termsType) {
        this.user = user;
        this.termsType = termsType;
    }

    public static Set<TermsType> requiredTermsTypes = new HashSet<>(Arrays.asList(TermsType.SERVICE, TermsType.DATA, TermsType.LOCATION));

    public static boolean checkAllRequiredTerms(Set<TermsType> termsTypes) {
        return termsTypes.containsAll(requiredTermsTypes);
    }
}
