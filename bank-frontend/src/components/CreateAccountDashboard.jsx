import React, { useState } from "react";
import { createAccount } from "../api";

function CreateAccountDashboard({ onBack }) {
    const [form, setForm] = useState({
        holderName: "",
        username: "",
        password: "",
        balance: "",
        accountType: "SAVINGS",
        aadhaarNumber: "",
        phoneNumber: "",
        fatherName: "",
        motherName: "",
        dob: ""
    });

    const [message, setMessage] = useState("");
    const [isSuccess, setIsSuccess] = useState(false);

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setIsSuccess(false);

        try {
            const res = await createAccount(form);
            if (res.toLowerCase().includes("account created")) {
                setIsSuccess(true);
                setMessage("✅ Account created successfully!");
                setForm({
                    holderName: "", username: "", password: "", balance: "",
                    accountType: "SAVINGS", aadhaarNumber: "", phoneNumber: "",
                    fatherName: "", motherName: "", dob: ""
                });
            } else {
                setMessage(res);
            }
        } catch (err) {
            setMessage(err.message);
        }
    };

    return (
        <div className="animate-fade-in">
            <div className="glass-panel" style={{ maxWidth: '700px', margin: '0 auto' }}>
                <h2 className="section-title" style={{ fontSize: '2rem', fontWeight: '800', marginBottom: '10px', textAlign: 'center' }}>Provision New Account</h2>
                <p style={{ textAlign: 'center', marginBottom: '40px', color: 'var(--text-secondary)' }}>Administrative account creation portal.</p>

                {message && (
                    <div style={{ 
                        marginBottom: '30px', 
                        padding: '16px', 
                        borderRadius: '12px', 
                        background: isSuccess ? 'rgba(16, 185, 129, 0.1)' : 'rgba(239, 68, 68, 0.1)', 
                        color: isSuccess ? 'var(--success)' : 'var(--danger)', 
                        textAlign: 'center', 
                        fontWeight: '600',
                        border: `1px solid ${isSuccess ? 'rgba(16, 185, 129, 0.2)' : 'rgba(239, 68, 68, 0.2)'}`
                    }}>
                        {message}
                    </div>
                )}

                <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                    <div>
                        <label>Holder Name</label>
                        <input name="holderName" placeholder="Full Name" value={form.holderName} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Username</label>
                        <input name="username" placeholder="Username" value={form.username} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Password</label>
                        <input type="password" name="password" placeholder="Password" value={form.password} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Initial Balance</label>
                        <input name="balance" placeholder="0.00" type="number" value={form.balance} onChange={handleChange} required />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <label>Account Type</label>
                        <select name="accountType" value={form.accountType} onChange={handleChange}>
                            <option value="SAVINGS">Savings Account</option>
                            <option value="CURRENT">Current Account</option>
                        </select>
                    </div>

                    <div>
                        <label>Aadhaar</label>
                        <input name="aadhaarNumber" placeholder="12 digits" value={form.aadhaarNumber} onChange={handleChange} required />
                    </div>

                    <div>
                        <label>Phone</label>
                        <input name="phoneNumber" placeholder="10 digits" value={form.phoneNumber} onChange={handleChange} required />
                    </div>

                    <div style={{ gridColumn: 'span 2' }}>
                        <label>Date of Birth</label>
                        <input type="date" name="dob" value={form.dob} onChange={handleChange} required />
                    </div>

                    <div style={{ gridColumn: 'span 2', display: 'flex', gap: '20px', marginTop: '10px' }}>
                        <button type="submit" className="premium-btn btn-cyan" style={{ flex: 1 }}>Create Account</button>
                        <button type="button" className="premium-btn btn-outline" style={{ flex: 1 }} onClick={onBack}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default CreateAccountDashboard;