import React from 'react';

function LoginSelection({ setPage }) {
    const heroImageUrl = "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80";

    return (
        <div className="full-height-center">
            <img src={heroImageUrl} alt="Bank Hero" className="hero-bg" />

            <div className="glass-panel" style={{ textAlign: 'center' }}>
                <h2 className="section-title" style={{ fontSize: '2.5rem', marginBottom: '10px' }}>Welcome Back</h2>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '48px' }}>Please select your login type to continue</p>
                
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    <div style={{ padding: '32px', background: 'rgba(255,255,255,0.02)', borderRadius: '24px', border: '1px solid var(--glass-border)', textAlign: 'left' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '20px' }}>
                            <div style={{ fontSize: '2rem', padding: '15px', background: 'rgba(190, 242, 100, 0.1)', borderRadius: '16px' }}>👤</div>
                            <div>
                                <h3 style={{ fontWeight: '700', fontSize: '1.25rem' }}>Personal Banking</h3>
                                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Manage your accounts and cards</p>
                            </div>
                        </div>
                        <button 
                            className="premium-btn btn-blue" 
                            onClick={() => setPage("userLogin")}
                        >
                            User Login
                        </button>
                    </div>

                    <div style={{ padding: '32px', background: 'rgba(255,255,255,0.02)', borderRadius: '24px', border: '1px solid var(--glass-border)', textAlign: 'left' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '20px' }}>
                            <div style={{ fontSize: '2rem', padding: '15px', background: 'rgba(190, 242, 100, 0.1)', borderRadius: '16px' }}>🔑</div>
                            <div>
                                <h3 style={{ fontWeight: '700', fontSize: '1.25rem' }}>Staff Portal</h3>
                                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Administrative control panel</p>
                            </div>
                        </div>
                        <button 
                            className="premium-btn btn-outline" 
                            onClick={() => setPage("adminLogin")}
                        >
                            Admin Login
                        </button>
                    </div>

                    <button 
                        className="premium-btn" 
                        style={{ background: 'transparent', color: 'var(--text-secondary)', textTransform: 'none', fontWeight: '500' }}
                        onClick={() => setPage("home")}
                    >
                        ← Back to Home
                    </button>
                </div>
            </div>
        </div>
    );
}

export default LoginSelection;
