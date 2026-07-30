import axiosConfig from "./axiosConfig";

const BASE_URL = "/transactions";

const transactionApi = {
    deposit(data) {
        return axiosConfig.post(`${BASE_URL}/deposit`, data);
    },

    withdraw(data) {
        return axiosConfig.post(`${BASE_URL}/withdraw`, data);
    },

    transfer(data) {
        return axiosConfig.post(`${BASE_URL}/transfer`, data);
    },

    getTransactionByReference(referenceNumber) {
        return axiosConfig.get(`${BASE_URL}/${referenceNumber}`);
    },

    getAccountTransactions(accountNumber) {
        return axiosConfig.get(`${BASE_URL}/account/${accountNumber}`);
    },

    getMiniStatement(accountNumber) {
        return axiosConfig.get(
            `${BASE_URL}/account/${accountNumber}/mini-statement`
        );
    },

    getTransactionsBetweenDates(accountNumber, startDate, endDate) {
        return axiosConfig.get(
            `${BASE_URL}/account/${accountNumber}/between-dates`,
            {
                params: {
                    startDate,
                    endDate,
                },
            }
        );
    },

    downloadStatement() {
        return axiosConfig.get(`${BASE_URL}/statement/pdf`, {
            responseType: "blob",
        });
    },
};

export default transactionApi;