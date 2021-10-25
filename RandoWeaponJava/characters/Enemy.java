package characters;

public class Enemy extends Character {
    public Enemy() {
        super("Enemy", (int)(Math.floor(Math.random() * 50) + 100), 1, 1, 1.2);
    }

    public void act(Character target) {
        int rand = (int)(Math.floor(Math.random() * 3));

        if(rand == 0) {
            this.attack(target);
        }
        else if(rand == 1) {
            this.switchWeapon();
        }
        else {
            this.block();
        }
    }
}
