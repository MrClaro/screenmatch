package br.com.alura.screenmatch.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
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
  private List<Serie> series = new ArrayList<>();
  private Optional<Serie> serieBusca;

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
          4 - Buscar série por titulo
          5 - Buscar séries por ator
          6 - Top 5 séries com maior avaliação
          7 - Buscar séries por genero
          8 - Buscar episodio por trecho
          9 - Buscar top 5 episódios por série

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
        case 4:
          buscarSeriePorTitulo();
          break;
        case 5:
          buscarPorAtor();
          break;
        case 6:
          buscarTop5SerieComMaiorAvaliacao();
          break;
        case 7:
          buscarSeriesPorGenero();
          break;
        case 8:
          buscarEpisodioPorTrecho();
          break;
        case 9:
          buscarTopEpisodiosPorSerie();
          break;
        case 11:
          buscarEpisodiosDepoisDeUmaData();
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
    series = serieRepository.findAll();
    series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
  }

  private void buscarEpisodioPorSerie() {
    listarSeriesBuscadas();
    System.out.println("==========================");
    System.out.println("Digite o nome da série para buscar os episódios");
    var nomeSerie = leitura.nextLine();
    Optional<Serie> serie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);
    if (serie.isPresent()) {
      var serieEncontrada = serie.get();
      List<DadosTemporada> temporadas = new ArrayList<>();
      for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
        var json = consumo
            .obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_URL);
        DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
        temporadas.add(dadosTemporada);
      }
      temporadas.forEach(System.out::println);
      List<Episodio> episodios = temporadas.stream()
          .flatMap(d -> d.episodios().stream().map(e -> new Episodio(d.numero(), e)))
          .collect(Collectors.toList());
      serieEncontrada.setEpisodios(episodios);
      serieRepository.save(serieEncontrada);
    } else {
      System.out.println("Série não encontrada.");
    }
  }

  private void buscarSeriePorTitulo() {
    System.out.println("Escolha uma série para buscar por título:");
    var nomeSerie = leitura.nextLine();
    serieBusca = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);
    if (serieBusca.isPresent()) {
      System.out.println("Série encontrada: " + serieBusca.get());
    } else {
      System.out.println("Série não encontrada.");
    }
  }

  private void buscarPorAtor() {
    System.out.println("Digite o nome do ator para buscar por nome");
    var nomeAtor = leitura.nextLine();
    List<Serie> seriesEncontradas = serieRepository.findByAtoresContainingIgnoreCase(nomeAtor);
    System.out.println("Series em que o ator " + nomeAtor + " participou:");
    seriesEncontradas.forEach(s -> System.out.println("Titulo: " + s.getTitulo() + ", Avaliação: " + s.getAvaliacao()));
  }

  private void buscarTop5SerieComMaiorAvaliacao() {
    List<Serie> series = serieRepository.findTop5ByOrderByAvaliacaoDesc();
    System.out.println("Top 5 séries com maior avaliação:");
    series.forEach(s -> System.out.println("Titulo: " + s.getTitulo() + ", Avaliação: " + s.getAvaliacao()));
  }

  private void buscarSeriesPorGenero() {
    System.out.println("Digite o genero para buscar por");
    var genero = leitura.nextLine();
    Categoria categoria = Categoria.fromPortugues(genero);
    List<Serie> series = serieRepository.findByGenero(categoria);
    System.out.println("Séries do gênero " + genero + ":");
    series.forEach(s -> System.out.println("Titulo: " + s.getTitulo() + ", Avaliação: " + s.getAvaliacao()));
  }

  private void buscarEpisodioPorTrecho() {
    System.out.println("Digite o trecho do titulo do episodio para buscar");
    var trecho = leitura.nextLine();
    List<Episodio> episodios = serieRepository.episodiosPorTrecho(trecho);
    episodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
        e.getSerie().getTitulo(), e.getTemporada(),
        e.getNumeroEpisodio(), e.getTitulo()));
  }

  private void buscarTopEpisodiosPorSerie() {
    buscarSeriePorTitulo();
    if (serieBusca.isPresent()) {
      Serie serie = serieBusca.get();
      List<Episodio> topEpisodios = serieRepository.topEpisodiosPorSerie(serie);
      topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
          e.getSerie().getTitulo(), e.getTemporada(),
          e.getNumeroEpisodio(), e.getTitulo()));
    }
  }

  private void buscarEpisodiosDepoisDeUmaData() {
    buscarSeriePorTitulo();
    if (serieBusca.isPresent()) {
      Serie serie = serieBusca.get();
      System.out.println("Digite o ano limite de lançamento");
      var anoLancamento = leitura.nextInt();
      leitura.nextLine();

      List<Episodio> episodiosAno = serieRepository.episodiosPorSerieEAno(serie, anoLancamento);
      episodiosAno.forEach(System.out::println);
    }
  }
}
