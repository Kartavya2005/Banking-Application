import { createContext, useState, useContext } from 'react';

const BankContext = createContext();

export const useBank = () => useContext(BankContext);

export const BankProvider = ({ children }) => {
  const [balance, setBalance] = useState(5430.50);
  const [transactions, setTransactions] = useState([
    { id: 1, type: 'credit', amount: 1200.00, date: '2023-10-25', description: 'Salary' },
    { id: 2, type: 'debit', amount: 50.00, date: '2023-10-26', description: 'Grocery Store' },
    { id: 3, type: 'debit', amount: 120.00, date: '2023-10-27', description: 'Electric Bill' },
  ]);

  const transferMoney = (amount, recipient) => {
    if (amount > balance) {
      alert("Insufficient funds");
      return false;
    }
    setBalance(prev => prev - amount);
    setTransactions(prev => [
      {
        id: Date.now(),
        type: 'debit',
        amount: Number(amount),
        date: new Date().toISOString().split('T')[0],
        description: `Transfer to ${recipient}`
      },
      ...prev
    ]);
    return true;
  };

  return (
    <BankContext.Provider value={{ balance, transactions, transferMoney }}>
      {children}
    </BankContext.Provider>
  );
};
