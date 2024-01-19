package com.brvsk.ZenithActive.membership;

import java.util.UUID;

public class NoMembershipException extends RuntimeException {

    public NoMembershipException(UUID memberId) {
        super("No membership found for the member with id: " + memberId);
    }

}
