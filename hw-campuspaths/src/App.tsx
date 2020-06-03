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
import Map from "./Map";

class App extends Component<{}, {}> {

    render() {
        return (
            <div>
            <p id="app-title">Campus Pathfinder</p>
            <Map width={4330} height={2964}/>
            </div>
        );
    }

}

export default App;
