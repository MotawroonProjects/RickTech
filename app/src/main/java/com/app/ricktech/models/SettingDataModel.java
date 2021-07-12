package com.app.ricktech.models;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
       private int id;
       private String terms_condition_link;
       private String about_us_link;
       private String privacy_policy_link;
       private String header_logo;
       private String footer_logo;
       private String login_banner;
       private String image_slider;
       private String footer_desc;
       private String company_profile_pdf;
       private double latitude;
       private double longitude;
       private String phone1;
       private String phone2;
       private String fax;
       private String android_app;
       private String ios_app;
       private String email1;
       private String email2;
       private String link;
       private String sms_user_name;
       private String sms_user_pass;
       private String sms_sender;
       private String publisher;
       private String default_language;
       private String default_theme;
       private String offer_muted;
       private int offer_notification;
       private String facebook;
       private String twitter;
       private String instagram;
       private String linkedin;
       private String telegram;
       private String youtube;
       private String google_plus;
       private String snapchat_ghost;
       private String whatsapp;
       private int user_start_points;
       private int site_commission;
       private int debt_limit;
       private int min_Shipping_value;
       private int max_Shipping_value;
       private int maximum_number_days_for_returns;
       private String trans_title;
       private String trans_desc;
       private String trans_about_app;
       private String trans_terms_condition;
       private String trans_privacy_policy;


        public int getId() {
            return id;
        }

        public String getTerms_condition_link() {
            return terms_condition_link;
        }

        public String getAbout_us_link() {
            return about_us_link;
        }

        public String getPrivacy_policy_link() {
            return privacy_policy_link;
        }

        public String getHeader_logo() {
            return header_logo;
        }

        public String getFooter_logo() {
            return footer_logo;
        }

        public String getLogin_banner() {
            return login_banner;
        }

        public String getImage_slider() {
            return image_slider;
        }

        public String getFooter_desc() {
            return footer_desc;
        }

        public String getCompany_profile_pdf() {
            return company_profile_pdf;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getPhone1() {
            return phone1;
        }

        public String getPhone2() {
            return phone2;
        }

        public String getFax() {
            return fax;
        }

        public String getAndroid_app() {
            return android_app;
        }

        public String getIos_app() {
            return ios_app;
        }

        public String getEmail1() {
            return email1;
        }

        public String getEmail2() {
            return email2;
        }

        public String getLink() {
            return link;
        }

        public String getSms_user_name() {
            return sms_user_name;
        }

        public String getSms_user_pass() {
            return sms_user_pass;
        }

        public String getSms_sender() {
            return sms_sender;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDefault_language() {
            return default_language;
        }

        public String getDefault_theme() {
            return default_theme;
        }

        public String getOffer_muted() {
            return offer_muted;
        }

        public int getOffer_notification() {
            return offer_notification;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getYoutube() {
            return youtube;
        }

        public String getGoogle_plus() {
            return google_plus;
        }

        public String getSnapchat_ghost() {
            return snapchat_ghost;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public int getUser_start_points() {
            return user_start_points;
        }

        public int getSite_commission() {
            return site_commission;
        }

        public int getDebt_limit() {
            return debt_limit;
        }

        public int getMin_Shipping_value() {
            return min_Shipping_value;
        }

        public int getMax_Shipping_value() {
            return max_Shipping_value;
        }

        public int getMaximum_number_days_for_returns() {
            return maximum_number_days_for_returns;
        }

        public String getTrans_title() {
            return trans_title;
        }

        public String getTrans_desc() {
            return trans_desc;
        }

        public String getTrans_about_app() {
            return trans_about_app;
        }

        public String getTrans_terms_condition() {
            return trans_terms_condition;
        }

        public String getTrans_privacy_policy() {
            return trans_privacy_policy;
        }
    }
}
