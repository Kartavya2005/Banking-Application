import axiosInstance from "./axiosConfig";

/**
 * Create Bank Account
 */
export const createAccount = async (request) => {
    return await axiosInstance.post(
        "/api/bank-accounts",
        request
    );
};

/**
 * Get Logged In Customer Accounts
 */
export const getMyAccounts = async () => {
    return await axiosInstance.get(
        "/api/bank-accounts/my-accounts"
    );
};

/**
 * Get Account By Account Number
 */
export const getAccountByNumber = async (accountNumber) => {
    return await axiosInstance.get(
        `/api/bank-accounts/account-number/${accountNumber}`
    );
};

/**
 * Update Account
 */
export const updateAccount = async (
    accountId,
    request
) => {
    return await axiosInstance.put(
        `/api/bank-accounts/${accountId}`,
        request
    );
};

/**
 * Freeze Account
 */
export const freezeAccount = async (
    accountId
) => {
    return await axiosInstance.put(
        `/api/bank-accounts/${accountId}/freeze`
    );
};

/**
 * Unfreeze Account
 */
export const unfreezeAccount = async (
    accountId
) => {
    return await axiosInstance.put(
        `/api/bank-accounts/${accountId}/unfreeze`
    );
};

/**
 * Close Account
 */
export const closeAccount = async (
    accountId
) => {
    return await axiosInstance.put(
        `/api/bank-accounts/${accountId}/close`
    );
};