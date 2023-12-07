package com.brvsk.ZenithActive.notification.sms;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
public class SmsConfig {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;
    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;
    @Value("${twilio.trial_number}")
    private String fromNumber;



    public String getACCOUNT_SID() {
        return ACCOUNT_SID;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public String getFromNumber() {
        return fromNumber;
    }

}