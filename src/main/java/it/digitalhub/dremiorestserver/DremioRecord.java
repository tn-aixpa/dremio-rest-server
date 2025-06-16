// SPDX-FileCopyrightText: © 2025 DSLab - Fondazione Bruno Kessler
//
// SPDX-License-Identifier: Apache-2.0

package it.digitalhub.dremiorestserver;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class DremioRecord {
    private Map<String,Object> recordMap;

    public DremioRecord() {}

    public DremioRecord(Map<String,Object> recordMap) {
        this.recordMap = recordMap;
    }

    @JsonAnyGetter
    public Map<String,Object> getRecordMap() {
        return this.recordMap;
    }

    public void setRecordMap(Map<String,Object> recordMap) {
        this.recordMap = recordMap;
    }
}
