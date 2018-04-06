import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../ui/form/FormValidator';
import TextInput from '../../../ui/input/TextInput';
import SelectInput from '../../../ui/input/SelectInput';
import PerfilConciliacaoService from '../../../../shared/services/PerfilConciliacao';
import LayoutService from '../../../../shared/services/Layout';
import {constants} from '../../../../shared/util/Constants';
import {sort} from '../../../../shared/util/Utils';
import RegrasMultiplesInputs from './RegrasMultiplesInputs';
import './Style.css';

export default class Regras extends Component {

	constructor(props) {
		super(props);
		
		this.state = {
			regras: []
		};

		this.carregaRegrasEValida = this.carregaRegrasEValida.bind(this);
		this.getCampoServicoByCodigo = this.getCampoServicoByCodigo.bind(this);
		this.clear = this.clear.bind(this);
	}

	isValidForm() {
		return this.carregaRegrasEValida();
	}

	carregaRegrasEValida () {
		this.state.regras.length = 0;
		var rows = this.refs.addRemoveMultiplesInputs.state.rows;
		var isVazio = rows.length == 1 && rows[0]['modulo0'].codigo == '' 
						&& rows[0]['condicao0'].codigo == ''&& rows[0]['filtro0'] == '';
		if (isVazio) {
			return true;
		}
		for (var index in rows) {
			var modulo = rows[index]['modulo'+index].codigo;
			var campo = rows[index]['campo'+index].codigo;
			var campoCarga = modulo == 'CA'? this.getCampoServicoByCodigo(rows[index]['campo'+index].codigo) : null;
			var campoImportacao = modulo == 'IM'? rows[index]['campo'+index].codigo : null;
			var condicao = rows[index]['condicao'+index].codigo;
			var filtro = rows[index]['filtro'+index];

			if (modulo != "" && campo != "" && condicao != "" && filtro != "") {
				this.state.regras.push({modulo: constants.getModuloPorCodigo(modulo),
							campoCarga: campoCarga,
							campoImportacao: campoImportacao,
							condicao: constants.getCondicaoPorCodigo(condicao),
							filtro: filtro});
			} else {
				var isLinhaVazia = modulo == "" && campo == "" && condicao == "" && filtro == "";
				var isInvalidComModulo = modulo != "" && (campo == "" || condicao == "" || filtro == "");
				var isInvalidSemModulo = modulo == "" && (condicao != "" || filtro != "");

				if (isLinhaVazia || isInvalidComModulo || isInvalidSemModulo) {
					this.props.warningMessage('Para cadastrar uma regra, todas as informações devem ser preenchidas!');
					return false;
				}
			}
		}
		return true;
	}

	getCampoServicoByCodigo(codigoCampo) {
		for (var indexCampo in this.props.configuracao.camposCarga) {
			var campo = this.props.configuracao.camposCarga[indexCampo];
			if (campo.codigo == codigoCampo) {
				return campo;
			}
		}
		return "";
	}

	clear () {
		this.state.regras.length = 0;
		this.refs.formValidator.clearValues();

		var newState = update(this.state, {showErrors: {$set: false}});
		this.setState(newState);
	}

	render(){
	    
		return (
			<div className="cia-animation-fadein">
    			<div className="row">
    				
					<div className="col-lg-12">
						<div className="panel panel-default" id="cadPanel">
							<div className="panel-body">
								<FormValidator ref="formValidator">
									
									<RegrasMultiplesInputs 
											ref="addRemoveMultiplesInputs"
											showError={this.state.showErrors} 
											configuracao={this.props.configuracao}/>

								</FormValidator>
							</div>
						</div>
					</div>
    			</div>
    		</div>
		);
	}
}