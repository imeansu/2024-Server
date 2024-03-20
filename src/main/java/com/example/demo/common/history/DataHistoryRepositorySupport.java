package com.example.demo.common.history;

import com.example.demo.common.Constant.DataEvent;
import com.example.demo.common.Constant.EventType;
import com.example.demo.common.history.entity.DataHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.common.history.entity.QDataHistory.dataHistory;

@Repository
public class DataHistoryRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public DataHistoryRepositorySupport(JPAQueryFactory queryFactory) {
        super(DataHistory.class);
        this.queryFactory = queryFactory;
    }

    public Page<DataHistory> searchUsers(Long userId, EventType eventType, DataEvent dataEvent, LocalDate createdAt, Pageable pageable)
    {
        Predicate datePredicate = null;
        if (createdAt != null) {
            LocalDateTime from = createdAt.atStartOfDay();
            LocalDateTime to = createdAt.atTime(23, 59, 59, 59);
            datePredicate = dataHistory.createdAt.between(from, to);
        }

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqUserId(userId))
                .and(eqEventType(eventType))
                .and(eqDataEvent(dataEvent))
                .and(datePredicate);

        List<DataHistory> dataHistories = queryFactory
                .selectFrom(dataHistory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(dataHistory.id.desc())
                .fetch();

        long totalCount = queryFactory
                .select(dataHistory.count())
                .from(dataHistory)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(dataHistories, pageable, totalCount);
    }

    private BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return dataHistory.userId.eq(userId);
    }

    private BooleanExpression eqEventType(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return dataHistory.eventType.eq(eventType);
    }

    private BooleanExpression eqDataEvent(DataEvent dataEvent) {
        if (dataEvent == null) {
            return null;
        }
        return dataHistory.dataEvent.eq(dataEvent);
    }

}
