import React, { Component } from 'react';
import './Style.css';

export default class ButtonCrud extends Component {

	constructor (props) {
		super(props);
	}

	render () {

		var disableAlterar = this.props.disabledAlterar != undefined && this.props.disabledAlterar ? true : false;

		var classFigureClear = this.props.classFigureClear != undefined ? this.props.classFigureClear : 'fa fa-eraser';

		var espacoEntreBotoes = this.props.showIncluir || this.props.showAlterar || this.props.showConsultar ? <span>&nbsp;&nbsp;</span> : '';

		return (
			<div>
				<button type="button" className={this.props.showIncluir ? 'displayButton' + ' btn btn-social btn-success': 'notDisplayButton'} id="btnIncluir" onClick={this.props.incluir}><i className="fa fa-check"></i>Incluir</button>
				
				<button type="button" className={this.props.showAlterar ? 'displayButton' + ' btn btn-social btn-success': 'notDisplayButton'} id="btnAlterar" disabled={disableAlterar} onClick={this.props.alterar}><i className="fa fa-pencil-square-o"></i>Alterar</button>
				
				<button type="button" className={this.props.showConsultar ? 'displayButton' + ' btn btn-social btn-primary': 'notDisplayButton'} id="btnConsultar" onClick={this.props.consultar}><i className="fa fa-search"></i>Consultar</button>
				{espacoEntreBotoes}
				<button type="button" className='displayButton btn btn-social btn-default' id="btnCancelar" onClick={this.props.clear}><i className={classFigureClear}></i>{this.props.label}</button>
			</div>
		);
	}
}