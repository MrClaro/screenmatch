package br.com.alura.screenmatch.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

public class Principal {

  private Scanner leitura = new Scanner(System.in);
  private ConsumoApi consumo = new ConsumoApi();
  private ConverteDados conversor = new ConverteDados();
  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = System.getenv("API_KEY");
  private final String API_URL = "&apikey=" + API_KEY;
  private List<DadosSerie> dadosSeries = new ArrayList<>();
  private SerieRepository serieRepository;

  public Principal(SerieRepository serieRepository) {
    this.serieRepository = serieRepository;
  }

  public void exibeMenu() {
    var opcao = -1;
    while (opcao != 0) {

      var menu = """
          ==========================
          Screenmatch - Menu
          ==========================
          1 - Buscar séries
          2 - Buscar episódios
          3 - Listar séries buscadas

          0 - Sair
          ==========================
          """;

      System.out.print(menu);
      opcao = leitura.nextInt();
      leitura.nextLine();

      switch (opcao) {
        case 1:
          buscarSerieWeb();
          break;
        case 2:
          buscarEpisodioPorSerie();
          break;
        case 3:
          listarSeriesBuscadas();
          break;
        case 0:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida");
      }
    }
  }

  private void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
    Serie serie = new Serie(dados);
    serieRepository.save(serie);
    System.out.println("==========================");
    System.out.println(dados);
  }

  private DadosSerie getDadosSerie() {
    System.out.println("==========================");
    System.out.println("Digite o nome da série para busca");
    var nomeSerie = leitura.nextLine();
    var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_URL);
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
    return dados;
  }

  private void listarSeriesBuscadas() {
    System.out.println("==========================");
    System.out.println("Séries buscadas:");
    List<Serie> series = serieRepository.findAll();
    series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
  }

  private void buscarEpisodioPorSerie() {
    System.out.println("==========================");
    DadosSerie dadosSerie = getDadosSerie();
    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
      var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_URL);
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);
  }
}
