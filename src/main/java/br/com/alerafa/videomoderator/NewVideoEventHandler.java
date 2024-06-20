package br.com.alerafa.videomoderator;

import com.google.cloud.functions.CloudEventsFunction;
import com.google.gson.Gson;
import io.cloudevents.CloudEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@ApplicationScoped
public class NewVideoEventHandler implements CloudEventsFunction {

    @Inject
    VideoModerator videoModerator;

    @Override
    public void accept(CloudEvent event) throws Exception {

        NewVideoEvent newVideoEvent = getNewVideoEvent(event);

        String moderation = videoModerator.moderate(newVideoEvent.gsUri());

        System.out.printf("RESULTADO DA MODERAÇÃO (%s): %s", newVideoEvent.gsUri(), moderation);

    }

    private NewVideoEvent getNewVideoEvent(CloudEvent event) {
        String cloudEventData = new String(Objects.requireNonNull(event.getData()).toBytes(), StandardCharsets.UTF_8);

        Gson gson = new Gson();
        PubSubJsonPayload pubSubJsonPayload = gson.fromJson(cloudEventData, PubSubJsonPayload.class);
        String eventData = pubSubJsonPayload.data();
        return gson.fromJson(eventData, NewVideoEvent.class);
    }

}
