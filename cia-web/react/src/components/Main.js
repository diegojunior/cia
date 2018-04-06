import React, {Component} from 'react';
import { Link } from 'react-router';
import {Button, Popover, PopoverTitle, PopoverContent } from 'reactstrap';

export default class Main extends Component {

    constructor(props) {
        super(props);
        this.state = {popoverParametrizacaoOpen: false, popoverNotificacaoOpen: false};

        this.toggleParametrizacao = this.toggleParametrizacao.bind(this);
        this.toggleNotificacao = this.toggleNotificacao.bind(this);
    }

    toggleParametrizacao() {
        this.setState({popoverParametrizacaoOpen: !this.state.popoverParametrizacaoOpen});
    }

    toggleNotificacao() {
        this.setState({popoverNotificacaoOpen: !this.state.popoverNotificacaoOpen});
    }

	render(){

    	return (

        	<div id="wrapper">

                <nav className="navbar navbar-default navbar-static-top" role="navigation" style={{marginBottom: 0}}>

                	<div className="navbar-header">
                        <div style={{marginTop: 10 + 'px', marginLeft: 71 + 'px', fontSize: 30 + 'px', fontFamily: ['Helvetica Neue', 'Helvetica', 'Arial', 'sans-serif'], fontStretch: 'ultra-expanded'}}>
                        	<Link to="/cia/home">C.I.A<span style={{width: 38 + 'px', display: 'inline-block'}}></span></Link>
                        </div>
                    </div>

                    <button id="buttonToggleNavigation" type="button" className="navbar-left navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span className="sr-only">Toggle navigation</span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                        <span className="icon-bar"></span>
                    </button>

                    <ul className="nav navbar-top-links navbar-right">
                        <li className="dropdown">
                            <a className="dropdown-toggle" data-toggle="dropdown" href="#">
                                <i className="fa fa-user fa-fw"></i><i className="fa fa-caret-down"></i>
                            </a>
                            <ul className="dropdown-menu dropdown-user">
                                <li><a href="/cia/logout"><i className="fa fa-sign-out fa-fw"></i> Sair</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    
                    <div className="navbar-default sidebar" role="navigation">
                        <div className="sidebar-nav">
                            <ul className="nav" id="side-menu" style={{'textAlign': 'center'}}>
                                <li>
                                    <a id="PopoverParametrizacao" href="#" onClick={this.toggleParametrizacao}><i className="fa fa-gears fa-2x"></i><br /><br /> Parametrização</a>
                                    <Popover placement="right bottom" isOpen={this.state.popoverParametrizacaoOpen} target="PopoverParametrizacao" toggle={this.toggleParametrizacao}>
                                        <PopoverTitle>
                                            <div className="panel panel-primary">
                                                <div className="panel-heading">
                                                    Parametrização
                                                </div>
                                            </div>
                                        </PopoverTitle>
                                        <PopoverContent>
                                            <ul className="nav">
                                                <li style={{'display': 'none'}}>
                                                    <Link to="/cia/configuracaoservico" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Configuração de Serviço</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/campo" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Campo</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/layout/delimitador" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Layout Delimitador</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/layout/posicional" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Layout Posicional</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/layout/xml" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Layout XML</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/unidadeimportacao/bloco" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Unidade por Bloco</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/unidadeimportacao/chave" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Unidade por Chave</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/transformacao" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Transformação</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/validacao/arquivo" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Validação de Arquivo</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/perfilconciliacao" activeClassName="active" onClick={this.toggleParametrizacao}><i className="fa fa-gear"></i> Perfil de Conciliação</Link>
                                                </li>
                                            </ul>
                                        </PopoverContent>
                                    </Popover>

                                </li>
                                <li>
                                	<Link to="/cia/carga" activeClassName="active"><i className="glyphicon glyphicon-log-in fa-2x"></i><br /><br /> Carga<span></span></Link>
                                </li>
                                <li>
                                	<Link to="/cia/importacao" activeClassName="active"><i className="glyphicon glyphicon-import fa-2x"></i><br /><br /> Importação</Link>
                                </li>
                                <li>
                                	<Link to="/cia/conciliacao" activeClassName="active"><i className="fa fa-random fa-2x"></i><br /><br /> Conciliação</Link>
                                </li>
                                <li>
                                	<Link to="/cia/painel" activeClassName="active"><i className="fa fa-laptop fa-2x"></i><br /><br /> Painel</Link>
                                </li>

                                <li>
                                    <a id="PopoverNotificacao" href="#" onClick={this.toggleNotificacao}><i className="glyphicon glyphicon-list fa-2x"></i><br /><br /> Notificações</a>
                                    <Popover placement="right bottom" isOpen={this.state.popoverNotificacaoOpen} target="PopoverNotificacao" toggle={this.toggleNotificacao}>
                                        <PopoverTitle>
                                            <div className="panel panel-primary">
                                                <div className="panel-heading">
                                                    Notificações
                                                </div>
                                            </div>
                                        </PopoverTitle>
                                        <PopoverContent>
                                            <ul className="nav">
                                                <li>
                                                    <Link to="/cia/notificacao/carga" activeClassName="active" onClick={this.toggleNotificacao}><i className="glyphicon glyphicon-list-alt"></i> Notificação de Carga</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/notificacao/importacao" activeClassName="active" onClick={this.toggleNotificacao}><i className="glyphicon glyphicon-list-alt"></i> Notificação de Importação</Link>
                                                </li>
                                                <li>
                                                    <Link to="/cia/notificacao/conciliacao" activeClassName="active" onClick={this.toggleNotificacao}><i className="glyphicon glyphicon-list-alt"></i> Notificação de Conciliação</Link>
                                                </li>
                                            </ul>
                                        </PopoverContent>
                                    </Popover>

                                </li>
                                
                            </ul>
                        </div>
                    </div>
                </nav>
                
                <div id="page-wrapper">
                    {this.props.children}
	            </div>

            </div>
       		
        );
    }
}