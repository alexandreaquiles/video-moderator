# video-moderator

A Video Moderator that uses the power of Google Gemini to moderate videos according to pre-defined rules.

## 

It runs on Google Cloud using:

- Google Cloud Vertex AI, specifically the `gemini-1.5-flash-001` generative AI model 
- Google Cloud Functions
- Google Cloud Pub/Sub
- Google Cloud Storage


## Code

The code is built using Java 21+ and Quarkus.

- `NewVideoEventHandler` is a CloudEventsFunction
- `VideoModerator` has the prompt and calls Vertex AI
- `ModerationRules` has the rules for video moderation (in Brazilian Portuguese)

First you need to build the project:

```shell
quarkus build
```

Then you should deploy the function to Google Cloud:

```shell
gcloud functions deploy new-video-event-handler --gen2 --entry-point=io.quarkus.gcp.functions.QuarkusCloudEventsFunction --runtime=java21 --trigger-topic=new-videos --source=target/deployment
```

In the above command we're deploying a `new-video-event-handler` function triggered by messages in a `new-videos` Pub/Sub topic.

## How to run it

Your videos should be stored on a Google Cloud Storage bucket. You should use the GS URI: `gs://your-bucket/your-video.mp4`

Then publishing a message in a Google Cloud Pub/Sub topic similar to the following:

```json
{
  "videoId": "a05da2e4-ed3a-4c33-a79b-2b65a3925bc5",
  "gsUri": "gs://vm-videos/escalada.mp4"
}
```

Then you can see on the function logs something like the following:

```txt
2024-06-20 19:11:39.354 BRT
RESULTADO DA MODERAÇÃO (gs://vm-videos/escalada.mp4):
2024-06-20 19:11:39.354 BRT {
2024-06-20 19:11:39.354 BRT "mostra um produto em uso": false,
2024-06-20 19:11:39.354 BRT "não menciona canais de venda de fora": true,
2024-06-20 19:11:39.354 BRT "tem áudio": false,
2024-06-20 19:11:39.354 BRT "não tem marca d’água de apps": true,
2024-06-20 19:11:39.354 BRT "não exibe conteúdo sexual": true,
2024-06-20 19:11:39.354 BRT "não tem voz nem texto em outra língua": true,
2024-06-20 19:11:39.354 BRT "possui mais que uma imagem estática": true
2024-06-20 19:11:39.354 BRT }
```
