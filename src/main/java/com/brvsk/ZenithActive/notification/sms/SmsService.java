package com.brvsk.ZenithActive.notification.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService implements SmsSender{

    private final SmsConfig smsConfig;

    @Override
    public void sendSMS(String phoneNumber) {
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(smsConfig.getFromNumber());
        String messageBody = "Hello! Thank you for using our service. \n Zenith Active ;)";
        MessageCreator creator =
                Message.creator(
                        to,
                        from,
                        messageBody);
        creator.create();

    }
}
