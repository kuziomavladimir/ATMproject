package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {

    INTRANSFER("Incoming transfer"), OUTTRANSFER("Outgoing transfer"),
    CHECKTRANSACTIONLIST("Checking the list of transactions"), CHECKBALANCE("Checking balance"),
    OPENCARD("Opening a new card");

    private String description;

    @Override
    public String toString() {
        return "[" + this.name() + "] - " + description;
    }
}
