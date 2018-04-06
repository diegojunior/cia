import React, { Component } from 'react';

export default class Div extends Component {
	
	constructor(props) {
		super(props);
	}

    render () {
	    return (
	    	<div className={this.props.className} style={this.props.styleName}>{this.props.children}</div>
		);
    }
}