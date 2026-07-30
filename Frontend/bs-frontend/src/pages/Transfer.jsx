import { useState } from 'react';
import { useBank } from '../context/BankContext';
import { useNavigate } from 'react-router-dom';

export default function Transfer() {
  const { balance, transferMoney } = useBank();
  const navigate = useNavigate();
  
  const [amount, setAmount] = useState('');
  const [recipient, setRecipient] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    
    if (!amount || isNaN(amount) || Number(amount) <= 0) {
      setError('Please enter a valid amount.');
      return;
    }
    
    if (!recipient.trim()) {
      setError('Please enter a recipient name.');
      return;
    }

    const success = transferMoney(Number(amount), recipient);
    if (success) {
      navigate('/transactions');
    } else {
      setError('Transfer failed. Please check your balance.');
    }
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-white p-8 rounded-lg shadow-md border border-gray-100">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Transfer Money</h1>
        
        <div className="mb-6 p-4 bg-blue-50 rounded text-blue-800">
          <p className="text-sm">Available Balance</p>
          <p className="text-2xl font-bold">${balance.toFixed(2)}</p>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 text-red-700 rounded border border-red-200">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Recipient Name
            </label>
            <input
              type="text"
              value={recipient}
              onChange={(e) => setRecipient(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500 outline-none"
              placeholder="John Doe"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Amount ($)
            </label>
            <input
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500 outline-none"
              placeholder="0.00"
            />
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded hover:bg-blue-700 transition"
          >
            Send Money
          </button>
        </form>
      </div>
    </div>
  );
}