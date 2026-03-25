package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Entities.Entity;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

public abstract class Weapon extends Item {
    private int damage;
    private int attackSpeed;
    private int range;

    public Weapon(String name, String description, int damage, int attackSpeed, int range) {
        super(name, description);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public abstract void attack(Entity entity);
}
