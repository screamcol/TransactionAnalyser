import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Transaction {
    private static HashMap<String, String[]> matchedTransactions = new HashMap<>();
    private static long fromDate;
    private static long toDate;
    private static String merchant;
    private static ArrayList<String> reversalID = new ArrayList<>();


    public void showAverageTransaction(String filepath, String from, String to, String merch) {
        try {
            fromDate = parseDateAndGetMilliseconds(from);
            toDate = parseDateAndGetMilliseconds(to);
            merchant = merch;
            getFileAndSelectTransactions(filepath);
            excludeReversalTransactions();
            System.out.printf("Number of transactions = %d \n", matchedTransactions.size());
            System.out.printf("Average Transaction Value = %.2f", averageTransactionValue());

        } catch (ParseException e) {
            System.out.println("Check input date format. It should be: 'dd/M/yyyyhh:mm:ss'. Also check filepath, symbol '\\' should be doubled");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You need to pass 4 argument: path to file with it's name, from date, to date, merchant name");
        }
//        merchant = args[3];

    }

    private static void getFileAndSelectTransactions(String filepath) throws ParseException {
        try(CSVReader reader = new CSVReader(new FileReader(filepath))) {
            reader.skip(1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (containReversal(nextLine)) reversalID.add(nextLine[Columns.RELATED_TRANSACTION.getIndex()].trim());
                if (parseDateAndGetMilliseconds(nextLine[Columns.Date.getIndex()]) >= fromDate
                        && parseDateAndGetMilliseconds(nextLine[Columns.Date.getIndex()]) <= toDate
                        && nextLine[Columns.MERCHANT.getIndex()].trim().equals(merchant)) {
                    String[] arr = new String[nextLine.length - 2];
                    System.arraycopy(nextLine, 1, arr, 0, arr.length);
                    matchedTransactions.put(nextLine[Columns.ID.getIndex()], arr);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File wasn't found. Check file destination");
        } catch (CsvValidationException e) {
            System.out.println("Exception while parse CSV file");
        } catch (IOException e) {
            System.out.println("Error during opening file");
        }
    }

    private static long parseDateAndGetMilliseconds(String inputDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyyHH:mm:ss");
        Date date = formatter.parse(inputDate);
        return date.getTime();
    }

    private static boolean containReversal(String[] transaction) {
        AtomicBoolean contain = new AtomicBoolean(false);
        Arrays.stream(transaction).forEach((n) -> {
            if (n.trim().equals("REVERSAL")) contain.set(true);
        });
        return contain.get();
    }

    private static double averageTransactionValue() {
        final double[] sumOfTransactions = {0.0};
        matchedTransactions.forEach((key, value) -> sumOfTransactions[0] += sumOfTransactions[0] + Double.parseDouble(value[1].trim()));
        return sumOfTransactions[0] / matchedTransactions.size();
    }

    private static void excludeReversalTransactions() {
        for (String str : reversalID) {
            matchedTransactions.entrySet().removeIf(e -> e.getKey().equals(str));
        }
    }
}
