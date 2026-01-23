package com.myapp.dto;

import java.time.LocalDate;

public class UpdateDueDateRequest {

    private LocalDate dueDate;

    public UpdateDueDateRequest() {
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

}
