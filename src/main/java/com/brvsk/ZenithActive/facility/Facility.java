package com.brvsk.ZenithActive.facility;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@Table(name = "facilities")
@Entity(name = "Facilities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated
    @Column(nullable = false)
    private FacilityType facilityType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "facility_opening_hours_start",
            joinColumns = @JoinColumn(name = "facilities_id"))
    @MapKeyColumn(name = "day_of_week", nullable = false)
    @Column(name = "opening_hours_start", nullable = false)
    private Map<DayOfWeek, LocalTime> openingHoursStart;

    @ElementCollection
    @CollectionTable(
            name = "facility_opening_hours_end",
            joinColumns = @JoinColumn(name = "facilities_id"))
    @MapKeyColumn(name = "day_of_week", nullable = false)
    @Column(name = "opening_hours_end", nullable = false)
    private Map<DayOfWeek, LocalTime> openingHoursEnd;

    @Override
    public String toString() {
        return "Facilities{" +
                "id=" + id +
                ", facilityType=" + facilityType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", openingHoursStart=" + openingHoursStart +
                ", openingHoursEnd=" + openingHoursEnd +
                '}';
    }
}
