package com.example.demo2.controller;

import com.example.demo2.Entity.exam;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ExamController {

    @PersistenceContext
    private EntityManager entityManager;

    public void addExam(exam newExam) {
        entityManager.persist(newExam);
    }

    public exam getExamById(Long id) {
        return entityManager.find(exam.class, id);
    }

    public List<exam> getExamsByLocation(String location) {
        return entityManager.createQuery("SELECT e FROM exam e WHERE e.location = :location", exam.class)
                .setParameter("location", location)
                .getResultList();
    }

    public List<exam> getExamsByName(String name) {
        return entityManager.createQuery("SELECT e FROM exam e WHERE e.name = :name", exam.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<exam> getAllExams() {
        return entityManager.createQuery("SELECT e FROM exam e", exam.class).getResultList();
    }

    public exam updateExam(Long id, exam updatedExam) {
        exam existingExam = entityManager.find(exam.class, id);
        if (existingExam != null) {
            existingExam.setName(updatedExam.getName());
            existingExam.setDuration(updatedExam.getDuration());
            existingExam.setAvailableDates(updatedExam.getAvailableDates());

            existingExam.setLocation(updatedExam.getLocation());
            return entityManager.merge(existingExam);
        }
        return null;
    }

    public void deleteExam(Long id) {
        exam examToDelete = entityManager.find(exam.class, id);
        if (examToDelete != null) {
            entityManager.remove(examToDelete);
        }
    }

    public List<exam>gettestcenterexam(long testcenter_id)
    {
        return entityManager.createQuery("SELECT e FROM exam e WHERE e.testcenter_id = :testcenter_id", exam.class)
                .setParameter("testcenter_id", testcenter_id)
                .getResultList();

    }
}
