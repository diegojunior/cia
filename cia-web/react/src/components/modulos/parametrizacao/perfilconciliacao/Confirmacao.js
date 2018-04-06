import React, { Component } from 'react';
import { Link } from 'react-router';
import {constants} from '../../../../shared/util/Constants';
import './Style.css';

export default class Confirmacao extends Component {

	constructor(props) {
		super(props);
		this.state = {perfil: {id: null, identificacao: null, configuracao: null, regras: null}};
			
		this.generateConfiguracao = this.generateConfiguracao.bind(this);
		this.generateRegras = this.generateRegras.bind(this);
		this.clear = this.clear.bind(this);
	}

	componentWillReceiveProps(nextProps) {
		this.state.perfil = {id: null, identificacao: null, configuracao: null};

		if (nextProps.perfil != null && nextProps.perfil.identificacao != null) {
			this.state.perfil.identificacao = nextProps.perfil.identificacao;
		}
		
		if (nextProps.perfil != null && nextProps.perfil.configuracao != null) {
			var configuracao = nextProps.perfil.configuracao.configuracao;
			
			this.state.perfil.configuracao = {localizacaoCampos: configuracao.localizacaoCampos,
												identificacao: configuracao.identificacao,
												servico: configuracao.servico,
												consolidarDados: configuracao.consolidarDados,
												configuracoesCampos: configuracao.configuracoesCampos};
				
		}

		if (nextProps.perfil != null && nextProps.perfil.regras != null) {
			this.state.perfil.regras = nextProps.perfil.regras;
		}
	}

	generateConfiguracao () {

		var configuracoesCampos = this.props.perfil.configuracao.configuracao.configuracoesCampos.map((configuracaoCampo, indexConfiguracaoCampo) => {

			var campoCarga = configuracaoCampo.campoCarga != null ? configuracaoCampo.campoCarga.label : '';
			var campoEquivalente = configuracaoCampo.campoEquivalente != null ? configuracaoCampo.campoEquivalente.label : '';
			return (<tr key={indexConfiguracaoCampo}>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.chave ? <div className="fa fa-check"></div> : ''}</td>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.conciliavel ? <div className="fa fa-check"></div> : ''}</td>
						<td style={{'textAlign' : 'center'}}>{configuracaoCampo.informativo ? <div className="fa fa-check"></div> : ''}</td>
						<td>{campoCarga}</td>
						<td>{configuracaoCampo.campoImportacao}</td>
						<td>{campoEquivalente}</td>
					</tr>)
		});

		return (<div className="row">
					<div className="col-lg-12">
						<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
							Configurações
						</div>
						<table id="cabecalho" className="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>Chave</th>
									<th>Conciliável</th>
									<th>Informativo</th>
									<th>Campo Carga</th>
									<th>Campo Importação</th>
									<th>Campo Equivalente</th>
								</tr>
							</thead>
							<tbody>
								{configuracoesCampos}
							</tbody>
						</table>
					</div>
				</div>);
	}

	generateRegras () {
		if (this.props.perfil.regras != null && this.props.perfil.regras.length > 0) {
			var regras = this.props.perfil.regras.map((regra, indexRegra) => {

				var modulo = regra.modulo.nome;
				var campo = regra.campoCarga != null ? regra.campoCarga.label : regra.campoImportacao;
				var condicao = regra.condicao.nome;
				var filtro = regra.filtro;
				return (<tr key={indexRegra}>
							<td>{modulo}</td>
							<td>{campo}</td>
							<td>{condicao}</td>
							<td>{filtro}</td>
						</tr>)
			});

			return (<div className="row">
						<div className="col-lg-12">
							<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
								Regras
							</div>
							<table id="cabecalhoRegras" className="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>Módulo</th>
										<th>Campo</th>
										<th>Condição</th>
										<th>Filtro</th>
									</tr>
								</thead>
								<tbody>
									{regras}
								</tbody>
							</table>
						</div>
					</div>);
		}
		return null;
	}

	clear () {
		this.setState({perfil: {id: null, identificacao: null, configuracao: null}});
	}

	render(){

		var codigo = '';
		var descricao = '';
		var sistema = '';
		var tipoLayout = '';
		var layout = '';
		var servico = '';
		var tituloLocalizacaoCampos = '';
		var valorLocalizacaoCampos = '';
		var rowsConfiguracao = '';
		var rowsRegras = '';
		var consolidarDados = '';

		var identificacao = this.props.perfil.identificacao;

		if (identificacao != null) {
			codigo = identificacao.codigo;
			descricao = identificacao.descricao;
			sistema = identificacao.sistema ? identificacao.sistema.nome : null;
			tipoLayout = identificacao.tipoLayout ? identificacao.tipoLayout.nome : null;
			layout = identificacao.layout ? identificacao.layout.codigo : null;
		}

		if(this.props.perfil.configuracao != null) {
			tituloLocalizacaoCampos = this.props.perfil.configuracao.configuracao.localizacaoCampos.nome;
			valorLocalizacaoCampos = this.props.perfil.configuracao.configuracao.identificacao;
			servico = this.props.perfil.configuracao.configuracao.servico;
			if (this.props.perfil.configuracao.configuracao.consolidarDados != null) {
				consolidarDados = constants.getSimOuNaoCodigo(this.props.perfil.configuracao.configuracao.consolidarDados.toString()).nome;
			}
			rowsConfiguracao = this.generateConfiguracao();
		}

		if(this.props.perfil.regras != null) {
			rowsRegras = this.generateRegras();
		}

		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				
					<div className="col-lg-12">
						<div className="panel panel-default" id="cadPanel">
							<div className="panel-body">
								<div className="page-header" style={{fontWeight : "bold", fontSize : "15px"}}>
									Identificação
								</div>
								<table id="cabecalho" className="table table-bordered">
									<thead>
										<tr>
											<th>Código <div className="fa fa-key" style={{color: '#c3ae09'}}></div></th>
											<th>Descrição</th>
											<th>Sistema</th>
											<th>Tipo do Layout</th>
											<th>Layout</th>
											<th>{tituloLocalizacaoCampos}</th>
											<th>Serviço</th>
											<th>Consolidar Dados</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>{codigo}</td>
											<td>{descricao}</td>
											<td>{sistema}</td>
											<td>{tipoLayout}</td>
											<td>{layout}</td>
											<td>{valorLocalizacaoCampos}</td>
											<td>{servico}</td>
											<td>{consolidarDados}</td>
										</tr>
									</tbody>
								</table>
								{rowsConfiguracao}
								{rowsRegras}
							</div>
						</div>
					</div>	
				</div>
			</div>
			
		);
	}
}