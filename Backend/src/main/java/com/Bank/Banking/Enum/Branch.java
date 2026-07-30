package com.Bank.Banking.Enum;

public enum Branch {

        VIJAYNAGAR("BANK0001"),
        SUKHLIYA("BANK0002"),
        NAVLAKHA("BANK0003"),
        RAU("BANK0004"),
        MHOW("BANK0005"),
        MAHALAXMI("BANK0006"),
        RAJWADA("BANK0007"),
        AIRPORTROAD("BANK0008"),
        PALASIA("BANK0009"),
        RAJENDRANAGAR("BANK0010"),
        BYPASS("BANK0011");

        private final String ifscCode;

        Branch(String ifscCode) {
            this.ifscCode = ifscCode;
        }

        public String getIfscCode() {
            return ifscCode;
        }
    }
