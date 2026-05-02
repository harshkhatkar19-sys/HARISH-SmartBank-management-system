import React, { useCallback, useEffect, useState } from "react";
import { deposit, getBalance, transfer, withdraw, getTransactions } from "../api";
import Cards from "./Cards";
import Loans from "./Loans";

function Dashboard({ setPage }) {
    const [user, setUser] = useState(null);
    const [balance, setBalance] = useState(0);
    const [transactions, setTransactions] = useState([]);
    const [activeTab, setActiveTab] = useState("account");
    
    const [transactionAmount, setTransactionAmount] = useState("");
    const [transferAmount, setTransferAmount] = useState("");
    const [toAcc, setToAcc] = useState("");

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (!storedUser) {
            setPage("userLogin");
            return;
        }
        setUser(JSON.parse(storedUser));
    }, [setPage]);

    const loadBalance = useCallback(async (currentAccNo) => {
        try {
            const bal = await getBalance(currentAccNo);
            setBalance(bal);
        } catch (err) {
            console.error(err);
        }
    }, []);

    const loadTransactions = useCallback(async (currentAccNo) => {
        try {
            const data = await getTransactions(currentAccNo);
            setTransactions(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error("Failed to load transactions:", err);
        }
    }, []);

    useEffect(() => {
        if (user) {
            const accNo = user.accNo || user.accountNumber;
            loadBalance(accNo);
            loadTransactions(accNo);
        }
    }, [user, loadBalance, loadTransactions]);

    const handleAction = async (fn, ...args) => {
        try {
            await fn(...args);
            const accNo = user.accNo || user.accountNumber;
            await loadBalance(accNo);
            await loadTransactions(accNo);
            setTransactionAmount("");
            setTransferAmount("");
            setToAcc("");
            alert("Success");
        } catch (err) {
            alert(err.message);
        }
    };

    const onLogout = () => {
        localStorage.clear();
        setPage("home");
    };

    if (!user) return null;

    return (
        <div className="dashboard-layout">
            <aside className="sidebar">
                <div style={{ marginBottom: '40px', display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <div style={{ width: '32px', height: '32px', background: 'var(--primary-blue)', borderRadius: '6px' }}></div>
                    <h1 style={{ fontSize: '1.25rem', fontWeight: '800', color: 'white', letterSpacing: '-0.5px' }}>BANK</h1>
                </div>

                <nav style={{ display: 'flex', flexDirection: 'column', gap: '8px', flex: 1 }}>
                    <button className={`premium-btn ${activeTab === 'account' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('account')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        📊 Dashboard
                    </button>
                    <button className={`premium-btn ${activeTab === 'cards' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('cards')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        💳 Cards
                    </button>
                    <button className={`premium-btn ${activeTab === 'loans' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('loans')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        💰 Loans
                    </button>
                </nav>

                <div style={{ marginTop: 'auto', paddingTop: '20px', borderTop: '1px solid var(--glass-border)' }}>
                    <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '4px' }}>{user.holderName}</div>
                    <button className="premium-btn btn-outline" onClick={onLogout} style={{ fontSize: '0.85rem', padding: '10px' }}>Sign Out</button>
                </div>
            </aside>

            <main className="main-content">
                {activeTab === 'account' && (
                    <div className="animate-fade-in">
                        <header style={{ marginBottom: '32px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <h2 className="section-title" style={{ margin: 0 }}>Overview</h2>
                            <div style={{ background: '#000000', padding: '8px 16px', borderRadius: '8px', border: '1px solid #333333', fontSize: '0.9rem', color: 'white' }}>
                                Account: <span style={{ color: 'white', fontWeight: '700' }}>{user.accNo || user.accountNumber}</span>
                            </div>
                        </header>

                        <div className="stat-grid">
                            <div className="stat-card">
                                <div className="stat-label">Total Balance <span className="trend-up">+2.4%</span></div>
                                <div className="stat-value">₹ {balance.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</div>
                            </div>
                            <div className="stat-card">
                                <div className="stat-label">Savings Target <span className="trend-up">On Track</span></div>
                                <div className="stat-value">₹ {(balance * 1.2).toLocaleString('en-IN', { maximumFractionDigits: 0 })}</div>
                            </div>
                            <div className="stat-card">
                                <div className="stat-label">Active Cards <span className="trend-up">Verified</span></div>
                                <div className="stat-value">1 Active</div>
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1.5fr 1fr', gap: '32px' }}>
                            <div className="glass-panel" style={{ maxWidth: 'none', padding: '24px' }}>
                                <h3 style={{ marginBottom: '20px', fontSize: '1.1rem', fontWeight: '700' }}>Recent Transactions</h3>
                                <div className="scrollable-panel">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>Description</th>
                                                <th>Amount</th>
                                                <th>Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {transactions.map((tx, idx) => (
                                                <tr key={idx}>
                                                    <td>{tx.type}</td>
                                                    <td style={{ fontWeight: '700', color: tx.type.toLowerCase().includes('withdraw') || tx.type.toLowerCase().includes('sent') ? 'var(--danger)' : 'var(--teal-green)' }}>
                                                        {tx.type.toLowerCase().includes('withdraw') || tx.type.toLowerCase().includes('sent') ? '-' : '+'}₹{tx.amount.toLocaleString()}
                                                    </td>
                                                    <td style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>{new Date(tx.date).toLocaleDateString()}</td>
                                                </tr>
                                            ))}
                                            {transactions.length === 0 && <tr><td colSpan="3" style={{ textAlign: 'center', padding: '30px', color: 'var(--text-secondary)' }}>No transactions found.</td></tr>}
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                                <div className="glass-panel" style={{ maxWidth: 'none', padding: '24px' }}>
                                    <h3 style={{ marginBottom: '20px', fontSize: '1.1rem', fontWeight: '700' }}>Quick Actions</h3>
                                    <label>Amount (₹)</label>
                                    <input type="number" placeholder="0.00" value={transactionAmount} onChange={(e) => setTransactionAmount(e.target.value)} />
                                    <div style={{ display: 'flex', gap: '12px' }}>
                                        <button className="premium-btn btn-blue" onClick={() => handleAction(deposit, user.accNo || user.accountNumber, parseFloat(transactionAmount))}>Deposit</button>
                                        <button className="premium-btn btn-outline" onClick={() => handleAction(withdraw, user.accNo || user.accountNumber, parseFloat(transactionAmount))}>Withdraw</button>
                                    </div>
                                </div>

                                <div className="glass-panel" style={{ maxWidth: 'none', padding: '24px' }}>
                                    <h3 style={{ marginBottom: '20px', fontSize: '1.1rem', fontWeight: '700' }}>Transfer Money</h3>
                                    <label>To Account</label>
                                    <input type="text" placeholder="ACC-XXXX" value={toAcc} onChange={(e) => setToAcc(e.target.value)} />
                                    <label>Amount (₹)</label>
                                    <input type="number" placeholder="0.00" value={transferAmount} onChange={(e) => setTransferAmount(e.target.value)} />
                                    <button className="premium-btn btn-cyan" onClick={() => handleAction(transfer, user.accNo || user.accountNumber, toAcc.trim(), parseFloat(transferAmount))}>Send Money</button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {activeTab === 'cards' && <Cards user={user} />}
                {activeTab === 'loans' && <Loans user={user} />}
            </main>
        </div>
    );
}

export default Dashboard;