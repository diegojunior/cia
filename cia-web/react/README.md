##FERRAMENTAS UTILIZADAS NO FRONTEND##

 * View Components: React - https://facebook.github.io/react/
 * Package Manager: Node - https://nodejs.org/en/download/
 * Module Loader: Webpack - https://webpack.js.org/guides/installation/
 * Compilador: Babel - https://babeljs.io/docs/setup/
 * Validação de código: http://eslint.org/

##INSTALAÇÕES NECESSÁRIAS PARA DESENVOLVER##

 - npm install: buscará todas as dependências no package.json
 
 - REACT: react react-dom react-router history file-loader react-bootstrap immutability-helper react-filtered-multiselect react-hot-loader prop-types create-react-class react-datepicker react-bootstrap-date-picker moment json-loader

 - WEBPACK: webpack webpack-dev-server

 - BABEL: babel-loader babel-core babel-preset-es2015 babel-preset-react

 - UGLIFY: uglify-js

 - STYLE E CSS LOADER: css-loader style-loader file-loader
 
 - AXIOS: axios

##COMANDOS UTEIS##

 - webpack: Para executar o BUILD, basta digitar "webpack" no prompt. Esse comando buscará o arquivo webpack.config.js no diretório corrente.
 
 - webpack-dev-server: Para subir o servidor node, utilizamos o webpack-dev-server. Basta digitar o comando "webpack-dev-server" e o mesmo buscará o arquivo webpack.config.js com as configuracoes necessarias e realizará as seguintes tarefas:
 	- Build
 	- Tests
 	- Deploy
	- Start Node Server

 - "webpack --display-error-details": Comando util para detalhar um erro de compilação.