package com.brvsk.ZenithActive.notification.sms;

import com.twilio.Twilio;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsInitializer {

    public SmsInitializer(SmsConfig smsConfig) {
        Twilio.init(
                smsConfig.getACCOUNT_SID(),
                smsConfig.getAUTH_TOKEN()
        );
    }
}
