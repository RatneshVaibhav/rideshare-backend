package com.example.rideshare.controller;

import com.example.rideshare.model.Ride;
import com.example.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverRideController {

    @Autowired
    private RideService rideService;

    // DRIVER: view all pending ride requests
    @GetMapping("/rides/requests")
    public List<Ride> getPendingRides() {
        return rideService.getPendingRidesForDriver();
    }

    // DRIVER: accept a ride
    @PostMapping("/rides/{rideId}/accept")
    public Ride acceptRide(@PathVariable String rideId,
                           Authentication authentication) {
        return rideService.acceptRide(rideId, authentication);
    }
}
