import React, { Component } from 'react';

export default class Label extends Component {
	
	constructor(props) {
		super(props);
	}

    render () {
	    return (
	    	<label className="control-label">{this.props.value}</label>
		);
    }
}