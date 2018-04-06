import React, { Component } from 'react';
import update from 'immutability-helper';
import './Style.css';

export default class AddRemoveSingleInput extends Component {
	
	constructor(props) {
		super(props);
	}
	
	generateHeaders() {
		return this.props.cols.map((col, index) => {
				return <tr key={index}><th key={col.key}>{col.label}</th></tr>;
		});
	}
	
	generateRows() {
		return this.props.itens.map((item, index) => {
			var cells = this.props.cols.map((col) => {
				return <td key={index} id={index}><input type="text" value={item.codigo} className="form-control" maxLength="30" onChange={this.props.handlerField(index)} onBlur={this.props.onBlur(index)}/></td>
			});
			return <tr key={index} className="odd gradeX">{cells}</tr>
		});
	}
	
	render() {
		var headerComponents = this.generateHeaders();
		var rowComponents = this.generateRows();
		
		return (
			<div className="form-group">
				<div className="row">
					<div className={this.props.className}>

						<div className="limitAddRemoveSingleInput">	
							<button type="button" className="btn btn-" aria-label="Left Align" onClick={this.props.addInput}>
								<span className="glyphicon glyphicon-plus"></span>
							</button>
							&nbsp;
							<button type="button" className="btn btn-" aria-label="Left Align" onClick={this.props.removeInput}>
								<span className="glyphicon glyphicon-minus"></span>
							</button>
							<table className="table table-striped table-bordered table-hover" id="dataTables">
								<thead>{headerComponents}</thead>
								<tbody>{rowComponents}</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		);
	}	
}