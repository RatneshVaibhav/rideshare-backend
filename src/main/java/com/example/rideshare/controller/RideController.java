package com.example.rideshare.controller;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.model.Ride;
import com.example.rideshare.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

    // USER: request a ride
    @PostMapping("/rides")
    public Ride createRide(@Valid @RequestBody CreateRideRequest request,
                           Authentication authentication) {
        return rideService.createRide(request, authentication);
    }

    // USER: get my rides
    @GetMapping("/user/rides")
    public List<Ride> getMyRides(Authentication authentication) {
        return rideService.getUserRides(authentication);
    }

    // USER/DRIVER: complete ride
    @PostMapping("/rides/{rideId}/complete")
    public Ride completeRide(@PathVariable String rideId) {
        return rideService.completeRide(rideId);
    }
}
