package DataBase;

public class Dbparser {
    class DataToInsert {
        private final String accountName;
        private final String accountNumber;
        private final String accountDomain;

        public DataToInsert(String accountName, String accountNumber, String accountDomain) {
            this.accountName = accountName;
            this.accountNumber = accountNumber;
            this.accountDomain = accountDomain;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountDomain() {
            return accountDomain;
        }
    }
}

