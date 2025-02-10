package com.helios.genovia.consultation.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helios.genovia.consultation.model.Question;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class QuestionRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final List<Question> questions;

    public QuestionRepository(@Value("classpath:data/questions.json") String questionsPath) {
        questions = loadFromFile(questionsPath);
    }

    public List<Question> getMandatoryQuestions() {
        return questions.stream().filter(this::mandatoryQuestionFilter).toList();
    }

    private boolean mandatoryQuestionFilter(Question question) {
        return Objects.nonNull(question.expectedAnswer());
    }

    public Set<UUID> getQuestionIds() {
        return questions.stream()
                .map(Question::id)
                .collect(Collectors.toSet());
    }

    private List<Question> loadFromFile(String questionsPath) {
        try {
            File file = ResourceUtils.getFile(questionsPath);
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Error while processing file", e);
            throw new RuntimeException(e);
        }
    }
}
