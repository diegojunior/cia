import React, { Component } from 'react';
import update from 'immutability-helper';
import Painel from './Painel';
import './Style.css';


export default class FormValidator extends Component {
	
	constructor(props) {
		super(props);
		this.state = {errors: [], clear: false};
		
		this.addError = this.addError.bind(this);
		this.removeError = this.removeError.bind(this);
		this.isValid = this.isValid.bind(this);

		this.clearValues = this.clearValues.bind(this);
	}

	componentDidUpdate (prevProps, prevState) {
		prevState.clear = false;
	}

	addRequiredError (key, label) {
		var validationErrors = label + ' é requerido';
		var found = this.state.errors.find(error => {
			return error.key === key;
		});
		if (!found) {
			this.state.errors.push({key: key, errors: validationErrors});
		}
	}

	addError (key, messages) {
		let newState = this.removeError(key);

		if (!$.isEmptyObject(messages)) {
			newState.errors = [...newState.errors, {key: key, errors: messages}];
			newState = update(newState, {clear: {$set: false}});
		}

		this.setState(newState);
	}

	removeError (key) {

		for (var index in this.state.errors) {
			if (this.state.errors[index].key == key) {
				this.state.errors.splice(index, 1);
				break;
			}
		}

		return update(this.state, {clear: {$set: false}});
	}

	clearValues () {
		this.setState({clear: true});
	}

	setClear (isClear) {
		this.setState({clear: isClear});
	}

	isValid(){
		return this.state.errors.length == 0;
	}

    render () {
    	const childrenWithProps = React.Children.map(this.props.children,
		     (child) => {		     	
		     	return React.cloneElement(child, {addError: this.addError, removeError: this.removeError, clearValue: this.state.clear});
		     	
		 });

	    return (<div>{childrenWithProps}</div>);
    }
}