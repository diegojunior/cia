import React, { Component } from 'react';
import {CSVLink} from 'react-csv';

export default class CSV extends Component {
    
        constructor(props) {
            super(props);
        }
    
        render(){
    
            return (
                <CSVLink id={this.props.id}
                    filename={this.props.filename}
                    separator={this.props.separator}
                    data={this.props.data}
                    className="btn btn-social btn-info">
                        <i className="glyphicon glyphicon-open"></i> {this.props.label}
                </CSVLink>
            );
        }
    }