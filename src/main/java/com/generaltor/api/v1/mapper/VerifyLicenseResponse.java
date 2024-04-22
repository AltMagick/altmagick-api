package com.generaltor.api.v1.mapper;

import com.google.cloud.Timestamp;

public class VerifyLicenseResponse {

    private Data data;

    public VerifyLicenseResponse(String subId, String userName, String userEmail, Timestamp createdAt, Timestamp renewsAt, Timestamp endAt, String status, boolean cancelled) {
        this.data = new Data(subId, userName, userEmail, createdAt, renewsAt, endAt, status, cancelled);
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
        private Timestamp createdAt;
        private Timestamp renewsAt;
        private Timestamp endAt;
        private String status;
        private boolean cancelled;

        public Data(String subId, String userName, String userEmail, Timestamp createdAt, Timestamp renewsAt, Timestamp endAt, String status, boolean cancelled) {
            this.subId = subId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.createdAt = createdAt;
            this.renewsAt = renewsAt;
            this.endAt = endAt;
            this.status = status;
            this.cancelled = cancelled;
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

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        public Timestamp getRenewsAt() {
            return renewsAt;
        }

        public void setRenewsAt(Timestamp renewAt) {
            this.renewsAt = renewAt;
        }

        public Timestamp getEndAt() {
            return endAt;
        }

        public void setEndAt(Timestamp endAt) {
            this.endAt = endAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }
}
