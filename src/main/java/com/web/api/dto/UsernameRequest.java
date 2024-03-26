package com.web.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UsernameRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min=5,max=50)
    private String username;
    @NotNull
    @NotEmpty
    @NotBlank
    private String password;

}
