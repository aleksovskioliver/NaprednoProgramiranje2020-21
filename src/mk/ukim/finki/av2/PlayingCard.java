package mk.ukim.finki.av2;

import java.util.Objects;

public class PlayingCard {

    public enum TYPE{
        HEARTS,
        DIAMONDS,
        SPIDES,
        CLUBS
    }
    private TYPE cardType;
    private int rank;

    public PlayingCard(TYPE type,int rank){
        this.cardType = type;
        this.rank = rank;
    }

    public TYPE getCardType() {
        return cardType;
    }

    public void setCardType(TYPE cardType) {
        this.cardType = cardType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayingCard that = (PlayingCard) o;
        return rank == that.rank && cardType == that.cardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardType, rank);
    }

    @Override
    public String toString() {
        return String.format("%d %s",rank,cardType.toString());
    }
}
