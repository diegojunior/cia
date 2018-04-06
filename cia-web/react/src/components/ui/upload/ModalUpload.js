import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import LabelProgress from './LabelProgress'

export default class ModalUpload extends Component {
    
    constructor(props) {
        super(props);

        this.state = {
            showModal: false
        };
        
        this.close = this.close.bind(this);        
    }

    show () {
        this.setState({showModal: true});
    }

    close () {
        this.setState({showModal: false});
    }

    progress (width) {
        let elem = $('#myBar')[0];
        elem.style.width = width + '%';
        this.refs.labelProgress.progress(width);
    }

    render () {

        return(

            <Modal className="modal-container" 
                show={this.state.showModal} 
                onHide={this.close}
                animation={true} 
                bsSize="large">

                <Modal.Header closeButton>
                    <Modal.Title>
                        <label className="control-label">Efetuando o upload do arquivo.</label>
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="alert alert-info">
                        <div id="myProgress">
                          <div id="myBar">
                            <LabelProgress ref="labelProgress"/>
                          </div>
                        </div>
                    </div>                                    
                </Modal.Body>

                <Modal.Footer>
                    <Button id="buttonOK" onClick={this.close} className="btn btn-info">OK</Button>                    
                </Modal.Footer>
            </Modal>
        );
    }
}