package com.example.demo.socket;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ship {
    private int x;
    private int y;

    public Ship()
    {
        this.x = 100;
        this.y = 100;
    }
}
