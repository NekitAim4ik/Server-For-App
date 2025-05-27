package com.shailush.server.controller;

import com.shailush.server.dto.ProfileRequest;
import com.shailush.server.dto.ProfileResponse;
import com.shailush.server.dto.ProfileUpdateRequest;
import com.shailush.server.model.Profile;
import com.shailush.server.repository.ProfileRepository;
import com.shailush.server.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        Profile profile =  profileRepository.getReferenceById(id);

        return ResponseEntity.ok(new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getRegistrationDate()
        ));
    }

    @RequestMapping(path = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        Profile profile = profileRepository.getReferenceById(profileUpdateRequest.getId());

        profile.setName(profileUpdateRequest.getName());
        profile.setSurname(profileUpdateRequest.getSurname());

        profileRepository.save(profile);

        return ResponseEntity.ok(new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getRegistrationDate()
        ));
    }
}
