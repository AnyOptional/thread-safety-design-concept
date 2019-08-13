package com.archer.composingobjects;

import com.archer.annotation.Immutable;

@Immutable
public class Point {

    final int x;
    final int y;

    Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    Point() {
        x = y = 0;
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
