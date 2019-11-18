'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

class App extends React.Component{

    constructor(props){
        super(props);
        this.state = {youtube: []};
    }

    componentDidMount(){
        fetch('/search/pm-XRnWhI4c')
            .then(response => {
                return response.json();
            }).then(res => {
            this.setState({youtube: res});
        });
    }

    render(){
        return (
            <YoutubeView youtube={this.state.youtube} />
        )
    }
}

class YoutubeView extends React.Component{
    render() {
        const video = this.props.youtube.map(video =>
            <Video youtube={video}/>
        );
        return (
            <table style={{ width : '100%', height: '600px'}}>
                <tbody>
                <tr>
                    <th>영상 제목</th>
                    <th>썸네일</th>
                    <th>영상</th>
                </tr>
                {video}
                </tbody>
            </table>
        )
    }
}

class Video extends React.Component{

    render(){
        return (
            <tr>
                <td>{this.props.youtube.title}</td>
                <td><img alt={this.props.youtube.title} src={this.props.youtube.thumbnail}/></td>
                <td><iframe width="100%" height="100%" src={this.props.youtube.url} /></td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('video')
);