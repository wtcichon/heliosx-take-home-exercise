package com.helios.genovia.consultation.dto.response;

import java.util.List;

public record ConsultationResponse(String result, List<String> rejectionReasons) {

    public static final String REJECTION_RESPONSE = "Unfortunately we won't be able to provide you with medicine due to following reasons";
    public static final String ACCEPTANCE_RESPONSE = "Waiting for doctor approval";

    public static ConsultationResponse create(List<String> rejectionReasons) {
        return new ConsultationResponse(
                rejectionReasons.isEmpty() ? ACCEPTANCE_RESPONSE : REJECTION_RESPONSE,
                rejectionReasons);
    }

    ;
}
