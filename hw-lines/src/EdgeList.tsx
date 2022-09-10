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

interface EdgeListProps {
    onChange(edges: string): any;
    onAdd(edges: string): any;
}

interface EdgeState {
    inputValue: string
}



/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeState> {
    constructor(props: EdgeListProps) {
        super(props);
        this.state = {inputValue: ""}
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    placeholder={"Please type in the coordinates and color of the line you want to display"}
                    value={this.state.inputValue}
                    onChange={this.handleChange}
                /> <br/>
                <button onClick={() => {
                    console.log('Draw onClick was called');
                    this.props.onChange(this.state.inputValue);
                }}>Draw</button>
                <button onClick={() => {
                    console.log('Add onClick was called');
                    this.props.onAdd(this.state.inputValue);
                }}>Add</button>
                <button onClick={() => {
                    console.log('Clear onClick was called');
                    this.props.onChange("");
                }}>Clear</button>
                <button onClick={() => {
                    console.log('Show UW onClick was called');
                    this.props.onChange("1252 724 1257 997 purple\n1257 997 1432 997 purple\n1432 997 1777 2292 purple\n1798 2287 2212 2292 purple\n2212 2292 2435 1500 purple\n2435 1500 2658 2297 purple\n2658 2297 3087 2271 purple\n3087 2271 3448 1012 purple\n3453 1007 3623 997 purple\n3623 997 3623 724 purple\n3623 724 2986 719 purple\n2992 729 3008 991 purple\n3008 991 3193 991 purple\n3193 991 2965 1804 purple\n2965 1804 2689 724 purple\n2689 724 2403 719 purple\n2403 719 2106 1825 purple\n2106 1825 1883 1033 purple\n1883 1033 2095 1023 purple\n2095 1023 2095 724 purple\n2095 724 1236 713 purple");
                }}>SHOW UW</button>
            </div>
        );
    }

    handleChange = (event: any) => {
        this.setState({
            inputValue: event.target.value
        });
    };


}

export default EdgeList;
