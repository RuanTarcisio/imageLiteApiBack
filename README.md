<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
</head>
<body>
  <h1>📡 Imagelite API (Back-end)</h1>
  <p>API REST desenvolvida com <strong>Spring Boot</strong> para autenticação, gerenciamento de usuários e outras funcionalidades do sistema Imagelite.</p>

  <h2>🚀 Tecnologias utilizadas</h2>
  <ul>
    <li>Java 17+</li>
    <li>Spring Boot</li>
    <li>Spring Security</li>
    <li>JWT (com cookie HttpOnly)</li>
    <li>Lombok</li>
    <li>JPA / Hibernate</li>
    <li>PostgreSQL</li>
    <li>Flyway</li>
    <li>OAuth2</li>
    <li>PostgreSQL</li>
    <li>Spring Mail</li>
    <li>PostgreSQL</li>
    <li>Docker (opcional)</li>
  </ul>

  <h2>📂 Estrutura principal</h2>
  <ul>
    <li><code>/condif</code> – classes de configuração</li>
    <li><code>/domain</code> – entidades de domínio</li>
    <li><code>/dtos</code> – classes dtos</li>
    <li><code>/repository</code> – interfaces do JPA</li>
    <li><code>/service</code> – regras de negócio</li>
    <li><code>/resources</code> – endpoints REST</li>
    <li><code>/security</code> – autenticação e filtros</li>
    <li><code>/utils</code> – utilitários diversos</li>
  </ul>

  <h2>🔐 Autenticação</h2>
  <p>Autenticação baseada em JWT:</p>
  <ul>
    <li>Login via <code>/signin</code></li>
    <li>Login via <code>Social login (google/ github) </code></li>
    <li>JWT gerado e enviado em cookie HttpOnly (<code>AUTH_TOKEN</code>)</li>
    <li>Filtro <code>JwtFilter</code> intercepta e valida o token</li>
  </ul>

  <h2>▶️ Como rodar o projeto</h2>
  <h3>Pré-requisitos</h3>
  <ul>
    <li>Java 17+</li>
    <li>Maven</li>
    <li>PostgreSQL</li>
  </ul>

  <h3>Executando localmente</h3>
  <pre><code>
cd backend
./mvnw spring-boot:run
  </code></pre>

  <h2>📫 Endpoints principais</h2>
  <table border="1">
    <thead>
      <tr>
        <th>Método</th>
        <th>Rota</th>
        <th>Descrição</th>
      </tr>
    </thead>
    <tbody>
      <tr><td>POST</td><td>/signin</td><td>Autentica usuário e gera JWT</td></tr>
      <tr><td>GET</td><td>/me</td><td>Retorna dados do usuário autenticado</td></tr>
      <tr><td>POST</td><td>/users</td><td>Cria novo usuário</td></tr>
    </tbody>
  </table>

  <h2>📁 Configuração</h2>
  <p>Exemplo de <code>application.properties</code>:</p>
  <pre><code>
spring.datasource.url=jdbc:postgresql://localhost:5432/imagelite
spring.datasource.username=usuario
spring.datasource.password=senha
jwt.secret=umaChaveSecreta

security.oauth2.client.registration.google.client-id= SEU GOOGLE CLIENT
security.oauth2.client.registration.google.client-secret.= SEU GOOGLE ID
  </code></pre>
</body>
</html>
