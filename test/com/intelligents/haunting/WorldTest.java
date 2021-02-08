package com.intelligents.haunting;

import org.junit.Before;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorldTest {


    @Test
    public void populateRoomList() {
        World world = new World();
        world.populateRoomList();
       // List<Room> rooms = new ArrayList<>();
        //world.setRooms(rooms);
        //assertTrue("Lobby",world.getRooms().contains("Lobby"));
    }
}