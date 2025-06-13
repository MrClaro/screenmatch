package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(ScreenmatchApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    var consumoApi = new ConsumoApi();
    var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=[API-KEY]");
    System.out.println(json);
    ConverteDados converteDados = new ConverteDados();
    DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
    System.out.println(dadosSerie);
  }

}
