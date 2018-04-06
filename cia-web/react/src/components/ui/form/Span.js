import React, { Component } from 'react';

export default class Span extends Component {
	
	constructor(props) {
		super(props);
	}

    render () {
	    return (
	    	<span className={this.props.className} style={this.props.styleName}>{this.props.children}</span>
		);
    }
}