package com.takeaway.game.model;

import com.takeaway.game.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer content;

    private String sender;

    private String receiver;

    private MessageType type;

}
