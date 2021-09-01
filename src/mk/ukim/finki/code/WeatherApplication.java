package mk.ukim.finki.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Subscriber{
    void update(float temperature,float humidity,float pressure);
}
class WeatherDispatcher{
    List<Subscriber> subscribers;

    public WeatherDispatcher(){
        this.subscribers = new ArrayList<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        for (Subscriber s : subscribers)
            s.update(temperature,humidity,pressure);
    }

    public void register(Subscriber s) {
        subscribers.add(s);
    }
    public void remove(Subscriber s){
        subscribers.remove(s);
    }
}
class CurrentConditionsDisplay implements Subscriber{

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher){
        weatherDispatcher.register(this);
    }
    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n",temperature,humidity);
    }
}
class ForecastDisplay implements Subscriber{

    float prevPressure = 0.0F;
    public ForecastDisplay(WeatherDispatcher weatherDispatcher){
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        if (pressure>prevPressure)
            System.out.println("Forecast: Improving");
        else if (pressure==prevPressure)
            System.out.println("Forecast: Same");
        else
            System.out.println("Forecast: Cooler");
        prevPressure = pressure;
    }
}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}