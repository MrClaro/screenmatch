# Screenmatch

Projeto completo para consulta e visualização de séries, integrando backend em Java (Spring Boot) com frontend em HTML/CSS/JS.

## Estrutura do Projeto

- **backend/**: API Java Spring Boot responsável por buscar e processar informações sobre séries usando a OMDb API.
- **frontend/**: Aplicação web para consumo da API, visualização e interação com os dados.
- **README.md**: Este arquivo.
- **.gitignore, .gitattributes**: Configurações do repositório.

---

## Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot**
- **HTTP Client (java.net.http.HttpClient)**
- **Jackson (conversão JSON)**
- **OMDb API** [(documentação)](https://www.omdbapi.com/)

### Frontend
- **HTML5, CSS3, JavaScript**
- **Live Server (VS Code extension recomendada)**

---

## Como rodar o projeto

### 1. Clone o repositório

```bash
git clone https://github.com/MrClaro/screenmatch.git
cd screenmatch
```

### 2. Configurar Backend

- Navegue até a pasta `backend`
- Insira sua **API KEY do OMDb** na classe principal (ex: `Principal.java`), substituindo `[API-KEY]` pelo seu token.
- Compile e rode:

```bash
cd backend
mvn clean package
mvn spring-boot:run
```
Ou:
```bash
java -jar target/screenmatch-*.jar
```

### 3. Configurar Frontend

- Navegue até a pasta `frontend`
- Recomendado: Utilize a extensão **Live Server** do VS Code.
- Clique com o botão direito em `index.html` > "Open with Live Server".

---

## Funcionalidades

- Busca de séries pelo nome.
- Consulta de detalhes (título, temporadas, avaliação IMDb).
- Listagem de temporadas e episódios com informações detalhadas.
- Exibição do top 5 episódios por avaliação.
- Estatísticas de avaliação por temporada (média, máximo, mínimo, soma total).

---

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch (`git checkout -b feature/nome-da-feature`)
3. Commit suas mudanças (`git commit -m 'feat: minha contribuição'`)
4. Push na branch (`git push origin feature/nome-da-feature`)
5. Abra um Pull Request.

---

## Licença

Projeto desenvolvido por [MrClaro](https://github.com/MrClaro) para fins educacionais.

---

## Observação

Para utilizar a OMDb API é necessário obter uma chave gratuita em [omdbapi.com/apikey.aspx](https://www.omdbapi.com/apikey.aspx).
