package com.train.services.ticketing.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.train.services.ticketing.model.Receipt;
import com.train.services.ticketing.model.Train;
import com.train.services.ticketing.model.User;
import com.train.services.ticketing.model.userResponse;
import com.train.services.ticketing.service.TicketService;
import com.train.services.ticketing.utils.ApiConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class TestTicketController {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TicketService ticketService;
    @Autowired
    private ObjectMapper objectMapper;

    // Default constructor is needed
    public TestTicketController() {
    }

    @Test
    public void getAllUsersApi() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setEmailAddress("a@gmail.com");
        user.setFirstName("vig");
        user.setLastName("kum");
        // Mock the service method
        List<Train> mockListOfUsersWithSeatAndSection = Collections.singletonList(new Train(ApiConstants.sectionA, 1, user));
        Mockito.when(ticketService.doRetrieveUsers()).thenReturn(mockListOfUsersWithSeatAndSection);

        // Perform the GET request
        mvc.perform(get("/ticket/users")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(mockListOfUsersWithSeatAndSection)))
                .andExpect(MockMvcResultMatchers.jsonPath("$..user.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$..section").isNotEmpty());
    }

    @Test
    public void getAllUsers_EmptyList() throws Exception {
        // Mock the service method to return an empty list
        Mockito.when(ticketService.doRetrieveUsers()).thenReturn(Collections.emptyList());

        // Perform the GET request
        mvc.perform(get("/ticket/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify that the doRetrieveUsers method was called once
        verify(ticketService, times(1)).doRetrieveUsers();
    }

    @Test
    public void doTestBookTicketForUser() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setEmailAddress("a@gmail.com");
        user.setFirstName("vig");
        user.setLastName("kum");


        // Mock the service method to return an empty list
        Mockito.when(ticketService.doTicketBooking(user)).thenReturn(
                new Receipt(ApiConstants.LONDON, ApiConstants.FRANCE, user, ApiConstants.PRICE_PAID));

        // Perform the GET request
        mvc.perform(post("/ticket/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        // Verify that the doRetrieveUsers method was called once
        verify(ticketService, times(1)).doTicketBooking(user);
    }

    @Test
    public void doTestGetReceiptForUserId() throws Exception {
        String userId = "1";
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        user.setEmailAddress("a@gmail.com");
        user.setFirstName("vig");
        user.setLastName("kum");


        // Mock the service method to return an empty list
        Mockito.when(ticketService.doRetrieveReceipt(userId)).thenReturn(
                new Receipt(ApiConstants.LONDON, ApiConstants.FRANCE, user, ApiConstants.PRICE_PAID));

        // Perform the GET request
        mvc.perform(get("/ticket/receipt/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..user.userId").value(1));

        // Verify that the doRetrieveUsers method was called once
        verify(ticketService, times(1)).doRetrieveReceipt(userId);
    }

    @Test
    public void doTestDeleteUser() throws Exception {
        String userId = "1";
        User user = new User();
        user.setUserId(Integer.parseInt(userId));
        user.setEmailAddress("a@gmail.com");
        user.setFirstName("vig");
        user.setLastName("kum");


        // Mock the service method to return an empty list
        Mockito.when(ticketService.doRemoveUser(userId)).thenReturn(
                new userResponse("User removed successfully"));

        // Perform the DELETE request
        mvc.perform(delete("/ticket/remove/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"User removed successfully\"}"));
    }

    @Test
    public void doTestModifySeatOfUser() throws Exception {
        String userId1 = "1";
        String userId2 = "2";
        User user1 = new User();
        user1.setUserId(Integer.parseInt(userId1));
        user1.setEmailAddress("a@gmail.com");
        user1.setFirstName("vig");
        user1.setLastName("kum");

        User user2 = new User();
        user2.setUserId(Integer.parseInt(userId2));
        user2.setEmailAddress("a@gmail.com");
        user2.setFirstName("san");
        user2.setLastName("kum");


        // Mock the service method to return an empty list
        Mockito.when(ticketService.doModifyUser(userId1)).thenReturn(
                new userResponse("Seat modified successfully"));

        // Perform the DELETE request
        mvc.perform(put("/ticket/modify/{userId}", userId1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Seat modified successfully\"}"));
    }
}
