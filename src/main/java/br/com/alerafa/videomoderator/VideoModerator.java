package br.com.alerafa.videomoderator;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VideoModerator {

    public static final String MODEL_NAME = "gemini-1.5-flash-001";

    public String moderate(String videoGsUri) throws Exception {

        try (var vertexAi = new VertexAI(System.getenv("GCLOUD_PROJECT_ID"), System.getenv("GCLOUD_LOCATION"))) {

            GenerationConfig generationConfig = GenerationConfig.newBuilder()
                    .setTemperature(0)
                    .build();

            GenerativeModel model = new GenerativeModel.Builder()
                    .setModelName(MODEL_NAME)
                    .setVertexAi(vertexAi)
                    .setGenerationConfig(generationConfig)
                    .build();

            var part = PartMaker.fromMimeTypeAndData("video/mp4", videoGsUri);

            var prompt = getPrompt();

            var content = ContentMaker.fromMultiModalData(part, prompt);
            GenerateContentResponse response = model.generateContent(content);

            return ResponseHandler.getText(response);
        }
    }

    private String getPrompt() {
        var regras = ModerationRules.RULES;

        return """
                Avalie cuidadosamente o vídeo em anexo se as regras a seguir são seguidas." +
                Baseie as avaliações estritamente nas informações disponíveis no vídeo em anexo.
                Olhe para cada frame do vídeo com cuidado.
                Retorne apenas um JSON com cada regra e true/false se são seguidas. Não retorne mais nada além do JSON.

                Regras:
                %s
                """.formatted(regras);
    }
}
