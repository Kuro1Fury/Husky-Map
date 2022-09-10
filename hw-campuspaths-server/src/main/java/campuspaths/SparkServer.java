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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import pathfinder.CampusMap;

import java.util.Map;


public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap map = new CampusMap();
        Spark.get("/route-A-to-B", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                // As a first example, let's just return a static string.
                String A = request.queryParams("start");
                String B = request.queryParams("end");
                if (A == null || B == null) {
                    Spark.halt(400, "must have start and end");
                }
                Path<Point> path = null;
                try {
                    path = map.findShortestPath(A, B);
                } catch (IllegalArgumentException e) {
                    Spark.halt(400, "start and end do not present");
                }
                Gson gson = new Gson();
                return gson.toJson(path);
            }
        });

        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                // As a first example, let's just return a static string.
                CampusMap map = new CampusMap();
                Map<String, String> m = map.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(m.keySet());
            }
        });



    }

}
