import React, { useEffect, useState } from "react";
import {
    blockAccount,
    unblockAccount,
    deleteAccount,
    getTotalAccounts,
    getTotalBalance,
    getAllLoans,
    updateLoanStatus,
    deleteCards,
    searchAccounts
} from "../api";
import CreateAccountDashboard from "./CreateAccountDashboard";

function AdminDashboard({ setPage }) {
    const [totalAccounts, setTotalAccounts] = useState(0);
    const [totalBalance, setTotalBalance] = useState(0);
    const [accNo, setAccNo] = useState("");
    const [searchName, setSearchName] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [loans, setLoans] = useState([]);
    const [activeTab, setActiveTab] = useState("overview");

    const loadData = async () => {
        try {
            setTotalAccounts(await getTotalAccounts());
            setTotalBalance(await getTotalBalance());
            const loanData = await getAllLoans();
            setLoans(Array.isArray(loanData) ? loanData : []);
        } catch (err) {
            console.error("Failed to load admin data:", err);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAction = async (actionFn, ...args) => {
        if (!accNo) {
            alert("Please provide an account number");
            return;
        }
        try {
            await actionFn(...args);
            alert("Action successful");
            loadData();
        } catch (err) {
            alert(err.message);
        }
    };

    const handleSearch = async () => {
        if (!searchName) return;
        try {
            const results = await searchAccounts(searchName);
            setSearchResults(results);
        } catch (err) {
            alert(err.message);
        }
    };

    const handleLoanStatus = async (loanId, status) => {
        try {
            await updateLoanStatus(loanId, status);
            alert(`Loan ${status}`);
            loadData();
        } catch (err) {
            alert(err.message);
        }
    };

    const onLogout = () => {
        setPage("home");
    };

    return (
        <div className="dashboard-layout">
            <aside className="sidebar">
                <div style={{ marginBottom: '40px', display: 'flex', alignItems: 'center', gap: '12px' }}>
                    <div style={{ width: '32px', height: '32px', background: 'var(--bright-blue)', borderRadius: '6px' }}></div>
                    <h1 style={{ fontSize: '1.1rem', fontWeight: '800', color: 'white', letterSpacing: '-0.5px' }}>ADMIN CONSOLE</h1>
                </div>

                <nav style={{ display: 'flex', flexDirection: 'column', gap: '8px', flex: 1 }}>
                    <button className={`premium-btn ${activeTab === 'overview' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('overview')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        🏢 Overview
                    </button>
                    <button className={`premium-btn ${activeTab === 'loans' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('loans')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        🏦 Loan Requests
                    </button>
                    <button className={`premium-btn ${activeTab === 'create' ? 'btn-blue' : 'btn-outline'}`} onClick={() => setActiveTab('create')} style={{ justifyContent: 'flex-start', padding: '12px 16px' }}>
                        👤 New Account
                    </button>
                </nav>

                <div style={{ marginTop: 'auto' }}>
                    <button className="premium-btn btn-outline" onClick={onLogout} style={{ color: 'var(--danger)', fontSize: '0.85rem' }}>Logout System</button>
                </div>
            </aside>

            <main className="main-content">
                {activeTab === 'overview' && (
                    <div className="animate-fade-in">
                        <header style={{ marginBottom: '32px' }}>
                            <h2 className="section-title">System Overview</h2>
                        </header>

                        <div className="stat-grid">
                            <div className="stat-card">
                                <div className="stat-label">Total Managed Assets <span className="trend-up">+4.2%</span></div>
                                <div className="stat-value">₹ {totalBalance.toLocaleString()}</div>
                            </div>
                            <div className="stat-card">
                                <div className="stat-label">Verified Accounts <span className="trend-up">Active</span></div>
                                <div className="stat-value">{totalAccounts}</div>
                            </div>
                            <div className="stat-card">
                                <div className="stat-label">System Health <span className="trend-up">Optimal</span></div>
                                <div className="stat-value">100%</div>
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
                            <div className="glass-panel" style={{ maxWidth: 'none', padding: '24px' }}>
                                <h3 style={{ marginBottom: '20px', fontSize: '1.1rem', fontWeight: '700' }}>Account Governance</h3>
                                <label>Target Account Number</label>
                                <input type="text" placeholder="ACC-XXXX" value={accNo} onChange={(e) => setAccNo(e.target.value)} />
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '12px' }}>
                                    <button className="premium-btn btn-blue" onClick={() => handleAction(blockAccount, accNo)}>Block</button>
                                    <button className="premium-btn btn-blue" onClick={() => handleAction(unblockAccount, accNo)}>Unblock</button>
                                </div>
                                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
                                    <button className="premium-btn btn-outline" style={{ color: 'var(--danger)' }} onClick={() => handleAction(deleteAccount, accNo)}>Terminate</button>
                                    <button className="premium-btn btn-outline" style={{ color: 'var(--warning)' }} onClick={() => handleAction(deleteCards, accNo)}>Clear Cards</button>
                                </div>
                            </div>

                            <div className="glass-panel" style={{ maxWidth: 'none', padding: '24px' }}>
                                <h3 style={{ marginBottom: '20px', fontSize: '1.1rem', fontWeight: '700' }}>Account Search</h3>
                                <div style={{ display: 'flex', gap: '12px' }}>
                                    <input type="text" placeholder="Search by name..." value={searchName} onChange={(e) => setSearchName(e.target.value)} style={{ marginBottom: 0 }} />
                                    <button className="premium-btn btn-cyan" style={{ width: '100px' }} onClick={handleSearch}>Find</button>
                                </div>
                                <div style={{ marginTop: '20px', maxHeight: '150px', overflowY: 'auto' }}>
                                    {searchResults.map(res => (
                                        <div key={res.accountNumber} style={{ padding: '10px', borderBottom: '1px solid var(--glass-border)', fontSize: '0.85rem', display: 'flex', justifyContent: 'space-between' }}>
                                            <span>{res.holderName}</span>
                                            <span style={{ color: 'var(--bright-blue)', fontWeight: '700', cursor: 'pointer' }} onClick={() => setAccNo(res.accountNumber)}>{res.accountNumber}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {activeTab === 'loans' && (
                    <div className="animate-fade-in">
                        <header style={{ marginBottom: '32px' }}>
                            <h2 className="section-title">Loan Underwriting</h2>
                        </header>

                        <div className="glass-panel" style={{ maxWidth: 'none', padding: '0', overflow: 'hidden' }}>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Account</th>
                                        <th>Type</th>
                                        <th>Amount</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {loans.map(loan => (
                                        <tr key={loan.id}>
                                            <td>{loan.accountNumber}</td>
                                            <td>{loan.loanType}</td>
                                            <td style={{ fontWeight: '700' }}>₹{loan.amount.toLocaleString()}</td>
                                            <td><span className={`status-badge status-${loan.status.toLowerCase()}`}>{loan.status}</span></td>
                                            <td>
                                                {loan.status === 'PENDING' && (
                                                    <div style={{ display: 'flex', gap: '8px' }}>
                                                        <button className="premium-btn btn-cyan" style={{ padding: '6px 12px', fontSize: '0.7rem' }} onClick={() => handleLoanStatus(loan.id, 'APPROVED')}>Approve</button>
                                                        <button className="premium-btn btn-outline" style={{ padding: '6px 12px', fontSize: '0.7rem', color: 'var(--danger)' }} onClick={() => handleLoanStatus(loan.id, 'REJECTED')}>Reject</button>
                                                    </div>
                                                )}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === 'create' && (
                    <CreateAccountDashboard onBack={() => setActiveTab('overview')} />
                )}
            </main>
        </div>
    );
}

export default AdminDashboard;
