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
    onChange(edges: any): void;  // called when a new edge list is ready
}

/**
 * A Textfield that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps> {
    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={() => {console.log('textarea onChange was called');}}
                    value={"I'm stuck..."}
                /> <br/>
                <button onClick={() => {console.log('Draw onClick was called');}}>Draw</button>
                <button onClick={() => {console.log('Clear onClick was called');}}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
