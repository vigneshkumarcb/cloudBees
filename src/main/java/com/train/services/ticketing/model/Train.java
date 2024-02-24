package com.train.services.ticketing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Train {

    private String section;
    private int seat;
    private User user;

}
