package app.dtos.savedpaintings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//Hører til Threads, tæller fetching-tid
public class FetchPaintingsTimeStatsDTO {
    private String artist;
    private long durationMs;
    private int count;
}
