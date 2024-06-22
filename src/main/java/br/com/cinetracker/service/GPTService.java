package br.com.cinetracker.service;

import io.github.cdimascio.dotenv.Dotenv;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class GPTService {

    private static final Dotenv dotenv = Dotenv.load();

    public static String getTranslate(String text) {
        OpenAiService service = new OpenAiService(dotenv.get("GPT_KEY"));

        CompletionRequest req = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var res = service.createCompletion(req);
        return res.getChoices().get(0).getText();
    }

}
