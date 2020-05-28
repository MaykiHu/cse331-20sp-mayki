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

interface EdgeListProps {
    clicked: boolean             // if draw was clicked
    value: string;               // text to display in the text area
    onChange(edges: any, click: any): void;  // called when a new edge list is ready
}

/**
 * A Textfield that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            clicked: false
        };
    }

    onInputChange = (event: any) => {
        // Every event handler with JS can optionally take a single parameter that
        // is an "event" object - contains information about an event. For mouse clicks,
        // it'll tell you thinks like what x/y coordinates the click was at. For text
        // box updates, it'll tell you the new contents of the text box, like here.
        //
        // We wrote "any" here because the type of this object is long and complex.
        // If you're curious, the exact type would be: React.ChangeEvent<HTMLInputElement>
        //
        const stringText: string = event.target.value;
        this.props.onChange(stringText, false); // No click happened, so false
    };

    onMouseClick = () => {
        this.props.onChange(this.props.value, true); // Click happened, so true
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    value={this.props.value}
                    onChange={this.onInputChange}
                /> <br/>
                <button onClick={this.onMouseClick}>Draw</button>
                <button onClick={this.onInputChange}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
