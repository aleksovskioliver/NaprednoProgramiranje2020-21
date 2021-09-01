package mk.ukim.finki.av3;

public class UnknowOperatorExcetion extends Exception{
    public UnknowOperatorExcetion(char operator) {
        super(String.format("unknow operator is : %c",operator));
    }
}
