package com.tarzia.taskapi.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskUpdateDTO {

    private String title;
    private String description;
    private Boolean completed;
}
