package com.brvsk.ZenithActive.facility;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacilityNotFoundException extends RuntimeException{
    public FacilityNotFoundException(final UUID facilityId) {
        super("Facility with id "+facilityId+" not found");
    }
}

