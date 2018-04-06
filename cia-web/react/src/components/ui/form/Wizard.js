import React, { Component } from 'react';

export default class Wizard extends Component {
	
	constructor(props) {
		super(props);

		this.clear = this.clear.bind(this);
	}

	componentDidUpdate (prevProps, prevState) {
		var self = this;
		
		$('#wizard').smartWizard();

		$('.buttonCancel').addClass('btn btn-default');
		$('.buttonNext').addClass('btn btn-primary');
		$('.buttonPrevious').addClass('btn btn-info');
		$('.buttonFinish').addClass('btn btn-success');

		var buttonNextEventsClick = $.extend( true, {}, $._data($(".buttonNext")[0], "events").click );
		$('.buttonNext').off('click');
		var buttonFinishEventsClick = $.extend( true, {}, $._data($(".buttonFinish")[0], "events").click );
		$('.buttonFinish').off('click');

		this.clickNext(self, prevProps, buttonNextEventsClick);
		this.clickPrevious(self, prevProps);
		this.clickFinish(self, prevProps);
		this.clickCancel(self, prevProps);
		
	}

	clickNext(self, prevProps, buttonNextEvents) {
		$('.buttonNext').on('click', function (event) {
			event.preventDefault();

			var isValid = false;

			if ($('a[href="#step-1"]').hasClass('selected')){
				if (self.refs.step1.showErrors) {
					self.refs.step1.showErrors(true);
				}
				
				if (self.refs.step1.isValidForm == undefined || self.refs.step1.isValidForm()) {
					isValid = true;
				}
			}

			if ($('a[href="#step-2"]').hasClass('selected')){
				if (self.refs.step2.showErrors) {
					self.refs.step2.showErrors(true);
				}
				
				if (self.refs.step2.isValidForm == undefined || self.refs.step2.isValidForm()) {
					isValid = true;
				}
			}

			if ($('a[href="#step-3"]').hasClass('selected')){
				if (self.refs.step3.showErrors) {
					self.refs.step3.showErrors(true);
				}
				
				if (self.refs.step3.isValidForm == undefined || self.refs.step3.isValidForm()) {
					isValid = true;
				}
			}

			if (prevProps.numberOfSteps && prevProps.numberOfSteps == 4) {
				if ($('a[href="#step-4"]').hasClass('selected')){
					if (self.refs.step4.showErrors) {
						self.refs.step4.showErrors(true);
					}
					
					if (self.refs.step4.isValidForm == undefined || self.refs.step4.isValidForm()) {
						isValid = true;
					}
				}
			}

			if (isValid) {
				if (prevProps.next) {
					if (prevProps.numberOfSteps && prevProps.numberOfSteps == 4) {
						prevProps.next(self.refs.step1.state, 
							self.refs.step2.state, 
							self.refs.step3.state,
						self.refs.step4.state);
					} else {
						prevProps.next(self.refs.step1.state, 
							self.refs.step2.state, 
							self.refs.step3.state);
					}
				}
				buttonNextEvents[0].handler(event);
			}
		});
	}

	clickPrevious(self, prevProps) {
		$('.buttonPrevious').on('click', function (event) {
			event.preventDefault();
			if (prevProps.previous){
				prevProps.previous();
			}
		});
	}

	clickFinish(self, prevProps) {

		$('.buttonFinish').on('click', function (event) {
			event.preventDefault();
			if (prevProps.finish) {
				if (prevProps.numberOfSteps && prevProps.numberOfSteps == 4){
					prevProps.finish(self.refs.step1.state, self.refs.step2.state, self.refs.step3.state, self.refs.step4.state)
						.then (data => {
							if (data == null || data == '') {
								if (self.refs.step1) {
									self.refs.step1.clear();
								}
								if (self.refs.step2) {
									self.refs.step2.clear();
								}
								if (self.refs.step3) {
									self.refs.step3.clear();
								}
								if (self.refs.step4) {
									self.refs.step4.clear();
								}
								$('.buttonCancel').click();
								return data;
							}
						}).catch (error => {
							console.log('WIZARD ', error);
							return error;
						});
				} else {
					prevProps.finish(self.refs.step1.state, self.refs.step2.state, self.refs.step3.state)
						.then (data => {
							if (data == null || data == '') {
								if (self.refs.step1) {
									self.refs.step1.clear();
								}
								if (self.refs.step2) {
									self.refs.step2.clear();
								}
								if (self.refs.step3) {
									self.refs.step3.clear();
								}
								$('.buttonCancel').click();
								return data;
							}
						}).catch (error => {
							console.log('WIZARD ', error);
							return error;
						});
				}		
			}
		});
	}

	clickCancel(self, prevProps) {
		$('.buttonCancel').on('click', function (event) {
			event.preventDefault();
			if (prevProps.cancel){
				
				if (self.refs.step1.showErrors) {
					self.refs.step1.showErrors(false);
				}

				if (self.refs.step2.showErrors) {
					self.refs.step2.showErrors(false);
				}

				if (self.refs.step3.showErrors) {
					self.refs.step3.showErrors(false);
				}

				if (prevProps.numberOfSteps && prevProps.numberOfSteps == 4){
					if (self.refs.step4.showErrors) {
						self.refs.step4.showErrors(false);
					}
				}

				prevProps.cancel();
			}
		});
	}

	clear () {
		if (this.refs.step1.clear) {
			this.refs.step1.clear();
		}
		if (this.refs.step2.clear) {
			this.refs.step2.clear();
		}
		if (this.refs.step3.clear) {
			this.refs.step3.clear();
		}
		if (this.props.numberOfSteps && this.props.numberOfSteps == 4){
			if (this.refs.step4.clear) {
				this.refs.step4.clear();
			}
		}
	}

    render () {

		var lastStep = '';
		var lastDiv = '';
		if (this.props.numberOfSteps && this.props.numberOfSteps == 4) {
			lastStep = <li>
							<a href="#step-4">
								<span className="step_no">4</span>
								<span className="step_descr">{this.props.children[3].props.label}</span>
							</a>
						</li> 

			lastDiv = <div id="step-4">{React.cloneElement(this.props.children[3], {ref: "step4"})}</div>
		}

		return <div id="wizard" className="form_wizard wizard_horizontal">
					<ul className="wizard_steps">
						<li>
							<a href="#step-1">
								<span className="step_no">1</span>
								<span className="step_descr">{this.props.children[0].props.label}</span>
							</a>
						</li>
						<li>
							<a href="#step-2">
								<span className="step_no">2</span>
								<span className="step_descr">{this.props.children[1].props.label}</span>
							</a>
						</li>
						<li>
							<a href="#step-3">
								<span className="step_no">3</span>
								<span className="step_descr">{this.props.children[2].props.label}</span>
							</a>
						</li>
						{lastStep}
					</ul>
				
					<div id="step-1">{React.cloneElement(this.props.children[0], {ref: "step1"})}</div>
					<div id="step-2">{React.cloneElement(this.props.children[1], {ref: "step2"})}</div>
					<div id="step-3">{React.cloneElement(this.props.children[2], {ref: "step3"})}</div>
					{lastDiv}
				</div>
   }
}