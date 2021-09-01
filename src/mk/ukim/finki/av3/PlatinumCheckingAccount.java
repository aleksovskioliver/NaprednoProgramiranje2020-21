package mk.ukim.finki.av3;

public class PlatinumCheckingAccount extends Account implements InterestBearingAccount{
    public PlatinumCheckingAccount(String accountOwner, int ID, double currentAmount) {
        super(accountOwner, ID, currentAmount);
    }

    @Override
    public void addInterest() {
        addAmount(getCurrentAmount() * (InterestCheckingAccount.INTEREST_RATE*2));
    }
}
