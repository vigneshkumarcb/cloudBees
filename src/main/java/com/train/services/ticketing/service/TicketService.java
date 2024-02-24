package com.train.services.ticketing.service;

import com.train.services.ticketing.configuration.TrainSeatsConfiguration;
import com.train.services.ticketing.exception.GlobalExceptionHandler;
import com.train.services.ticketing.model.Receipt;
import com.train.services.ticketing.model.Train;
import com.train.services.ticketing.model.User;
import com.train.services.ticketing.model.userResponse;
import com.train.services.ticketing.utils.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class TicketService {


    private TrainSeatsConfiguration trainSeats;

    @Autowired
    public TicketService(TrainSeatsConfiguration trainSeats
    ) {

        this.trainSeats = trainSeats;
    }

    public Receipt doTicketBooking(User user) {
        if (trainSeats.trainSizeLimitedLinkedHashSet().add(user)) {
            return new Receipt(ApiConstants.LONDON, ApiConstants.FRANCE, user, ApiConstants.PRICE_PAID);
        } else if (trainSeats.trainSizeLimitedLinkedHashSet().getListFlag() == 1) {
            throw new GlobalExceptionHandler.SameUserBookingException("Please try again with different UserId");
        } else {
            throw new GlobalExceptionHandler.SeatsNotAvailableException("Please try again later");
        }

    }

    public Receipt doRetrieveReceipt(String userId) {
        return getReceipt(userId, trainSeats.trainSizeLimitedLinkedHashSet().iterator());
    }

    public List<Train> doRetrieveUsers() {
        List<Train> trainList = new LinkedList<>();
        if (!trainSeats.trainSizeLimitedLinkedHashSet().isEmpty()) {
            for (User user : trainSeats.trainSizeLimitedLinkedHashSet().getOrderList()) {
                if (!trainSeats.trainSizeLimitedLinkedHashSet().getSectionA().isEmpty()) {
                    for (User userInSectionA : trainSeats.trainSizeLimitedLinkedHashSet().getSectionA()) {
                        if (userInSectionA.getUserId() == user.getUserId()) {
                            trainList.add(new Train(ApiConstants.sectionA, trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionA().indexOf(user) + 1, user));
                        }
                    }
                }
                if (!trainSeats.trainSizeLimitedLinkedHashSet().getSectionB().isEmpty()) {
                    for (User userInSectionB : trainSeats.trainSizeLimitedLinkedHashSet().getSectionB()) {
                        if (userInSectionB.getUserId() == user.getUserId()) {
                            trainList.add(new Train(ApiConstants.sectionB, trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionB().indexOf(user) + 1, user));
                        }
                    }
                }
            }
        }
        return trainList;
    }

    public userResponse doRemoveUser(String userId) {
        // Find and remove the user from the overall set
        User userToBeRemoved = null;
        for (User user : trainSeats.trainSizeLimitedLinkedHashSet()) {
            if (user.getUserId() == Integer.parseInt(userId)) {
                userToBeRemoved = user;
                trainSeats.trainSizeLimitedLinkedHashSet().remove(user);
                break;
            }
        }

        if (userToBeRemoved == null) {
            return null;
        }

        // Remove the user from the specific section
        for (User userInSectionA : trainSeats.trainSizeLimitedLinkedHashSet().getSectionA()) {
            if (userInSectionA.getUserId() == userToBeRemoved.getUserId()) {
                trainSeats.trainSizeLimitedLinkedHashSet().removeFromSectionA(userToBeRemoved);
            }
        }

        for (User userInSectionB : trainSeats.trainSizeLimitedLinkedHashSet().getSectionB()) {
            if (userInSectionB.getUserId() == userToBeRemoved.getUserId()) {
                trainSeats.trainSizeLimitedLinkedHashSet().removeFromSectionB(userToBeRemoved);
            }
        }

        return new userResponse("User removed successfully");
    }

    public userResponse doModifyUser(String userId) {
        User userToBeModified = null;
        int seatNumberOfUserToBeModified;

        // Find the user in the overall set
        for (User user : trainSeats.trainSizeLimitedLinkedHashSet()) {
            if (user.getUserId() == Integer.parseInt(userId)) {
                userToBeModified = user;

                break;
            }
        }

        if (userToBeModified == null) {
            return null;
        }

        // Modify the seat of the user
        trainSeats.trainSizeLimitedLinkedHashSet().modifySeatInOrderedList(userToBeModified);
        if (trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionA().contains(userToBeModified)) {
            //seatNumberOfUserToBeModified = trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionA().indexOf(userToBeModified);
            trainSeats.trainSizeLimitedLinkedHashSet().modifySeatInSectionA(userToBeModified);
        } else if (trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionB().contains(userToBeModified)) {
            //seatNumberOfUserToBeModified = trainSeats.trainSizeLimitedLinkedHashSet().getListOfSectionB().indexOf(userToBeModified);
            trainSeats.trainSizeLimitedLinkedHashSet().modifySeatInSectionB(userToBeModified);
        }

        return new userResponse("Seat modified successfully");
    }

    public Receipt getReceipt(String userId, Iterator<User> iterator) {
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (Integer.parseInt(userId) == user.getUserId()) {
                return new Receipt(ApiConstants.LONDON, ApiConstants.FRANCE, user, ApiConstants.PRICE_PAID);
            }
        }
        return null;
    }


}
