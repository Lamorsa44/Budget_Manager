package budget;

import java.io.*;
import java.util.*;

public class Main {
    private static Logic logic;
    static String in = "purchases.txt";
    static String out = "purchases.txt";

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        if (logic == null)
            logic = new Logic();

        while (true) {
            Logic.printActions();

            String answer = scanner.nextLine();
            System.out.println();

            switch (answer) {
                case "1" -> {
                    logic.addIncome();
                }
                case "2" -> {
                    logic.addPurchase();
                }
                case "3" -> {
                   logic.printPurchases();
                }
                case "4" -> {
                    logic.printBalance();
                }
                case "5" -> {
                    try (var yes = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(out)))) {
                        yes.writeObject(logic);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Purchases were saved!\n");
                }
                case "6" -> {
                    try (var yes = new ObjectInputStream(new BufferedInputStream(new FileInputStream(in)))){
                        logic = (Logic) yes.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Purchases were loaded!\n");
                }
                case "7" -> {
                    logic.printAnalyze();
                }
                case "0" -> {
                    System.out.println("Bye!");

                    scanner.close();
                    return;
                }
            }
        }
    }
}
