package com.example.demo2.resoure;

import com.example.demo2.Entity.exam;
import com.example.demo2.Entity.testcenter;
import com.example.demo2.JwtUtil;
import com.example.demo2.controller.ExamController;
import com.example.demo2.controller.testcenterController;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;

@Path("/testcenters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class testcenterResource {

    @EJB
    @Inject
    private testcenterController controller;


    @EJB
    @Inject
    private ExamController examController;

    private ObjectMapper objectMapper = new ObjectMapper();


    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(testcenter ts) {
        System.out.println(ts);
        controller.signUp(ts);
        String jsonResponse;
        try {
            // Serialize the object to JSON
            jsonResponse = objectMapper.writeValueAsString("signup successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(jsonResponse).build();
    }

    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(testcenter ts) {
        Optional<testcenter> optionalIts = Optional.ofNullable(controller.getTestCenterByName(ts.getName()));
        String jsonResponse;
        try {
            // Check if test center exists
            if (optionalIts.isEmpty()) {
                jsonResponse = objectMapper.writeValueAsString("User not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(jsonResponse)
                        .build();
            }

            // Extract test center from Optional
            testcenter existingts = optionalIts.get();

            // Check if the password matches
            if (!existingts.getPassword().equals(ts.getPassword())) {
                jsonResponse = objectMapper.writeValueAsString("Incorrect password");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(jsonResponse)
                        .build();
            }

            // Generate JWT token
            String token = JwtUtil.generateToken(existingts.getId());

            // Create response with token
            jsonResponse = objectMapper.writeValueAsString("Signin successful. Token: " + token);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Internal Server Error").build();
        }
        return Response.ok(jsonResponse).build();
    }




    @GET
    @Path("/getinfobyid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response gettestcenterById(@QueryParam("id") Long queryId, @HeaderParam("Authorization") String authHeader) {

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

        // Optionally, you can compare the queryId with the tokenId if necessary
        if (!queryId.equals(tokenId)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token ID does not match query ID").build();
        }

        testcenter tc = controller.gettestcenterById(queryId);
        if (tc != null) {
            return Response.ok(tc).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }




    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getinfobyname")
    public Response getTestCenterByName(@QueryParam("name") String name,@HeaderParam("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header must be provided").build();
        }

        // Extract the token from the Authorization header
        String token = authHeader.substring(7);

        // Decode the token to get the ID
        Long id;
        try {
            id = JwtUtil.getIdFromToken(token);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        testcenter testcenter = controller.getTestCenterByName(name);
        if (testcenter != null) {
            return Response.ok(testcenter).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getallcenters")
    public List<testcenter> getAlltestcenters() {
        return controller.getAlltestcenters();
    }




    @PUT
    @Path("/updatecenterbyid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatetestcenter(@QueryParam("id") Long queryId, testcenter updatedTestcenter,@HeaderParam("Authorization") String authHeader) {


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

        // Optionally, you can compare the queryId with the tokenId if necessary
        if (!queryId.equals(tokenId)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token ID does not match query ID").build();
        }
        controller.updatetestcenter(tokenId, updatedTestcenter);
        // Assuming controller.updatetestcenter() returns the updated TestCenter object
        return Response.ok(updatedTestcenter).build();
    }

    @DELETE
    @Path("/deletecenterbyid")
    public Response deletetestcenter(@QueryParam("id") Long id) {
        controller.deletetestcenter(id);
        String jsonResponse;
        try {
        jsonResponse = objectMapper.writeValueAsString("center has deleted successfully");
    } catch (Exception e) {
        e.printStackTrace();
        return Response.serverError().build();
    }
        return Response.ok(jsonResponse).build();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getinfobyLocation")
    public List<testcenter> gettestcenterBylocation(@QueryParam("location") String location) {
        return controller.getTestCenterByLocation(location);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/gettestcenterexams")
    public Response gettestcenterexams(@QueryParam("testcenter_id") long testcenter_id, @HeaderParam("Authorization") String authHeader) {
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

        // Assuming you want to ensure the token ID matches the testcenter_id
        if (tokenId != testcenter_id) {
            return Response.status(Response.Status.FORBIDDEN).entity("Token ID does not match test center ID").build();
        }

        List<exam> exams = examController.gettestcenterexam(testcenter_id);
        if (exams != null) {
            return Response.ok(exams).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }





}
