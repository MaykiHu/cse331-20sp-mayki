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

interface GridProps {
    size: number;    // size of the grid to display
    width: number;   // width of the canvas on which to draw
    height: number;  // height of the canvas on which to draw
    edges: string;   // the information of all the edges
    clicked: boolean;// if draw button has been clicked
}

interface GridState {
    backgroundImage: any  // image object rendered into the canvas (once loaded)
}

/**
 *  A simple grid with a variable size
 *
 *  Most of the assignment involves changes to this class
 */
class Grid extends Component<GridProps, GridState> {
    // scalar for scaling points to grid coordinates
    private scalar = (this.props.width / (this.props.size + 1)) // shift/scale amount
    canvasReference: React.RefObject<HTMLCanvasElement>

    constructor(props: GridProps) {
        super(props);
        this.state = {
            backgroundImage: null  // An image object to render into the canvas.
        };
        this.canvasReference = React.createRef();
    }

    componentDidMount() {
        // Since we're saving the image in the state and re-using it any time we
        // redraw the canvas, we only need to load it once, when our component first mounts.
        this.fetchAndSaveImage();
        // Make sure scalar is up-to-date with props
        this.scalar = (this.props.width / (this.props.size + 1)) // shift/scale amount
        this.redraw();
    }

    componentDidUpdate() {
        // Make sure scalar is up-to-date with props
        this.scalar = (this.props.width / (this.props.size + 1)) // shift/scale amount
        this.redraw()
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        const background = new Image();
        background.onload = () => {
            const newState = {
                backgroundImage: background
            };
            this.setState(newState);
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./image.jpg";
    }

    redraw = () => {
        if(this.canvasReference.current === null) {
            throw new Error("Unable to access canvas.");
        }
        const ctx = this.canvasReference.current.getContext('2d');
        if (ctx === null) {
            throw new Error("Unable to create canvas drawing context.");
        }

        ctx.clearRect(0, 0, this.props.width, this.props.height);
        // Once the image is done loading, it'll be saved inside our state.
        // Otherwise, we can't draw the image, so skip it.
        if (this.state.backgroundImage !== null) {
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        // Draw all the dots.
        const coordinates = this.getCoordinates();
        for (let coordinate of coordinates) {
            this.drawCircle(ctx, coordinate);
        }
        // Draw all the edges.
        if (this.props.clicked && this.validateEdges()) { // If edges clicked and are valid edges
            let edges = this.getEdges();
            for (let edge of edges) {
                this.drawEdge(ctx, edge);
            }
        }
    };
    /*
     * Validates the information on all edges given and alerts user if not valid
     * @return boolean if edges given are valid, true if valid and false otherwise
     */
    validateEdges = (): boolean => {
        let isValid = true;
        let alertMessage = "There was an error with some of your line input.\nFor reference, the correct" +
            "form for each line is: x1,y1 x2,y2 color\n\n";
        // Return the scaled coordinates where grid edges should be drawn
        if (isNaN(this.props.edges.length) || this.props.edges.length === 0) { // if empty edges drawn
            return !isValid; // Cannot draw nothing, so false
        }
        let edgeParser = this.props.edges.split("\n"); // Split edge input into lines of edges
        let sizeRequired = this.props.size; // Size required by the graph to draw the edge
        for (let line = 1; line <= edgeParser.length; line++) { // For each line containing edge info
            if (edgeParser[line - 1].length === 0) { // If empty line
                continue; // Skip to next line
            }
            let edge = edgeParser[line - 1].split(" "); // [x1,y1, x2,y2 color] if correctly inputted
            if (edge.length !== 3) { // First check for valid input: 3 properties of an edge
                alertMessage += "Line " + line + ": Missing a portion of the line, or missing a space.\n";
                isValid = false;
            } else { // Let's check for next possible error
                for (let i = 1; i <= 2; i++) { // There are two points, if formatted right
                    let point = edge[i - 1].split(","); // [x1, y1]
                    if (point.length !== 2) { // Check for valid input: x,y in a point
                        alertMessage += "Line " + line +
                            ": Wrong number of arguments to coordinate number " + i + "\n";
                        isValid = false;
                    } else { // Let's check for next possible error with points
                        let xPoint = parseInt(point[0]);
                        let yPoint = parseInt(point[1]);
                        if (isNaN(xPoint) || isNaN(yPoint)) { // If not given numbers
                            alertMessage += "Line " + line +
                                ": Coordinate(s) contain non-integer value(s).\n";
                            isValid = false;
                        } else { // Given numbers
                            // Update the size required by point to fit in the grid
                            sizeRequired = Math.max(xPoint + 1, yPoint + 1, sizeRequired);
                        }
                    }
                }
            }
        }
        // Last check.. can the grid fit all the points?
        if (sizeRequired > this.getSize()) { // If size required is greater than size of grid
            alertMessage += "Cannot draw edges, grid must be at least size " + sizeRequired + ".\n";
            isValid = false;
        }
        if (!isValid) { // If edges aren't valid
            alert(alertMessage); // alert the user why
            return false;
        }
        return isValid; // return true, isValid
    }

    /*
     * Returns an array of edges that represent pairs of coordinates where grid dots should
     * be connected to draw an edge and the color of each edge
     * @spec.requires given correct input
     */
    getEdges = (): [number, number, number, number, string][] => {
        let edgeInfo : [number, number, number, number, string][] = []; // [[x1, y1, x2, y2, color], [...]]
        let edgeParser = this.props.edges.split("\n"); // Split edge input into lines of edges
        for (let line = 0; line < edgeParser.length; line++) { // For each line containing edge info
            if (edgeParser[line].length === 0) { // If empty line
                continue; // Skip to next line
            }
            let edge = edgeParser[line].split(" "); // [x1,y1, x2,y2 color] if correctly inputted
            let points : number[] = []; // [x1, y1, x2, y2] result array if given correct input
            for (let i = 0; i < 2; i++) { // There are two points, if formatted right
                let point = edge[i].split(","); // [x1, y1]
                let xPoint = parseInt(point[0]);
                let yPoint = parseInt(point[1]);
                // Let's add the scaled point then.
                points.push(xPoint * this.scalar + this.scalar);
                points.push(yPoint * this.scalar + this.scalar);
            }
            // Add the edge
            edgeInfo.push([points[0], points[1], points[2], points[3], edge[2]]);
        }
        return edgeInfo;
    }
    /**
     * Returns an array of coordinate pairs that represent all the points where grid dots should
     * be drawn.
     */
    getCoordinates = (): [number, number][] => {
        let points : [number, number][] = []; // Where points are stored
        for (let row = 0; row < this.props.size; row++) {
            for (let col = 0; col < this.props.size; col++) {
                points.push([row * this.scalar + this.scalar, col * this.scalar + this.scalar]);
            }
        }
        // Return the scaled coordinates where grid dots should be drawn
        return points;
    };

    // You could write CanvasRenderingContext2D as the type for ctx, if you wanted.
    drawCircle = (ctx: any, coordinate: [number, number]) => {
        ctx.fillStyle = "white";
        // Generally use a radius of 4, but when there are lots of dots on the grid (> 50)
        // we slowly scale the radius down so they'll all fit next to each other.
        const radius = Math.min(4, 100 / this.props.size);
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    drawEdge = (ctx: any, edge: [number, number, number, number, string]) => {
        ctx.strokeStyle = edge[4]; // The color is specified by the edge given at index 4
        ctx.lineWidth = Math.min(4, 100 / this.props.size * 2); // The stroke width
        ctx.beginPath();
        ctx.moveTo(edge[0], edge[1]); // Pen to beginning of edge
        ctx.lineTo(edge[2], edge[3]); // End point of the line
        ctx.stroke();                 // Draw the line
    }

    /*
     * Returns the size of the grid
     */
    getSize = (): number => {
        if (Number.isNaN(this.props.size)) { // If empty size/string
            return 0;
        } else {
            return this.props.size;
        }
    }

    download = () => {
        let link = document.createElement('a');
        link.download = 'PollockDots.png';
        // @ts-ignore
        link.href = document.getElementById('drawing').toDataURL();
        link.click();
    }

    render() {
        return (
            <div id="grid">
                <canvas id="drawing" ref={this.canvasReference} width={this.props.width} height={this.props.height}/>
                <p>Current Grid Size: {this.getSize()}</p>
                <button onClick={this.download}>Download Drawing</button>
            </div>
        );
    }
}

export default Grid;
