package com.web.api.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String othername;
    private String username;

}
