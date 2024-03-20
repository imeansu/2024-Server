package com.example.demo.common.history;

import com.example.demo.common.history.entity.DataHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataHistoryRepository extends JpaRepository<DataHistory, Long> {

}
