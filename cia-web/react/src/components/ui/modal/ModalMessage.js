import React, { Component } from 'react';
import ModalInfo from './ModalInfo';
import ModalSuccess from './ModalSuccess';
import ModalWarning from './ModalWarning';
import ModalError from './ModalError';
import ModalConfirm from './ModalConfirm';
import ModalNotificacao from './ModalNotificacao';
import update from 'immutability-helper';

export default class ModalMessage extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            callbackFunction: null
        }
    }

    showInfo(message) {
        this.refs.modalInfo.show(message);
    }

    showNotificacao(message) {
        this.refs.modalNotificacao.show(message);
    }

    showSuccess(message) {
        this.refs.modalSuccess.show(message);
    }

    showWarning(message) {
        this.refs.modalWarning.show(message);
    }

    showError(message) {
        this.refs.modalError.show(message);
    }

    showConfirmMessage (label, message, callbackFunction) {
        let newState = update(this.state, {callbackFunction: {$set: callbackFunction}});
        this.refs.modalConfirmacao.show(label, message);
        this.setState(newState);
    }

    render() {
        return(
            <div>
                <ModalInfo ref="modalInfo" />
                <ModalSuccess ref="modalSuccess" />
                <ModalWarning ref="modalWarning" />
                <ModalError ref="modalError" />
                <ModalNotificacao ref="modalNotificacao" />
                <ModalConfirm ref="modalConfirmacao" callbackFunction={this.state.callbackFunction} />
            </div>
        );
    }
}