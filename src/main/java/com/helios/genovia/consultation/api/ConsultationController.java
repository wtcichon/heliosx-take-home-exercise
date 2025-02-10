package com.helios.genovia.consultation.api;

import com.helios.genovia.consultation.dto.request.ConsultationRequest;
import com.helios.genovia.consultation.dto.response.ConsultationResponse;
import com.helios.genovia.consultation.dto.response.QuestionDto;
import com.helios.genovia.consultation.service.ConsultationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/1/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping(value = "/questions", produces = "application/json")
    public List<QuestionDto> getConsultationQuestions() {
        return consultationService.getQuestions();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ConsultationResponse startConsultation(
            @RequestBody ConsultationRequest answers) {
        log.info("Received consultation request");
        ConsultationResponse result = consultationService.getConsultationResult(answers.answers());
        log.info("Completes evaluation");
        return result;
    }

}
