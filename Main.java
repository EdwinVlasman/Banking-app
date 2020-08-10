package budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Account accessed!\n");
        var account = new Account(username);
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    System.out.println("\nBye!");
                    break;
                case 1:
                    account.addIncome(scanner);
                    break;
                case 2:
                    account.addPurchase(scanner);
                    break;
                case 3:
                    account.printShoppingList(scanner);
                    break;
                case 4:
                    account.getBalance();
                    break;
                case 5:
                    account.savePurchases();
                    break;
                case 6:
                    account.loadPurchases();
                    break;
                case 7:
                    account.sortChoice(scanner);
                    break;
            }
            if (choice == 0) {
                break;
            }
        }
    }

    public static void printMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
    }
}
