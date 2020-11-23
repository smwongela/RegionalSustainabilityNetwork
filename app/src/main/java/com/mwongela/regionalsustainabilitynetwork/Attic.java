package com.mwongela.regionalsustainabilitynetwork;

public class Attic {


        //declare the variable
        private String title, desc, postImage, displayName, profilePhoto, time, date,organisation, country;
        //create a constructor

        public Attic(String title, String desc, String displayName, String profilePhoto,String date, String organisation, String country) {
            this.title = title;
            this.desc = desc;

            this.displayName = displayName;
            this.profilePhoto = profilePhoto;

            this.date = date;

            this.organisation=organisation;
            this.country=country;
        }

        //requires an empty constructor
        public Attic() {
        }

        // setters


        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }



        public void setDate(String date) {
            this.date = date;
        }
        public  void setCountry(String country){
            this.country=country;
        }
        public  void setOrganisation(String organisation){
            this.organisation=organisation;
        }

        //getters
    public  String getOrganisation(){
            return organisation;
    }
    public  String getCountry(){
            return country;
    }
        public String getDisplayName() {
            return displayName;
        }



        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }


        public String getDate() {
            return date;
        }




    }



