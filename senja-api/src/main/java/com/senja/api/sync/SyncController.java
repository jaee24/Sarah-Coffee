package com.senja.api.sync;

import java.util.Map;
import com.senja.api.device.DeviceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SyncController {

    private final SyncService syncService;
    private final DeviceService deviceService;

    public SyncController(SyncService syncService, DeviceService deviceService) {
        this.syncService = syncService;
        this.deviceService = deviceService;
    }

    @PostMapping("/sync")
    public Map<String, Object> sync(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @RequestBody Map<String, Object> body) {
        deviceService.verify(deviceId, apiKey);
        return syncService.sync(deviceId, body);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() { return syncService.stats(); }
}