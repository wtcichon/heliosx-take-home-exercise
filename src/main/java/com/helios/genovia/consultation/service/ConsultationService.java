package com.helios.genovia.consultation.service;

import com.helios.genovia.consultation.dto.request.AnswerDto;
import com.helios.genovia.consultation.dto.response.ConsultationResponse;
import com.helios.genovia.consultation.dto.response.QuestionDto;
import com.helios.genovia.consultation.exceptions.ConsultationFormException;
import com.helios.genovia.consultation.exceptions.DuplicateAnswersException;
import com.helios.genovia.consultation.model.Question;
import com.helios.genovia.consultation.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ConsultationService {

    private final QuestionRepository questionRepository;

    public List<QuestionDto> getQuestions() {
        return questionRepository.getQuestions()
                .stream()
                .map(question -> new QuestionDto(question.id(), question.question()))
                .toList();
    }

    public ConsultationResponse getConsultationResult(List<AnswerDto> answers) {
        log.info("Start to process answers");
        Map<UUID, AnswerDto> answersMap = createAnswersMap(answers);
        log.info("Mapped answers to questions");
        List<Question> mandatoryQuestions = questionRepository.getMandatoryQuestions();
        log.info("Obtained list of mandatory questions");
        List<String> rejectionReasons = mandatoryQuestions.stream()
                .filter(q -> areNotSame(q, answersMap.get(q.id())))
                .map(Question::rejection).toList();
        log.info("Processing completed, number of rejection reasons is {}", rejectionReasons.size());
        return ConsultationResponse.create(rejectionReasons);
    }


    private boolean areNotSame(Question question, AnswerDto answer) {
        return !question.expectedAnswer().equals(answer.answer());
    }

    private Map<UUID, AnswerDto> createAnswersMap(List<AnswerDto> answers) {
        checkForDuplicateAnswers(answers);

        Map<UUID, AnswerDto> answersMap = answers.stream().collect(Collectors.toMap(AnswerDto::questionId, Function.identity())); //this might throw IllegalStateException if there are multiple answers to same question
        Set<UUID> answeredQuestionsIds = answersMap.keySet();

        Set<UUID> questionsIds = questionRepository.getQuestionIds();

        List<UUID> unansweredQuestions = questionsIds.stream().filter(Predicate.not(answeredQuestionsIds::contains)).toList();
        List<UUID> invalidId = answeredQuestionsIds.stream().filter(Predicate.not(questionsIds::contains)).toList();

        if (!unansweredQuestions.isEmpty() || !invalidId.isEmpty()) {
            log.warn("Discovered some errors while processing answers : answers to {} questions are missing and {} answers didnt match questions", unansweredQuestions.size(), invalidId.size());
            throw new ConsultationFormException(unansweredQuestions, invalidId);
        }
        return answersMap;
    }

    private void checkForDuplicateAnswers(List<AnswerDto> answers) {
        log.info("Checks for duplicated answers");
        List<UUID> questionIds = answers.stream().map(AnswerDto::questionId).toList();
        Set<UUID> duplicateAnswers = questionIds.stream().filter(questionId -> Collections.frequency(questionIds, questionId) > 1).collect(Collectors.toSet());

        if (!duplicateAnswers.isEmpty()) {
            log.warn("Request contained duplicated answers");
            throw new DuplicateAnswersException(duplicateAnswers);
        }
    }

}
