package com.example.hwiai.evaluation.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.hwiai.error.exception.UniqueConstraintViolationException;
import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.repository.EvaluationRepository;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public void saveAll(List<Evaluation> evaluations) {
        try {
            evaluationRepository.saveAllAndFlush(evaluations);
        } catch (DataIntegrityViolationException ex) {
            ConstraintViolationException constraintViolation = findConstraintViolation(ex);
            if (constraintViolation != null) {
                throw new UniqueConstraintViolationException(constraintViolation.getConstraintName());
            }
            throw ex;
        }
    }

    public boolean existsByReviewIdAndCharacteristicId(Long productId, Long characteristicId) {
        return evaluationRepository.existsByReviewIdAndCharacteristicId(productId, characteristicId);
    }

    public void save(Evaluation evaluation) {
        try {
            evaluationRepository.save(evaluation);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException cve) {
                throw new UniqueConstraintViolationException(cve.getConstraintName());
            }
            throw ex;
        }
    }

    private ConstraintViolationException findConstraintViolation(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ConstraintViolationException constraintViolation) {
                return constraintViolation;
            }
            current = current.getCause();
        }
        return null;
    }
    
    public List<Evaluation> findByReviewProductIdAndCharacteristicIdAndIsRelatedTrue(Long productId, Long characteristicId) {
        return evaluationRepository.findByReviewProductIdAndCharacteristicIdAndIsRelatedTrue(productId, characteristicId);
    }
}
