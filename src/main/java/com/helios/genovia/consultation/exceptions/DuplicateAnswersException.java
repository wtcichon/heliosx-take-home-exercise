package com.helios.genovia.consultation.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DuplicateAnswersException extends RuntimeException {
    private final Set<UUID> duplicateAnswers;
}
