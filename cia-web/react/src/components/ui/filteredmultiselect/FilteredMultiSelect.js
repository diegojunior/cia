import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {getValueByProperty}  from '../../../shared/util/Utils'

const DEFAULT_CLASS_NAMES = {
  button: 'FilteredMultiSelect__button',
  buttonActive: 'FilteredMultiSelect__button--active',
  filter: 'FilteredMultiSelect__filter',
  select: 'FilteredMultiSelect__select'
}

function makeLookup(arr, prop) {
  let lkup = {}
  for (let i = 0, l = arr.length; i < l; i++) {
    if (prop) {
      lkup[getValueByProperty(arr[i], prop)] = true
    }
    else {
      lkup[arr[i]] = true
    }
  }
  return lkup
}

function getItemsByProp(arr, prop, values) {
  let items = []
  let found = 0
  let valuesLookup = makeLookup(values)
  for (let i = 0, la = arr.length, lv = values.length;
       i < la && found < lv;
       i++) {
    let valor = getValueByProperty(arr[i], prop);
    if (valuesLookup[valor]) {
      items.push(arr[i])
      found++
    }
  }
  return items
}

export default class FilteredMultiSelect extends Component {

  constructor(props) {
      super(props);
	    this.state = {filter: '', filteredOptions: [], selectedValues: []};

      this._onFilterChange = this._onFilterChange.bind(this);
      this._onFilterKeyPress = this._onFilterKeyPress.bind(this);
      this._updateSelectedValues = this._updateSelectedValues.bind(this);
      this._getClassName = this._getClassName.bind(this);
      this._filterOptions = this._filterOptions.bind(this);
      this._addSelectedToSelection = this._addSelectedToSelection.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
        filteredOptions: this._filterOptions(this.state.filter,
                                             nextProps.selectedOptions,
                                             nextProps.options)
      }, this._updateSelectedValues);

  }

  _getClassName(name, ...modifiers) {
    let classNames = [this.props.classNames[name] || DEFAULT_CLASS_NAMES[name]]
    for (let i = 0, l = modifiers.length; i < l; i++) {
      if (modifiers[i]) {
        classNames.push(this.props.classNames[modifiers[i]] || DEFAULT_CLASS_NAMES[modifiers[i]])
      }
    }
    return classNames.join(' ')
  }

  _filterOptions(filter, selectedOptions, options) {
    if (typeof filter == 'undefined') {
      filter = this.state.filter;
    }
    if (typeof selectedOptions == 'undefined') {
      selectedOptions = this.props.selectedOptions && this.props.selectedOptions.length > 0? this.props.selectedOptions : [];
    }
    if (typeof options == 'undefined') {
      options = this.props.options;
    }
    filter = filter.toUpperCase();

    let {textProp, valueProp} = this.props;
    let selectedValueLookup = makeLookup(selectedOptions, valueProp);
    let filteredOptions = [];

    for (let i = 0, l = options.length; i < l; i++) {
      if (!selectedValueLookup[getValueByProperty(options[i], valueProp)] &&
          (!filter || getValueByProperty(options[i], textProp).toUpperCase().indexOf(filter) !== -1)) {
        filteredOptions.push(options[i]);
      }
    }

    return filteredOptions;
  }

  _onFilterChange(e) {

    let filter = e.target.value;

    if (this.props.onFilterChange) {
      this.props.onFilterChange(filter);
    }

    this.setState({filter, filteredOptions: this._filterOptions(filter)}, this._updateSelectedValues);
    
  }

  _onFilterKeyPress(e) {
    
    if (e.key === 'Enter') {
      e.preventDefault()
      
      if (this.state.filteredOptions.length === 1) {
        let selectedOption = this.state.filteredOptions[0]
        let selectedOptions = [selectedOption];
        if (this.props.selectedOptions) {
            selectedOptions = this.props.selectedOptions.concat([selectedOption]);
        }
         
        this.setState({filter: '', selectedValues: []}, () => {
          this.props.onChange(selectedOptions)
        })
      }
    }
  }

  _updateSelectedValues(e) {
    let el = e ? e.target : this.refs.select
    let selectedValues = []
    for (let i = 0, l = el.options.length; i < l; i++) {
      if (el.options[i].selected) {
        selectedValues.push(el.options[i].value)
      }
    }

    if (e || String(this.state.selectedValues) !== String(selectedValues)) {
      this.setState({selectedValues: selectedValues})
    }
  }

  _addSelectedToSelection(e) {
    let option = getItemsByProp(this.state.filteredOptions,
                                this.props.valueProp,
                                this.state.selectedValues);

    let selectedOptions = option;
    if (this.props.selectedOptions) {
      selectedOptions = this.props.selectedOptions.concat(option);
    }
    
    this.setState({selectedValues: []}, () => {
      this.props.onChange(selectedOptions)
    })
  }

  render() {
    let {filter, filteredOptions, selectedValues} = this.state;
    let {className, disabled, placeholder, size, textProp, valueProp} = this.props;
    let hasSelectedOptions = selectedValues.length > 0;
    return (
        <div className="FilteredMultiSelect">
            <input
              id={this.props.id+"_search"}
              type="text"
              className={this._getClassName('filter')}
              placeholder={placeholder}
              value={filter}
              onChange={this._onFilterChange}
              onKeyPress={this._onFilterKeyPress}
              disabled={disabled} />
            
            <select multiple
              id={this.props.id+"_list"}
              ref="select"
              className={this._getClassName('select')}
              size={6}
              value={selectedValues}
              onChange={this._updateSelectedValues}
              onDoubleClick={this._addSelectedToSelection}
              disabled={disabled}>
              {filteredOptions.map((option) => {
                let value = getValueByProperty(option, valueProp);
                let text = getValueByProperty(option, textProp);
                return <option key={value} value={value}>{text}</option>
              })}
            </select>

            <button
              id={this.props.id+"_button"} 
              type="button"
              className={this._getClassName('button', hasSelectedOptions && 'buttonActive')}
              disabled={!hasSelectedOptions}
              onClick={this._addSelectedToSelection}>
              {this.props.buttonText}
            </button>

          </div>)
  }
}

FilteredMultiSelect.propTypes = {
    onChange: PropTypes.func.isRequired,
    options: PropTypes.array.isRequired,

    buttonText: PropTypes.string,
    
    classNames: PropTypes.object,
    defaultFilter: PropTypes.string,
    disabled: PropTypes.bool,
    placeholder: PropTypes.string,
    selectedOptions: PropTypes.array,
    size: PropTypes.number,
    textProp: PropTypes.string,
    valueProp: PropTypes.string
}