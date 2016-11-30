package project.businessstats.Database;

//This class builds the database queries based on inputs given by the user

public class QueryBuilder {

    public static String yearStart, yearEnd, category;

    public QueryBuilder(String yearStart, String yearEnd, String category){
        this.yearStart = yearStart;
        this.yearEnd = yearEnd;
        this.category = category;
    }

    public QueryBuilder(String category){
        this.category = category;
    }

    public QueryBuilder(){
        //Blank Constructor
    }

    public String buildQuery(){

        String selectQuery;

        switch (category){
            case "Country" :
                selectQuery = "select b.bookingYear, c.country, COUNT(*) as count_rec from bookings b left join countries c ON b.countries_id = c.countries_id WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by c.country, b.bookingYear";
                break;

            case "Gender" :
                selectQuery = "select b.bookingYear, g.gender, COUNT(*) as count_rec from bookings b left join genders g ON b.genders_id = g.genders_id WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by g.gender, b.bookingYear";
                break;

            case "Agent" :
                selectQuery = "select b.bookingYear, a.agent, COUNT(*) as count_rec from bookings b left join agents a ON b.agents_id = a.agents_id WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by a.agent, b.bookingYear";
                break;

            case "Tour Type" :
                selectQuery = "select b.bookingYear, t.tourType, COUNT(*) as count_rec from bookings b left join tourTypes t ON b.tourTypes_id = t.tourTypes_id WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by t.tourType, b.bookingYear";
                break;

            case "Age Range" :
                selectQuery = "select b.bookingYear, b.ageRange, COUNT(*) as count_rec from bookings b WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by b.ageRange, b.bookingYear";
                break;

            default : selectQuery = "select b.bookingYear, c.country, COUNT(*) as count_rec from bookings b left join countries c ON b.countries_id = c.countries_id WHERE bookingYear BETWEEN "+yearStart+" AND "+yearEnd+" group by c.country, b.bookingYear";
        }
        return  selectQuery;
    }
}
