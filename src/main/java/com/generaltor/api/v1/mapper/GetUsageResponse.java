package com.generaltor.api.v1.mapper;

public class GetUsageResponse {

    private Data data;

    public GetUsageResponse(String subId, int usageCount, int maxAllowedUsageCount) {
        this.data = new Data(subId, usageCount, maxAllowedUsageCount);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String subId;
        private int usageCount;
        private int maxAllowedUsageCount;

        public Data(String subId, int usageCount, int maxAllowedUsageCount) {
            this.subId = subId;
            this.usageCount = usageCount;
            this.maxAllowedUsageCount = maxAllowedUsageCount;
        }

        public String getSubId() {
            return subId;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public int getUsageCount() {
            return usageCount;
        }

        public void setUsageCount(int usageCount) {
            this.usageCount = usageCount;
        }

        public int getMaxAllowedUsageCount() {
            return maxAllowedUsageCount;
        }

        public void setMaxAllowedUsageCount(int maxAllowedUsageCount) {
            this.maxAllowedUsageCount = maxAllowedUsageCount;
        }
    }
}
