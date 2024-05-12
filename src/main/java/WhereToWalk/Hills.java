package WhereToWalk;

import WhereToWalk.loc.LocationDistance;
import WhereToWalk.sorting.Default;
import WhereToWalk.weather.WeatherForecast;

import java.util.*;


public class Hills {
    public static class ResourceMissingException extends RuntimeException{};
    private Comparator<String[]> sorter;
    private Comparator<String[]> secondarySorter;
    private int pointer;
    private List<String[]> hills;

    protected Hills(Comparator<String[]> sort, Comparator<String[]> ss) {
        secondarySorter = ss;
        sorter = sort;
        pointer = 0;
        try {
            List<String[]> tmp = CSVReaderTools.read("hillcsv/DoBIH_v18.csv");
            tmp.removeFirst();
            LocationDistance.distanceFromUser(tmp);
            tmp.sort(sorter);
            hills = tmp;

        } catch (Exception e) {
            System.out.println("Failed to read csv");
            throw new ResourceMissingException();
        }
    }
    protected Hills(Comparator<String[]> sort){
        this(sort, null);
    }

    protected void setSorter(Comparator<String[]> sorter) {
        setSorter(sorter, null);
    }

    protected void setSorter(Comparator<String[]> sorter, Comparator<String[]> ss) {
        secondarySorter = ss;
        this.sorter = sorter;
        this.pointer = 0;
    }

    private String[] getHill(int n) throws IndexOutOfBoundsException{
        return hills.get(n);
    }


    protected List<Hill> getFirstNHills(int n) {
        try {
            List<String[]> hillsRaw = new ArrayList<>();
            for (int i=pointer;i<pointer+n;i++) {
                hillsRaw.add(getHill(i));
            }
            hillsRaw.sort(secondarySorter);
            List<Hill> hills = new ArrayList<>();
            for (String[] hill : hillsRaw) {
                // Hill( HillID, Hill Name, Hill distance from user, Height in metres )
                // TODO: CONTINUE FROM HERE
                WeatherForecast weatherForecast = new WeatherForecast(Double.parseDouble(hill[33]),Double.parseDouble(hill[34]));
                hills.add(new Hill(hill[0], ));
            }
            pointer +=n;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Reached end somehow??");
        } catch (Exception e) {
            System.out.println("Failure");
        }


    }

    public static void main(String[] args) {
        Comparator<String[]> sorter = new Default();
        Hills hills = new Hills(sorter);
        System.out.println("Done!");
        for (int i=0;i<10;i++) {
            for (String s : hills.getHill(i)) {
                System.out.print(s);
            }
            System.out.println();
        }
    }
}
