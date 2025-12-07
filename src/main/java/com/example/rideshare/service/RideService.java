package com.example.rideshare.service;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.exception.NotFoundException;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.User;
import com.example.rideshare.repository.RideRepository;
import com.example.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    // USER: create ride
    public Ride createRide(CreateRideRequest request, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only ROLE_USER can request rides");
        }

        Ride ride = new Ride(
                user.getId(),
                request.getPickupLocation(),
                request.getDropLocation()
        );

        return rideRepository.save(ride);
    }

    // USER: get own rides
    public List<Ride> getUserRides(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return rideRepository.findByUserId(user.getId());
    }



    // DRIVER: view all pending rides
    public List<Ride> getPendingRidesForDriver() {
        return rideRepository.findByStatus("REQUESTED");
    }

    // DRIVER: accept a ride
    public Ride acceptRide(String rideId, Authentication auth) {
        User driver = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only ROLE_DRIVER can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return rideRepository.save(ride);
    }

    // USER/DRIVER: complete ride
    public Ride completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride must be ACCEPTED to complete");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }
}
