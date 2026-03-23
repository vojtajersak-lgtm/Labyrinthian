package cz.cvut.fel.pjv.labyrinthian.Entities;

public abstract class GameObject {
    protected int cordY, cordX;

    public GameObject(int cordY, int cordX) {
        this.cordY = cordY;
        this.cordX = cordX;
    }
}
