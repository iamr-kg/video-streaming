package com.video.streaming.dto;

import lombok.Getter;

@Getter
public enum VideoStatus {
    PUBLIC(1),PRIVATE(0);
    private final int status;

    private VideoStatus(int status){
        this.status = status;
    }

}
