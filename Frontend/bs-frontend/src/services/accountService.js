import * as accountApi from "../api/accountApi";

export const getMyAccounts = async () => {
    try {
        const response = await accountApi.getMyAccounts();
        return response.data;
    } catch (error) {
        console.error("Failed to fetch accounts:", error);
        throw error;
    }
};

export const getAccountByNumber = async (accountNumber) => {
    try {
        const response = await accountApi.getAccountByNumber(accountNumber);
        return response.data;
    } catch (error) {
        console.error("Failed to fetch account:", error);
        throw error;
    }
};

export const createAccount = async (request) => {
    try {
        const response = await accountApi.createAccount(request);
        return response.data;
    } catch (error) {
        console.error("Failed to create account:", error);
        throw error;
    }
};

export const updateAccount = async (accountId, request) => {
    try {
        const response = await accountApi.updateAccount(accountId, request);
        return response.data;
    } catch (error) {
        console.error("Failed to update account:", error);
        throw error;
    }
};

export const freezeAccount = async (accountId) => {
    try {
        const response = await accountApi.freezeAccount(accountId);
        return response.data;
    } catch (error) {
        console.error("Failed to freeze account:", error);
        throw error;
    }
};

export const unfreezeAccount = async (accountId) => {
    try {
        const response = await accountApi.unfreezeAccount(accountId);
        return response.data;
    } catch (error) {
        console.error("Failed to unfreeze account:", error);
        throw error;
    }
};

export const closeAccount = async (accountId) => {
    try {
        const response = await accountApi.closeAccount(accountId);
        return response.data;
    } catch (error) {
        console.error("Failed to close account:", error);
        throw error;
    }
};