package com.helios.genovia.consultation.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ConsultationFormException extends RuntimeException{

    private final List<UUID> unansweredQuestions;
    private final List<UUID> invalidId;

}
