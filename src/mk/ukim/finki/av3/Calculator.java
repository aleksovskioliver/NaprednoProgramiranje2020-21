package mk.ukim.finki.av3;

public class Calculator {
    private double result;
    private Strategy strategy;

    public Calculator(){
        this.result = 0.0;
    }
    public String execute(char operation,double value) throws UnknowOperatorExcetion {
        if (operation == '+') strategy = new Addition();
        else if (operation == '-') strategy = new Subtract();
        else throw new UnknowOperatorExcetion(operation);

        result = strategy.doOperation(result,value);
        return String.format("result %c %.2f = %.2f",operation,value,result);
    }

    public String init(){
        return String.format("Calculator is on.\nInit value is: %.2f",result);
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.format("updated result = %.2f",result);
    }
}
