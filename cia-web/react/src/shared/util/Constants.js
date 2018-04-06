import {sort} from './Utils';
export const constants = {

	ModuloEnum: [
		{constant: 'CARGA', enum: {codigo: 'CA', nome: 'Carga'}},
		{constant: 'IMPORTACAO', enum: {codigo: 'IM', nome: 'Importação'}}
	 ],

	 CondicaoEnum: [
		{constant: 'COMECA_COM', enum: {codigo: 'CC', nome: 'Começa Com'}},
		{constant: 'CONTEM', enum: {codigo: 'CO', nome: 'Contém'}},
		{constant: 'DIFERENTE', enum: {codigo: 'DI', nome: 'Diferente'}},
		{constant: 'IGUAL', enum: {codigo: 'IG', nome: 'Igual'}}
	 ],
	
	TipoValorDominioEnum: [
		  {constant: 'ALFANUMERICO', enum: {codigo: 'ALF', nome: 'Alfanumérico', noScale: 0, scale: 0, precisaoPadrao: 0}},
	      {constant: 'NUMERICO', enum: {codigo: 'NUM', nome: 'Numérico', noScale: 15, scale: 12, precisaoPadrao: 17}},
	      {constant: 'DATA', enum: {codigo: 'DAT', nome: 'Data', noScale: 0, scale: 0, precisaoPadrao: 0}}
	],

	FilesTypesEnum: [
		{constant: 'TXT', enum: {codigo: 'text/plain', nome: 'Txt'}},
		{constant: 'DAT', enum: {codigo: 'text/dat', nome: 'DAT'}},
		{constant: 'XML', enum: {codigo: 'text/xml', nome: 'Xml'}},
		{constant: 'CSV', enum: {codigo: 'application/vnd.ms-excel', nome: 'CSV'}}
	],

	SistemaEnum: [
	   {constant: 'AMPLIS', enum: {codigo: 'AMP', nome: 'AMPLIS'}}
	   /*{constant: 'JCOT', enum: {codigo: 'JCT', nome: 'JCOT'}}*/
	],

	StatusCargaEnum: [
	   {constant: 'ANDAMENTO', enum: {codigo: 'AN', nome: 'Em Andamento'}},
	   {constant: 'ERRO', enum: {codigo: 'ER', nome: 'Erro'}},
	   {constant: 'CONCLUIDO', enum: {codigo: 'CO', nome: 'Concluído'}},
	   {constant: 'NAO_EXECUTADO', enum: {codigo: 'NE', nome: 'Não Executado'}}
	],

	TipoExecucaoCargaEnum: [
	   {constant: 'CLIENTE', enum: {codigo: 'CL', nome: 'Cliente'}},
	   {constant: 'GRUPO', enum: {codigo: 'GR', nome: 'Grupo'}},
	   {constant: 'TODOS', enum: {codigo: 'TO', nome: 'Todos'}}
	],

	StatusImportacaoEnum: [
	   {constant: 'ANDAMENTO', enum: {codigo: 'AN', nome: 'Em Andamento'}},
	   {constant: 'ERRO', enum: {codigo: 'ER', nome: 'Erro'}},
	   {constant: 'CONCLUIDO', enum: {codigo: 'CO', nome: 'Concluído'}},
	   {constant: 'NAO_EXECUTADO', enum: {codigo: 'NE', nome: 'Não Executado'}}
	],

	StatusLoteCargaCarteiraEnum: [
	   {constant: 'SUCESSO', enum: {codigo: 'SU', nome: 'Sucesso'}},
	   {constant: 'ERRO', enum: {codigo: 'ER', nome: 'Erro'}},
	   {constant: 'ATENCAO', enum: {codigo: 'AT', nome: 'Atenção'}}
	],

	ServicoEnum: [
	   {constant: 'RENDAVARIAVEL_AVISTA', enum: {codigo: 'RVA', nome: 'Renda Variável À Vista'}},
	   {constant: 'RENDAVARIAVEL_OPCOES', enum: {codigo: 'RVO', nome: 'Renda Variável Opcões'}},
	   {constant: 'RENDAVARIAVEL_EMPRESTIMO', enum: {codigo: 'RVE', nome: 'Renda Variável Emprestimo'}},
	   {constant: 'RENDAVARIAVEL_TERMO', enum: {codigo: 'RVT', nome: 'Renda Variável Termo'}},
	   {constant: 'RENDAVARIAVEL_FUNDO_A_MERCADO', enum: {codigo: 'RVF', nome: 'Renda Variável Fundo À Mercado'}},
	   {constant: 'DERIVATIVOS_DISPONIVEL', enum: {codigo: 'DED', nome: 'Derivativos Disponível'}},
	   {constant: 'DERIVATIVOS_FUTUROS', enum: {codigo: 'DEF', nome: 'Derivativos Futuros'}},
	   {constant: 'RENDAFIXA_COMPROMISSADA', enum: {codigo: 'RFC', nome: 'Renda Fixa Compromissada'}},
	   {constant: 'RENDAFIXA_DEFINITIVA', enum: {codigo: 'RFD', nome: 'Renda Fixa Definitiva'}},
	   {constant: 'RENDAFIXA_TERMO', enum: {codigo: 'RFT', nome: 'Renda Fixa Termo'}},
	   {constant: 'SWAP', enum: {codigo: 'SWP', nome: 'Swap'}},
	   {constant: 'FUNDO_INVESTIMENTO', enum: {codigo: 'FUI', nome: 'Fundo Investimento'}},
	   {constant: 'PATRIMONIO', enum: {codigo: 'PTR', nome: 'Patrimonio'}},
	],

	TipoServicoEnum: [
	   {constant: 'POSICAO', enum: {codigo: 'POS', nome: 'Posição'}},
	   {constant: 'MOVIMENTO', enum: {codigo: 'MOV', nome: 'Movimento'}}
	],

	TipoLayoutEnum: [
	   {constant: 'POSICIONAL', enum: {codigo: 'POS', nome: 'Posicional'}, patternFile: ['text/plain', 'text/dat']},
	   {constant: 'DELIMITADOR', enum: {codigo: 'DEL', nome: 'Delimitador'}, patternFile: ['text/plain', 'text/dat', 'application/vnd.ms-excel']},
	   {constant: 'XML', enum: {codigo: 'XML', nome: 'XML'}, patternFile: ['text/xml']}
	],

	TipoTransformacaoEnum: [
	   {constant: 'FIXO', enum: {codigo: 'FIX', nome: 'Fixo'}},
	   {constant: 'EQUIVALENCIA', enum: {codigo: 'EQU', nome: 'Equivalência'}}	   
	],

	TipoImportacaoEnum: [
	   {constant: 'ARQUIVO', enum: {codigo: 'ARQ', nome: 'Arquivo'}}
	],

	TipoValidacaoEnum: [
	   {constant: 'ARQUIVO', enum: {codigo: 'ARQ', nome: 'Arquivo'}}
	],

	CampoValidacaoEnum: [
	   {constant: 'DATA', enum: {codigo: 'DAT', nome: 'Data'}},
	   {constant: 'CORRETORA', enum: {codigo: 'COR', nome: 'Corretora'}}
	],

	LocalValidacaoEnum: [
	   {constant: 'EXTERNO', enum: {codigo: 'EXT', nome: 'Externo'}},
	   {constant: 'INTERNO', enum: {codigo: 'INT', nome: 'Interno'}}
	],

	SimOuNaoEnum: [
       {constant: 'NAO', enum: {codigo: false, nome: 'Não'}},
       {constant: 'SIM', enum: {codigo: true, nome: 'Sim'}}
	],

	StatusEnum: [
	   {constant: 'ATIVO', enum: {codigo: 'ATV', nome: 'Ativo'}},
	   {constant: 'INATIVO', enum: {codigo: 'ITV', nome: 'Inativo'}}
	],

	TipoEquivalenciaEnum: [
		{constant: 'CORRETORA', enum: {codigo: '23', nome: 'Corretora'}}
	],

	LocalizacaoCamposEnum: [
	   {constant: 'SESSAO', enum: {codigo: 'SES', nome: 'Sessão do Layout'}},
	   {constant: 'UNIDADE', enum: {codigo: 'UNI', nome: 'Parametrização de Unidade'}}
	],

	StatusConciliacaoEnum: [
		{constant: 'ANDAMENTO', enum: {codigo: 'AN', nome: 'Em Andamento'}},
		{constant: 'DIVERGENTE', enum: {codigo: 'DI', nome: 'Divergente'}},
		{constant: 'OK', enum: {codigo: 'OK', nome: 'OK'}},
		{constant: 'ERRO', enum: {codigo: 'ER', nome: 'Erro'}},
		{constant: 'GRAVADA', enum: {codigo: 'GR', nome: 'Gravada'}},
		{constant: 'GRAVADA_DIVERGENCIA', enum: {codigo: 'GD', nome: 'Gravada com Divergências'}},
		{constant: 'NAO_EXECUTADO', enum: {codigo: 'NE', nome: 'Não Executado'}}
	 ],

	 TipoDelimitadorEnum: [
	   {constant: 'TABULAR', enum: {codigo: 'TAB', nome: 'Tabular'}},
	   {constant: 'PONTO_VIRGULA', enum: {codigo: 'PVA', nome: 'Ponto Vírgula'}},
	   {constant: 'VIRGULA', enum: {codigo: 'VIR', nome: 'Vírgula'}}
	],

	getModulosEnum () {
		var modulo = [];
		for (let index in this.ModuloEnum) {
			modulo[index] = this.ModuloEnum[index].enum;
		}
		return modulo;
	},

	getCondicoesEnum () {
		var condicoes = [];
		for (let index in this.CondicaoEnum) {
			condicoes[index] = this.CondicaoEnum[index].enum;
		}
		return condicoes;
	},
	
	getTiposValoresDominioEnum() {
		var tipos = [];
		for (let index in this.TipoValorDominioEnum) {
			tipos[index] = this.TipoValorDominioEnum[index].enum;
		}
		return tipos;
	},

	getFileTypesEnum() {
		var tipos = [];
		for (let index in this.FilesTypesEnum) {
			tipos[index] = this.FilesTypesEnum[index].enum;
		}
		return tipos;
	},

	getSistemasEnum () {
		var sistema = [];
		for (let index in this.SistemaEnum) {
			sistema[index] = this.SistemaEnum[index].enum;
		}
		return sistema;
	},

	getStatusConciliacoesEnum () {
		var status = [];
		for (let index in this.StatusConciliacaoEnum) {
			status[index] = this.StatusConciliacaoEnum[index].enum;
		}
		return status;
	},
	
	getStatusCargasEnum () {
		var status = [];
		for (let index in this.StatusCargaEnum) {
			status[index] = this.StatusCargaEnum[index].enum;
		}
		return status;
	},

	getTipoExecucaoCargaEnum () {
		var status = [];
		for (let index in this.TipoExecucaoCargaEnum) {
			status[index] = this.TipoExecucaoCargaEnum[index].enum;
		}
		return status;
	},

	getStatusImportacaoEnum () {
		var status = [];
		for (let index in this.StatusImportacaoEnum) {
			status[index] = this.StatusImportacaoEnum[index].enum;
		}
		return status;
	},

	getStatusLoteCargaCarteiraEnum () {
		var status = [];
		for (let index in this.StatusLoteCargaCarteiraEnum) {
			status[index] = this.StatusLoteCargaCarteiraEnum[index].enum;
		}
		return status;
	},

	getServicosEnum () {
		var servicos = [];
		for (let index in this.ServicoEnum) {
			servicos[index] = this.ServicoEnum[index].enum;
		}
		return servicos;
	},

	getTiposServicosEnum () {
		var tiposServicos = [];
		for (let index in this.TipoServicoEnum) {
			tiposServicos[index] = this.TipoServicoEnum[index].enum;
		}
		return tiposServicos;
	},

	getTiposLayouts () {
		var tipos = [];
		sort(this.TipoLayoutEnum, 'enum.nome');
		for (let index in this.TipoLayoutEnum) {
			tipos[index] = this.TipoLayoutEnum[index].enum;
		}
		return tipos;
	},

	getTiposTransformacoes () {
		var tipos = [];
		for (let index in this.TipoTransformacaoEnum) {
			tipos[index] = this.TipoTransformacaoEnum[index].enum;
		}
		return tipos;
	},

	getTiposValidacoes () {
		var tipos = [];
		for (let index in this.TipoValidacaoEnum) {
			tipos[index] = this.TipoValidacaoEnum[index].enum;
		}
		return tipos;
	},

	getCamposValidacoes () {
		var campos = [];
		for (let index in this.CampoValidacaoEnum) {
			campos[index] = this.CampoValidacaoEnum[index].enum;
		}
		return campos;
	},

	getLocaisValidacoes () {
		var locais = [];
		for (let index in this.LocalValidacaoEnum) {
			locais[index] = this.LocalValidacaoEnum[index].enum;
		}
		return locais;
	},

	getLocalizacoesCampos () {
		var locais = [];
		for (let index in this.LocalizacaoCamposEnum) {
			locais[index] = this.LocalizacaoCamposEnum[index].enum;
		}
		return locais;
	},

	getTipoImportacao () {
		var tipo = [];
		for (let index in this.TipoImportacaoEnum) {
			tipo[index] = this.TipoImportacaoEnum[index].enum;
		}
		return tipo;
	},

	getSimOuNao () {
		var opcao = [];
		for (let index in this.SimOuNaoEnum) {
			opcao[index] = this.SimOuNaoEnum[index].enum;
		}
		return opcao;
	},

	getStatus () {
		var status = [];
		for (let index in this.StatusEnum) {
			status[index] = this.StatusEnum[index].enum;
		}
		return status;
	},

	getTipoEquivalenciaEnum() {
		var tipos = [];
		for (let index in this.TipoEquivalenciaEnum) {
			tipos[index] = this.TipoEquivalenciaEnum[index].enum;
		}
		return tipos;
	},

	getTipoDelimitadorEnum() {
		var tipos = [];
		sort(this.TipoDelimitadorEnum, 'enum.nome');
		for (let index in this.TipoDelimitadorEnum) {
			tipos[index] = this.TipoDelimitadorEnum[index].enum;
		}
		return tipos;
	},

	getTipoValorDominioPorCodigo (codigo) {
		for (let index in this.TipoValorDominioEnum) {
			if (this.TipoValorDominioEnum[index].enum.codigo == codigo) {
				return this.TipoValorDominioEnum[index].enum;
			}
		}
		return null;
	},

	getModuloPorCodigo (codigo) {
		for (let index in this.ModuloEnum) {
			if (this.ModuloEnum[index].enum.codigo == codigo) {
				return this.ModuloEnum[index].enum;
			}
		}
		return null;
	},

	getCondicaoPorCodigo (codigo) {
		for (let index in this.CondicaoEnum) {
			if (this.CondicaoEnum[index].enum.codigo == codigo) {
				return this.CondicaoEnum[index].enum;
			}
		}
		return null;
	},

	getSistemasPorCodigo (codigo) {
		for (let index in this.SistemaEnum) {
			if (this.SistemaEnum[index].enum.codigo == codigo) {
				return this.SistemaEnum[index].enum;
			}
		}
		return null;
	},

	getStatusConciliacaoPorCodigo (codigo) {
		for (let index in this.StatusConciliacaoEnum) {
			if (this.StatusConciliacaoEnum[index].enum.codigo == codigo) {
				return this.StatusConciliacaoEnum[index].enum;
			}
		}
		return null;
	},

	getStatusCargaPorCodigo (codigo) {
		for (let index in this.StatusCargaEnum) {
			if (this.StatusCargaEnum[index].enum.codigo == codigo) {
				return this.StatusCargaEnum[index].enum;
			}
		}
		return null;
	},

	getTipoExecucaoCargaPorCodigo (codigo) {
		for (let index in this.TipoExecucaoCargaEnum) {
			if (this.TipoExecucaoCargaEnum[index].enum.codigo == codigo) {
				return this.TipoExecucaoCargaEnum[index].enum;
			}
		}
		return null;
	},

	getStatusImportacaoPorCodigo (codigo) {
		for (let index in this.StatusImportacaoEnum) {
			if (this.StatusImportacaoEnum[index].enum.codigo == codigo) {
				return this.StatusImportacaoEnum[index].enum;
			}
		}
		return null;
	},

	getStatusLoteCargaCarteiraPorCodigo (codigo) {
		for (let index in this.StatusLoteCargaCarteiraEnum) {
			if (this.StatusLoteCargaCarteiraEnum[index].enum.codigo == codigo) {
				return this.StatusLoteCargaCarteiraEnum[index].enum;
			}
		}
		return null;
	},

	getServicoPorCodigo (codigo) {
		for (let index in this.ServicoEnum) {
			if (this.ServicoEnum[index].enum.codigo == codigo) {
				return this.ServicoEnum[index].enum;
			}
		}
		return null;
	},

	getTipoServicoPorCodigo (codigo) {
		for (let index in this.TipoServicoEnum) {
			if (this.TipoServicoEnum[index].enum.codigo == codigo) {
				return this.TipoServicoEnum[index].enum;
			}
		}
		return null;
	},

	getTipoImportacaoPorCodigo (codigo) {
		for (let index in this.TipoImportacaoEnum) {
			if (this.TipoImportacaoEnum[index].enum.codigo == codigo) {
				return this.TipoImportacaoEnum[index].enum;
			}
		}
		return null;
	},

	getTipoLayoutPorCodigo (codigo) {
		for (let index in this.TipoLayoutEnum) {
			if (this.TipoLayoutEnum[index].enum.codigo == codigo) {
				return this.TipoLayoutEnum[index].enum;
			}
		}
		return null;
	},

	getTipoTransformacaoPorCodigo (codigo) {
		for (let index in this.TipoTransformacaoEnum) {
			if (this.TipoTransformacaoEnum[index].enum.codigo == codigo) {
				return this.TipoTransformacaoEnum[index].enum;
			}
		}
		return null;
	},

	getTipoValidacaoPorCodigo (codigo) {
		for (let index in this.TipoValidacaoEnum) {
			if (this.TipoValidacaoEnum[index].enum.codigo == codigo) {
				return this.TipoValidacaoEnum[index].enum;
			}
		}
		return null;
	},

	getCampoValidacaoPorCodigo (codigo) {
		for (let index in this.CampoValidacaoEnum) {
			if (this.CampoValidacaoEnum[index].enum.codigo == codigo) {
				return this.CampoValidacaoEnum[index].enum;
			}
		}
		return null;
	},

	getLocalValidacaoPorCodigo (codigo) {
		for (let index in this.LocalValidacaoEnum) {
			if (this.LocalValidacaoEnum[index].enum.codigo == codigo) {
				return this.LocalValidacaoEnum[index].enum;
			}
		}
		return null;
	},

	getLocalizacoesCamposPorCodigo (codigo) {
		for (let index in this.LocalizacaoCamposEnum) {
			if (this.LocalizacaoCamposEnum[index].enum.codigo == codigo) {
				return this.LocalizacaoCamposEnum[index].enum;
			}
		}
		return null;
	},

	getSimOuNaoCodigo (codigo) {
		for (let index in this.SimOuNaoEnum) {
			if (this.SimOuNaoEnum[index].enum.codigo.toString() == codigo) {
				return this.SimOuNaoEnum[index].enum;
			}
		}
		return null;
	},

	getStatusCodigo (codigo) {
		for (let index in this.StatusEnum) {
			if (this.StatusEnum[index].enum.codigo == codigo) {
				return this.StatusEnum[index].enum;
			}
		}
		return null;
	},

	getTipoEquivalenciaCodigo (codigo) {
		for (let index in this.TipoEquivalenciaEnum) {
			if (this.TipoEquivalenciaEnum[index].enum.codigo == codigo) {
				return this.TipoEquivalenciaEnum[index].enum;
			}
		}
		return null;
	},

	getConstantTipoValorDominioPorCodigo (codigo) {
		for (let index in this.TipoValorDominioEnum) {
			if (this.TipoValorDominioEnum[index].enum.codigo == codigo) {
				return this.TipoValorDominioEnum[index].constant;
			}
		}
		return null;
	},

	getConstantModuloPorCodigo (codigo) {
		for (let index in this.ModuloEnum) {
			if (this.ModuloEnum[index].enum.codigo == codigo) {
				return this.ModuloEnum[index].constant;
			}
		}
		return null;
	},

	getConstantCondicaoPorCodigo (codigo) {
		for (let index in this.CondicaoEnum) {
			if (this.CondicaoEnum[index].enum.codigo == codigo) {
				return this.CondicaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantSistemasPorCodigo (codigo) {
		for (let index in this.SistemaEnum) {
			if (this.SistemaEnum[index].enum.codigo == codigo) {
				return this.SistemaEnum[index].constant;
			}
		}
		return null;
	},

	getConstantStatusConciliacaoPorCodigo (codigo) {
		for (let index in this.StatusConciliacaoEnum) {
			if (this.StatusConciliacaoEnum[index].enum.codigo == codigo) {
				return this.StatusConciliacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantStatusCargaPorCodigo (codigo) {
		for (let index in this.StatusCargaEnum) {
			if (this.StatusCargaEnum[index].enum.codigo == codigo) {
				return this.StatusCargaEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoExecucaoCargaPorCodigo (codigo) {
		for (let index in this.TipoExecucaoCargaEnum) {
			if (this.TipoExecucaoCargaEnum[index].enum.codigo == codigo) {
				return this.TipoExecucaoCargaEnum[index].constant;
			}
		}
		return null;
	},

	getConstantStatusImportacaoPorCodigo (codigo) {
		for (let index in this.StatusImportacaoEnum) {
			if (this.StatusImportacaoEnum[index].enum.codigo == codigo) {
				return this.StatusImportacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantStatusLoteCargaCarteiraPorCodigo (codigo) {
		for (let index in this.StatusLoteCargaCarteiraEnum) {
			if (this.StatusLoteCargaCarteiraEnum[index].enum.codigo == codigo) {
				return this.StatusLoteCargaCarteiraEnum[index].constant;
			}
		}
		return null;
	},

	getConstantServicoPorCodigo (codigo) {
		for (let index in this.ServicoEnum) {
			if (this.ServicoEnum[index].enum.codigo == codigo) {
				return this.ServicoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoServicoPorCodigo (codigo) {
		for (let index in this.TipoServicoEnum) {
			if (this.TipoServicoEnum[index].enum.codigo == codigo) {
				return this.TipoServicoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoLayoutPorCodigo (codigo) {
		for (let index in this.TipoLayoutEnum) {
			if (this.TipoLayoutEnum[index].enum.codigo == codigo) {
				return this.TipoLayoutEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoTransformacaoPorCodigo (codigo) {
		for (let index in this.TipoTransformacaoEnum) {
			if (this.TipoTransformacaoEnum[index].enum.codigo == codigo) {
				return this.TipoTransformacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoValidacaoPorCodigo (codigo) {
		for (let index in this.TipoValidacaoEnum) {
			if (this.TipoValidacaoEnum[index].enum.codigo == codigo) {
				return this.TipoValidacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantCampoValidacaoPorCodigo (codigo) {
		for (let index in this.CampoValidacaoEnum) {
			if (this.CampoValidacaoEnum[index].enum.codigo == codigo) {
				return this.CampoValidacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantLocalValidacaoPorCodigo (codigo) {
		for (let index in this.LocalValidacaoEnum) {
			if (this.LocalValidacaoEnum[index].enum.codigo == codigo) {
				return this.LocalValidacaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantLocalizacoesCamposPorCodigo (codigo) {
		for (let index in this.LocalizacaoCamposEnum) {
			if (this.LocalizacaoCamposEnum[index].enum.codigo == codigo) {
				return this.LocalizacaoCamposEnum[index].constant;
			}
		}
		return null;
	},

	getConstantSimOuNaoPorCodigo (codigo) {
		for (let index in this.SimOuNaoEnum) {
			if (this.SimOuNaoEnum[index].enum.codigo.toString() == codigo) {
				return this.SimOuNaoEnum[index].constant;
			}
		}
		return null;
	},

	getConstantStatusCodigo (codigo) {
		for (let index in this.StatusEnum) {
			if (this.StatusEnum[index].enum.codigo == codigo) {
				return this.StatusEnum[index].constant;
			}
		}
		return null;
	},

	getConstantTipoEquivalenciaCodigo (codigo) {
		for (let index in this.TipoEquivalenciaEnum) {
			if (this.TipoEquivalenciaEnum[index].enum.codigo == codigo) {
				return this.TipoEquivalenciaEnum[index].constant;
			}
		}
		return null;
	},

	getPatternFileTipoLayoutPorCodigo (codigo) {
		for (let index in this.TipoLayoutEnum) {
			if (this.TipoLayoutEnum[index].enum.codigo == codigo) {
				return this.TipoLayoutEnum[index].patternFile;
			}
		}
		return null;
	},

	getTipoDelimitadorPorCodigo (codigo) {
		for (let index in this.TipoDelimitadorEnum) {
			if (this.TipoDelimitadorEnum[index].enum.codigo == codigo) {
				return this.TipoDelimitadorEnum[index].enum;
			}
		}
		return null;
	},

	getConstantTipoDelimitadorCodigo (codigo) {
		for (let index in this.TipoDelimitadorEnum) {
			if (this.TipoDelimitadorEnum[index].enum.codigo == codigo) {
				return this.TipoDelimitadorEnum[index].constant;
			}
		}
		return null;
	}
}