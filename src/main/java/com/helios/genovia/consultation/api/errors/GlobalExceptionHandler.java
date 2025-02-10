package com.helios.genovia.consultation.api.errors;

import com.helios.genovia.consultation.exceptions.ConsultationFormException;
import com.helios.genovia.consultation.exceptions.DuplicateAnswersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    public static final String UNANSWERED_QUESTIONS_PROPERTY = "UNANSWERED_QUESTIONS";
    public static final String INVALID_IDS_PROPERTY = "INVALID_IDS";
    public static final String DUPLICATE_ANSWERS_PROPERTY = "DUPLICATE_ANSWERS";
    public static final String INVALID_CONSULTATION_DATA_MESSAGE = "Invalid consultation data";

    @ExceptionHandler(ConsultationFormException.class)
    public ProblemDetail handleConsultationFormException(ConsultationFormException exception) {
        String invalidIds = exception.getInvalidId().stream().map(UUID::toString).collect(Collectors.joining(","));
        String unansweredQuestions = exception.getUnansweredQuestions().stream().map(UUID::toString).collect(Collectors.joining(","));
        ProblemDetail problemDetail
                = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, INVALID_CONSULTATION_DATA_MESSAGE);
        problemDetail.setProperty(UNANSWERED_QUESTIONS_PROPERTY, unansweredQuestions);
        problemDetail.setProperty(INVALID_IDS_PROPERTY, invalidIds);
        return problemDetail;
    }

    @ExceptionHandler(DuplicateAnswersException.class)
    public ProblemDetail handleDuplicateAnswersException(DuplicateAnswersException exception) {
        String duplicateQuestions = exception.getDuplicateAnswers().stream().map(UUID::toString).collect(Collectors.joining(","));
        ProblemDetail problemDetail
                = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, INVALID_CONSULTATION_DATA_MESSAGE);
        problemDetail.setProperty(DUPLICATE_ANSWERS_PROPERTY, duplicateQuestions);
        return problemDetail;
    }
}