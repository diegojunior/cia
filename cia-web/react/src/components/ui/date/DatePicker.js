import React, { Component } from 'react';
import ReactDatePicker from  './ReactBootstrapDatePicker';
import Moment from 'moment';
import update from 'immutability-helper';
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import {required, integerOnly} from '../../../shared/util/Rules';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import InputError from "../input/InputError";
import './Style.css';


var self;
export default class DatePicker extends ValidatorComponent {

	constructor (props) {
		super (props);
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
		self = this;
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.clearValue && nextProps.selectedValue && nextProps.selectedValue != '') {
			this.setState({value: nextProps.selectedValue});
		} else if (nextProps.clearValue) {
			this.clear();
		}
		self = this;
    }

    handlerField (data, formattedValue) {
    	let newState = update(this.state, {value: {$set: data}, formattedValue: {$set: formattedValue}});
        newState.validationErrors = run(newState, this.state.fieldValidations);

        if ($.isEmptyObject(newState.validationErrors)) {
            this.changeParent(data);
        }

        if (this.props.addError) {
            this.props.addError(this.props.id, newState.validationErrors);    
        }

        this.setState(newState);
	}
	
	handlerFieldOnBlur () {		
		if (self.props.valueOnBlur) {
			self.props.valueOnBlur();
		}
	}

    monthLabels () {
    	return [
    			'Janeiro', 'Fevereiro', 'Março', 'Abril',
    			'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro',
    			'Outubro', 'Novembro', 'Dezembro'];
    }

    dayLabels () {
    	return ['Dom','Seg', 'Terç', 'Qua', 'Qui', 'Sex', 'Sáb'];
    }

    render() {
		var label = this.props.label && this.props.label != '' ? 
			<label className="control-label">{this.props.label}</label> :
			'';

		var noRow = !this.props.noRow || this.props.noRow == false ? 'row' : '';

		return (

			<div className={this.props.required && label != ''? "form-group required": "form-group"} >
				<div className={noRow}>
					<div className={this.props.className}>
						{label}
						<div className={this.props.uniqueName}>
							<ReactDatePicker
								id={this.props.id}
								placeholder="DD/MM/AAAA"
								monthLabels={this.monthLabels()}
								dayLabels={this.dayLabels()}
								disabled={this.props.readOnly}
								clearButtonElement="x"
								value={this.state.value}
								onChange={this.handlerField} 
								onBlur={this.handlerFieldOnBlur}/>
		
							<InputError 
								visible={this.shouldDisplayError()}
								errorMessage={this.errorFor('value')} />
		
						</div>
					</div>
				</div>
			</div>
		);
	}
}