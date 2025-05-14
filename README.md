<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>README - Projeto ImageLite App</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      color: #333;
      line-height: 1.6;
      margin: 0;
      padding: 20px;
    }
    h1, h2 {
      color: #4CAF50;
    }
    h3 {
      color: #333;
    }
    pre {
      background-color: #282c34;
      color: #fff;
      padding: 10px;
      border-radius: 5px;
      font-size: 14px;
    }
    code {
      font-family: Consolas, monospace;
      font-size: 1.1em;
    }
    ul {
      margin-bottom: 20px;
    }
    li {
      margin-bottom: 8px;
    }
    .content {
      max-width: 800px;
      margin: 0 auto;
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }
  </style>
</head>
<body>

  <div class="content">
    <h1>Projeto ImageLite App</h1>
    <p>Este é o README do projeto ImageLite App, uma aplicação para gerenciamento de imagens com funcionalidades de busca por tags e formatos.</p>

    <h2>Estrutura do Projeto</h2>
    <p>O projeto é dividido em duas partes principais:</p>
    <ul>
      <li><strong>Back-end:</strong> Desenvolvido com Java, Spring Boot e API REST.</li>
      <li><strong>Front-end:</strong> Desenvolvido com Next.js, React e Tailwind CSS.</li>
    </ul>

    <h2>Back-End</h2>
    <h3>Requisitos</h3>
    <ul>
      <li>Java 17 ou superior</li>
      <li>Spring Boot 2.x</li>
      <li>Banco de Dados Relacional (ex: MySQL, PostgreSQL)</li>
      <li>JDK 17 ou superior</li>
      <li>IDE de sua escolha (ex: IntelliJ IDEA, Eclipse)</li>
    </ul>

    <h3>Como Rodar</h3>
    <pre><code>git clone https://github.com/seu-usuario/imagelite-backend.git
cd imagelite-backend
./mvnw spring-boot:run</code></pre>

    <h3>Endpoints principais</h3>
    <ul>
      <li><code>GET /api/images</code> - Recupera a lista de imagens cadastradas.</li>
      <li><code>POST /api/images</code> - Envia uma nova imagem.</li>
      <li><code>GET /api/images/{id}</code> - Recupera os detalhes de uma imagem específica.</li>
    </ul>

    <h2>Front-End</h2>
    <h3>Requisitos</h3>
    <ul>
      <li>Node.js v16.x ou superior</li>
      <li>npm ou yarn para gerenciamento de pacotes</li>
    </ul>

    <h3>Como Rodar</h3>
    <pre><code>git clone https://github.com/seu-usuario/imagelite-frontend.git
cd imagelite-frontend
npm install
npm run dev</code></pre>

    <h3>Estrutura de Arquivos</h3>
    <ul>
      <li><code>pages/</code> - Contém as páginas principais da aplicação.</li>
      <li><code>components/</code> - Contém os componentes reutilizáveis (ex: Header, Footer).</li>
      <li><code>public/</code> - Imagens, ícones e outros arquivos estáticos.</li>
    </ul>

    <h2>Como Contribuir</h2>
    <ul>
      <li>Faça um fork do repositório.</li>
      <li>Crie uma branch para suas alterações: <code>git checkout -b minha-feature</code></li>
      <li>Commit suas alterações: <code>git commit -am 'Adicionando nova feature'</code></li>
      <li>Push para a branch: <code>git push origin minha-feature</code></li>
      <li>Abra um pull request para a branch principal.</li>
    </ul>

    <h2>Licença</h2>
    <p>Este projeto está licenciado sob a licença MIT - veja o arquivo <code>LICENSE</code> para mais detalhes.</p>

    <h2>Contato</h2>
    <p>Para mais informações, entre em contato com o desenvolvedor principal:</p>
    <ul>
      <li>Email: <a href="mailto:seu-email@dominio.com">seu-email@dominio.com</a></li>
      <li>GitHub: <a href="https://github.com/seu-usuario" target="_blank">https://github.com/seu-usuario</a></li>
    </ul>
  </div>

</body>
</html>
