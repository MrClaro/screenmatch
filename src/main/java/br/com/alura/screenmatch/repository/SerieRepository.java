package br.com.alura.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
  Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

  List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);
  // List<Serie>
  // findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThenEqual(String
  // nomeAtor);

  List<Serie> findTop5ByOrderByAvaliacaoDesc();

  List<Serie> findByGenero(Categoria categoria);

  @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
  List<Serie> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);

  @Query("select e from serie s join s.episodios e where e.titulo ilike %:trecho%")
  List<Episodio> episodiosPorTrecho(String trecho);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
  List<Episodio> topEpisodiosPorSerie(Serie serie);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
  List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

}
