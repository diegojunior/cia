var webpack = require('webpack');
var path = require('path');

var APP_DIR = path.resolve(__dirname, 'src');
var BUILD_DIR = path.resolve(__dirname, 'public');

var config = {

  entry: APP_DIR + "/App.js",

  output: {
    path: BUILD_DIR,
	filename: "bundle.js"
  },
  
  resolve: {
	extensions: ['.js', '.jsx', '.json']
  },
  
  devServer: {
	  inline: true,
	  contentBase: BUILD_DIR,
    port: 3333
  },
  
  module: {
    loaders: [
    	{
	        test: /\.js$/, 
	        loaders: ['babel-loader?presets[]=react,presets[]=es2015'],
	        exclude: /node_modules/
		  },
      {
        test: /\.css$/, 
	      loaders: 'style-loader!css-loader'
      },
      {
        test: /\.json$/, 
        loaders: 'json-loader',
        exclude: /node_modules/
      }
    ]
  }
 
};

module.exports = config;