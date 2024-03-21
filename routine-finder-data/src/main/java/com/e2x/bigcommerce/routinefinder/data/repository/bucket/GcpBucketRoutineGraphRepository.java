package com.e2x.bigcommerce.routinefinder.data.repository.bucket;

import com.e2x.bigcommerce.routinefinder.data.RoutineGraphRepository;
import com.e2x.bigcommerce.routinefinder.data.repository.RoutineGraphReaderWriter;
import com.e2x.bigcommerce.routinefinder.enquiry.RoutineGraph;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

@Slf4j
public class GcpBucketRoutineGraphRepository implements RoutineGraphRepository {

    public static final String BLOB_NAME = "routine-graph.gml";

    private final String bucketName;
    private final StorageOptions storageOptions;
    private final RoutineGraphReaderWriter routineGraphReaderWriter;
    private static RoutineGraph routineGraph;

    public GcpBucketRoutineGraphRepository(String bucketName, StorageOptions storageOptions, RoutineGraphReaderWriter routineGraphReaderWriter) {
        this.bucketName = bucketName;
        this.storageOptions = storageOptions;
        this.routineGraphReaderWriter = routineGraphReaderWriter;
    }

    @Override
    public synchronized RoutineGraph fetchCurrent() {
        if (routineGraph == null) {
            log.info(String.format("initialising routine graph from %s:%s", bucketName, BLOB_NAME));
            routineGraph = loadRoutineGraph();
        }

        return routineGraph;
    }

    @Override
    public void save(RoutineGraph graph) {
        Storage storage = storageOptions.getService();
        var bucket = storage.get(bucketName);

        if (!bucket.exists()) {
            throw new RuntimeException(String.format("could not find bucket %s", bucket));
        }

        try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
            log.info(String.format("storing graph in %s:%s", bucketName, BLOB_NAME));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8));
            routineGraphReaderWriter.write(writer, graph);
            bucket.create(BLOB_NAME, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("failed to save bucket", e);
        }

        log.info(String.format("new graph stored in %s:%s", bucketName, BLOB_NAME));    }

    public static void reset() {
        routineGraph = null;
    }

    private synchronized RoutineGraph loadRoutineGraph() {
        Storage storage = storageOptions.getService();
        var bucket = storage.get(bucketName);

        try (var resourceReader = new InputStreamReader(Channels.newInputStream(bucket.get(BLOB_NAME).reader()), StandardCharsets.UTF_8)) {
            log.info(String.format("loading routine graph from resource %s:%s", bucketName, BLOB_NAME));

            var routineGraph = routineGraphReaderWriter.read(resourceReader);
            log.info("routine graph loaded.");

            return routineGraph;
        } catch (IOException e) {
            throw new RuntimeException(String.format("failed to read routine graph configuration at %s:%s", bucketName, BLOB_NAME), e);
        }
    }
}
