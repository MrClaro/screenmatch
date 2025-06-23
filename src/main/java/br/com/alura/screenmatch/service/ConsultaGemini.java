package br.com.alura.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {
  public static String obterTraducao(String texto) {
    // The client gets the API key from the environment variable `GOOGLE_API_KEY`.
    try (Client client = new Client()) {
      GenerateContentResponse response = client.models.generateContent(
          "gemini-2.5-flash",
          "Traduza o texto para o português" + texto,
          null);

      var resposta = response.text();
      return resposta;
    } catch (Exception e) {
      System.out.println("Erro: " + e.getMessage());
      return "Erro: " + e.getMessage();
    }

  }
}
