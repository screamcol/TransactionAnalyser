# Task

Once initialised, the system should report the total number of transactions and the average transaction value for a specific merchant in a specific date range.
An additional requirement is that, if a transaction record has a REVERSAL transaction, then it should not be included in the computed statistics, even if the reversing transaction is outside of the requested date range.

#Solution
To run program, you need to run .jar file with such parameters: filepath, fromDate, toDate, nameOfMerchant.
Example: D:\\inputfile.csv, 20/08/201812:00:00, 20/08/201813:00:00, Kwik-E-Mart
Note, that need to escape symbol '\' in filepath. Alsoo pass date without space
