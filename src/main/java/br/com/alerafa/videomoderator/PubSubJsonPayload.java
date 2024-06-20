package br.com.alerafa.videomoderator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record PubSubJsonPayload(PubSubMessage message) {

    String data() {
        String encodedData = message().data();
        byte[] decodedData = Base64.getDecoder().decode(encodedData);
        return new String(decodedData, StandardCharsets.UTF_8);
    }

}

record PubSubMessage(String data) {
}