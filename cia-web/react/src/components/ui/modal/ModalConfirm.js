import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

export default class ModalConfirm extends Component {
    
    constructor(props) {
        super(props);

        this.state = {
            showModal: false,
            estadoConfirmacao: false,
            label: '',
            message: ''
        };
        this.close = this.close.bind(this);
        this.sim = this.sim.bind(this);
        this.nao = this.nao.bind(this);
    }

    show (label, message) {
        this.setState({showModal: true, message: message, label: label});
    }

    sim () {
        this.setState({showModal: false});
        this.props.callbackFunction();
    }

    nao () {
        this.setState({showModal: false});
    }

    confirmacao () {
        return this.state.estadoConfirmacao;
    }

    close() {
        this.setState({showModal: false});
    }

    render() {
        return(
            <Modal 
                className="modal-container" 
                show={this.state.showModal} 
                onHide={this.close}
                animation={true} 
                bsSize="small">

                <Modal.Header closeButton>
                    <Modal.Title>
                        <label className="control-label">{this.state.label}</label>
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>                    
                    <div className="alert alert-info">
                        {this.state.message}
                    </div>    
                </Modal.Body>

                <Modal.Footer>
                    <Button id="buttonSim" onClick={this.sim} className="btn btn-primary">Sim</Button> 
                    <Button id="buttonNao" onClick={this.nao} className="btn">Não</Button>                                       
                </Modal.Footer>
            </Modal>
        );
    }
}