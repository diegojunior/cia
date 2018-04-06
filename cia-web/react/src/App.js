import React, { Component } from 'react';
import { render } from 'react-dom';
import {Router, Route, browserHistory} from 'react-router';

import Main from './components/Main';
import Home from './components/modulos/Home';

import ConfiguracaoServicoTemplate from './components/modulos/parametrizacao/configuracaoservico/Template';
import CampoTemplate from './components/modulos/parametrizacao/campo/Template';
import LayoutPosicionalTemplate from './components/modulos/parametrizacao/layout/posicional/Template';
import LayoutDelimitadorTemplate from './components/modulos/parametrizacao/layout/delimitador/Template';
import LayoutXmlTemplate from './components/modulos/parametrizacao/layout/xml/Template';
import UnidadeImportacaoChaveTemplate from './components/modulos/parametrizacao/unidadeimportacao/chave/Template';
import UnidadeImportacaoBlocoTemplate from './components/modulos/parametrizacao/unidadeimportacao/bloco/Template';
import TransformacaoTemplate from './components/modulos/parametrizacao/transformacao/Template';
import ValidacaoArquivoTemplate from './components/modulos/parametrizacao/validacao/arquivo/Template';
import PerfilConciliacaoTemplate from './components/modulos/parametrizacao/perfilconciliacao/Template';
import CargaTemplate from './components/modulos/carga/Template';
import ImportacaoTemplate from './components/modulos/importacao/Template';
import ConciliacaoTemplate from './components/modulos/conciliacao/execucao/Template';
import PainelConciliacao from './components/modulos/conciliacao/painel/Painel';
import NotificacaoCargaTemplate from './components/modulos/notificacao/carga/Template';
import NotificacaoImportacaoTemplate from './components/modulos/notificacao/importacao/Template';
import NotificacaoConciliacaoTemplate from './components/modulos/notificacao/conciliacao/Template';


render(
	    <Router history={browserHistory}>
	    	
    		 <Route component={Main}>
           
	        	<Route path="/" component={Home}/>
	        	<Route path="/cia/home" component={Home}/>

				<Route path="/cia/configuracaoservico" component={ConfiguracaoServicoTemplate} />
				<Route path="/cia/campo" component={CampoTemplate} />
				<Route path="/cia/layout/posicional" component={LayoutPosicionalTemplate} />
				<Route path="/cia/layout/delimitador" component={LayoutDelimitadorTemplate} />
				<Route path="/cia/layout/xml" component={LayoutXmlTemplate} />
				<Route path="/cia/unidadeimportacao/chave" component={UnidadeImportacaoChaveTemplate} />
				<Route path="/cia/unidadeimportacao/bloco" component={UnidadeImportacaoBlocoTemplate} />
	            <Route path="/cia/transformacao" component={TransformacaoTemplate} />
				<Route path="/cia/validacao/arquivo" component={ValidacaoArquivoTemplate} />
				<Route path="/cia/perfilconciliacao" component={PerfilConciliacaoTemplate} />
	            <Route path="/cia/carga" component={CargaTemplate} />
	            <Route path="/cia/importacao" component={ImportacaoTemplate} />
	            <Route path="/cia/conciliacao" component={ConciliacaoTemplate} />
				<Route path="/cia/painel" component={PainelConciliacao} />
				<Route path="/cia/notificacao/carga" component={NotificacaoCargaTemplate} />
				<Route path="/cia/notificacao/importacao" component={NotificacaoImportacaoTemplate} />
				<Route path="/cia/notificacao/conciliacao" component={NotificacaoConciliacaoTemplate} />
	         </Route>
	    </Router>,
		
	    document.getElementById('app')
);