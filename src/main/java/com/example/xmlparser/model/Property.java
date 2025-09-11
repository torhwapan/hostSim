package com.example.xmlparser.model;

import java.util.HashMap;
import java.util.Map;

public class Property {
    private String name;
    private String systemId;
    private String connectionType;
    private String mqLibrary;
    private ConnectionSet connectionSet;

    public Property() {
        this.connectionSet = new ConnectionSet();
    }

    public Property(String name, String systemId, String connectionType, String mqLibrary) {
        this.name = name;
        this.systemId = systemId;
        this.connectionType = connectionType;
        this.mqLibrary = mqLibrary;
        this.connectionSet = new ConnectionSet();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getMqLibrary() {
        return mqLibrary;
    }

    public void setMqLibrary(String mqLibrary) {
        this.mqLibrary = mqLibrary;
    }

    public ConnectionSet getConnectionSet() {
        return connectionSet;
    }

    public void setConnectionSet(ConnectionSet connectionSet) {
        this.connectionSet = connectionSet;
    }

    // ConnectionSet内部类，表示可展开的连接设置
    public static class ConnectionSet {
        private String ip;
        private String port;
        private String listenQueue;
        private String targetQueue;

        // Getters and setters
        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getListenQueue() {
            return listenQueue;
        }

        public void setListenQueue(String listenQueue) {
            this.listenQueue = listenQueue;
        }

        public String getTargetQueue() {
            return targetQueue;
        }

        public void setTargetQueue(String targetQueue) {
            this.targetQueue = targetQueue;
        }
    }
}