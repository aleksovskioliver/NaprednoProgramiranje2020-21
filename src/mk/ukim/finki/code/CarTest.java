package mk.ukim.finki.code;

import java.util.*;
import java.util.stream.Collectors;

class Car implements Comparable<Car>{
    String manufacturer;
    String model;
    int price;
    float power;

    public Car(String manufacturer, String model, int price, float power) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.power = power;
    }

    //Citroen C2 (64KW) 12900
    @Override
    public String toString() {
        return String.format("%s %s (%.0fKW) %d",manufacturer,model,power,price);
    }

    @Override
    public int compareTo(Car o) {
        return this.model.compareTo(o.model);
    }
    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getPrice() {
        return price;
    }

    public float getPower() {
        return power;
    }
}
class CarCollection{
    List<Car> cars;
    //Map<Manufacturer,TreeSet<Cars>>
    Map<String ,TreeSet<Car>> carsByManufacturer;

    public CarCollection(){
        this.cars = new ArrayList<>();
        this.carsByManufacturer = new HashMap<>();
    }
    public void addCar(Car car){
        cars.add(car);

        carsByManufacturer.putIfAbsent(car.getManufacturer().toLowerCase(),new TreeSet<>());
        carsByManufacturer.computeIfPresent(car.getManufacturer().toLowerCase(), (k,v)->{
            v.add(car);
            return v;
        });
    }
    public void sortByPrice(boolean asceding){
        Comparator<Car> carComparator = Comparator.comparing(Car::getPrice)
                .thenComparing(Car::getPower);
        if (asceding)
            cars.sort(carComparator);
        else
            cars.sort(carComparator.reversed());
    }
    public List<Car> filterByManufacturer(String manufacturer){
        return carsByManufacturer
                .get(manufacturer.toLowerCase())
                .stream()
                .collect(Collectors.toList());
    }
    public List<Car> getList(){
        return cars;
    }
}

public class CarTest {
    public static void main(String[] args) {
        CarCollection carCollection = new CarCollection();
        String manufacturer = fillCollection(carCollection);
        carCollection.sortByPrice(true);
        System.out.println("=== Sorted By Price ASC ===");
        print(carCollection.getList());
        carCollection.sortByPrice(false);
        System.out.println("=== Sorted By Price DESC ===");
        print(carCollection.getList());
        System.out.printf("=== Filtered By Manufacturer: %s ===\n", manufacturer);
        List<Car> result = carCollection.filterByManufacturer(manufacturer);
        print(result);
    }

    static void print(List<Car> cars) {
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    static String fillCollection(CarCollection cc) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            if(parts.length < 4) return parts[0];
            Car car = new Car(parts[0], parts[1], Integer.parseInt(parts[2]),
                    Float.parseFloat(parts[3]));
            cc.addCar(car);
        }
        scanner.close();
        return "";
    }
}


// vashiot kod ovde