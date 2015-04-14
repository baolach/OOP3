package com.example.morrisson.oop3;

/**
 * Created by MORRISSON on 13/04/2015.
 */
public class Application{
    private String country;
    private String city;

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String toString(){
        return "City: " + this.city + "\n" +
                "Country: " + this.country +"\n";
    }





}
