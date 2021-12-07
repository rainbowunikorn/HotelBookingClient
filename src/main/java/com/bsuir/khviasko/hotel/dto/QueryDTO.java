package com.bsuir.khviasko.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QueryDTO {
    private String command;
    private Long userId;
    private String role;
    private Long roomId;
    private Long reservationId;

}

