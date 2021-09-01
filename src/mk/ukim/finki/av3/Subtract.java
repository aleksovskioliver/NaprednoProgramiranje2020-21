package mk.ukim.finki.av3;

public class Subtract implements Strategy{
    @Override
    public double doOperation(double x, double y) {
        return x-y;
    }
}
