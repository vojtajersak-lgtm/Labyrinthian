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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

}
