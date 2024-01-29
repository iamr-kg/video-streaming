package com.video.streaming.dto;

import lombok.Getter;

@Getter
public enum ReactionType {
    DISLIKE(-1), LIKE(1), NEUTRAL(0);
    private final int reactionNumber;

   private ReactionType(int reactionNumber) {
        this.reactionNumber = reactionNumber;
    }

}
