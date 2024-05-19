package com.example.demo2.controller;

import com.example.demo2.Entity.exam;
import com.example.demo2.Entity.exam_result;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ExamResultController {

    @PersistenceContext
    private EntityManager em;


    public List<exam_result> getAllExamResults() {
        return em.createQuery("SELECT er FROM exam_result er", exam_result.class).getResultList();
    }
    public exam_result getExamResultById(Long id) {
        return em.find(exam_result.class, id);
    }


    // New method to add an exam result
    public exam_result addExamResult(exam_result newExamResult) {
        em.persist(newExamResult);
        return newExamResult;
    }



    public List<exam_result> getExamResultsByExamId(Long examId) {
        return em.createQuery("SELECT er FROM exam_result er WHERE er.exam_id = :examId", exam_result.class)
                .setParameter("examId", examId)
                .getResultList();
    }
    public List<exam_result> getExamResultsByStudentId(Long student_id) {
        return em.createQuery("SELECT er FROM exam_result er WHERE er.studentId = :student_id", exam_result.class)
                .setParameter("student_id", student_id)
                .getResultList();
    }




    public void updateExamResult(exam_result examResult) {
        em.merge(examResult);
    }

    public void deleteExamResult(Long id) {
        exam_result examResult = em.find(exam_result.class, id);
        if (examResult != null) {
            em.remove(examResult);
        }
    }
}
