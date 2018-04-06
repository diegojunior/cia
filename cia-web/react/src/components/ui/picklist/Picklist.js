import React, { Component } from 'react';
import update from 'immutability-helper';
import FilteredMultiSelect from '../filteredmultiselect/FilteredMultiSelect';
import ValidatorComponent from '../../../shared/infra/ValidatorComponent';
import {required} from '../../../shared/util/Rules';
import {ruleRunner, run} from '../../../shared/util/RuleRunner';
import InputError from "../input/InputError";
import {objectAsSame} from '../../../shared/util/Utils';
import {sort} from '../../../shared/util/Utils';

export default class Picklist extends ValidatorComponent {
  
  constructor(props) {
	    super(props);
	    this.state.selectedOptions = [];
	    this.state.options = [];
	
	    this.deSelect = this.deSelect.bind(this);
	    this.select = this.select.bind(this);
  }

  componentWillMount() {
      if (this.props.required) {
        var labelValidator = this.props.labelValidator ? this.props.labelValidator : '';
        this.state.fieldValidations = [ruleRunner('selectedOptions', "Campo "+labelValidator, required)];
      }
      this.state.validationErrors = run(this.state, this.state.fieldValidations);
	}

	componentDidMount() {
      if (!$.isEmptyObject(this.state.validationErrors)) {
        this.props.addError(this.props.id, this.state.validationErrors);
      } 
	}

	componentWillReceiveProps (nextProps) {

      if (this.props.required) {
        var labelValidator = this.props.labelValidator ? this.props.labelValidator : '';
        this.state.fieldValidations = [ruleRunner('selectedOptions', "Campo "+labelValidator, required)];
      }

      this.state.validationErrors = run(this.state, this.state.fieldValidations);
      
      if (!nextProps.clearValue && !objectAsSame(this.state.options, nextProps.options, this.props.propToCheck, this.props.valueProp)) {
        if (this.props.propertyToSort) {
          sort(nextProps.options, this.props.propertyToSort); 
        }
        let newState = update(this.state, {options: {$set: nextProps.options}});
        this.setState(newState); 
      
      } else if (!nextProps.clearValue && !objectAsSame(this.state.options, nextProps.options, this.props.valueProp)) {
        if (this.props.propertyToSort) {
          sort(nextProps.options, this.props.propertyToSort); 
        }
        let newState = update(this.state, {options: {$set: nextProps.options}});
        this.setState(newState);  

      } else if (nextProps.clearValue && nextProps.selectedValue && 
          (!$.isEmptyObject(nextProps.selectedValue) || nextProps.selectedValue.length > 0)) {
        if (this.props.propertyToSort) {
          sort(nextProps.selectedValue, this.props.propertyToSort); 
        }
        let newState = update(this.state, {selectedOptions: {$set: nextProps.selectedValue}});
        newState.validationErrors = {};
        if (this.props.addError) {
            this.props.addError(this.props.id, newState.validationErrors);
        }

        this.setState(newState);

      } else if (nextProps.clearValue) {
        
        this.clear();
      
      }
      
  }

  clear () {
    
      this.state.selectedOptions = [];
      let newState = update(this.state, {options: {$set: []}});
      newState.validationErrors = run(newState, this.state.fieldValidations);

      if (!$.isEmptyObject(newState.validationErrors) && this.props.addError) {
          this.props.addError(this.props.id, newState.validationErrors);
      }

      this.setState(newState);
  }

  deSelect(deselectedOptions) {
	    var newSelectedOptions = this.state.selectedOptions.slice();
	    deselectedOptions.forEach(option => {
	      newSelectedOptions.splice(newSelectedOptions.indexOf(option), 1)
	    });
      this.select(newSelectedOptions);
  }

  select(options) {
      if (this.props.propertyToSort && options) {
        sort(options, this.props.propertyToSort); 
      }
      let newState = update(this.state, {selectedOptions: {$set: options}});
      newState.validationErrors = run(newState, this.state.fieldValidations);
      this.props.onChange(options);
      if (this.props.addError) {
          this.props.addError(this.props.id, newState.validationErrors);    
      }

	  	this.setState(newState);
  }

  clearFilter () {
    this.refs.filteredMultiSelect.state.filter = '';
  }

  render() {
    var label = this.props.label && this.props.label != '' ? 
      <label className="control-label">{this.props.label}</label> : '';
    
    var display = {'display': this.props.show == undefined || this.props.show ? 'block' : 'none'};

	  return (
            <div className={this.props.required && label != ''? "form-group required": "form-group"} style={display}>
                {label}
                <div className="row">
                    <div className={this.props.className}>
                        <FilteredMultiSelect
                          id={this.props.id+"_filtered_options"}
                          ref="filteredMultiSelect"
                          buttonText="Adicionar"
                          placeholder="Digite para filtrar"
                          classNames={{
                            filter: 'form-control',
                            select: 'form-control',
                            button: 'btn btn btn-block btn-default',
                            buttonActive: 'btn btn btn-block btn-primary'
                          }}
                          onChange={this.select}
                          onFilterChange={this.props.onFilterChange}
                          options={this.state.options}
                          selectedOptions={this.state.selectedOptions}
                          disabled={this.props.readOnly}
                          textProp={this.props.textProp}
										      valueProp={this.props.valueProp}
                          />
                    </div>
                    <div className={this.props.className}>
                        <FilteredMultiSelect
                          id={this.props.id+"_filtered_selected"}
                          buttonText="Remover"
                          placeholder="Digite para filtrar"
                          classNames={{
                            filter: 'form-control',
                            select: 'form-control',
                            button: 'btn btn btn-block btn-default',
                            buttonActive: 'btn btn btn-block btn-primary'
                          }}
                          onChange={this.deSelect}
                          options={this.state.selectedOptions}
                          disabled={this.props.readOnly}
                          textProp={this.props.textProp}
										      valueProp={this.props.valueProp}
                          />

                        <InputError 
                          visible={this.shouldDisplayError()}
                          errorMessage={this.errorFor('selectedOptions')} />
                    </div>

                </div>
            </div>
          
	  );
  }
}