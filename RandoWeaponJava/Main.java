import characters.*;
import characters.Character;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        String characterChoice;
        Character player;
        String name;

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n~| R A N D O W E A P O N |~\n");
        System.out.print("Enter your fighter's name: ");
        name = scanner.nextLine();
        
        do {
            System.out.println("Ninja\tTank\tKnight");
            System.out.print("Choose your character type: ");
            characterChoice = scanner.nextLine();

            if(characterChoice.equalsIgnoreCase("ninja")) {
                player = new Ninja(name);
                break;
            }
            else if(characterChoice.equalsIgnoreCase("tank")) {
                player = new Tank(name);
                break;
            }
            else if(characterChoice.equalsIgnoreCase("knight")) {
                player = new Knight(name);
                break;
            }
            else {
                System.out.println("Invalid response. Try again.\n");
            }
        } while(true);

        String action;

        Enemy enemy = new Enemy();

        boolean newGame = true;

        while(true) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nKills: " + player.getKillCount());
            System.out.println("----------------------------------------------------------------");

            if(!newGame) {
                Character.printEvents();
                
                if(player.getCurrentHP() == 0) {
                    break;
                }
                else {
                    System.out.println();
                }
            }
            else {
                newGame = false;
            }

            if(enemy.getCurrentHP() == 0) {
                System.out.println("ENEMY DEFEATED...");
                enemy = new Enemy();
                System.out.println("...BUT A NEW CHALLENGER HAS EMERGED\n");
            }

            System.out.println("PLAYER STATUS: ");
            player.printStatus();
            System.out.println("\nENEMY STATUS: ");
            enemy.printStatus();

            System.out.println("----------------------------------------------------------------");

            boolean validResponse;
            
            do{
                validResponse = true;

                System.out.println("\nAttack\tBlock\tSwitch");
                System.out.print("Choose an action: ");
                action = scanner.nextLine();

                if(action.equalsIgnoreCase("attack")) {
                    player.attack(enemy);
                }
                else if(action.equalsIgnoreCase("block")) {
                    player.block();
                }
                else if(action.equalsIgnoreCase("switch")) {
                    player.switchWeapon();
                }
                else {
                    validResponse = false;
                }
            }while(!validResponse);

            enemy.act(player);
            Character.executeActions(player, enemy);
        }

        System.out.println("----------------------------------------------------------------\n");
        System.out.println("~| G A M E O V E R |~\n");
        System.out.println(player.getName() + " slayed " + player.getKillCount() + " enemies.\n");

        scanner.close();
    }
}
