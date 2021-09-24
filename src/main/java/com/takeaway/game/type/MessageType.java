package com.takeaway.game.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
    @JsonProperty SYN, @JsonProperty SYN_ACK, @JsonProperty ACK, @JsonProperty DEFAULT
}
