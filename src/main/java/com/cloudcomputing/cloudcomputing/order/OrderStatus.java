package com.cloudcomputing.cloudcomputing.order;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)

public enum OrderStatus {
    PROCESSING, SHIPPING ,FINISHED, CANCELED;
}
