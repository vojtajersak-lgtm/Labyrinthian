package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

public abstract class Weapon extends Item {
    private double damage;
    private int attackSpeed;
    private double range;

    public Weapon(String name, String description, double damage, int attackSpeed, double range) {
        super(name, description);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    @Override
    public void use(Player player, GameManager gameManager) {
    }

    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.setActiveweapon(this);
    }
}
