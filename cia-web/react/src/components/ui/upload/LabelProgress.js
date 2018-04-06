import React, { Component } from 'react';
import update from 'immutability-helper';


export default class LabelProgress extends Component {
    
    constructor(props) {
        super(props);

        this.state = {
            width: 0
        };
        
    }

    progress (width) {
        let newState = update(this.state, {width: {$set: width}})
        this.setState(newState);
    }

    render () {

        return(
            
            <div>
                <label id="progressLabel">{this.state.width} %</label>
            </div>
        );
    }
}