package com.e2x.bigcommerce.routinefinder.data.repository;

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RoutineGraphUriRepository implements RoutineGraphRepository {

    private final URI uri;
    private final RoutineGraphReaderWriter routineGraphReaderWriter;
    private RoutineGraph fetchedGraph = null;

    public RoutineGraphUriRepository(URI uri, RoutineGraphReaderWriter routineGraphReaderWriter) {
        this.uri = uri;
        this.routineGraphReaderWriter = routineGraphReaderWriter;
    }

    @Override
    public RoutineGraph fetchCurrent() {
        if (fetchedGraph == null) {
            var file = getFile();

            try (var reader = new FileReader(file, StandardCharsets.UTF_8)) {
                fetchedGraph = routineGraphReaderWriter.read(reader);
            } catch (IOException e) {
                throw new RuntimeException("failed to import graph", e);
            }
        }

        return fetchedGraph;
    }

    @Override
    public void save(RoutineGraph graph) {
        var file = getFile();

        log.info(String.format("saving graph to %s", file.getAbsolutePath()));

        try (var writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            routineGraphReaderWriter.write(writer, graph);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("failed to export graph", e);
        }
    }

    private File getFile() {
        log.info(uri.toString());
        return new File(uri);
    }
}
