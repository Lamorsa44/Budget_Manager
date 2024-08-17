package budget;

import java.io.*;
import java.util.*;

public class Logic implements Serializable {
    private static final long serialVersionUID = 1L;
    static transient Scanner scanner = new Scanner(System.in);
    Categories categories = new Categories();
    double balance = 0;

    public void addIncome() {
        System.out.println("Enter income:");
        balance += Double.parseDouble(scanner.nextLine());
        System.out.println("Income was added!\n");
    }

    public void addPurchase() {
        while (true) {
            System.out.print("""
                    Choose the type of purchase
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other
                    5) Back
                    """);

            String action = scanner.nextLine();

            if (action.equals("5"))
                break;
            else if (!(Integer.parseInt(action) >= 1 && Integer.parseInt(action) < 5)) {
                continue;
            }

            System.out.println("Enter purchase name:");
            String purchase = scanner.nextLine();

            System.out.println("Enter its price:");
            double price = Double.parseDouble(scanner.nextLine());

            categories.add(action, purchase, price);

            balance -= price;
            System.out.println("Purchase was added!\n");
        }
    }

    public void printPurchases() {
        while (true) {
            System.out.print("""
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other
                    5) All
                    6) Back
                    """);

            String action = scanner.nextLine();
            System.out.println();

            if (action.equals("6"))
                break;

            categories.printCategory(action);
        }
    }

    public void printBalance() {
        System.out.printf("Balance: $%.2f\n\n", balance);
    }

    public void printAnalyze() {
        while (true) {
            System.out.print("""
                    How do you want to sort?
                    1) Sort all purchases
                    2) Sort by type
                    3) Sort certain type
                    4) Back
                    """);

            String answer = scanner.nextLine();
            System.out.println();

            if (answer.equals("4"))
                break;

            switch (answer) {
                case "1" -> {
                    var yea = categories.getProducts("");
                    var sorted = new TreeMap<String, Double>(
                            (key1, key2) -> {
                                int comp = yea.get(key2).compareTo(yea.get(key1));
                                if (comp == 0) {
                                    return key2.compareTo(key1);
                                }
                                return comp;
                            }
                    );

                    sorted.putAll(yea);

                    if (yea.isEmpty()) {
                        System.out.println("The purchase list is empty\n");
                    } else {
                        System.out.println("All: ");

                        sorted.forEach((purchase, price) ->
                                System.out.printf("%s $%.2f\n", purchase, price));

                        double sum = sorted.values().stream().reduce(0.0, Double::sum);
                        System.out.printf("Total sum: $%.2f\n\n", sum);
                    }
                }
                case "2" -> {
                    double food = categories.getProducts("1")
                            .values().stream().reduce(0.0, Double::sum);
                    double clothes = categories.getProducts("2")
                            .values().stream().reduce(0.0, Double::sum);
                    double entertainment = categories.getProducts("3")
                            .values().stream().reduce(0.0, Double::sum);
                    double other = categories.getProducts("4")
                            .values().stream().reduce(0.0, Double::sum);
                    double total = food + clothes + entertainment + other;

                    var original = new HashMap<String, Double>(Map.ofEntries(
                            Map.entry("Food", food),
                            Map.entry("Clothes", clothes),
                            Map.entry("Entertainment", entertainment),
                            Map.entry("Other", other)
                    ));

                    var result = new TreeMap<String, Double>(
                            (key1, key2) -> {
                                int tmp = original.get(key2).compareTo(original.get(key1));
                                if (tmp == 0) {
                                    return key2.compareTo(key1);
                                }
                                return tmp;
                            });

                    result.putAll(original);
                    System.out.println("Types:");
                    result.forEach((key, value) -> System.out.printf("%s - $%.2f\n", key, value));
                    System.out.printf("Total sum: $%.2f\n\n", total);
                }
                case "3" -> {
                    System.out.print("""
                            Choose the type of purchase
                            1) Food
                            2) Clothes
                            3) Entertainment
                            4) Other
                            """);

                    String typeOfPurchase = scanner.nextLine();
                    System.out.println();

                    var yea = categories.getProducts(typeOfPurchase);
                    if (yea.isEmpty())
                        System.out.println("The purchase list is empty\n");
                    else {
                        var sorted = new TreeMap<String, Double>(
                                (key1, key2) -> {
                                    int comp = yea.get(key2).compareTo(yea.get(key1));
                                    if (comp == 0) {
                                        return key2.compareTo(key1);
                                    }
                                    return comp;
                                }
                        );

                        sorted.putAll(yea);

                        double total = sorted.values().stream().reduce(0.0, Double::sum);

                        System.out.println(switch (typeOfPurchase) {
                            case "1" -> "Food:";
                            case "2" -> "Clothes:";
                            case "3" -> "Entertainment:";
                            case "4" -> "Other:";
                            default -> "idk";
                        });
                        sorted.forEach((key, value) -> System.out.printf("%s $%.2f\n", key, value));
                        System.out.printf("Total sum: $%.2f\n\n", total);
                    }
                }
            }
        }
    }

    private class Categories implements Serializable {
        HashMap<String, Double> food = new HashMap<String, Double>();
        HashMap<String, Double> clothes = new HashMap<String, Double>();
        HashMap<String, Double> entertainment = new HashMap<String, Double>();
        HashMap<String, Double> other = new HashMap<String, Double>();

        void add(String category, String purchase, double price) {
            switch (category) {
                case "1" -> {
                    food.put(purchase,
                            food.getOrDefault(purchase, 0.0) + price);
                }
                case "2" -> {
                    clothes.put(purchase,
                            clothes.getOrDefault(purchase, 0.0) + price);
                }
                case "3" -> {
                    entertainment.put(purchase,
                            entertainment.getOrDefault(purchase, 0.0) + price);
                }
                case "4" -> {
                    other.put(purchase,
                            other.getOrDefault(purchase, 0.0) + price);
                }
            }
        }

        HashMap<String, Double> getProducts(String category) {
            HashMap<String, Double> listOfPurchases = switch (category) {
                case "1" -> food;
                case "2" -> clothes;
                case "3" -> entertainment;
                case "4" -> other;
                default -> {
                    var tmp = new HashMap<String, Double>();
                    var list = List.of(food, clothes, entertainment, other);

                    for (HashMap<String, Double> map : list) {
                        tmp.putAll(map);
                    }

                    yield tmp;
                }
            };
            return listOfPurchases;
        }

        void printCategory(String category) {
            var listOfPurchases = getProducts(category);

            if (listOfPurchases.isEmpty()) {
                System.out.println("The purchase list is empty\n");
            } else {
                System.out.println(switch (category) {
                    case "1" -> "Food";
                    case "2" -> "Clothes";
                    case "3" -> "Entertainment";
                    case "4" -> "Other";
                    default -> "All";
                });

                listOfPurchases.forEach((purchase, price) ->
                        System.out.printf("%s $%.2f\n", purchase, price));

                double sum = listOfPurchases.values().stream().reduce(0.0, Double::sum);
                System.out.printf("Total sum: $%.2f\n\n", sum);
            }
        }

    }

    public static void printActions() {
        System.out.print("""          
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit
                    """);
    }
}

