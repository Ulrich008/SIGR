package com.example.SIGR.controller;

import com.example.SIGR.dto.request.MissionRequest;
import com.example.SIGR.dto.response.MissionResponse;
import com.example.SIGR.services.MissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public MissionResponse create(@RequestBody MissionRequest request) {
        return missionService.create(request);
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public MissionResponse getByCode(@PathVariable String code) {
        return missionService.getByCode(code);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public List<MissionResponse> getAll() {
        return missionService.getAll();
    }

    @GetMapping("/processus/{processusId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AGENT')")
    public List<MissionResponse> getByProcessusId(@PathVariable String processusId) {
        return missionService.getByProcessusId(processusId);
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public MissionResponse update(@PathVariable String code, @RequestBody MissionRequest request) {
        return missionService.update(code, request);
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void delete(@PathVariable String code) {
        missionService.delete(code);
    }
}
