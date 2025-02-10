package com.helios.genovia.consultation.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helios.genovia.consultation.dto.request.AnswerDto;
import com.helios.genovia.consultation.dto.request.ConsultationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsultationApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("Test is get questions endpoint returns valid results")
    public void testForValidResults() throws Exception {
        mvc.perform(get("/api/1/consultation/questions"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Are you over 18?")))
                .andExpect(content().string(containsString("Have you used our medicine before?")))
                .andExpect(content().string(containsString("Are you allergic to compound X that is used in our medicines?")))
                .andExpect(content().string(containsString("Do you have an Y condition?")));
    }
    @Test
    @DisplayName("Test for invalid submission - not all answer answered")
    public void testForNotAllAnswerPresent() throws Exception {
        String payload = objectMapper.writeValueAsString(new ConsultationRequest(List.of()));
        mvc.perform(post("/api/1/consultation")
                        .content(payload)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"UNANSWERED_QUESTIONS\"")))
                .andExpect(content().string(containsString("d3be7310-d52a-451a-86f3-a34297216fc")))
                .andExpect(content().string(containsString("fd434b60-9704-499f-8a95-165c9eca29d0")))
                .andExpect(content().string(containsString("3659a568-2719-4cec-b42c-9e7e2e488218")))
                .andExpect(content().string(containsString("56b09455-33a9-4c41-94e1-86309f092a2d")))
                .andExpect(content().string(containsString("\"INVALID_IDS\":\"\"")));



    }

    @Test
    @DisplayName("Test for invalid submission - question id is invalid")
    public void testForNotInvalidQuestionId() throws Exception {
        List<AnswerDto> answers = List.of(
                new AnswerDto(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"), true),
                new AnswerDto(UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"), true),
                new AnswerDto(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"), true),
                new AnswerDto(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"), true),
                new AnswerDto(UUID.fromString("79993caf-c4e9-4025-b5b2-09da3276385a"), true)
                );

        String payload = objectMapper.writeValueAsString(new ConsultationRequest(answers));

        mvc.perform(post("/api/1/consultation").content(payload).contentType("application/json"))
                .andExpect(status().isBadRequest()).andExpect(content().string(containsString("\"INVALID_IDS\":\"79993caf-c4e9-4025-b5b2-09da3276385a\"")));
    }

    @Test
    @DisplayName("Test for invalid submission - Duplicate answers")
    public void testForDuplicateAnswers() throws Exception {
        List<AnswerDto> answers = List.of(
                new AnswerDto(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"), true),
                new AnswerDto(UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"), true),
                new AnswerDto(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"), true),
                new AnswerDto(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"), true),
                new AnswerDto(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"), true)
        );

        String payload = objectMapper.writeValueAsString(new ConsultationRequest(answers));

        mvc.perform(post("/api/1/consultation").content(payload).contentType("application/json"))
                .andExpect(status().isBadRequest()).andExpect(content().string(containsString("\"DUPLICATE_ANSWERS\":\"3659a568-2719-4cec-b42c-9e7e2e488218\"")));
    }
    @Test
    @DisplayName("Test for valid submission - Rejected")
    public void testForConsultationRejected() throws Exception {
        List<AnswerDto> answers = List.of(
                new AnswerDto(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"), true),
                new AnswerDto(UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"), true),
                new AnswerDto(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"), true),
                new AnswerDto(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"), true)
        );

        String payload = objectMapper.writeValueAsString(new ConsultationRequest(answers));

        mvc.perform(post("/api/1/consultation").content(payload).contentType("application/json"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"Unfortunately we won't be able to provide you with medicine due to following reasons\",\"rejectionReasons\":[\"Our medicine could cause allergic reaction\",\"Our medicine could cause allergic reaction\"")));
    }

    @Test
    @DisplayName("Test for valid submission - Accepted")
    public void testForConsultationAccepted() throws Exception {
        List<AnswerDto> answers = List.of(
                new AnswerDto(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"), true),
                new AnswerDto(UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"), true),
                new AnswerDto(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"), false),
                new AnswerDto(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"), false)
        );

        String payload = objectMapper.writeValueAsString(new ConsultationRequest(answers));

        mvc.perform(post("/api/1/consultation").content(payload).contentType("application/json"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("\"result\":\"Waiting for doctor approval\"")));
    }

}
