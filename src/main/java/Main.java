public class Main {
    public static void main(String[] args) {
        try {
            String filepath = args[0];
            String fromDate = args[1];
            String toDate = args[2];
            String merchant = args[3];
            Transaction transaction = new Transaction();
            transaction.showAverageTransaction(filepath, fromDate, toDate, merchant);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You need to pass 4 argument: path to file with it's name, from date, to date, merchant name");
        }
    }
}
