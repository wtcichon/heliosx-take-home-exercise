package com.helios.genovia.consultation.repository;


import com.helios.genovia.consultation.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class QuestionRepositoryTest {


    @Test
    @DisplayName("Create repository, and get questions")
    void testCreateAndGet() {
        QuestionRepository questionRepository = new QuestionRepository("classpath:data/questions.json");
        List<Question> questions = questionRepository.getQuestions();
        List<Question> expected = createQuestions();
        assertThat("List equality without order",
                questions, containsInAnyOrder(expected.toArray()));
    }

    @Test
    @DisplayName("Create repository, and get mandatory questions")
    void testCreateAndGetMandatoryQuestions() {
        QuestionRepository questionRepository = new QuestionRepository("classpath:data/questions.json");
        List<Question> questions = questionRepository.getMandatoryQuestions();
        List<Question> expected = createMandatoryQuestions();
        assertThat("List equality without order",
                questions, containsInAnyOrder(expected.toArray()));
    }

    @Test
    @DisplayName("Create repository, and get question Ids")
    void testCreateAndGetQuestionIds() {
        QuestionRepository questionRepository = new QuestionRepository("classpath:data/questions.json");
        Set<UUID> questions = questionRepository.getQuestionIds();
        List<UUID> expected = createQuestionsIds();
        assertThat("List equality without order",
                questions, containsInAnyOrder(expected.toArray()));
    }

    private List<UUID> createQuestionsIds() {
        return List.of(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"),
                UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"),
                UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"),
                UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"));
    }

    private List<Question> createQuestions() {
        return List.of(
                new Question(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"),
                        "Are you over 18?", true, "You should be over 18 years old to use our service"),

                new Question(UUID.fromString("fd434b60-9704-499f-8a95-165c9eca29d0"),
                        "Have you used our medicine before?",
                        null,
                        null),
                new Question(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"),
                        "Are you allergic to compound X that is used in our medicines?",
                        false,
                        "Our medicine could cause allergic reaction"),
                new Question(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"),
                        "Do you have an Y condition?",
                        false,
                        "Our medicine could cause allergic reaction"));
    }

    private List<Question> createMandatoryQuestions() {
        return List.of(
                new Question(UUID.fromString("d3be7310-d52a-451a-86f3-a34297216fc8"),
                        "Are you over 18?", true, "You should be over 18 years old to use our service"),
             new Question(UUID.fromString("3659a568-2719-4cec-b42c-9e7e2e488218"),
                        "Are you allergic to compound X that is used in our medicines?",
                        false,
                        "Our medicine could cause allergic reaction"),
                new Question(UUID.fromString("56b09455-33a9-4c41-94e1-86309f092a2d"),
                        "Do you have an Y condition?",
                        false,
                        "Our medicine could cause allergic reaction"));
    }


}
