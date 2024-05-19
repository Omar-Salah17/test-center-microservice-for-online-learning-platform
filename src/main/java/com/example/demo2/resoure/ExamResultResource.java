package com.example.demo2.resoure;

import com.example.demo2.Entity.exam;
import com.example.demo2.Entity.exam_result;
import com.example.demo2.JwtUtil;
import com.example.demo2.controller.ExamResultController;
import com.example.demo2.controller.ExamController;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/exam_results")
public class ExamResultResource {

    @Inject
    private ExamResultController examResultController;



    @GET
    @Path("/getAllExamResults")
    @Produces(MediaType.APPLICATION_JSON)
    public List<exam_result> getAllExamResults() {
        return examResultController.getAllExamResults();
    }

    @GET
    @Path("/getExamResultById")
    @Produces(MediaType.APPLICATION_JSON)
    public List<exam_result> getExamResultById(@QueryParam("exam_id") Long id) {
        return  examResultController.getExamResultsByExamId(id);
    }

    @POST
    @Path("addstudenttoexam")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addstudenttoexam(exam_result examResult) {
        try {
            exam_result savedExamResult = examResultController.addExamResult(examResult);
            return Response.status(Response.Status.CREATED).entity(savedExamResult).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add student to exam").build();
        }
    }

    @GET
    @Path("/getExamResultByStudentid")
    @Produces(MediaType.APPLICATION_JSON)
    public List<exam_result> getExamResultByStudentid(@QueryParam("Studentid") Long Studentid) {
        return  examResultController.getExamResultsByStudentId(Studentid);
    }


    @GET
    @Path("/getExamResultByStudentidintestcenter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExamResultByStudentidintestcenter(@QueryParam("Studentid") Long Studentid, @HeaderParam("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header must be provided").build();
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(7);

        // Decode the token to get the ID
        Long tokenId;
        try {
            tokenId = JwtUtil.getIdFromToken(token);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }



        List<exam_result> examResults = examResultController.getExamResultsByStudentId(Studentid);
        if (examResults != null && !examResults.isEmpty()) {
            return Response.ok(examResults).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No exam results found for the given Student ID").build();
        }
    }




    @PUT
    @Path("/updateExamResult")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExamResult(@QueryParam("id") Long id, exam_result examResult, @HeaderParam("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header must be provided").build();
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(7);

        // Decode the token to get the ID
        Long tokenId;
        try {
            tokenId = JwtUtil.getIdFromToken(token);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }



        exam_result existingExamResult = examResultController.getExamResultById(id);
        if (existingExamResult != null) {
            examResult.setId(id); // Ensure the ID is set to the correct existing entity
            examResultController.updateExamResult(examResult);
            return Response.ok(examResult).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
