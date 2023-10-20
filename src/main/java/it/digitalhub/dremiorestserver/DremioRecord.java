package it.digitalhub.dremiorestserver;

import java.util.Map;

public class DremioRecord {
    private Map<String,Object> record;

    public DremioRecord() {}

    public DremioRecord(Map<String,Object> record) {
        this.record = record;
    }

    public Map<String,Object> getRecord() {
        return this.record;
    }

    public void setRecord(Map<String,Object> record) {
        this.record = record;
    }
}
