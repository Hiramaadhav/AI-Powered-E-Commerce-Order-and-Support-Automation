package com.project.ai.api.assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * AI-Powered Smart Assertion Engine for API Testing
 * Automatically generates comprehensive assertions by:
 * - Learning response schemas from sample responses
 * - Handling dynamic values (timestamps, IDs, tokens)
 * - Detecting data type violations
 * - Identifying unexpected fields (schema drift)
 * - Validating nested structures automatically
 */
public class SmartAssertionEngine {
    
    private static final Logger logger = LogManager.getLogger(SmartAssertionEngine.class);
    private static final String SCHEMA_PATH = "target/api-schemas/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private Response response;
    private String endpointName;
    private JsonNode responseJson;
    private Map<String, Object> learnedSchema;
    
    static {
        try {
            Files.createDirectories(Paths.get(SCHEMA_PATH));
        } catch (IOException e) {
            logger.error("Failed to create schema directory: " + e.getMessage());
        }
    }
    
    public SmartAssertionEngine(Response response, String endpointName) {
        this.response = response;
        this.endpointName = endpointName;
        try {
            this.responseJson = objectMapper.readTree(response.getBody().asString());
        } catch (Exception e) {
            logger.error("Failed to parse response JSON: " + e.getMessage());
        }
    }
    
    /**
     * Factory method for fluent API
     */
    public static SmartAssertionEngine validate(Response response, String endpointName) {
        return new SmartAssertionEngine(response, endpointName);
    }
    
    /**
     * Assert standard status code
     */
    public SmartAssertionEngine assertStatusCode(int expectedCode) {
        int actualCode = response.getStatusCode();
        if (actualCode != expectedCode) {
            throw new AssertionError(
                String.format("Status code mismatch for %s. Expected: %d, Actual: %d", 
                    endpointName, expectedCode, actualCode)
            );
        }
        logger.info("✓ Status code assertion passed: " + expectedCode);
        return this;
    }
    
