package com.web.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}
