package com.bsuir.khviasko.hotel.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO {
    private String login;

    private String password;

    private String firstname;

    private String surname;

    private boolean isDeleted;

    private RoleDTO role;
}
