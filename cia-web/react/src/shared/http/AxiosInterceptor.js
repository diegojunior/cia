import Axios from 'axios';

let AxiosInterceptor = Axios;

AxiosInterceptor.interceptors.response.use( response => {
    if (response.data.indexOf) {
    	if (response.data.indexOf("loginForm") != -1) {
    		window.location.href = "login";
	    }
	    if (response.data.indexOf("invalidSession") != -1) {
	    	window.location.href = "login?invalid";
	    }
	    if (response.data.indexOf("exipiredSession") != -1) {
	    	window.location.href = "login?expired";
	    }	
    }
    
	return response;

}, error => {
    console.log('AxiosInterceptor RESPONSE: ERROR')
    return Promise.reject(error)
});

export default AxiosInterceptor;