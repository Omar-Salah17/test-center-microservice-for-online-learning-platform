package com.example.demo2.resoure;

import com.example.demo2.Entity.exam;
import com.example.demo2.JwtUtil;
import com.example.demo2.controller.ExamController;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/exams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class examResource {

    @EJB
    @Inject
    private ExamController controller;

    @GET
    @Path("/getallexams")
    public Response getAllExams() {
        List<exam> exams = controller.getAllExams();
        return Response.ok(exams).build();
    }

    @GET
    @Path("/getexambyid")
    public Response getExamById(@QueryParam("id") Long id) {
        exam exam = controller.getExamById(id);
        if (exam != null) {
            return Response.ok(exam).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/getexambylocation")
    public List<exam> getExamsByLocation(@QueryParam("location") String location) {
        return controller.getExamsByLocation(location);
    }

    @GET
    @Path("/getexambyname")
    public List<exam> getExamsByName(@QueryParam("name") String name) {
        return controller.getExamsByName(name);
    }

    @POST
    @Path("/addExam")
    public Response addExam(exam newExam,@HeaderParam("Authorization") String authHeader) {
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

        controller.addExam(newExam);
        return Response.status(Response.Status.CREATED).entity(newExam).build();
    }

    @PUT
    @Path("/updateexam")
    public Response updateExam(@QueryParam("id") Long id, exam updatedExam) {
        exam exam = controller.updateExam(id, updatedExam);
        if (exam != null) {
            return Response.ok(exam).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/deleteexam")
    public Response deleteExam(@QueryParam("id") Long id) {
        controller.deleteExam(id);
        return Response.noContent().build();
    }
}
