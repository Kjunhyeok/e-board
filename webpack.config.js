const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = (env) => {
    let clientPath = path.resolve(__dirname, 'src/main/client');
    let outputPath = path.resolve(__dirname, (env === 'production') ? 'src/main/resources/static' : 'out');

    return {
        mode: !env ? 'development' : env,
        entry: clientPath + '/index.js',
        cache: true,
        output: {
            path: outputPath,
            filename: 'index.js'
        },
        devServer: {
            contentBase: outputPath,
            publicPath: '/',
            host: '0.0.0.0',
            port: 8081,
            proxy: {
                '**': 'http://127.0.0.1:8080'
            },
            inline: true,
            hot: false
        },
        module: {
            rules: [
                {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/react'],
                    }
                }
            },{
                test: /\.html$/,
                    use:[
                        {
                            loader: "html-loader",
                            options: {minimize: true}
                        }
                    ]
                },
                {
                test: /\.(css)$/,
                use: [{
                    loader: MiniCssExtractPlugin.loader
                }, {
                    loader: 'css-loader'
                }]
            }, {
                test: /\.(jpe?g|png|gif)$/i,
                use: [{
                    loader: 'url-loader',
                    options: {
                        limit: 1024 * 10 // 10kb
                    }
                }]
            }, {
                test: /\.(svg)$/i,
                use: [{
                    loader: 'file-loader',
                    options: {
                        name: '[name].[ext]',
                        outputPath: 'images/'
                    }
                }]
            }, {
                test: /\.(wav|mp3)$/i,
                use: [{
                    loader: 'file-loader',
                    options: {
                        name: '[name].[ext]',
                        outputPath: 'midi/'
                    }
                }]
            }]
        },
        plugins: [
            new HtmlWebPackPlugin({
                template: "./src/main/resources/templates/index.html",
                filename: "index.html"
            }),
            new MiniCssExtractPlugin({
                path: outputPath,
                filename: '[name].css'
            })
        ]
    }
};