package budget;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Account {

    private final String username;
    private final List<Purchase> purchaseList;
    private final Map<String, Double> purchaseTypeTotalCost;
    private final Map<Integer, String> purchaseChoices;
    private final Map<Integer, String> purchaseListTypes;
    private double balance;
    private File file;

    public Account(String username) {
        this.username = username;
        this.purchaseList = new ArrayList<Purchase>();
        this.purchaseTypeTotalCost = new HashMap<>();
        this.purchaseTypeTotalCost.put("Food" , 0.0);
        this.purchaseTypeTotalCost.put("Entertainment" , 0.0);
        this.purchaseTypeTotalCost.put("Clothes" , 0.0);
        this.purchaseTypeTotalCost.put("Other" , 0.0);
        this.purchaseChoices = Map.of(
                1, "Food",
                2, "Clothes",
                3, "Entertainment",
                4, "Other",
                5, "Back"
        );
        this.purchaseListTypes = Map.of(
                1, "Food",
                2, "Clothes",
                3, "Entertainment",
                4, "Other",
                5, "All",
                6, "Back"
        );
        this.balance = 0.0;
        this.file = new File(".\\" + this.username + ".txt");
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    public void addIncome(java.util.Scanner scanner) {
        System.out.println("\nEnter Income:");
        double income = scanner.nextDouble();
        scanner.nextLine();
        setBalance(income);
        System.out.println("Income was added!\n");
    }

    public void addPurchase(java.util.Scanner scanner) {
        int choice;
        String type;
        while (true) {
            System.out.println("\nChoose the type of purchases\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) Back");
            choice = scanner.nextInt();
            scanner.nextLine();
            type = purchaseChoices.getOrDefault(choice, "Wrong choice!");
            if ("Back".equals(type)) {
                System.out.println();
                break;
            } else if ("Wrong choice".equals(type)) {
                System.out.println(type);
                continue;
            }
            System.out.println("\nEnter purchase name:");
            String description = scanner.nextLine();
            System.out.println("Enter its price:");
            double cost = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Purchase was added!");
            var purchase = new Purchase(description, cost, type);
            this.purchaseList.add(purchase);
            if (this.balance - cost < 0) {
                this.balance = 0;
            } else {
                this.balance -= cost;
            }
            purchaseTypeTotalCost.put(type, purchaseTypeTotalCost.getOrDefault(type, 0.0) + cost);
            purchaseTypeTotalCost.put("All", purchaseTypeTotalCost.getOrDefault("All", 0.0) + cost);
        }
    }

    private void addPurchase(String type, String description, double cost) {
        var purchase = new Purchase(description, cost, type);
        this.purchaseList.add(purchase);
        purchaseTypeTotalCost.put(type, purchaseTypeTotalCost.getOrDefault(type, 0.0) + cost);
        purchaseTypeTotalCost.put("All", purchaseTypeTotalCost.getOrDefault("All", 0.0) + cost);

    }

    private void printShoppingList(String type) {
        if ("All".equals(type)) {
            for (Purchase purchase : purchaseList) {
                purchase.printPurchase();
            }
        } else {
            for (Purchase purchase : purchaseList) {
                if (purchase.getType().equals(type)) {
                    purchase.printPurchase();
                }
            }
        }
        System.out.printf("Total sum: $%.2f\n", purchaseTypeTotalCost.get(type));
    }

    public void printShoppingList(java.util.Scanner scanner) {
        if (purchaseTypeTotalCost.getOrDefault("All", 0.0) == 0) {
            System.out.println("\nPurchase list is empty!\n");
            ;
        } else {
            int choice;
            String type;
            while (true) {
                System.out.println("\nChoose the type of purchases\n" +
                        "1) Food\n" +
                        "2) Clothes\n" +
                        "3) Entertainment\n" +
                        "4) Other\n" +
                        "5) All\n" +
                        "6) Back");
                choice = scanner.nextInt();
                type = purchaseListTypes.getOrDefault(choice, "Wrong choice!");
                if ("Back".equals(type)) {
                    System.out.println();
                    break;
                } else if (purchaseTypeTotalCost.getOrDefault(type, 0.0) == 0) {
                    System.out.println("\n" + type + ":\n" +
                            "Purchase list is empty!");
                } else if ("Wrong choice".equals(type)) {
                    System.out.println("\nWrong choice!");
                } else {
                    System.out.println("\n" + type + ":");
                    printShoppingList(type);
                }
            }
        }
    }

    public void getBalance() {
        System.out.printf("\nBalance: $%.2f\n\n", this.balance);
    }

    public void savePurchases() {
        try (PrintWriter printWriter = new PrintWriter(this.file)) {
            printWriter.println(this.balance);
            for (Purchase purchase : purchaseList) {
                printWriter.println(purchase.getType() + "?"
                + purchase.getDescription() + "?"
                + purchase.getCost());
            }
            System.out.println("\nPurchases were saved!\n");
        } catch (IOException e) {
            System.out.printf("An exception occurs %s\n", e.getMessage());
        }
    }

    public void loadPurchases() {
        try (Scanner scanner = new Scanner(this.file)) {
            this.balance = Double.parseDouble(scanner.nextLine());
            while (scanner.hasNext()) {
                String[] savedPurchase = scanner.nextLine().split("\\?");
                String type = savedPurchase[0];
                String description = savedPurchase[1];
                double cost = Double.parseDouble(savedPurchase[2]);
                addPurchase(type, description, cost);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("An exception occurs %s\n", e.getMessage());
        }
        System.out.println("\nPurchases were loaded!\n");
    }
    // Method for handling the sorting choice part of the app
    public void sortChoice(java.util.Scanner scanner) {
        while (true) {
            System.out.println("\nHow do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) sort by type\n" +
                    "3) sort certain type\n" +
                    "4) Back");
            int choice = scanner.nextInt();
//            scanner.nextLine();
            switch (choice) {
                case 1:
                    if (purchaseList.isEmpty()) {
                        System.out.println("\nPurchase list is empty!");
                    } else {
                        sortAll();
                        System.out.println();
                        printShoppingList("All");
                    }
                    break;
                case 2:
                    sortType();
                    break;
                case 3:
                    sortCertainType(scanner);
                    break;
                case 4:
                    System.out.println();
                    break;
                default:
                    System.out.println("Wrong choice!");
                    break;
            }
            if (choice == 4) {
                break;
            }
        }
    }
    // bubble sort array of purchases
    private void sortAll() {
        for (int i = 0; i < this.purchaseList.size() - 1; i++) {
            for (int j = 0; j < this.purchaseList.size() - 1 - i; j++) {
                if ((this.purchaseList.get(j).getCost()) < (this.purchaseList.get(j + 1).getCost())) {
                    Purchase temp = purchaseList.get(j + 1);
                    purchaseList.remove(j + 1);
                    purchaseList.add(j, temp);
                }
            }
        }
    }

    // sort map via streams
    private void sortType() {
        System.out.print("\nTypes:");
        final Map<String, Double> sortedByCount = purchaseTypeTotalCost.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : sortedByCount.entrySet()) {
            if (!"All".equals(entry.getKey())) {
                System.out.printf("\n%s - $%.2f",entry.getKey(),entry.getValue());
            }
        }
        System.out.printf("\nTotal sum: $%.2f\n", purchaseTypeTotalCost.getOrDefault("All", 0.0));
    }

    // The menu for the option "Sort certain type". Using the other methods to actually sort.
    private void sortCertainType(java.util.Scanner scanner) {
        int choice;
        String type;
        while (true) {
            System.out.println("\nChoose the type of purchases\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other");
            choice = scanner.nextInt();
//            scanner.nextLine();
            type = purchaseListTypes.getOrDefault(choice, "Wrong choice!");
            if (choice > 4) {
                System.out.println("\nWrong choice!");
                break;
            } else if (purchaseTypeTotalCost.getOrDefault(type, 0.0) == 0) {
                System.out.println("\nPurchase list is empty!");
                break;
            } else {
                sortAll();
                System.out.println("\n" + type + ":");
                printShoppingList(type);
                break;
            }
        }
    }

}
// Class for keeping the purchase.
class Purchase {

    private final String description;
    private final double cost;
    private final String type;

    public Purchase(String description, double cost, String type) {
        this.description = description;
        this.cost = cost;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public void printPurchase() {
        System.out.printf(this.description + " $%.2f\n", this.cost);
    }
}
