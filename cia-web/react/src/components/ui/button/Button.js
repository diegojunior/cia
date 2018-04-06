import React, { Component } from 'react';
import './Style.css';

export default class Button extends Component {

	constructor (props) {
		super(props);
	}

	render () {
		var display = this.props.show == undefined || this.props.show ? this.props.className : 'notDisplayButton'
		return ( 
			
				<button 
					type="button" 
					className={display}
					id={this.props.id}
					disabled={this.props.disabled} 
					onClick={this.props.onClick}>
					
					<i className={this.props.classFigure}></i>{this.props.label}

				</button>
			
		);
	}
}