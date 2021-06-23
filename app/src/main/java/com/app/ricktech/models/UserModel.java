package com.app.ricktech.models;

import java.io.Serializable;

public class UserModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }
    public static class Data implements Serializable
    {
        private int id;
        private String name;
        private String username;
        private String email;
        private String email_activation_code;
        private String email_verified_at;
        private String password_rest_code;
        private String logo;
        private String is_block;
        private String is_login;
        private int logout_time;
        private String software_type;
        private String created_at;
        private String updated_at;
        private String token;
        private String firebase_token;


        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getEmail_activation_code() {
            return email_activation_code;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getPassword_rest_code() {
            return password_rest_code;
        }

        public String getLogo() {
            return logo;
        }

        public String getIs_block() {
            return is_block;
        }

        public String getIs_login() {
            return is_login;
        }

        public int getLogout_time() {
            return logout_time;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getToken() {
            return token;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public void setFirebase_token(String firebase_token) {
            this.firebase_token = firebase_token;
        }
    }

}

