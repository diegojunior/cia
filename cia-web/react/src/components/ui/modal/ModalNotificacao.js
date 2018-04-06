import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

export default class ModalNotificacao extends Component {
    
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
                bsSize="large">

                <Modal.Header closeButton>
                    <Modal.Title>
                        <label className="control-label">Notificação</label>
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <div className="alert alert-info">
                        <div dangerouslySetInnerHTML={{__html:this.state.message}} />
                    </div>                                    
                </Modal.Body>

                <Modal.Footer>
                    <Button id="buttonOK" onClick={this.close} className="btn btn-info">OK</Button>                    
                </Modal.Footer>
            </Modal>
        );
    }
}