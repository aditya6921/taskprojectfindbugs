package com.internal.tasktracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
        @RequestParam(required = false, defaultValue = "") String q,
        @RequestParam(required = false) String status,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Validate pagination input
        if (page < 1 || pageSize < 1) {
            return ResponseEntity.badRequest()
                .body("Page must be >= 1 and pageSize must be > 0");
        }

        // Normalize and escape query input
        String query = q == null ? "" : q.trim().toLowerCase();
        String escapedQuery = query
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_");

        String searchTerm = escapedQuery + "%";

        // Parse status filter
        String normalizedStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                normalizedStatus = TaskStatus.valueOf(status.trim().toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                    .body("Invalid status: " + status);
            }
        }

        System.out.println("[TaskController] q=\"" + query + "\" status=" + normalizedStatus
            + " page=" + page + " pageSize=" + pageSize);

        // Database performs pagination
        Page<Task> taskPage = taskRepository.searchTasks(
            searchTerm,
            normalizedStatus,
            PageRequest.of(page - 1, pageSize)
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", taskPage.getContent());
        response.put("total", taskPage.getTotalElements());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}
