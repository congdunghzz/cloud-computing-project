package com.cloudcomputing.cloudcomputing.user.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Gender {
    MALE,
    FEMALE,
    OTHERS;
}
