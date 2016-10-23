package com.messaging.config;

import ch.qos.logback.core.status.StatusManager;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

import static com.google.common.io.Files.createTempDir;
import static java.lang.String.valueOf;

/**
 * Created by sanjaya on 10/22/16.
 */

public class QpidBroker {

    private final Broker broker = new Broker();

    private final int port;

    public QpidBroker(int port) {
        this.port = port;
    }

    private BrokerOptions brokerOption() throws Exception {
        BrokerOptions brokerOptions = new BrokerOptions();
        brokerOptions.setConfigProperty("qpid.amqp_port", valueOf(port));
        brokerOptions.setConfigProperty("qpid.pass_file", getFilePath("passwd.properties"));
        brokerOptions.setConfigProperty("qpid.work_dir", createTempDir().getAbsolutePath());
        brokerOptions.setInitialConfigurationLocation(getFilePath("qpid-config.json"));
        return brokerOptions;
    }

    private String getFilePath(String name) {
        return this.getClass().getClassLoader().getResource(name).getPath();
    }

    public void start() throws Exception {
        broker.startup(brokerOption());
    }

    public void shutdown() {
        broker.shutdown();
    }

}
