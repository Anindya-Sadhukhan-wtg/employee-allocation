package org.example.employeeallocation.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(T data) {
        this.data = data;
        this.message = null;
    }

    public ApiResponse(String message) {
        this.data = null;
        this.message = message;
    }
}

