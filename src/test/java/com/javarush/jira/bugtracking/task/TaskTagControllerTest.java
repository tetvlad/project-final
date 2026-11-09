package com.javarush.jira.bugtracking.task;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.bugtracking.task.TaskController.REST_URL;
import static com.javarush.jira.bugtracking.task.TaskTestData.NOT_FOUND;
import static com.javarush.jira.bugtracking.task.TaskTestData.TASK1_ID;
import static com.javarush.jira.login.internal.web.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskTagControllerTest extends AbstractControllerTest {

    private static final String TAGS_URL = REST_URL + "/" + TASK1_ID + "/tags";

    @Autowired
    private TaskService taskService;

    /**
     * Тег добавляется к задаче.
     */
    @Test
    @WithUserDetails(value = USER_MAIL)
    void addTag() throws Exception {
        perform(MockMvcRequestBuilders.post(TAGS_URL)
                .param("tag", "backend"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertTrue(taskService.getTags(TASK1_ID).contains("backend"));
    }

    /**
     * Тег удаляется из задачи.
     */
    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteTag() throws Exception {
        taskService.addTag(TASK1_ID, "backend");

        perform(MockMvcRequestBuilders.delete(TAGS_URL)
                .param("tag", "backend"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(taskService.getTags(TASK1_ID).contains("backend"));
    }

    /**
     * Теги задачи отдаются списком.
     */
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTags() throws Exception {
        taskService.addTag(TASK1_ID, "backend");

        perform(MockMvcRequestBuilders.get(TAGS_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Для несуществующей задачи теги не отдаются.
     */
    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTagsNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND + "/tags"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Без входа теги не отдаются.
     */
    @Test
    void getTagsUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(TAGS_URL))
                .andExpect(status().isUnauthorized());
    }
}
