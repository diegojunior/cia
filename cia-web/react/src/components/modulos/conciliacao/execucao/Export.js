import React, { Component } from 'react';
import CSV from '../../../ui/export/CSV';
import {format} from '../../../../shared/util/Utils';

export default class Export extends Component {

	constructor(props) {
		super(props);
		this.state = {conciliacaoExport : {id: null}};

		this.disable = this.disable.bind(this);
	}

	componentWillReceiveProps (nextProps) {
		if (nextProps.conciliacao && 
			nextProps.conciliacao.id != '') {
			this.state.conciliacaoExport = nextProps.conciliacao;
		}
	}

	parseJsonToCSV () {	
		var csv = [];

		var lote = this.state.conciliacaoExport.lote;
		if (lote != null) {
			for (var indexUnidade in lote.unidades) {
				var unidade = lote.unidades[indexUnidade];
				var headerLote = lote.configuracaoPerfil.identificacao + ' - ' +  lote.configuracaoPerfil.servico;
				var json = 
					{'Data' : this.state.conciliacaoExport.data,
						'Sistema' : this.state.conciliacaoExport.perfil.identificacao.sistema.nome,
						'Layout' : this.state.conciliacaoExport.perfil.identificacao.layout.codigo,
						'Perfil' : this.state.conciliacaoExport.perfil.identificacao.codigo,
						'Lote' : headerLote};
				for(var indexChave in unidade.camposChave) {
					var campo = unidade.camposChave[indexChave];
					json[campo.nome] = campo.valor == null ? '' : format(campo.valor);
					if (campo.campoEquivalente != null) {
						json[campo.campoEquivalente] = campo.valorEquivalente == null ? '': format(campo.valorEquivalente);
					}
				}
				for(var indexConciliavel in unidade.camposConciliaveis) {
					var campo = unidade.camposConciliaveis[indexConciliavel];
					if (campo.campoEquivalente != null) {
						json[campo.nome + ' - ' + campo.campoEquivalente] = campo.valorEquivalente == null ? '': format(campo.valorEquivalente);
					}
					json[campo.nome + ' - Carga'] = campo.valorCarga == null ? '' : format(campo.valorCarga);
					json[campo.nome + ' - Importação'] = campo.valorImportacao == null ? '' : format(campo.valorImportacao);
					json[campo.nome + ' - Conciliação'] = campo.valorConciliacao == null ? '' : format(campo.valorConciliacao);
				}
				for(var indexInformativo in unidade.camposInformativos) {
					var campo = unidade.camposInformativos[indexInformativo];
					json[campo.nome] = campo.valor == null ? '' : format(campo.valor);
				}
				json['Status'] = unidade.status != null ? unidade.status.nome : '';
				csv.push(json);
			}
		}
		return csv;
	}

	disable () {
		return this.state.conciliacaoExport.id == null;
	}

    render () {

	    return <CSV id={"btnExportar"}
					label={"Exportar"}
					filename={"conciliacal.csv"}
					disable={this.disable()}
					separator={";"}
					data={this.parseJsonToCSV()}/>
    }
}