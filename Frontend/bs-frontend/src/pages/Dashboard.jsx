import { useBank } from '../context/BankContext';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { balance, transactions } = useBank();
  
  // Get latest 3 transactions
  const recentTransactions = transactions.slice(0, 3);

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Welcome Back</h1>
      
      <div className="bg-white p-6 rounded-lg shadow-md border border-gray-100">
        <h2 className="text-lg font-medium text-gray-500 mb-2">Available Balance</h2>
        <p className="text-4xl font-bold text-gray-900">${balance.toFixed(2)}</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-md border border-gray-100">
          <h2 className="text-xl font-semibold mb-4 text-gray-800">Quick Actions</h2>
          <div className="flex gap-4">
            <Link to="/transfer" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
              Transfer Money
            </Link>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md border border-gray-100">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold text-gray-800">Recent Activity</h2>
            <Link to="/transactions" className="text-blue-600 hover:underline text-sm">View All</Link>
          </div>
          <ul className="space-y-3">
            {recentTransactions.map(tx => (
              <li key={tx.id} className="flex justify-between items-center p-3 hover:bg-gray-50 rounded">
                <div>
                  <p className="font-medium text-gray-800">{tx.description}</p>
                  <p className="text-xs text-gray-500">{tx.date}</p>
                </div>
                <span className={`font-semibold ${tx.type === 'credit' ? 'text-green-600' : 'text-red-600'}`}>
                  {tx.type === 'credit' ? '+' : '-'}${tx.amount.toFixed(2)}
                </span>
              </li>
            ))}
            {recentTransactions.length === 0 && (
              <p className="text-gray-500 text-sm">No recent transactions.</p>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
}