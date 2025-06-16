// SPDX-FileCopyrightText: © 2025 DSLab - Fondazione Bruno Kessler
//
// SPDX-License-Identifier: Apache-2.0

package it.digitalhub.dremiorestserver;

import java.util.List;

public class DremioResult {
    List<DremioRecord> records;
    long count;

    public DremioResult() {
    }
    
    public DremioResult(List<DremioRecord> records, long count) {
        this.records = records;
        this.count = count;
    }

    public List<DremioRecord> getRecords() {
        return records;
    }
    public void setRecords(List<DremioRecord> records) {
        this.records = records;
    }
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
}
