import React, { Component } from 'react';
import { Link } from 'react-router';
import update from 'immutability-helper';
import FormValidator from '../../../../ui/form/FormValidator';
import TextInput from '../../../../ui/input/TextInput';
import './Style.css';

export default class Identificacao extends Component {

	constructor(props) {
		super(props);
		
		this.state = {
			identificacao: {codigo: '', descricao: '', tagRaiz: ''},

			showErrors: false
		};

		this.changeCodigo = this.changeCodigo.bind(this);
		this.changeDescricao = this.changeDescricao.bind(this);
		this.changeTagRaiz = this.changeTagRaiz.bind(this);

		this.showErrors = this.showErrors.bind(this);
		this.clear = this.clear.bind(this);
	}

	showErrors (isShowErrors) {
		this.setState({showErrors: isShowErrors});
	}

	isValidForm() {
		return this.refs.formValidator.isValid();
	}

	changeCodigo (value) {
		var newState = update(this.state, {identificacao: {codigo: {$set: value}}});
		this.setState(newState);
	}

	changeDescricao (value) {
		var newState = update(this.state, {identificacao: {descricao: {$set: value}}});
		this.setState(newState);
	}

	changeTagRaiz (value) {
		var newState = update(this.state, {identificacao: {tagRaiz: {$set: value}}});
		this.setState(newState);
	}

	formatValue (value) {
		return value.toUpperCase();
	}

	clear () {
		this.state.identificacao.codigo = '';
		this.state.identificacao.descricao = '';
		this.state.identificacao.tagRaiz = '';

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
									
									<TextInput
										id="codigo" 
										ref="codigo"
										type="text"
										label="Código"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="30"
										required={true}
										chave={true}
										formatValue={this.formatValue}										
										showError={this.state.showErrors}
										valueChange={this.changeCodigo} />

									<TextInput
										id="descricao" 
										ref="descricao"
										type="text"
										label="Descrição"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="60"
										showError={this.state.showErrors}
										valueChange={this.changeDescricao} />

									<TextInput
										id="tagRaiz" 
										ref="tagRaiz"
										type="text"
										label="Tag Raíz"
										className="col-sm-5 col-md-5 col-lg-5"
										maxLength="30"
										required={true}
										showError={this.state.showErrors}
										valueChange={this.changeTagRaiz} />

								</FormValidator>
							</div>
						</div>
					</div>
    			</div>
    		</div>
		);
	}
}