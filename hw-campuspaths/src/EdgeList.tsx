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

import React, {Component} from 'react';
import { Animate, AnimateGroup } from "react-simple-animate";


interface EdgeListProps {
    onChange(edges: string): any;
}

interface EdgeState {
    buildings: []
    start: string
    end: string
    color: string
    selected: string
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons and dropdowns that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeState> {
    constructor(props: EdgeListProps) {
        super(props);
        this.state = {buildings: [], start: '', end: '', color: '', selected: ''}
    }

    // function that request server and return the building list
    makeRequest = async () => {
        try {
            let tempResponse = fetch("http://localhost:4567/buildings");
            let response = await tempResponse;
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }
            let bds = await response.json();
            this.setState({
                buildings: bds
            })
        } catch (e) {
            alert("There was an error contacting the server.");
        }
    }

    // function that avoid request server multiple timss
    componentDidMount() {
        this.makeRequest();
    }

    render() {
        // buildings option
        let options: any[] = []
        let bd: string
        for (let i = 0; i < this.state.buildings.length; i++) {
            bd = this.state.buildings[i]
            options.push(
                <option key = {i} value = {bd}>{bd}</option>
            )
        }

        // colors option
        let colors: any[] = []
        let cl: string
        let c = ['red', 'blue', 'pink', 'green', 'yellow', 'black', 'brown', 'grey']
        for (let i = 0; i < c.length; i++) {
            cl = c[i]
            colors.push(
                <option key = {i} value = {cl}>{cl}</option>
            )
        }

        return (
            <div id="edge-list" >
                <AnimateGroup play>
                    {/*first building dropdown*/}
                    <Animate start={{ opacity: 0 }} end={{ opacity: 1 }} sequenceIndex={0}>
                        <p><strong>First building</strong></p>
                        <select
                            value = {this.state.start}
                            onChange={(event)=>{this.setState({start: event.target.value})}}>
                            <option>Please select a building</option> {options}
                        </select> <br/>
                    </Animate>
                    {/*second building dropdown*/}
                    <Animate start={{ opacity: 0 }} end={{ opacity: 1 }} sequenceIndex={1}>
                        <p><strong>Second building</strong></p>
                        <select
                            value = {this.state.end}
                            onChange={(event)=>{this.setState({end: event.target.value})}}>
                            <option>Please select a building</option> {options}
                        </select>
                    </Animate>
                    {/*color dropdown*/}
                    <Animate start={{ opacity: 0 }} end={{ opacity: 1 }} sequenceIndex={2}>
                        <p><strong>Color</strong></p>
                        <select
                            value = {this.state.color}
                            onChange={(event)=>{this.setState({color: event.target.value})}}>
                            <option>Please select a color</option> {colors}
                        </select><br/>
                    </Animate>
                    {/*draw and clear button*/}
                    <Animate start={{ opacity: 0 }} end={{ opacity: 1 }} sequenceIndex={3}>
                        <button onClick={() => {
                            this.props.onChange(this.state.start + "\n" + this.state.end + "\n" + this.state.color);
                        }}>Draw</button>
                        <button onClick={() => {
                            this.props.onChange("");
                        }}>Clear</button>
                        <button type="button" onClick={this.handleClear}>Reset</button>
                    </Animate>
                </AnimateGroup>
            </div>
        );
    }

    handleClear = () => {
        this.setState({start: "", end: "", color: ""});
        this.props.onChange("");
    }

}

export default EdgeList;
