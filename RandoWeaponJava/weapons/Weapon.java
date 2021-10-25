package weapons;

public abstract class Weapon {
    private int damage;
    private double baseAccuracy;
    private int swingsLeft;

    public Weapon(int damage, double baseAccuracy, int swingsLeft) {
        this.damage = damage;
        this.baseAccuracy = baseAccuracy;
        this.swingsLeft = swingsLeft;
    }

    public int getDamage() {
        return damage;
    }

    public double getAccuracy() {
        return baseAccuracy;
    }

    public int getSwingsLeft() {
        return swingsLeft;
    }

    public int decrementSwings() {
        return --swingsLeft;
    }

    public String toString() {
        return "WEAPON";
    }
}
