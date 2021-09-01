package mk.ukim.finki.av3;

import java.util.Arrays;

public class Bank {
    public Account[] accounts;
    public int maxAccounts;
    public int totalAccounts;

    public Bank(int maxAccounts) {
        this.maxAccounts = maxAccounts;
        this.totalAccounts = 0;
        accounts = new Account[maxAccounts];
    }
    public void addAccount(Account account){
        for (Account account1 : accounts){
            if (account1.equals(account)) return;
        }
        if (totalAccounts == maxAccounts) {
            accounts = Arrays.copyOf(accounts, maxAccounts * 2);
            maxAccounts *= 2;
        }
        accounts[totalAccounts++] = account;
    }
    public void addInterestToAllAccounts(){
        for (Account account : accounts){
            if (account instanceof InterestBearingAccount){
                InterestBearingAccount interestBearingAccount = (InterestBearingAccount) account;
                interestBearingAccount.addInterest();
            }
        }
    }
    public double totalAssets(){
        double sum = 0.0;
        for (Account account : accounts){
            sum += account.getCurrentAmount();
        }
        return sum;
    }

    public static void main(String[] args) {

    }
}
