package com.bsuir.khviasko.hotel.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class RoomDTO {
    private Long id;

    private String roomNumber;

    private String roomType;

    private int capacity;

    private double price;

    private String description;
}
