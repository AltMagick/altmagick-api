package com.altmagick.api.v1.entity;

public class License {
    private String licenseKey;
    private boolean isDisabled;

    public License() {
    }

    public License(String licenseKey, boolean isDisabled) {
        this.licenseKey = licenseKey;
        this.isDisabled = isDisabled;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenceKey) {
        this.licenseKey = licenceKey;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
