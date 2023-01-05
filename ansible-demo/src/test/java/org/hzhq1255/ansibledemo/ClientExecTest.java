package org.hzhq1255.ansibledemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiException;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-05 15:07
 */
public class ClientExecTest {
    private static final Logger LOGGER = Logger.getLogger("clientUtilTest");

//    @Test
//    public void run() throws Exception {
//        Exec exec = new Exec();
//        boolean tty = System.console() != null;
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        String[] commandLines = new String[]{
//                "ansible-playbook",
//                "/playbooks/playbook.yaml"
//        };
//        Thread thread = new Thread(() -> {
//            try {
//                final Process process =
//                        exec.exec("default", "ansible-collect-759bd55676-zxlwp", commandLines, true, tty);
//                String res = new String(process.getInputStream().readAllBytes());
//                System.out.println(res);
//                list.add(res);
//                int exitCode = process.waitFor();
//                if (exitCode != 0) {
//                    LOGGER.log(Level.WARNING, String.format("exit code not zero unexpected result %d", exitCode));
//                }
//                process.destroy();
//            } catch (IOException | ApiException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//
//        thread.join();
//        thread.interrupt();
//    }


}
