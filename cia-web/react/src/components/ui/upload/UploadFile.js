import React, {Component} from 'react';
import PropTypes from 'prop-types';
import Http from '../../../shared/http/Http';
import Importacao from '../../../shared/services/Importacao';
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import update from 'immutability-helper';
import {required, integerOnly} from '../../../shared/util/Rules';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import InputError from "../input/InputError";
import FileException from './FileException';

import ModalUpload from './ModalUpload';

import './Style.css';

export default class UploadFile extends ValidatorComponent {

	constructor (props) {
		super(props);
		this.state.id = '';
		this.handlerUploadFile = this.handlerUploadFile.bind(this);
	}

	componentWillMount () {

		if (this.props.required) {
			this.state.fieldValidations = [ruleRunner("value", this.props.label, required)];
		}

		this.state.validationErrors = run(this.state, this.state.fieldValidations);
		
	}

	componentDidMount() {
		
		if (!$.isEmptyObject(this.state.validationErrors) && this.props.addError) {
			this.props.addError(this.props.id, this.state.validationErrors);
		}

	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.clearValue && 
			nextProps.selectedValue && 
			(!$.isEmptyObject(nextProps.selectedValue) || nextProps.selectedValue > 0)) {
			
			let newState = update(this.state, {value: {$set: nextProps.selectedValue}});
            newState.validationErrors = {};

			if (nextProps.addError) {
				nextProps.addError(nextProps.id, newState.validationErrors);
			}

			this.setState(newState);
		} else if (nextProps.clearValue) {
			this.state.id = '';
			this.clear();
		}
    }

   	handlerUploadFile () {

		return (e) => {
			e.preventDefault();
			let reader = new FileReader();
			let file = e.target.files[0];
			//Limpando o conteudo do component para que possa efetuar upload do msmo arquivo
			e.target.value = null;
			var react = this;
			try {
				this.validateFileExtention(file.type, file.name);
				this.props.validateFile(file);
				this.refs.modalUpload.show();
				var width = 0;
				var id = null;
				
				reader.onprogress = function (data) {
					if (data.lengthComputable) {
						var progress = parseInt(((data.loaded / data.total) *100), 10);
						id = setInterval(function () {
							if (width >= 100) {
						      clearInterval(id);
						    } else {
						    	width = width + progress;
						    	if (width >= 100) {
						    		width = 100;	
						    	}
						      	react.refs.modalUpload.progress(width);
						    }
						}, 5);
					}
				}.bind(this);

				reader.onloadend = function () {
					
					Importacao
						.incluirArquivo(file, this.state.id)
						.then(data => {
								let newState = update(this.state, {value: {$set: data.fileName}, id: {$set: data.id}});
					            newState.validationErrors = run(newState, this.state.fieldValidations);
					            if ($.isEmptyObject(newState.validationErrors)) {
					                this.changeParent(data);
					            }

					            if (this.props.addError) {
					                this.props.addError(this.props.id, newState.validationErrors);    
					            }

					            this.setState(newState);
							}).catch(error => {
								this.props.handlerErrorMessage(error)
							});


				}.bind(this);

				reader.readAsDataURL(file);
			} catch (e) {
				this.clear();
				this.props.handlerErrorMessage(e.message);
			}
		}
	}

	validateFileExtention (type, fileName) {
		let valid = true;
		if (!type) {
			if (fileName.toUpperCase().endsWith(".DAT")) {
				valid = this.props.fileType('text/dat');
			}
		} else {
			valid = this.props.fileType(type);
		}
		if (!valid) throw new FileException('Arquivo inválido!'); 
	}

	close() {
        this.setState({showModal: false});
    }

	render () {
		var label = this.props.label && this.props.label != '' ? 
			<label className="control-label">{this.props.label}</label> :
			'';
		
		return (

			<div className={this.props.required && label != ''? "form-group required": "form-group"} >
				<div className="row">
					<div className={this.props.className}>
		       			<label className="control-label">{label}</label>
		       			<div className="form-inline">
	     				<input type="text" id={this.props.id} readOnly={this.props.readOnly} className="form-control limitWidth" value={this.state.value} />
		     			<span className="button btn btn-social btn-primary btn-file">
							<i className="glyphicon glyphicon-folder-open"></i>Diretório <input id="idFile" type="file" disabled={this.props.readOnly} onChange={this.handlerUploadFile()} />
						</span>
						</div>
	     				
	     				<InputError 
							visible={this.shouldDisplayError()}
							errorMessage={this.errorFor('value')} />	
		     			
			        </div>
		        </div>
		        <ModalUpload ref="modalUpload"/>

			</div>
		);
	}

}

UploadFile.propTypes = {
	fileType: PropTypes.func.isRequired,
	validateFile: PropTypes.func.isRequired
}
