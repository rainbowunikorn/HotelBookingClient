package com.bsuir.khviasko.hotel.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ReservationDTO {
    private Long id;

    private String firstname;

    private String surname;

    private String roomNumber;

    private String roomType;

    private double totalPrice;

    private LocalDate createDate;

    private String status;
}
