import transactionApi from "../api/transactionApi";

export const deposit = async (data) => {
    try {
        const response = await transactionApi.deposit(data);
        return response.data;
    } catch (error) {
        console.error("Deposit failed:", error);
        throw error;
    }
};

export const withdraw = async (data) => {
    try {
        const response = await transactionApi.withdraw(data);
        return response.data;
    } catch (error) {
        console.error("Withdrawal failed:", error);
        throw error;
    }
};

export const transfer = async (data) => {
    try {
        const response = await transactionApi.transfer(data);
        return response.data;
    } catch (error) {
        console.error("Transfer failed:", error);
        throw error;
    }
};

export const getTransactionByReference = async (reference) => {
    try {
        const response = await transactionApi.getTransactionByReference(reference);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch transaction:", error);
        throw error;
    }
};

export const getAccountTransactions = async (accountNumber) => {
    try {
        const response = await transactionApi.getAccountTransactions(accountNumber);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch transactions:", error);
        throw error;
    }
};

export const getMiniStatement = async (accountNumber) => {
    try {
        const response = await transactionApi.getMiniStatement(accountNumber);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch mini statement:", error);
        throw error;
    }
};

export const getTransactionsBetweenDates = async (
    accountNumber,
    startDate,
    endDate
) => {
    try {
        const response =
            await transactionApi.getTransactionsBetweenDates(
                accountNumber,
                startDate,
                endDate
            );

        return response.data;
    } catch (error) {
        console.error("Failed to fetch statement:", error);
        throw error;
    }
};

export const downloadStatement = async () => {
    try {
        const response = await transactionApi.downloadStatement();
        return response.data;
    } catch (error) {
        console.error("Failed to download statement:", error);
        throw error;
    }
};