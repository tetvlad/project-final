package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {
    private static final long TASK_ID = 5;

    @Autowired
    private TaskService taskService;

    @Autowired
    private Handlers.TaskHandler taskHandler;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void addStatus(String status, String time) {
        jdbcTemplate.update("insert into ACTIVITY (AUTHOR_ID, TASK_ID, UPDATED, STATUS_CODE) values (1, ?, cast(? as timestamp), ?)",
                TASK_ID, time, status);
    }

    @Test
    void countDevAndTestTime() {
        addStatus("in_progress", "2023-06-14 10:00:00");
        addStatus("ready_for_review", "2023-06-15 15:30:00");
        addStatus("done", "2023-06-16 12:00:00");
        Task task = taskHandler.getRepository().getExisted(TASK_ID);

        assertEquals(Duration.ofHours(29).plusMinutes(30), taskService.countDevTime(task));
        assertEquals(Duration.ofHours(20).plusMinutes(30), taskService.countTestTime(task));
    }

    @Test
    void countTimeWithoutHistory() {
        Task task = taskHandler.getRepository().getExisted(TASK_ID);

        assertEquals(Duration.ZERO, taskService.countDevTime(task));
        assertEquals(Duration.ZERO, taskService.countTestTime(task));
    }
}
