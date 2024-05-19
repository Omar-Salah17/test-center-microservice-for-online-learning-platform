package com.example.demo2.controller;

import com.example.demo2.Entity.testcenter;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class testcenterController {

    @PersistenceContext
    private EntityManager entityManager;


    public void signUp(testcenter ts) {
        entityManager.persist(ts);
    }
    public testcenter gettestcenterById(Long id) {
        return entityManager.find(testcenter.class, id);
    }
    public testcenter getTestCenterByName(String name) {
        TypedQuery<testcenter> query = entityManager.createQuery(
                "SELECT tc FROM testcenter tc WHERE tc.name = :name", testcenter.class);
        ((TypedQuery<?>) query).setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Handle case where no test center with that name is found
        }
    }

    public List<testcenter> getTestCenterByLocation(String location) {
        TypedQuery<testcenter> query = entityManager.createQuery(
                "SELECT tc FROM testcenter tc WHERE tc.location = :location", testcenter.class);
        query.setParameter("location", location);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return List.of(); // Return an empty list when no results are found
        }
    }
    public List<testcenter> getAlltestcenters() {
        return entityManager.createQuery("SELECT t FROM testcenter t", testcenter.class).getResultList();
    }

    public testcenter updatetestcenter(Long id, testcenter updatedTestcenter) {
        testcenter existingTestcenter = entityManager.find(testcenter.class, id);
        if (existingTestcenter != null) {
            existingTestcenter.setName(updatedTestcenter.getName());
            existingTestcenter.setPassword(updatedTestcenter.getPassword());
            existingTestcenter.setEmail(updatedTestcenter.getEmail());
            existingTestcenter.setAddress(updatedTestcenter.getAddress());
            existingTestcenter.setLocation(updatedTestcenter.getLocation());
            existingTestcenter.setBranches(updatedTestcenter.getBranches());
            existingTestcenter.setBio(updatedTestcenter.getBio());
            return entityManager.merge(existingTestcenter);
        }
        return null; // Or throw an exception
    }

    public void deletetestcenter(Long id) {
        testcenter testcenterToDelete = entityManager.find(testcenter.class, id);
        if (testcenterToDelete != null) {
            entityManager.remove(testcenterToDelete);
        }
    }

}
