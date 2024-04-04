package com.generaltor.api.v1.mapper;

public class VerifyLicenseResponse {

    private Data data;

    public VerifyLicenseResponse(String subId, String userName) {
        this.data = new Data(subId, userName);
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

        public Data(String subId, String userName) {
            this.subId = subId;
            this.userName = userName;
        }

        public String getSubId() {
            return subId;
        }

        public String getUserName() {
            return userName;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
