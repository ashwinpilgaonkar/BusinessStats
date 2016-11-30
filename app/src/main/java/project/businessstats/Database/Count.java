package project.businessstats.Database;

public class Count {
    private String year;
    private String category;
    private String countrec;

    public Count(String dockers, String s) {
    }

    public Count(String year, String category, String countrec) {
        this.year = year;
        this.category = category;
        this.countrec = countrec;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCountrec(String records) {
        this.countrec = records;
    }

    public String getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public String getCountrec() {
        return countrec;
    }
}