package mk.ukim.finki.av3;

import java.util.Objects;
import java.util.Random;

public abstract class Account {
    private String accountOwner;
    private int ID;
    private double currentAmount;

    public Account(String accountOwner, int ID, double currentAmount) {
        this.accountOwner = accountOwner;
        this.ID = ID;
       // Random random = new Random(); -> za ID
        this.currentAmount = currentAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }
    public void addAmount(double amount){
        this.currentAmount += amount;
    }
    public void withdrawAmount(double amount){
        if (this.currentAmount >= amount)
            currentAmount -= amount;
        else
            System.out.println("Nemate dovolno pari na smetkata!!!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ID == account.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
