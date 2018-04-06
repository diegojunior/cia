import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

export default class ModalSuccess extends Component {
    
    constructor(props) {
        super(props);

        this.state = {
            showModal: false,
            message: ''
        };
        
        this.close = this.close.bind(this);        
    }

    show(message) {
        this.setState({showModal: true, message: message});
    }

    close() {
        this.setState({showModal: false});
    }

    render() {
        return(
            <Modal className="modal-container" 
                show={this.state.showModal} 
                onHide={this.close}
                animation={true} 
                bsSize="small">

                <Modal.Header closeButton>
                    <Modal.Title>
                        <label className="control-label">Sucesso</label>
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="alert alert-success">
                        {this.state.message}
                    </div>                                    
                </Modal.Body>

                <Modal.Footer>
                    <Button id="buttonOK" onClick={this.close} className="btn btn-success">OK</Button>                    
                </Modal.Footer>
            </Modal>
        );
    }
}