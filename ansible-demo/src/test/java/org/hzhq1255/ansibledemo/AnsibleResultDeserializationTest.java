package org.hzhq1255.ansibledemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hzhq1255.ansibledemo.model.AnsiblePlaybookResult;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-05 20:32
 */
public class AnsibleResultDeserializationTest {

    @Test
    public void convertToObj() throws Exception{

        try (InputStream fileIn =  this.getClass().getClassLoader().getResourceAsStream("stdout.json");) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(fileIn, AnsiblePlaybookResult.class);
        }
    }
}
