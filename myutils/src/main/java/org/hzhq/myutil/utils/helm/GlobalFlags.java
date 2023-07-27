package org.hzhq.myutil.utils.helm;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2023-03-10 上午12:20
 */
public interface GlobalFlags<T> {
    T burstLimit(Integer burstLimit);

    T debug();

    T help();

    T kubeAPIServer(String kubeAPIServer);

    T kubeAsUser(String kubeAsUser);

    T kubeAsGroup(String... kubeAsGroup);

    T kubeCAFile(String kubeCAFile);

    T kubeContext(String kubeContext);

    T kubeInsecureSkipTLSVerify();

    T kubeTLSServerName(String kubeTLSServerName);

    T kubeToken(String kubeToken);

    T kubeconfig(String kubeconfig);

    T namespace(String namespace);

    T registryConfig(String registryConfig);

    T repositoryCache(String registryCache);

    T repositoryConfig(String repositoryConfig);

    String exec();

    String exec(long timoutMillSeconds);



}
