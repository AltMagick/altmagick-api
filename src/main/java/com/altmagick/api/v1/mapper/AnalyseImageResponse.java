package com.altmagick.api.v1.mapper;

public class AnalyseImageResponse {

    private Data data;

    public AnalyseImageResponse(String subId, String analyseText, long elapsedTime, long timestamp, int usageCount, String language) {
        this.data = new Data(subId, analyseText, elapsedTime, timestamp, usageCount, language);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String subId;
        private String analyseText;
        private long elapsedTime;
        private long timestamp;
        private int usageCount;
        private String language; // Ajout du nouveau champ

        public Data(String subId, String analyseText, long elapsedTime, long timestamp, int usageCount, String language) {
            this.subId = subId;
            this.analyseText = analyseText;
            this.elapsedTime = elapsedTime;
            this.timestamp = timestamp;
            this.usageCount = usageCount;
            this.language = language; // Initialisation du nouveau champ
        }

        public String getSubId() {
            return subId;
        }

        public void setSubId(String subId) {
            this.subId = subId;
        }

        public String getAnalyseText() {
            return analyseText;
        }

        public void setAnalyseText(String analyseText) {
            this.analyseText = analyseText;
        }

        public long getElapsedTime() {
            return elapsedTime;
        }

        public void setElapsedTime(long elapsedTime) {
            this.elapsedTime = elapsedTime;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getUsageCount() {
            return usageCount;
        }

        public void setUsageCount(int usageCount) {
            this.usageCount = usageCount;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}