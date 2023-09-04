package net.premierstudios.region.util.chatresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class ChatResponse {

    private Consumer<String> consumer;
    private String cancelMessage;

}
