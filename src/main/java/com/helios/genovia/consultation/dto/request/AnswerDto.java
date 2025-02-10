package com.helios.genovia.consultation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AnswerDto(
        @NotNull(message = "Answer is not link to the question")
        UUID questionId,

        @NotNull(message = "Answer cannot be empty")
        Boolean answer) {
}
