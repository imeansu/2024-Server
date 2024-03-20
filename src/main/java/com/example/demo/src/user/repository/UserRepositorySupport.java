package com.example.demo.src.user.repository;

import com.example.demo.common.Constant.UserStatus;
import com.example.demo.src.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.src.user.entity.QUser.user;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public Page<User> searchUsers(String name, String loginId, LocalDate createdAt, UserStatus status, PageRequest pageRequest) {
        Predicate datePredicate = null;
        if (createdAt != null) {
            LocalDateTime from = createdAt.atStartOfDay();
            LocalDateTime to = createdAt.atTime(23, 59, 59, 59);
            datePredicate = user.createdAt.between(from, to);
        }

        PageRequest pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by("id").descending());

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqName(name))
                .and(eqLoginId(loginId))
                .and(eqUserStatus(status))
                .and(datePredicate);

        List<User> users = queryFactory
                .selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.id.desc())
                .fetch();

        long totalCount = queryFactory
                .select(user.count())
                .from(user)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(users, pageable, totalCount);
    }

    private BooleanExpression eqName(String name) {
        if (!StringUtils.hasLength(name)) {
            return null;
        }
        return user.name.eq(name);
    }

    private BooleanExpression eqLoginId(String loginId) {
        if (!StringUtils.hasLength(loginId)) {
            return null;
        }
        return user.loginId.eq(loginId);
    }

    private BooleanExpression eqUserStatus(UserStatus status) {
        if (status == null) {
            return null;
        }

        return Expressions.numberTemplate(Integer.class, "function('bitand', {0}, {1})", user.userStatus, status.getValue()).gt(0);
    }

}
