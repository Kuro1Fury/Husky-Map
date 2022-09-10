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

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  input: Array<Array<any>>
}

interface MapState {}

class Map extends Component<MapProps, MapState> {
  constructor(props: MapProps) {
      super(props);
      this.state = {
          edges: [[]]
      }
  }

  render() {
    let lines = [];
    for (let i = 0; i < this.props.input.length; i++) {
        lines.push(<MapLine
            //key = {i}
            x1 = {this.props.input[i][0]}
            y1 = {this.props.input[i][1]}
            x2 = {this.props.input[i][2]}
            y2 = {this.props.input[i][3]}
            color = {this.props.input[i][4]}
        />)
    }

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {
            lines
          }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
