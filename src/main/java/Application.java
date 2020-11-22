import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Controller controller = new Controller(args);
        Scanner in = new Scanner(System.in);
        System.out.print("Введите строку: ");
        String userInput = in.nextLine();
        long startTime = System.currentTimeMillis();
        String[] airports = controller.findAirports(userInput);
        long endTime = System.currentTimeMillis();
        for (String airport : airports) {
            System.out.println(airport);
        }
        System.out.println("Количество найденных строк: " + airports.length);
        System.out.println("Время, затраченное на поиск: " + (endTime - startTime) + "ms");
    }
}
