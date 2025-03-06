import com.mysite.banking.view.ConsoleUI;

public class ApplicationRunner {
    public static void main(String[] args) {

        try (ConsoleUI consoleUI = new ConsoleUI()){
            consoleUI.startMenu();
        }
        catch (Throwable ex){
            ex.getCause();
            System.out.println("System error!");
        }
    }
}
