package com.helios.genovia.consultation.model;

import java.util.UUID;


public record Question(UUID id, String question, Boolean expectedAnswer, String rejection) {
}
