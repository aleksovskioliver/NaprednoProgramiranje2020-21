package mk.ukim.finki.code;
import java.util.*;
import java.util.stream.IntStream;

class Sector{
    String code;
    int size;
    //Map<seat,type>
    Map<Integer,Integer> seadAndType;

    public Sector(String code, int size) {
        this.code = code;
        this.size = size;
        this.seadAndType = new TreeMap<>();
    }
    //E	1000/1000	0.0%

    int emptySeats(){
        return this.size - seadAndType.size();
    }

    public String getCode() {
        return code;
    }

    double occupancy(){
        return (seadAndType.size() * 100.0) / size;
    }
    //zafateni/kapacitet
    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",
                code,
                emptySeats(),
                size,
                occupancy());
    }
    void buyTicket(int seat,int type) throws SeatNotAllowedException {
        if (type == 1){
            if (seadAndType.containsValue(2))
                throw new SeatNotAllowedException();
        }
        if (type==2){
            if (seadAndType.containsValue(1))
                throw new SeatNotAllowedException();
        }
        seadAndType.put(seat,type);
    }
}
class Stadium{
    String name;
    //Map<sectorName,Sector>
    Map<String ,Sector> sectorByName;

    public Stadium (String name){
        this.name = name;
        this.sectorByName = new HashMap<>();
    }
    void createSectors(String[] sectorNames,int[] sizes){
        for (int i=0;i<sectorNames.length;i++) {
            Sector sector = new Sector(sectorNames[i],sizes[i]);
            sectorByName.put(sectorNames[i], sector);
        }
       // IntStream.range(0,sectorNames.length)
        //        .forEach(i->sectorByName.put(sectorNames[i],new Sector(sectorNames[i],sizes[i]); ) ));
    }
    void buyTicket(String sectorName,int seat,int type) throws SeatTakenException, SeatNotAllowedException {
        Sector sector = sectorByName.get(sectorName);
        if (sector.seadAndType.containsKey(seat))
            throw new SeatTakenException();
        sector.buyTicket(seat,type);
        sectorByName.put(sectorName,sector);
    }
    void showSectors(){
        Comparator<Sector> comparator = Comparator.comparing(Sector::emptySeats).reversed()
                .thenComparing(Sector::getCode);
        sectorByName.values()
                .stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }
}
class SeatTakenException extends Exception{

}
class SeatNotAllowedException extends Exception{

}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
