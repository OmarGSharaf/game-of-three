package com.takeaway.game.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
    @JsonProperty("syn") SYN, @JsonProperty("synAck") SYN_ACK, @JsonProperty("ack") ACK
}
