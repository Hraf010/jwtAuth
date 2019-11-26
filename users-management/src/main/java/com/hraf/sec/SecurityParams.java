package com.hraf.sec;

public interface SecurityParams {
    String HEADER_NAME = "Authorization";
    String SECRET = "eladssaoui@gmail.com";
    long EXPIRATION =  10*24*3600;
    String HEADER_PREFIX = "Bearer ";
}
