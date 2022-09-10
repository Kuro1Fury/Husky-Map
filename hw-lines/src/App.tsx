/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    edges: string
}

class App extends Component<{}, AppState> { // <- {} means no props.

  constructor(props: any) {
    super(props);
    this.state = {
      edges: ""
    };
  }

  draw(value: string) {
      this.setState({edges: value});
  }

  add(value: string) {
      this.setState({edges: this.state.edges + "\n" + value})
  }

  render() {
    // Create an array to store arrays of edge information
    let parsedEdges: Array<Array<any>> = [];
    let edges = this.state.edges.trim().split("\n");
    // Parse the string into an array which represents the edge
    if (this.state.edges.length != 0) {
        outerloop:
        for (let i = 0; i < edges.length; i++) {
            let typeEdge = [];
            let edge = edges[i].trim().split(" ");
            if (edge.length != 5) {
                parsedEdges = [];
                this.setState({edges: ""});
                window.alert("Please enter a valid input! (x1, y1, x2, y2, color)")
                break;
            } else {
                for (let j = 0; j < 4; j++) {
                    let coordinate = parseInt(edge[j]);
                    if (isNaN(coordinate)) {
                        parsedEdges = []
                        this.setState({edges: ""});
                        window.alert("The coordinates are not integer value!")
                        break outerloop;
                    }
                    if (coordinate > 4000 || coordinate < 0) {
                        parsedEdges = []
                        this.setState({edges: ""});
                        window.alert("The coordinates are not in the interval between 0 to 4000!")
                        break outerloop;
                    }
                    typeEdge.push(coordinate);
                }
                typeEdge.push(edge[4])
            }
            parsedEdges.push(typeEdge)
        }
    }

    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          <Map
            input = {parsedEdges}
          />
        </div>
        <EdgeList
          onChange={(value) => {
            this.draw(value)
          }}
          onAdd={(value) => {
            this.add(value)
          }}
        />
      </div>
    );
  }


}

export default App;
