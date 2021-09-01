package mk.ukim.finki.av3;

public class InterestCheckingAccount extends Account implements InterestBearingAccount{
    public static double INTEREST_RATE = 0.03;

    public InterestCheckingAccount(String accountOwner, int ID, double currentAmount) {
        super(accountOwner, ID, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * INTEREST_RATE);
    }
}
