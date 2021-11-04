package com.asusoftware.taxirentapi.registration.model;

import com.asusoftware.taxirentapi.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final UserRole userRole;
    //private final Date birthday;
}
