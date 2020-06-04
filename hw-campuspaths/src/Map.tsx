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
    buildings: string[] // the short name list of buildings
    startValue: string // the starting building in drop down
    endValue: string // the ending building in drop down
    drawnPath: [number, number, number, number][] // the path drawn's location info
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
            buildings: [],     // list of all the buildings (populate it when loaded)
            startValue: "BAG", // BAG is the building name always starting in drop down list first
            endValue: "BAG", // BAG is the building name always ending in the drop down list first
            drawnPath: [],    // the path locations drawn, none yet
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage();
        this.fetchDropList();
        this.drawBackgroundImage();
        this.redraw();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
        this.redraw();
    }

    // redraws the current path on screen from drawnPath's JSON (specifies path info)
    redraw() {
        if(this.canvas.current === null) {
            throw new Error("Unable to access canvas.");
        }
        const ctx = this.canvas.current.getContext('2d');
        if (ctx === null) {
            throw new Error("Unable to create canvas drawing context.");
        }

        ctx.clearRect(0, 0, this.props.width, this.props.height);
        // Once the image is done loading, it'll be saved inside our state.
        // Otherwise, we can't draw the image, so skip it.
        if (this.state.backgroundImage !== null) {
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        // Draw the current path -- all its edge glory.  :)
        for (let edge of this.state.drawnPath) { // For each edge in path to draw
            this.drawEdge(ctx, edge);
        }
    }

    // Draws edges based on given array of coordinates to draw
    drawEdge = (ctx: any, edge: [number, number, number, number]) => {
        ctx.strokeStyle = "purple"; // Draw path in purple UW color!!  Yaaaaay~ :)
        ctx.lineCap = "round"; // Lines have rounded edges
        ctx.lineWidth = 10; // The stroke width
        // Adds outer faint gold glowing effect
        ctx.shadowBlur = 20;
        ctx.shadowColor = "gold";
        ctx.beginPath();
        ctx.moveTo(edge[0], edge[1]); // Pen to beginning of edge
        ctx.lineTo(edge[2], edge[3]); // End point of the line
        ctx.stroke();                 // Draw the line
    }

    // Creates drop-down list of buildings on campus from the server data of campus buildings
    fetchDropList() {
        // Get the JSON info from server on buildings, sorted alphabetically
        fetch("http://localhost:4567/buildings")
            .then((res) => {
                return res.json();
            })
            // Save the building information
            .then(data => {
                this.setState({
                    buildings: Object.keys(data).sort() // Short names of buildings, sorted
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

    // Updates the path drawn state that needs to be drawn on screen based on dropdown buildings
    handleFindClick = (event: any) => {
        // The server request URL based on start/stop state in dropdown lists
        let serverReqUrl = "http://localhost:4567/path?startName=" + this.state.startValue +
            "&endName=" + this.state.endValue;
        let edgeInfo : [number, number, number, number][] = []; // [startX, startY, endX, endY]
        fetch(serverReqUrl)
            .then((res) => {
                return res.json();
            })
            .then(data => {
                let paths = data["path"]; // Get the data on the paths to be drawn
                for (let edge of paths) { // For each edge in paths
                    let startData = edge["start"]["data"]; // data on start building
                    let endData = edge["end"]["data"]; // data on end building
                    let startX = startData["x"]; // starting building's X loc
                    let startY = startData["y"]; // starting building's Y loc
                    let endX = endData["x"]; // ending building's X loc
                    let endY = endData["y"]; // ending building's Y loc
                    edgeInfo.push([startX, startY, endX, endY]); // Save curr path info
                }
                this.setState({ // Update with paths that now needs to be drawn
                    drawnPath: edgeInfo
                })
            });
    }

    // Resets the program to make page as if freshly loaded
    handleClearClick = (event: any) => {
        this.setState({
            startValue: "BAG", // BAG is the building name always starting in drop down list first
            endValue: "BAG", // BAG is the building name always ending in the drop down list first
            drawnPath: []    // the path locations drawn, none yet
        })
    }

    render() {
        return (
            <div id="map">
                <p id="app-title">Campus Pathfinder</p>
                <div id="dropdown">
                    <div id="start-dropdown">
                        <p>Start:</p>
                        <select value={this.state.startValue} onChange={this.handleStartChange}>
                            {this.state.buildings.map((building) =>
                                <option key={building} value={building}>{building}</option>)}
                        </select>
                    </div>
                    <div id="stop-dropdown">
                        <p>Stop:</p>
                        <select value={this.state.endValue} onChange={this.handleEndChange}>
                            {this.state.buildings.map((building) =>
                                <option key={building} value={building}>{building}</option>)}
                        </select>
                    </div>
                    <button id="find-button" onClick={this.handleFindClick}>Find Path</button>
                    <button id="clear-button" onClick={this.handleClearClick}>Clear All</button>
                </div>
                <canvas ref={this.canvas} width={this.props.width} height={this.props.height}/>
            </div>
        );
    }
}

export default Map;