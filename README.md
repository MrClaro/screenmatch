# Screenmatch (sem web)

Projeto Java para consulta de séries e episódios via terminal, utilizando a API pública do OMDb (Open Movie Database).

## Funcionalidades

- Busca de séries pelo nome.
- Consulta de detalhes sobre a série (título, número de temporadas, avaliação IMDb).
- Listagem de todas as temporadas e episódios, com dados individuais (título, número do episódio, avaliação, data de lançamento).
- Exibição do top 5 episódios por avaliação.
- Cálculo de estatísticas de avaliação por temporada (média, máximo, mínimo, soma total).

## Como funciona

O programa é executado via linha de comando (CLI) com Spring Boot. O usuário informa o nome da série, e o sistema consulta a API OMDb para obter as informações. Todos os dados são processados e apresentados no terminal.

### Exemplo de fluxo:

1. O usuário executa o programa e digita o nome da série desejada.
2. O sistema consulta a API, exibe informações gerais e busca todas as temporadas e episódios.
3. O usuário visualiza o top 5 episódios e estatísticas de avaliação.

## Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot** (CLI via `CommandLineRunner`)
- **API HTTP Client** (`java.net.http.HttpClient`)
- **Jackson** (converter JSON)
- **OMDb API** (https://www.omdbapi.com/)

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/MrClaro/screenmatch-sem-web.git
   cd screenmatch-sem-web
   ```

2. Insira sua API KEY do OMDb na classe `Principal.java`, substituindo `[API-KEY]` pelo seu token.

3. Compile o projeto (necessário Java e Maven instalados):
   ```bash
   mvn clean package
   ```

4. Execute o aplicativo:
   ```bash
   mvn spring-boot:run
   ```
   Ou:
   ```bash
   java -jar target/screenmatch-sem-web-*.jar
   ```

5. Siga as instruções no terminal.

## Estrutura do Projeto

- `src/main/java/br/com/alura/screenmatch/client/Principal.java` — Classe principal, interação com o usuário e lógica de busca.
- `src/main/java/br/com/alura/screenmatch/service/ConsumoApi.java` — Requisições HTTP à API OMDb.
- `src/main/java/br/com/alura/screenmatch/service/ConverteDados.java` — Conversão de JSON para objetos Java.
- `src/main/java/br/com/alura/screenmatch/model/` — Modelos de dados (Série, Temporada, Episódio).

## Observação

Para utilizar o aplicativo, é necessário obter uma chave de API gratuita em [omdbapi.com/apikey.aspx](https://www.omdbapi.com/apikey.aspx) e incluí-la no código-fonte.

---

Projeto desenvolvido por [MrClaro](https://github.com/MrClaro)