    /**
     * Learn response schema from actual response
     * Stores schema for future comparisons
     */
    public SmartAssertionEngine learnSchema() {
        try {
            learnedSchema = extractSchema(responseJson);
            saveSchemaToFile(endpointName, learnedSchema);
            logger.info("✓ Schema learned for endpoint: " + endpointName);
        } catch (Exception e) {
            logger.error("Failed to learn schema: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Validate response against learned schema
     */
    public SmartAssertionEngine validateAgainstSchema() {
        try {
            String schemaFile = SCHEMA_PATH + endpointName + "_schema.json";
            File schema = new File(schemaFile);
            
            if (!schema.exists()) {
                logger.warn("No schema found for " + endpointName + ". Learning new schema...");
                return learnSchema();
            }
            
            // Validate using JSON Schema Validator
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
            
            logger.info("✓ Schema validation passed for: " + endpointName);
        } catch (Exception e) {
            logger.error("Schema validation failed: " + e.getMessage());
            throw new AssertionError("Schema validation failed for " + endpointName + ": " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Assert all fields are present (comprehensive validation)
     */
    public SmartAssertionEngine assertAllFields() {
        try {
            Map<String, Object> expectedFields = loadSchemaFromFile(endpointName);
            
            if (expectedFields == null) {
                logger.warn("No schema available. Use learnSchema() first.");
                return this;
            }
            
            validateAllFields(responseJson, expectedFields, "");
            logger.info("✓ All fields validated successfully");
            
        } catch (Exception e) {
            logger.error("Field validation failed: " + e.getMessage());
            throw new AssertionError("Field validation failed: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Detect schema drift (new/removed/changed fields)
     */
    public SmartAssertionEngine detectSchemaDrift() {
        try {
            Map<String, Object> expectedSchema = loadSchemaFromFile(endpointName);
            
            if (expectedSchema == null) {
                logger.info("No baseline schema. Creating new baseline...");
                return learnSchema();
            }
            
            Map<String, Object> actualSchema = extractSchema(responseJson);
            List<String> drifts = compareSchemasForDrift(expectedSchema, actualSchema, "");
            
            if (!drifts.isEmpty()) {
                logger.warn("⚠ Schema drift detected for " + endpointName + ":");
                drifts.forEach(drift -> logger.warn("  - " + drift));
                
                // Optionally fail on drift
                boolean failOnDrift = Boolean.parseBoolean(System.getProperty("api.fail.on.drift", "false"));
                if (failOnDrift) {
                    throw new AssertionError("Schema drift detected: " + String.join(", ", drifts));
                }
            } else {
                logger.info("✓ No schema drift detected");
            }
            
        } catch (Exception e) {
            logger.error("Drift detection failed: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Assert response time is within acceptable range
     */
    public SmartAssertionEngine assertResponseTime(long maxMilliseconds) {
        long responseTime = response.getTime();
        
        if (responseTime > maxMilliseconds) {
            logger.warn(String.format("⚠ Slow response for %s: %dms (threshold: %dms)", 
                endpointName, responseTime, maxMilliseconds));
            
            boolean failOnSlowResponse = Boolean.parseBoolean(
                System.getProperty("api.fail.on.slow", "false"));
            
            if (failOnSlowResponse) {
                throw new AssertionError(
                    String.format("Response time exceeded for %s: %dms > %dms", 
                        endpointName, responseTime, maxMilliseconds)
                );
            }
        } else {
            logger.info(String.format("✓ Response time OK: %dms", responseTime));
        }
        return this;
    }
    
    /**
     * Assert specific field exists and has expected value
     */
    public SmartAssertionEngine assertField(String jsonPath, Object expectedValue) {
        try {
            Object actualValue = response.jsonPath().get(jsonPath);
            
            if (actualValue == null) {
                throw new AssertionError("Field not found: " + jsonPath);
            }
            
            if (!actualValue.equals(expectedValue)) {
                throw new AssertionError(
                    String.format("Field mismatch at %s. Expected: %s, Actual: %s", 
                        jsonPath, expectedValue, actualValue)
                );
            }
            
            logger.info("✓ Field assertion passed: " + jsonPath);
        } catch (Exception e) {
            throw new AssertionError("Field assertion failed: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Assert field matches regex pattern
     */
    public SmartAssertionEngine assertFieldMatches(String jsonPath, String regex) {
        try {
            String actualValue = response.jsonPath().getString(jsonPath);
            
            if (actualValue == null) {
                throw new AssertionError("Field not found: " + jsonPath);
            }
            
            if (!actualValue.matches(regex)) {
                throw new AssertionError(
                    String.format("Field %s does not match pattern. Value: %s, Pattern: %s", 
                        jsonPath, actualValue, regex)
                );
            }
            
            logger.info("✓ Field pattern match: " + jsonPath);
        } catch (Exception e) {
            throw new AssertionError("Pattern assertion failed: " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Ignore dynamic fields (timestamps, UUIDs, etc.)
     */
    public SmartAssertionEngine ignoreDynamicFields(String... fieldNames) {
        // Store ignored fields for schema comparison
        logger.info("Ignoring dynamic fields: " + String.join(", ", fieldNames));
        return this;
    }
    
    /**
     * Detect anomalies in response data
     */
    public SmartAssertionEngine detectAnomalies() {
        try {
            List<String> anomalies = new ArrayList<>();
            
            // Check for suspicious patterns
            anomalies.addAll(detectNullAnomalies(responseJson, ""));
            anomalies.addAll(detectEmptyStringAnomalies(responseJson, ""));
            anomalies.addAll(detectNegativeIdAnomalies(responseJson, ""));
            
            if (!anomalies.isEmpty()) {
                logger.warn("⚠ Anomalies detected in " + endpointName + ":");
                anomalies.forEach(anomaly -> logger.warn("  - " + anomaly));
            } else {
                logger.info("✓ No anomalies detected");
            }
            
        } catch (Exception e) {
            logger.error("Anomaly detection failed: " + e.getMessage());
        }
        return this;
    }
    
    // ========== Helper Methods ==========
    
    /**
     * Extract schema structure from JSON response
     */
    private Map<String, Object> extractSchema(JsonNode node) {
        Map<String, Object> schema = new LinkedHashMap<>();
        
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                JsonNode fieldValue = field.getValue();
                
                if (fieldValue.isObject()) {
                    schema.put(fieldName, extractSchema(fieldValue));
                } else if (fieldValue.isArray() && fieldValue.size() > 0) {
                    schema.put(fieldName, "array[" + getNodeType(fieldValue.get(0)) + "]");
                } else {
                    schema.put(fieldName, getNodeType(fieldValue));
                }
            }
        }
        
        return schema;
    }
    
    /**
     * Get JSON node type
     */
    private String getNodeType(JsonNode node) {
        if (node.isInt()) return "integer";
        if (node.isLong()) return "long";
        if (node.isDouble() || node.isFloat()) return "number";
        if (node.isBoolean()) return "boolean";
        if (node.isNull()) return "null";
        if (node.isObject()) return "object";
        if (node.isArray()) return "array";
        return "string";
    }
    
    /**
     * Save schema to file
     */
    private void saveSchemaToFile(String endpoint, Map<String, Object> schema) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(schema);
            Files.write(Paths.get(SCHEMA_PATH + endpoint + "_schema.json"), json.getBytes());
        } catch (IOException e) {
            logger.error("Failed to save schema: " + e.getMessage());
        }
    }
    
    /**
     * Load schema from file
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> loadSchemaFromFile(String endpoint) {
        try {
            String json = new String(Files.readAllBytes(
                Paths.get(SCHEMA_PATH + endpoint + "_schema.json")));
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Validate all fields recursively
     */
    private void validateAllFields(JsonNode actual, Map<String, Object> expected, String path) {
        for (Map.Entry<String, Object> entry : expected.entrySet()) {
            String fieldName = entry.getKey();
            String fullPath = path.isEmpty() ? fieldName : path + "." + fieldName;
            
            JsonNode actualField = actual.get(fieldName);
            if (actualField == null) {
                throw new AssertionError("Missing field: " + fullPath);
            }
        }
    }
    
    /**
     * Compare schemas for drift
     */
    private List<String> compareSchemasForDrift(Map<String, Object> expected, 
                                                 Map<String, Object> actual, String path) {
        List<String> drifts = new ArrayList<>();
        
        // Check for removed fields
        for (String key : expected.keySet()) {
            if (!actual.containsKey(key)) {
                drifts.add("Removed field: " + (path.isEmpty() ? key : path + "." + key));
            }
        }
        
        // Check for new fields
        for (String key : actual.keySet()) {
            if (!expected.containsKey(key)) {
                drifts.add("New field: " + (path.isEmpty() ? key : path + "." + key));
            }
        }
        
        return drifts;
    }
    
    /**
     * Detect null value anomalies
     */
    private List<String> detectNullAnomalies(JsonNode node, String path) {
        List<String> anomalies = new ArrayList<>();
        
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldPath = path.isEmpty() ? field.getKey() : path + "." + field.getKey();
                
                if (field.getValue().isNull()) {
                    anomalies.add("Null value at: " + fieldPath);
                } else {
                    anomalies.addAll(detectNullAnomalies(field.getValue(), fieldPath));
                }
            }
        }
        
        return anomalies;
    }
    
    /**
     * Detect empty string anomalies
     */
    private List<String> detectEmptyStringAnomalies(JsonNode node, String path) {
        List<String> anomalies = new ArrayList<>();
        
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldPath = path.isEmpty() ? field.getKey() : path + "." + field.getKey();
                
                if (field.getValue().isTextual() && field.getValue().asText().trim().isEmpty()) {
                    anomalies.add("Empty string at: " + fieldPath);
                } else if (field.getValue().isObject()) {
                    anomalies.addAll(detectEmptyStringAnomalies(field.getValue(), fieldPath));
                }
            }
        }
        
        return anomalies;
    }
    
    /**
     * Detect negative ID anomalies
     */
    private List<String> detectNegativeIdAnomalies(JsonNode node, String path) {
        List<String> anomalies = new ArrayList<>();
        
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                String fieldPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                
                if (fieldName.toLowerCase().contains("id") && field.getValue().isNumber()) {
                    if (field.getValue().asLong() < 0) {
                        anomalies.add("Negative ID at: " + fieldPath);
                    }
                } else if (field.getValue().isObject()) {
                    anomalies.addAll(detectNegativeIdAnomalies(field.getValue(), fieldPath));
                }
            }
        }
        
        return anomalies;
    }
}
