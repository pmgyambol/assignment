import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        while( true ) {
            OrganizerJSON json = new OrganizerJSON(args[0]);
            json.printTreeView();
            System.out.printf("\nЗа промени натиснете д(а); за изход произволен друг клавиш: ");
            Scanner in = new Scanner(System.in);
            String  st = in.nextLine();
            if( st.charAt(0) == 'д' ) {
                json.changeStatus();
            }
            else {
                break;
            }
        }
    }

}
