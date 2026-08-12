package com.microservice.mongodb.controller;

import com.microservice.mongodb.dto.request.InstructorRequest;
import com.microservice.mongodb.dto.response.InstructorResponse;
import com.microservice.mongodb.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public ResponseEntity<InstructorResponse> registerInstructor(@Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.registerInstructor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{instructorId}")
    public ResponseEntity<InstructorResponse> updateInstructor(
            @PathVariable Long instructorId,
            @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.updateInstructor(instructorId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{instructorId}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long instructorId) {
        instructorService.deleteInstructor(instructorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getInstructorCount() {
        long count = instructorService.getInstructorCount();
        return ResponseEntity.ok(count);
    }
}