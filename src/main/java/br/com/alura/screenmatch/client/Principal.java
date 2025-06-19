package br.com.alura.screenmatch.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

public class Principal {
  private Scanner scan = new Scanner(System.in);
  private final String URL_BASE = "http://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=[API-KEY]&";
  private ConsumoApi consumoApi = new ConsumoApi();
  private ConverteDados converteDados = new ConverteDados();

  public void exibeMenu() {
    System.out.println("Digite o nome da série para buscar: ");
    var nomeSerie = scan.nextLine();
    consumoApi = new ConsumoApi();
    String url = URL_BASE + nomeSerie.toLowerCase().replace(" ", "+") + API_KEY;

    var json = consumoApi.obterDados(url);
    DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
    System.out.println(dadosSerie);

    System.out.println(
        "===========================================================");
    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {

      String urlSeasons = URL_BASE + nomeSerie.toLowerCase().replace(" ", "+") + "&season=" + i + API_KEY;
      json = consumoApi.obterDados(urlSeasons);
      var dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);

    // for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
    // List<DadosEpisodio> episodios = temporadas.get(i).episodios();
    // for (int j = 0; j < episodios.size(); j++) {
    // DadosEpisodio episodio = episodios.get(j);
    // System.out.println("Temporada " + (i + 1) + " Episódio " + (j + 1) + ": " +
    // episodio.titulo());
    // }
    // }

    temporadas.forEach(t -> t.episodios().forEach(e -> {
      System.out.println("Temporada " + t.numero() + " Episódio " + e.numero() + ": " + e.titulo());
    }));
    List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream())
        .toList();
    // .collect(Collectors.toList());
    System.out.println("===========================================================");

    System.out.println("\nTop 5 Episodios");
    dadosEpisodios
        .stream()
        .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
        .sorted(Comparator.comparing(DadosEpisodio::avaliacao)
            .reversed())
        .limit(5)
        .forEach(System.out::println);

    List<Episodio> episodios = temporadas.stream()
        .flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numero(), d)))
        .collect(Collectors.toList());

    System.out.println("===========================================================");
    episodios.forEach(System.out::println);

    // System.out.println("===========================================================");
    // System.out.println("A partir de qual ano você deseja ver os episódios?");
    // var ano = scan.nextInt();
    // scan.nextLine();
    //
    // LocalDate dataBusca = LocalDate.of(ano, 1, 1);
    // DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // episodios.stream()
    // .filter(e -> e.getDataLancamento() != null &&
    // e.getDataLancamento().isAfter(dataBusca))
    // .forEach(e -> System.out.println(
    // "Temporada: " + e.getTemporada() + " Episódio: " + e.getNumeroEpisodio() + "
    // Titulo: " + e.getTitulo()
    // + " - "
    // + e.getDataLancamento().format(df) + " - Avaliação: " + e.getAvaliacao()));
    //
    // var trechoTitulo = "";
    // Optional<Episodio> episodio = episodios.stream().filter(e ->
    // e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase())).findFirst();
    // if( episodio.isPresent() ) {System.out.println("Episódio encontrado!"); }
    // else { System.out.println("Episódio não encontrado!"); }

    Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
        .filter(e -> e.getAvaliacao() > 0.0)
        .collect(Collectors.groupingBy(Episodio::getTemporada,
            Collectors.averagingDouble(Episodio::getAvaliacao)));
    System.out.println("===========================================================");
    System.out.println("Avaliações por temporada:");
    System.out.println(avaliacoesPorTemporada);

    DoubleSummaryStatistics est = episodios.stream()
        .filter(e -> e.getAvaliacao() > 0.0)
        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
    System.out.println("===========================================================");
    System.out.println("Estatísticas de Avaliação:");
    System.out.println("Média: " + est.getAverage());
    System.out.println("Máximo: " + est.getMax());
    System.out.println("Mínimo: " + est.getMin());
    System.out.println("Total: " + est.getSum());
  }
}
