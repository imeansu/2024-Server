package com.example.demo.common.history;

import com.example.demo.common.history.entity.DataHistory;
import com.example.demo.common.history.model.GetDataHistoryRes;
import com.example.demo.src.admin.model.HistorySearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class DataHistoryService {

    private final MongoTemplate mongoTemplate;
    private final String DATA_HISTORY_COLLECTION_NAME = "data_history";

    public void save(DataHistory dataHistory) {
        mongoTemplate.save(dataHistory, DATA_HISTORY_COLLECTION_NAME);
    }

    public List<GetDataHistoryRes> searchHistory(HistorySearchCriteria c, PageRequest pageRequest) {
        Query query = new Query();
        if (c.getUserId() != null) {
            query.addCriteria(Criteria.where("userId").is(c.getUserId()));
        }
        if (StringUtils.hasLength(c.getEventType())) {
            query.addCriteria(Criteria.where("eventType").is(c.getEventType()));
        }
        if (StringUtils.hasLength(c.getDataEvent())) {
            query.addCriteria(Criteria.where("dataEvent").is(c.getDataEvent()));
        }
        if (c.getStartAt() != null) {
            query.addCriteria(Criteria.where("createdAt").gte(c.getStartAt()));
        }
        if (c.getEndAt() != null) {
            query.addCriteria(Criteria.where("createdAt").lte(c.getEndAt()));
        }

        query.with(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by("createdAt").descending()));
        return mongoTemplate.find(query, DataHistory.class, DATA_HISTORY_COLLECTION_NAME)
                .stream()
                .map(GetDataHistoryRes::new)
                .collect(Collectors.toList());
    }

}
