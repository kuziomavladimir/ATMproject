package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {

    INTRANSFER("Incoming transfer"), OUTTRANSFER("outgoing transfer"),
    CHECKTRANSACTIONLIST("checking the list of transactions"), CHECKBALANCE("checking balance");

    private String description;

    @Override
    public String toString() {
        return "[" + this.name() + "] - " + description;
    }
}
