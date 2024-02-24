package com.train.services.ticketing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.services.ticketing.exception.GlobalExceptionHandler;
import com.train.services.ticketing.model.Receipt;
import com.train.services.ticketing.model.Train;
import com.train.services.ticketing.model.User;
import com.train.services.ticketing.model.userResponse;
import com.train.services.ticketing.service.TicketService;
import jakarta.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ticket")
public class TicketController {


    private TicketService ticketService;

    private ObjectMapper objectMapper;

    @Autowired
    public TicketController(TicketService ticketService, ObjectMapper objectMapper) {
        this.ticketService = ticketService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/purchase")
    @Produces(value = "application/json")
    public ResponseEntity<String> purchaseTicket(@RequestBody User user) throws JsonProcessingException {
        return new ResponseEntity<>(objectMapper.writeValueAsString(ticketService.doTicketBooking(user)), HttpStatus.CREATED);

    }

    @GetMapping("/receipt/{userId}")
    @Produces(value = "application/json")
    public ResponseEntity<String> getReceipt(@PathVariable String userId) throws JsonProcessingException {
        Receipt receipt = ticketService.doRetrieveReceipt(userId);
        if (receipt != null) {
            return new ResponseEntity<>(objectMapper.writeValueAsString(receipt), HttpStatus.OK);
        } else {
            throw new GlobalExceptionHandler.UserNotFoundException(userId);
        }

    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() throws Exception {
        List<Train> listOfUsersWithSeatAndSection = ticketService.doRetrieveUsers();
        if (!listOfUsersWithSeatAndSection.isEmpty()) {
            return new ResponseEntity<>(objectMapper.writeValueAsString(listOfUsersWithSeatAndSection), HttpStatus.OK);
        } else {
            throw new GlobalExceptionHandler.UserListNotFoundException("Please try again later");
        }
    }

    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable String userId) throws JsonProcessingException {
        userResponse removedUserResponse = ticketService.doRemoveUser(userId);
        if (removedUserResponse != null) {
            return new ResponseEntity<>(objectMapper.writeValueAsString(removedUserResponse), HttpStatus.ACCEPTED);
        } else {
            throw new GlobalExceptionHandler.UserNotFoundException(userId);
        }

    }

    @PutMapping("/modify/{userId}")
    public ResponseEntity<String> modifySeat(@PathVariable String userId) throws JsonProcessingException {
        userResponse modifiedUserResponse = ticketService.doModifyUser(userId);
        if (modifiedUserResponse != null) {
            return new ResponseEntity<>(objectMapper.writeValueAsString(modifiedUserResponse), HttpStatus.OK);
        } else {
            throw new GlobalExceptionHandler.UserNotFoundException(userId);
        }
    }
}
