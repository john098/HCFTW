package com.example.johnpham.hcftw;

/**
 * Created by johnpham on 12/9/14.
 */
public class ServiceConstants {
    public static final String AUTHORITY_URL = "https://login.windows.net/common";
    public static final String RESOURCE_ID = "https://outlook.office365.com/";
    public static final String REDIRECT_URL = "https://clientconfig.microsoftonline-p.net";
    public static final String CLIENT_ID = "42918c10-b833-4b36-a650-5091145ae1c8";
    public static final String ENCRYPTION_KEY = "EncryptionKey";
    // it is generally the case for O365 services that the endpoint ID is the concatenation
    // RESOURCE_ID and the api version. Please check against your actual deployment
    public static final String ENDPOINT_ID = RESOURCE_ID + "api/v1.0";
}
