import moment from 'moment';

export const sort = (options, property) => {
	options.sort((i1, i2) => {
		let value1 = getValueByProperty(i1, property).toUpperCase();
		let value2 = getValueByProperty(i2, property).toUpperCase();
		if (value1 < value2)
			return -1;
		if (value1 > value2)
		    return +1;
		return 0;
	}); 
};

export const sortIntCollator = (options, property) => {	
	options.sort((i1, i2) => {
		let value1 = getValueByProperty(i1, property);
		let value2 = getValueByProperty(i2, property);
		return value1.localeCompare(value2, {numeric: true, sensitivity: 'base'})
	}); 
};

export const getValueByProperty = (object, property) => {
	let fields = property.split('.');
	let copyObject = object;
	while (fields.length) {
	  copyObject = copyObject[fields.shift()];
	}
	return copyObject;
};

export const objectAsSame = (obj1, obj2, ...property) => {
	let equal = false;
	if (obj1.length !== obj2.length) return false;
	if ($.isArray(obj1)) {
		for (let i in obj1) {
			for (let n in property) {
				if (property[n]) {
					let value1 = getValueByProperty(obj1[i], property[n])
					let value2 = getValueByProperty(obj2[i], property[n])
					equal = value1 === value2;
					if (!equal) return false;
				}
			}
		}
	}
	return equal;
};

export const copyObject = (obj) => {
    var newObj = {};
    for (var key in obj) {
        //copy all the fields
        newObj[key] = obj[key];
    }

    return newObj;
}

export const format = (value) => {
	return dateFormat(value);
}

var yyyyMMdd = /^\d{8}/;
var yyyy_MM_dd = /^\d{4}-\d{2}-\d{2}/;
var yyyyMMddTHHmmssSSSZ = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}Z/;
export const dateFormat = (value) => {
	if (yyyyMMddTHHmmssSSSZ.test(value)) {
		return moment(value).format('DD/MM/YYYY HH:mm:ss');
	} else if (yyyy_MM_dd.test(value)) {
		return moment(value).format('DD/MM/YYYY');
	} else if (yyyyMMdd.test(value)) {
		var date = moment(value, 'YYYYMMDD', true);
		if (date.isValid()) {
			return moment(date).format('DD/MM/YYYY');
		}
	}
	return value;
}