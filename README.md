<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Imagelite API (Back-end)</title>
</head>
<body>
  <h1>📡 Imagelite API (Back-end)</h1>

  <section>
    <h2>🌟 Sobre o projeto</h2>
    <p><strong>Imagelite</strong> é uma API REST robusta e moderna, desenvolvida com <strong>Spring Boot</strong> e <strong>Next Js</strong>, criada para fornecer autenticação segura, gerenciamento de usuários e funcionalidades essenciais para sistemas que precisam de performance, segurança e escalabilidade.</p>
    <p>Meu objetivo é entregar uma base confiável para aplicações web e móveis, utilizando as melhores práticas do mercado e tecnologias de ponta, garantindo que o software não apenas funcione, mas que ofereça uma experiência consistente e segura ao usuário final.</p>
  </section>

  <section>
    <h2>🚀 Tecnologias utilizadas</h2>
    <ul>
      <li>Java 17+</li>
      <li>Spring Boot</li>
      <li>Spring Security</li>
      <li>JWT (com cookie HttpOnly para maior segurança)</li>
      <li>Lombok</li>
      <li>Swagger para documentação</li>
      <li>JPA / Hibernate</li>
      <li>PostgreSQL</li>
      <li>Flyway para versionamento do banco</li>
      <li>OAuth2 (Google e GitHub)</li>
      <li>Spring Mail para envio de e-mails</li>
      <li>Docker para containerização</li>
    </ul>
  </section>

  <section>
    <h2>📂 Estrutura do projeto</h2>
    <p>Organização clara e modular para facilitar manutenção e evolução:</p>
    <ul>
      <li><code>/config</code> – configurações gerais do sistema</li>
      <li><code>/domain</code> – entidades de negócio</li>
      <li><code>/dtos</code> – objetos de transferência de dados</li>
      <li><code>/repository</code> – acesso a dados via JPA</li>
      <li><code>/service</code> – lógica de negócio</li>
      <li><code>/resources</code> – controladores REST</li>
      <li><code>/security</code> – autenticação, autorização e filtros</li>
      <li><code>/utils</code> – utilitários diversos</li>
    </ul>
  </section>

  <section>
    <h2>🔐 Segurança e autenticação</h2>
    <p>Segurança de ponta a ponta com:</p>
    <ul>
      <li>Autenticação via JWT, armazenado em cookie HttpOnly para proteger contra ataques XSS</li>
      <li>Suporte a autenticação social via OAuth2 (Google e GitHub)</li>
      <li>Filtros customizados para validação de token e controle de acesso</li>
    </ul>
  </section>

  <section>
  <h2>🐳 Deploy simplificado com Docker Compose</h2>
  <p>Para facilitar o desenvolvimento e implantação, fornecemos um arquivo <code>docker-compose.yml</code> que orquestra:</p>
  <ul>
    <li>API Spring Boot (backend)</li>
    <li>Frontend React/Next.js (<a href="https://github.com/RuanTarcisio/imageLiteApiFront" target="_blank" rel="noopener noreferrer">repositório do frontend</a>)</li>
    <li>Banco de dados PostgreSQL</li>
    <li>Servidor Nginx para proxy reverso e roteamento</li>
  </ul>
  <p>Para rodar todo o ambiente integrado, basta executar:</p>
  <pre><code>docker-compose up -d</code></pre>

<h3>Configuração via arquivo <code>.env</code></h3>
  <p>O arquivo <code>.env</code> permite configurar variáveis sensíveis e parâmetros de ambiente usados pelo backend e frontend, como credenciais OAuth e SMTP para envio de e-mails. Crie um arquivo chamado <code>.env</code> na raiz do projeto (ou na pasta onde o docker-compose está) com as seguintes variáveis:</p>
  <pre><code>GOOGLE_CLIENT_ID=seu-google-client-id-aqui
GOOGLE_CLIENT_SECRET=seu-google-client-secret-aqui
SMTP_MAIL=seu-email-smtp@dominio.com
SMTP_PASS=sua-senha-smtp-aqui
</code></pre>
  <p>Essas variáveis serão injetadas nos containers para que o backend realize autenticação social e envio de e-mails corretamente, e o frontend possa consumir APIs autenticadas.</p>
</section>

<section>
  <h2>▶️ Como rodar o projeto localmente</h2>
  <p><em>Pré-requisitos:</em> Java 17+, Maven, PostgreSQL</p>
  <pre><code>cd backend
./mvnw spring-boot:run
</code></pre>
  <p>Ou, para rodar todo o stack com Docker Compose, simplesmente:</p>
  <pre><code>docker-compose up -d</code></pre>
</section>

  <section>
    <h2>📫 Endpoints principais</h2>
    <table border="1" cellpadding="6" cellspacing="0">
      <thead>
        <tr>
          <th>Método</th>
          <th>Rota</th>
          <th>Descrição</th>
        </tr>
      </thead>
      <tbody>
        <tr><td>*</td><td>/users/**</td><td>Referente ao usuario</td></tr>
        <tr><td>*</td><td>/auth/**</td><td>Referente autenticação usuário e gera JWT</td></tr>
        <tr><td>*</td><td>/images/**</td><td>Cadastro de novas imagens</td></tr>
      </tbody>
    </table>
  </section>

  <section>
    <h2>📁 Configuração</h2>
    <p>Exemplo do arquivo <code>application.properties</code>:</p>
    <pre><code>spring.datasource.url=jdbc:postgresql://localhost:5432/imagelite
spring.datasource.username=usuario
spring.datasource.password=senha
jwt.secret=umaChaveSecreta

security.oauth2.client.registration.google.client-id=SEU_GOOGLE_CLIENT
security.oauth2.client.registration.google.client-secret=SEU_GOOGLE_SECRET
</code></pre>
  </section>
</body>
</html>
