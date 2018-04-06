import React, { Component } from 'react';
import { render } from 'react-dom';
import { Link } from 'react-router';

export default class Home extends Component {
    render(){
        return (
            <div className="cia-animation-fadein">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="page-header">
                            <Link to="/cia/home" className="fa fa-home"></Link>
                        </div>
                    </div>
                    <form role="form" name="form">
                        <div className="col-lg-12">
                            
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}