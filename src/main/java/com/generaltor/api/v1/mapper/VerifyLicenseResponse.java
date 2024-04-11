package com.generaltor.api.v1.mapper;

public class VerifyLicenseResponse {

    private Data data;

    public VerifyLicenseResponse(String subId, String userName, String userEmail) {
        this.data = new Data(subId, userName, userEmail);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String subId;
        private String userName;
        private String userEmail;

        public Data(String subId, String userName, String userEmail) {
            this.subId = subId;
            this.userName = userName;
            this.userEmail = userEmail;
        }

        public String getSubId() {
            return subId;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }
}
