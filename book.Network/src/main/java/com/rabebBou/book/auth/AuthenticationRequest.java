package com.rabebBou.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthenticationRequest {

    @Email(message = "Email is not formatted")
    @NotEmpty(message="email is mandatory")
    @NotBlank(message = "email is mandatory")
    private String email;
    @NotEmpty(message="Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8 , message = "Password should be 8 charcters long mininum")
    private String password;

}
