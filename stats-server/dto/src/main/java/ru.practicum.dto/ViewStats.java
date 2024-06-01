package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStats {
    private String uri;
    private String app;
    private Long hits;

    public void incHits() {
        this.hits++;
    }
}
