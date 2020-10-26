package com.mwongela.regionalsustainabilitynetwork;

public class EventModel {
    private String  profilePhoto,eventName, eventVenue,country,eventConvener,eventType,eventDate;
    //create a constructor

    public EventModel( String profilePhoto,String eventName, String eventVenue, String country, String eventConvener,String eventType, String eventDate) {

        this.profilePhoto=profilePhoto;
        this.eventName = eventName;
        this.eventVenue=eventVenue;
        this.country=country;
        this.eventConvener=eventConvener;
        this.eventType=eventType;
        this.eventDate=eventDate;

    }
    //requires an empty constructor
    public EventModel() {
    }
    // setters
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventVenue(String eventVenue){
        this.eventVenue=eventVenue;
    }
    public void setEventCountry(String country){
        this.country=country;
    }
    public void setEventConvener(String eventConvener){
        this.eventConvener=eventConvener;
    }
    public void setEventType(String eventType){
        this.eventType=eventType;
    }
    public void setEventDate(String eventDate){
        this.eventDate=eventDate;
    }

    //getters

    public String getProfilePhoto()
    {
        return profilePhoto;
    }
    public String getEventName() {
        return eventName;
    }
    public String getEventVenue(){
        return eventVenue;
    }
    public String getEventCountry(){
        return country;
    }
    public String getEventConvener(){
        return eventConvener;
    }
    public String getEventType(){
        return eventType;
    }
    public String getEventDate(){
        return eventDate;
    }


}