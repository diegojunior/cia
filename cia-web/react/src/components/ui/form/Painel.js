import React, { Component } from 'react';

export default class Painel extends Component {
	
	constructor(props) {
		super(props);
	}

    render () {

    	const childrenWithProps = React.Children.map(this.props.children,
		     (child) => {
		     	return React.cloneElement(child, {addError: this.props.addError});
		    });
    	
	    return (
	    	<div className="col-sm-12 col-md-12 col-lg-12">
				<div className="col-sm-12 col-md-12 col-lg-12">
		    		<div className="panel panel-primary">
						<div className="panel-heading">
							{this.props.label}
						</div>
						<div className="panel-body">
							<div className={this.props.cssName}>
					
								{childrenWithProps}

							</div>
						</div>
					</div>
				</div>
			</div>
		);
    }
}