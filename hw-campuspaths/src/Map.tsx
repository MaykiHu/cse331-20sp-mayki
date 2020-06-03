/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import "./Map.css";

// The props of this Map
interface MapProps {
    width: number;   // width of the canvas on which to draw
    height: number;  // height of the canvas on which to draw
}

interface MapState {
    backgroundImage: HTMLImageElement | null,
    buildings: string[]
    startValue: string
    endValue: string
}

class Map extends Component<MapProps, MapState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapProps) {
        super(props);
        this.state = {
            backgroundImage: null,
            buildings: [],
            startValue: "BAG", // BAG is the building name always starting in drop down list first
            endValue: "BAG" // BAG is the building name always ending in the drop down list first
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage();
        this.fetchDropList();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
    }

    // Creates drop-down list of buildings on campus from the server data of campus buildings
    fetchDropList() {
        // Get the JSON info from server on buildings
        fetch("http://localhost:4567/buildings")
            .then((res) => {
                return res.json();
            })
            // Save the building information
            .then(data => {
                this.setState({
                    buildings: Object.keys(data).sort()
                })
            });
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
    }

    // Updates the starting building value from the dropdown list
    handleStartChange = (event: any) => {
        this.setState({
           startValue: event.target.value
        });
    }

    // Updates the ending building value from the dropdown list
    handleEndChange = (event: any) => {
        this.setState({
           endValue: event.target.value
        });
    }

    // Creates the path from JSON that is generated from the start and stop of current dropdown
    // start and stop building values
    handleFindClick = (event: any) => {
        // The server request URL based on start/stop state
        let serverReqUrl = "http://localhost:4567/path?startName=" + this.state.startValue +
            "&endName=" + this.state.endValue;
        fetch(serverReqUrl)
            .then((res) => {
                return res.json();
            })
            .then(data => {
                console.log(data);
            })
    }

    render() {
        return (
            <div id="map">
                <div id="dropdown">
                    <div id="start-dropdown">
                        <p>Start Building:</p>
                        <select value={this.state.startValue} onChange={this.handleStartChange}>
                            {this.state.buildings.map((building) =>
                                <option key={building} value={building}>{building}</option>)}
                        </select>
                    </div>
                    <div id="stop-dropdown">
                        <p>End Building:</p>
                        <select value={this.state.endValue} onChange={this.handleEndChange}>
                            {this.state.buildings.map((building) =>
                                <option key={building} value={building}>{building}</option>)}
                        </select>
                    </div>
                    <button id="find-button" onClick={this.handleFindClick}>Find Shortest Path</button>
                    <button id="clear-button">Clear All</button>
                </div>
                <canvas ref={this.canvas} width={this.props.width} height={this.props.height}/>
            </div>
        );
    }
}

export default Map;