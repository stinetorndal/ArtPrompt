package app.dtos.savedpaintings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
//Hører til Threads og bruges til at tælle stats for fetching
public class FetchResultDTO {
    private long totalDurationMs;
    private int threadsUsed;
    private int totalPaintings;
    private List<FetchPaintingsTimeStatsDTO> stats;
    private List<SavedPaintingDTO> paintings;
}
