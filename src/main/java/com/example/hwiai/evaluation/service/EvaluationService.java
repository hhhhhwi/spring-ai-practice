package com.example.hwiai.evaluation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.repository.EvaluationRepository;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public void saveAll(List<Evaluation> evaluations) {
        evaluationRepository.saveAll(evaluations);
    }
}