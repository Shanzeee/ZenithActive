package com.brvsk.ZenithActive.diet;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class DietResponse {

    private UUID dietId;
    private List<DietDay> dietDayList;
    private LocalDateTime creationAt;

}
