package cs.rug.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.client.annotation.JobWorker;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestTaskWorker {

    private static final Logger LOG = LoggerFactory.getLogger(TestTaskWorker.class);
    private static final String KEIS = "keis";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @JobWorker(type = "test-task")
    public void handleTestTask(final JobClient jobClient, final ActivatedJob job) {
        handleJob(jobClient, job);
    }

    @JobWorker(type = "charge-payment")
    public void handleChargePayment(final JobClient jobClient, final ActivatedJob job) {
        handleJob(jobClient, job);
    }

    @JobWorker(type = "ship-items")
    public void handleShipItems(final JobClient jobClient, final ActivatedJob job) {
        handleJob(jobClient, job);
    }

    private void handleJob(final JobClient jobClient, final ActivatedJob job) {
        final Map<String, Object> variables = job.getVariablesAsMap();
        final List<KeiMetadata> keis = extractKeis(variables.get(KEIS), job);

        if (keis.isEmpty()) {
            LOG.info(
                    "Task has no KEI metadata: jobKey={}, processInstanceKey={}, bpmnElementId={}, jobType={}",
                    job.getKey(),
                    job.getProcessInstanceKey(),
                    job.getElementId(),
                    job.getType()
            );
        } else {
            logMalformedKeis(keis, job);

            LOG.info(
                    "KEI metadata found: jobKey={}, processInstanceKey={}, bpmnElementId={}, jobType={}, keis={}",
                    job.getKey(),
                    job.getProcessInstanceKey(),
                    job.getElementId(),
                    job.getType(),
                    keis
            );
        }

        jobClient.newCompleteCommand(job.getKey())
                .send()
                .join();

        LOG.info("Completed job with key={}, jobType={}", job.getKey(), job.getType());
    }

    private List<KeiMetadata> extractKeis(final Object rawKeis, final ActivatedJob job) {
        if (rawKeis == null) {
            return Collections.emptyList();
        }

        if (rawKeis instanceof List<?> rawList && rawList.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            final List<KeiMetadata> keis = OBJECT_MAPPER.convertValue(
                    rawKeis,
                    new TypeReference<List<KeiMetadata>>() {}
            );

            if (keis == null) {
                return Collections.emptyList();
            }

            return keis;
        } catch (IllegalArgumentException ex) {
            LOG.warn(
                    "Malformed KEI metadata: jobKey={}, processInstanceKey={}, bpmnElementId={}, jobType={}, keisRaw={}",
                    job.getKey(),
                    job.getProcessInstanceKey(),
                    job.getElementId(),
                    job.getType(),
                    rawKeis,
                    ex
            );
            return Collections.emptyList();
        }
    }

    private void logMalformedKeis(final List<KeiMetadata> keis, final ActivatedJob job) {
        for (final KeiMetadata kei : keis) {
            if (kei == null || isBlank(kei.getId()) || isBlank(kei.getUnit())) {
                LOG.warn(
                        "Malformed KEI metadata: jobKey={}, processInstanceKey={}, bpmnElementId={}, jobType={}, kei={}",
                        job.getKey(),
                        job.getProcessInstanceKey(),
                        job.getElementId(),
                        job.getType(),
                        kei
                );
            }
        }
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }
    
    public static class KeiMetadata {

        private String id;
        private String unit;
        private BigDecimal targetValue;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(final String unit) {
            this.unit = unit;
        }

        public BigDecimal getTargetValue() {
            return targetValue;
        }

        public void setTargetValue(final BigDecimal targetValue) {
            this.targetValue = targetValue;
        }

        @Override
        public String toString() {
            if (targetValue == null) {
                return "{id=" + id + ", unit=" + unit + "}";
            }

            return "{id=" + id + ", unit=" + unit + ", targetValue=" + targetValue + "}";
        }
    }
}
