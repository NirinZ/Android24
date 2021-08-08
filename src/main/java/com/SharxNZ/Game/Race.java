package com.SharxNZ.Game;

public enum Race {
    Saiyan(869965894804209664L), Frieza(869972011290599434L);

    private final long ID;

    public long getID(){
        return this.ID;
    }

    Race(long ID){
        this.ID = ID;
    }
}
