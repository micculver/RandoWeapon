package characters;

import weapons.*;

public class Character {
    private String name;
    private int currentHP;
    private int maxHP;
    private double accuracyPercentage; //determines probability of successful hit
    private Weapon currentWeapon;
    private boolean isBlocking; //if true and take damage, damage is lightened
    private double upperBound; //upperBound and lowerBound are multiplied with an attack's damage to add an element of randomness
    private double lowerBound;

    private int killCount;

    private static String[] events = new String[20];
    private static int eventCount = 0;

    private static int[] actionQueue = new int[2];
    private static int setIndex = 0;
    private int playerIndex;

    private Character currentTarget;

    public Character(String name, int maxHP, double accuracyPercentage, double upperBound, double lowerBound) {
        this.name = name.toUpperCase();
        this.currentHP = maxHP;
        this.maxHP = maxHP;
        this.accuracyPercentage = accuracyPercentage;
        this.currentWeapon = new Fist();
        this.isBlocking = false;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.killCount = 0;

        this.playerIndex = Character.setIndex;
        setIndex++;
    }

    public String getName() {
        return this.name;
    }

    //ATTACK action code = 1
    public void attack(Character target) {
        Character.addAction(1, this.playerIndex);
        currentTarget = target;
    }

    //deal damage to target character, return amount of damage dealt
    private int performAttack() {
        int random = (int)Math.floor((Math.random() * 100) + 1);
        int damage;

        //deal no damage when this character misses
        if(random > this.calculateAccuracy()) {
            damage = 0;

            addEvent(this.name + " missed their attack against " + currentTarget.getName());
        }
        else {
            damage = currentTarget.takeDamage(this.calculateDamage());

            addEvent(this.name + " attacked " + currentTarget.getName() + " with a "
                 + this.currentWeapon.toString() + " (-" + damage + " HP)");

            if(currentTarget.getCurrentHP() == 0) {
                this.killCount++;
            }
        }

        //decrement swings left if this character's current weapon is not a FIST
        if(currentWeapon.getSwingsLeft() != -1) {
            currentWeapon.decrementSwings();

            //change weapon to fist when current weapon's swings run out
            if(currentWeapon.getSwingsLeft() == 0) {
                currentWeapon = new Fist();

                addEvent(name + " has switched their weapon to " + currentWeapon.toString().toUpperCase());
            }
        }

        //set to null until another attack is performed
        currentTarget = null;

        return damage;
    }

    //Character loses HP, returns amount of HP lost
    private int takeDamage(int damageDealt) {
        if(this.isBlocking) {
            //damage taken is lowered if this character is currently blocking
            this.isBlocking = false;
            damageDealt *= (Math.random() * 0.35) + 0.4;
        }
        
        if(damageDealt >= currentHP) {
            //DIE
            damageDealt = this.currentHP;
            this.currentHP = 0;
            Character.addEvent(this.name + " has died.");
            setIndex--;
         }
         else {
            //survive an injury
            this.currentHP -= damageDealt;
        }

        return damageDealt;
    }

    private int calculateDamage() {
        //damage = weapon's base damage * random number between lowerBound and upperBound
        return (int)Math.round(currentWeapon.getDamage() * ((Math.random() * (upperBound - lowerBound)) + lowerBound));
    }

    private double calculateAccuracy() {
        return currentWeapon.getAccuracy() * this.accuracyPercentage;
    }

    //SWITCH action code = 2
    public void switchWeapon() {
        Character.addAction(2, this.playerIndex);
    }
    
    private void performSwitch() {

        int random = (int)Math.floor((Math.random() * 100) + 1); //random integer 1-100

        //30% chance of rolling Knife
        if(random <= 30) {
            currentWeapon = new Knife();
        }
        //20% chance of rolling Sword
        else if(random <= 50) {
            currentWeapon = new Sword();
        }
        //35% chance of rolling Bat
        else if(random <= 85) {
            currentWeapon = new Bat();
        }
        //10% chance of rolling Sledgehammer
        else if(random <= 95) {
            currentWeapon = new Sledgehammer();
        }
        //5% chance of rolling LaserSword
        else if(random <= 100) {
            currentWeapon = new LaserSword();
        }

        addEvent(this.name + " switched their weapon to " + this.currentWeapon.toString());
    }

    public int getSwingsLeft() {
        return currentWeapon.getSwingsLeft();
    }

    //BLOCK action code = 0
    public void block() {
        Character.addAction(0, this.playerIndex);
    }

    private void performBlock() {
        this.isBlocking = true;
        addEvent(this.name + " used BLOCK"); 
    }

    public int getCurrentHP() {
        return this.currentHP;
    }

    public String toStringHP() {
        return currentHP + "/" + maxHP;
    }

    public String getCurrentWeapon() {
        return this.currentWeapon.toString();
    }

    public int getKillCount() {
        return this.killCount;
    }

    public void printStatus() {
        System.out.println("Health = " + this.toStringHP());
        System.out.println("Current weapon = " + this.getCurrentWeapon());
        if(!this.currentWeapon.toString().equals("FIST")) {
            System.out.println("Swings left = " + this.getSwingsLeft());
        }
        else {
            System.out.println("Swings left = Infinity");
        }
    }

    public static void printEvents() {
        for(int i = 0; i < eventCount; i++) {
            System.out.println(events[i]);
            events[i] = null;
        }
        eventCount = 0;
    }

    private static void addEvent(String event) {
        events[eventCount] = event;
        eventCount++;
    }

    private static void addAction(int action, int index) {
        Character.actionQueue[index] = action;
    }

    //prevents a player from attacking before the target has performed their block
    public static void executeActions(Character player1, Character player2) {
        Character first;
        Character second;
        
        if(actionQueue[player1.playerIndex] <= actionQueue[player2.playerIndex]) {
            first = player1;
            second = player2;
        }
        else {
            first = player2;
            second = player1;
        }

        if(actionQueue[first.playerIndex] == 0) {
            first.performBlock();
        }
        else if(actionQueue[first.playerIndex] == 1) {
            first.performAttack();
        }
        else if(actionQueue[first.playerIndex] == 2){
            first.performSwitch();
        }

        if(actionQueue[second.playerIndex] == 0) {
            second.performBlock();
        }
        else if(actionQueue[second.playerIndex] == 1) {
            second.performAttack();
        }
        else if(actionQueue[second.playerIndex] == 2){
            second.performSwitch();
        }

        player1.isBlocking = false;
        player2.isBlocking = false;

        for(int i = 0; i < actionQueue.length; i++) {
            actionQueue[i] = -1;
        }
    }
}

