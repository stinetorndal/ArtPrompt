package app.dtos.savedpaintings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
//Hører til Threads
public class FetchResultDTO {
    private long totalDurationMs;
    private int threadsUsed;
    private int totalPaintings;
    private List<FetchPaintingsTimeStatsDTO> stats;
    private List<SavedPaintingDTO> paintings;
}
