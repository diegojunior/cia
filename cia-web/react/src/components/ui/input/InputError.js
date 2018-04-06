import React from 'react';
import PropTypes from 'prop-types';

export default class InputError extends React.Component {

	constructor (props) {
		super(props);
		this.state = {message: 'Campo requerido!'};
	}

	selectClass () {

		var errorClass = {
			'visible': this.props.visible,
			'invisible': !this.props.visible
		};

		var cssClass = null;
		for (let _class in errorClass) {
			if (errorClass[_class] === true) {
				cssClass = _class;		
			}
		}
		return cssClass;
	}

	render() {
		return (
			<div className={this.selectClass()}>
				<span className="errorMessage">{this.props.errorMessage}</span>
			</div>
		);
	}
}

InputError.propTypes = {
	visible: PropTypes.bool,
	errorMessage: PropTypes.string
}
