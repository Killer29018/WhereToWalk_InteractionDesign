package WhereToWalk;

import java.time.LocalDate;

public class Day {
    private int date;
    private String dayOfTheWeek;
    private String month;
    private int number;
    private static Day[] days=null;
    private static Day selectedDay=null;
    private Day(int date, String month, int number, String dayOfTheWeek) {
        this.date = date;
        this.month = month;
        this.dayOfTheWeek = dayOfTheWeek;
        this.number = number;
    }

    public static Day getSelectedDay() {
        if (selectedDay==null) {
            getDays();
        }
        return selectedDay;
    }

    public int getDate() {
        return date;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public String getMonth() {
        return month;
    }

    public int getNumber() {
        return number;
    }

    public static void setSelectedDay(Day day) {
        selectedDay = day;
    }

    public static Day[] getDays() {
        if (days!=null) {return days;}
        Day[] d = new Day[7];
        LocalDate ld = LocalDate.now();
        for (int i=0; i<d.length; i++) {
            d[i] = new Day(ld.getDayOfMonth(), ld.getMonth().toString(), i, ld.getDayOfWeek().toString());
            ld = ld.plusDays(1);
        }
        setSelectedDay(d[0]);
        days = d;
        return days;
    }
}
