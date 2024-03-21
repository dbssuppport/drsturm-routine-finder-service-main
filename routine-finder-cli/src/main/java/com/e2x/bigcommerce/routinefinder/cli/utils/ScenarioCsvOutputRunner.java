package com.e2x.bigcommerce.routinefinder.cli.utils;

import com.e2x.bigcommerce.routinefinder.cli.verification.Scenario;
import com.e2x.bigcommerce.routinefinder.cli.verification.ScenarioRunner;
import com.e2x.bigcommerce.routinefinder.data.QuestionDefinitionRepository;
import com.e2x.bigcommerce.routinefinder.enquiry.Step;
import com.google.api.client.util.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ScenarioCsvOutputRunner implements ScenarioRunner {

    private final QuestionDefinitionRepository questionDefinitionRepository;
    private final Map<String, Integer> questionIndex = Maps.newHashMap();
    private final String filename;
    private boolean initialised = false;

    public ScenarioCsvOutputRunner(QuestionDefinitionRepository questionDefinitionRepository, String filename) {
        this.questionDefinitionRepository = questionDefinitionRepository;
        this.filename = filename;
    }

    @Override
    public void execute(Scenario scenario) {
        try {
            if (!initialised) {
                initialiseFile(null);
            }

            writeLine(createScenarioLineFor(scenario));
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private String createScenarioLineFor(Scenario scenario) {
        List<String> conditions = questionIndex.values().stream().map(r -> "").collect(Collectors.toList());

        scenario
                .getAllAllowedChoices()
                .forEach(a -> {
                    var index = questionIndex.get(a.getQuestionId());
                    List<String> allowedOptions = a.getChoices().stream().map(s -> (String) s).collect(Collectors.toList());
                    var options = String.join(";", allowedOptions);
                    conditions.set(index, options);
                });

        conditions.add(scenario.getSteps().stream().map(Step::getSkuId).collect(Collectors.joining(";")));
        return String.join(",", conditions);
    }

    private void initialiseFile(String storeId) throws IOException {
        Files.deleteIfExists(Path.of(filename));
        var index = new AtomicInteger(0);
        var headers = new StringBuffer();

        questionDefinitionRepository
                .getAll(storeId)
                .forEach(q -> {
                    if (index.get() > 0) {
                        headers.append(",");
                    }
                    questionIndex.put(q.getId(), index.get());
                    headers.append(q.getId());

                    index.incrementAndGet();
                });

        headers.append(",routine");

        writeLine(headers.toString());

        initialised = true;
    }

    private void writeLine(String line) throws IOException {
        var file = new File(filename);
        if (file.createNewFile()) {
            log.info("new file created.");
        }

        try (var fos = new FileOutputStream(file, true)) {
            fos.write(line.getBytes(StandardCharsets.UTF_8));
            fos.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
