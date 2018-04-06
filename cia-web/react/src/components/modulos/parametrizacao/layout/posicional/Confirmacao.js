import React, { Component } from 'react';
import { Link } from 'react-router';
import './Style.css';

export default class Confirmacao extends Component {

	constructor(props) {
		super(props);
		this.state = {layout: {identificacao: null, sessoes: []}};
			
		this.generateRows = this.generateRows.bind(this);
		this.clear = this.clear.bind(this);
	}

	componentWillReceiveProps(nextProps) {
		this.state.layout = {identificacao: null, sessoes: []};

		if (nextProps.layout != null && nextProps.layout.identificacao != null) {
			this.state.layout.identificacao = nextProps.layout.identificacao;
		}
		
		if (nextProps.layout != null && 
			nextProps.layout.sessoes != null) {

				for (var index in nextProps.layout.sessoes) {

					var sessao = nextProps.layout.sessoes[index];
					
					this.state.layout.sessoes.push(sessao);
				}
		}
	}

	generateRows () {

		var sessoes = this.state.layout.sessoes.map((sessao, index) => {

			var campos = sessao.campos.map((campo, indexCampo) => {

				var dominio = campo.dominio != null ? campo.dominio.codigo : '';
				return (<tr key={indexCampo}>
							<td>{dominio}</td>
							<td>{campo.descricao}</td>
							<td>{campo.posicaoInicial}</td>
							<td>{campo.posicaoFinal}</td>
							<td>{campo.tamanho}</td>
							<td>{campo.pattern}</td>
						</tr>)

			});

			return (<div className="panel panel-default" key={index}>
						<div className="panel-heading">
							<a data-toggle="collapse" data-parent="#accordionPosicionalConfirmacao" href={"#accordionPosicionalConfirmacao"+index} aria-expanded="true" className="collapsed">
								{sessao.nome + ' - ' + sessao.codigo}
							</a>								
						</div>
						<div id={"accordionPosicionalConfirmacao"+index} className="panel-collapse collapse in" aria-expanded="true">
							<div className="panel-body">
								<div>
									<table id="cabecalho" className="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>Campo</th>
												<th>Descrição</th>
												<th>Posição Inicial</th>
												<th>Posição Final</th>
												<th>Tamanho</th>
												<th>Formato</th>
											</tr>
										</thead>
										<tbody>
											{campos}
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>)
		});

		return (<div className="row">
					<div className="col-lg-12">
						<div className="panel-group" id="accordionPosicionalConfirmacao">
							{sessoes}
						</div>
					</div>
				</div>);
	}

	clear () {
		this.setState({layout: {identificacao: null, sessoes: []}});
	}

	render(){

		var codigo = '';
		var descricao = '';
		var rows = '';

		var identificacao = this.props.layout.identificacao;

		if (identificacao != null) {
			codigo = identificacao.codigo;
			descricao = identificacao.descricao;
		}

		if(this.props.layout.sessoes != null) {
			rows = this.generateRows();
		}

		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				
					<div className="col-lg-12">						
						<div className="panel panel-default" id="cadPanel">
							<div className="panel-body">
								<table id="cabecalho" className="table table-bordered">
									<thead>
										<tr>
											<th>Código <div className="fa fa-key" style={{color: '#c3ae09'}}></div></th>
											<th>Descrição</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>{codigo}</td>
											<td>{descricao}</td>
										</tr>
									</tbody>
								</table>
								{rows}
							</div>
						</div>
					</div>	
				</div>
			</div>
			
		);
	}
}