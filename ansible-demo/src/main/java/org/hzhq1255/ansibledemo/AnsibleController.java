package org.hzhq1255.ansibledemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.Exec;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.commons.lang3.StringUtils;
import org.hzhq1255.ansibledemo.k8s.ClientUtil;
import org.hzhq1255.ansibledemo.model.AnsiblePlaybookResult;
import org.hzhq1255.ansibledemo.model.NodeService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-01-03 20:07
 */
@RestController
public class AnsibleController {
    private static final Integer TIME_OUT_SECONDS = 5;

    private static final Map<String, String> ANSIBLE_COLLECT_LABELS = new HashMap<>(5);

    private static final Set<String> SERVICE_STATUS_TASK_SET = new HashSet<>(1);

    static {
        ANSIBLE_COLLECT_LABELS.put("app", "ansible-collect");
        SERVICE_STATUS_TASK_SET.add("sshd status");
    }


    private static final Logger LOGGER = Logger.getLogger("AnsibleController");

    @RequestMapping(method = RequestMethod.GET, path = "/run-ansible")
    private HttpEntity<Object> runAnsible() throws Exception {
        return new HttpEntity<>(getServiceStatusFromShell());
    }

    @GetMapping("/run-ansible-json")
    private HttpEntity<Object> runAnsibleJson() throws Exception {
        return new HttpEntity<>(getNodeServiceStatusFromJson());
    }


    private List<NodeService> getNodeServiceStatusFromJson() throws InterruptedException {
        ClientUtil.getApiClient();
        Exec exec = new Exec();
        boolean tty = System.console() != null;
        String[] commandLines = new String[]{
                "ansible-playbook",
                "/playbooks/playbook.yaml"
        };
        List<NodeService> nodeServices = Collections.synchronizedList(new ArrayList<>());
        Thread thread = new Thread(() -> {
            CoreV1Api coreV1Api = new CoreV1Api();
            ObjectMapper objectMapper = new ObjectMapper();
            AnsiblePlaybookResult ansiblePlaybookResult;
            String labelSelector = ANSIBLE_COLLECT_LABELS.entrySet().stream().map(e -> String.format("%s=%s", e.getKey(), e.getValue())).collect(Collectors.joining(","));
            V1Pod v1Pod;
            try {
                V1PodList v1PodList =
                        coreV1Api.listPodForAllNamespaces(null, null, null, labelSelector, null, null, null, null, null, null);
                v1Pod = v1PodList.getItems().get(0);
            } catch (ApiException | NullPointerException | IndexOutOfBoundsException e) {
                LOGGER.log(Level.WARNING, String.format("not found ansible collect pod by labelSelector %s in cluster", labelSelector));
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            try {
                final Process process = exec.exec(Objects.requireNonNull(v1Pod.getMetadata()).getNamespace(), v1Pod.getMetadata().getName(), commandLines, true, tty);
                String res = new String(process.getInputStream().readAllBytes());
                List<NodeService> list = new ArrayList<>();
                ansiblePlaybookResult = objectMapper.readValue(res, AnsiblePlaybookResult.class);
                for (AnsiblePlaybookResult.PlaysDTO playsDTO : ansiblePlaybookResult.getPlays()) {
                    for (AnsiblePlaybookResult.PlaysDTO.TasksDTO tasksDTO : playsDTO.getTasks()) {
                        String taskName = tasksDTO.getTask().getName();
                        if (!SERVICE_STATUS_TASK_SET.contains(taskName)){
                            continue;
                        }
                        for (Map.Entry<String, AnsiblePlaybookResult.PlaysDTO.TasksDTO.NodeHostDTO> entry : tasksDTO.getHosts().entrySet()) {
                            NodeService nodeService = new NodeService();
                            nodeService.setServiceName(taskName);
                            String nodeName = entry.getKey();
                            nodeService.setNodeName(nodeName);
                            nodeService.setStatusDetail(entry.getValue().getStdout());
                            LOGGER.log(Level.INFO, String.format("taskName = %s nodeName = %s", taskName,  nodeName));
                            if (StringUtils.containsAnyIgnoreCase(entry.getValue().getStdout(),NodeService.StatusEnum.ACTIVE.toString())){
                                nodeService.setStatus(NodeService.StatusEnum.ACTIVE);
                            } else {
                                nodeService.setStatus(NodeService.StatusEnum.FAILED);
                            }
                            list.add(nodeService);
                        }
                    }

                }
                nodeServices.addAll(list);
            } catch (IOException | ApiException e) {
                LOGGER.log(Level.WARNING, String.format("failed exec ansible commands %s \n in %s pod", String.join(" ", commandLines), v1Pod.getMetadata().getName()));
                e.printStackTrace();
                throw new RuntimeException();
            }

        });
        thread.start();
        thread.join();
        thread.interrupt();
        return new ArrayList<>(nodeServices);
    }


    private List<NodeService> getServiceStatusFromShell() throws InterruptedException {
        ApiClient apiClient = ClientUtil.getApiClient();
        Exec exec = new Exec();
        boolean tty = System.console() != null;
        List<NodeService> list = Collections.synchronizedList(new ArrayList<>());
        String[] commandLines = new String[]{
                "ansible-playbook",
                "/playbooks/playbook.yaml"
        };
        Thread thread = new Thread(() -> {
            try {
                final Process process =
                        exec.exec("default", "ansible-collect-759bd55676-zxlwp", commandLines, true, tty);
                String res = new String(process.getInputStream().readAllBytes());
                System.out.println(res);
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    LOGGER.log(Level.WARNING, String.format("exit code not zero unexpected result %d", exitCode));
                } else {
                    String[] lines = res.split("\n");
                    String serviceName = "";
                    int debugFlag = 0;
                    NodeService nodeServiceStatus = new NodeService();
                    for (String line : lines) {
                        if (line.contains("TASK")) {
                            if (line.contains("sshd status")) {
                                serviceName = "sshd";
                            } else if (line.contains("debug")) {
                                debugFlag = 1;
                            }
                        }
                        if (line.contains("PLAY")) {
                            debugFlag = 0;
                        }
                        if (debugFlag == 1) {
                            if (line.startsWith("ok")) {
                                String nodeName = line.substring(line.indexOf("[") + 1, line.lastIndexOf("]"));
                                if (!StringUtils.equals(nodeName, nodeServiceStatus.getServiceName())) {
                                    nodeServiceStatus = new NodeService();
                                }
                                nodeServiceStatus.setNodeName(nodeName);
                            }
                            if (line.contains("msg") && line.contains("active")) {
                                nodeServiceStatus.setStatus(NodeService.StatusEnum.ACTIVE);
                                list.add(nodeServiceStatus);
                            }
                        }
                    }
                    for (NodeService s : list) {
                        s.setServiceName(serviceName);
                    }
                }
                process.destroy();
            } catch (IOException | ApiException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        thread.interrupt();
        return new ArrayList<>(list);
    }


}
