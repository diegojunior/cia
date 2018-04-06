import React, { Component } from 'react';
import ModalMessage from '../../components/ui/modal/ModalMessage';

let modal = null;
export default class BaseComponent extends Component {
	
	constructor(props) {
	    super(props);
        this.state ={};
	}

    infoMessage (menssage) {
        modal.showInfo(menssage);
    }

    showNotificacao (menssage) {
        modal.showNotificacao(menssage);
    }

    warningMessage (menssage) {
        modal.showWarning(menssage);
    }

    successMessage (menssage) {
        modal.showSuccess(menssage);
    }

    errorMessage (menssage) {
        modal.showError(menssage);
    }

    confirmMessage (label, message, callbackMessage) {
        modal.showConfirmMessage(label, message, callbackMessage);
    }
	
    render () {
        return (<ModalMessage ref={(modalMessage) => {modal = modalMessage;}} />);
    }
}