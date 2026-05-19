package cz.cvut.fel.pjv.labyrinthian.Items.Weapon;

import cz.cvut.fel.pjv.labyrinthian.Core.GameManager;
import cz.cvut.fel.pjv.labyrinthian.Entities.Player;
import cz.cvut.fel.pjv.labyrinthian.Items.Item;

/**
 * Abstract base class for all weapons.
 * Weapons are picked up by equipping them directly (replacing the current weapon).
 * Unlike consumables, weapons have their separate slot and are not stored in the 5-slot inventory.
 */
public abstract class Weapon extends Item {
    private double damage;
    private final int attackSpeed;
    private double range;


    public Weapon(String name, String description, double damage, int attackSpeed, double range) {
        super(name, description);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }
    public void setRange(double range) { this.range = range; }

    /** Weapons have no active use effect. */
    @Override
    public void use(Player player, GameManager gameManager) {}

    /**
     * Equips this weapon as the player's active weapon.
     *
     * @param player      the player to equip the weapon to
     * @param gameManager the game manager
     */
    @Override
    public void onInteraction(Player player, GameManager gameManager) {
        player.setActiveweapon(this);
    }
}
