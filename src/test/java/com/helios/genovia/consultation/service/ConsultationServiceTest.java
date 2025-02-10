package com.helios.genovia.consultation.service;

import com.helios.genovia.consultation.dto.request.AnswerDto;
import com.helios.genovia.consultation.dto.response.ConsultationResponse;
import com.helios.genovia.consultation.dto.response.QuestionDto;
import com.helios.genovia.consultation.model.Question;
import com.helios.genovia.consultation.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {

    private final static UUID QUESTION_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final static Question QUESTION = new Question(QUESTION_UUID, "QUESTION", false, "REJECTION");
    @Mock
    QuestionRepository questionRepository;
    @InjectMocks
    ConsultationService consultationService;

    @Test
    @DisplayName("Test for getting questions")
    public void testGetQuestions() {
        when(questionRepository.getQuestions()).thenReturn(List.of(QUESTION));
        List<QuestionDto> result = consultationService.getQuestions();
        assertEquals(1, result.size());
        assertEquals(QUESTION_UUID.toString(), result.getFirst().id().toString());
        assertEquals("QUESTION", result.getFirst().question());
        verify(questionRepository, times(1)).getQuestions();
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    @DisplayName("Test for getting consultation result - success")
    public void testGetConsultationResultSuccess() {
        when(questionRepository.getMandatoryQuestions()).thenReturn(List.of(QUESTION));
        when(questionRepository.getQuestionIds()).thenReturn(Set.of(QUESTION_UUID));

        List<AnswerDto> answers = List.of(new AnswerDto(QUESTION_UUID, false));

        ConsultationResponse result = consultationService.getConsultationResult(answers);

        assertEquals(ConsultationResponse.ACCEPTANCE_RESPONSE, result.result());
        assertEquals(0, result.rejectionReasons().size());

        verify(questionRepository, times(1)).getMandatoryQuestions();
        verify(questionRepository, times(1)).getQuestionIds();
        verifyNoMoreInteractions(questionRepository);
    }

    @Test
    @DisplayName("Test for getting consultation result - rejected")
    public void testGetConsultationResultRejected() {
        when(questionRepository.getMandatoryQuestions()).thenReturn(List.of(QUESTION));
        when(questionRepository.getQuestionIds()).thenReturn(Set.of(QUESTION_UUID));

        List<AnswerDto> answers = List.of(new AnswerDto(QUESTION_UUID, true));

        ConsultationResponse result = consultationService.getConsultationResult(answers);

        assertEquals(ConsultationResponse.REJECTION_RESPONSE, result.result());
        assertEquals(1, result.rejectionReasons().size());
        assertEquals("REJECTION", result.rejectionReasons().getFirst());

        verify(questionRepository, times(1)).getMandatoryQuestions();
        verify(questionRepository, times(1)).getQuestionIds();
        verifyNoMoreInteractions(questionRepository);
    }
}