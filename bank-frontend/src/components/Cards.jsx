import React, { useState, useCallback, useEffect } from 'react';
import { getCards, issueCard, updateCardStatus } from '../api';

function Cards({ user }) {
    const [cards, setCards] = useState([]);
    const [loading, setLoading] = useState(true);
    const accNo = user.accountNumber || user.accNo;

    const fetchCards = useCallback(async () => {
        try {
            const data = await getCards(accNo);
            setCards(data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [accNo]);

    useEffect(() => {
        if (accNo) fetchCards();
    }, [accNo, fetchCards]);

    const handleIssueCard = async (type) => {
        try {
            const res = await issueCard(accNo, user.holderName, type);
            if (res === 'SUCCESS') {
                alert(`${type} Card Issued Successfully!`);
                fetchCards();
            } else if (res === 'ALREADY_EXISTS_OR_FAILED') {
                alert(`You already have an issued card. You cannot issue another card for this account.`);
            } else {
                alert('Failed to issue card. Please try again.');
            }
        } catch (err) {
            alert(err.message);
        }
    };

    const toggleStatus = async (cardNumber, currentStatus) => {
        const newStatus = currentStatus === 'ACTIVE' ? 'BLOCKED' : 'ACTIVE';
        try {
            const res = await updateCardStatus(cardNumber, newStatus);
            if (res === 'SUCCESS') {
                fetchCards();
            }
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <div className="animate-fade-in">
            <header style={{ marginBottom: '40px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                    <h2 className="section-title" style={{ marginBottom: '5px' }}>My Cards</h2>
                    <p style={{ color: 'var(--text-secondary)' }}>Manage your digital and physical payment methods</p>
                </div>
                <div style={{ display: 'flex', gap: '15px' }}>
                    <button className="premium-btn btn-cyan" style={{ width: 'auto' }} onClick={() => handleIssueCard('DEBIT')}>+ New Debit Card</button>
                    <button className="premium-btn btn-blue" style={{ width: 'auto' }} onClick={() => handleIssueCard('CREDIT')}>+ New Credit Card</button>
                </div>
            </header>
            
            <div className="cards-grid">
                {cards.map(card => (
                    <div key={card.cardNumber} className={`glass-card ${card.cardType.toLowerCase()}`}>
                        <div className={`status-badge status-${card.status.toLowerCase()}`}>
                            {card.status}
                        </div>
                        
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                            <div className="card-chip"></div>
                            <div style={{ fontWeight: '800', fontSize: '1.25rem', letterSpacing: '1px' }}>{card.cardType}</div>
                        </div>

                        <div className="card-number">
                            {card.cardNumber.match(/.{1,4}/g).join(' ')}
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end' }}>
                            <div>
                                <div style={{ fontSize: '0.625rem', color: 'rgba(255,255,255,0.5)', textTransform: 'uppercase', marginBottom: '4px' }}>Card Holder</div>
                                <div className="card-holder">{card.holderName}</div>
                            </div>
                            <div style={{ textAlign: 'right' }}>
                                <div style={{ fontSize: '0.625rem', color: 'rgba(255,255,255,0.5)', textTransform: 'uppercase', marginBottom: '4px' }}>Expires</div>
                                <div style={{ fontWeight: '600' }}>{card.expiryDate}</div>
                            </div>
                        </div>

                        <div style={{ marginTop: '20px', borderTop: '1px solid rgba(255,255,255,0.1)', paddingTop: '15px' }}>
                            <button 
                                className="premium-btn btn-outline"
                                style={{ padding: '8px 16px', fontSize: '0.75rem' }}
                                onClick={() => toggleStatus(card.cardNumber, card.status)}
                            >
                                {card.status === 'ACTIVE' ? '🔐 Freeze Card' : '🔓 Unfreeze Card'}
                            </button>
                        </div>
                    </div>
                ))}

                {cards.length === 0 && !loading && (
                    <div className="glass-panel" style={{ gridColumn: '1 / -1', textAlign: 'center', padding: '60px' }}>
                        <div style={{ fontSize: '3rem', marginBottom: '20px' }}>💳</div>
                        <h3 style={{ marginBottom: '10px' }}>No Active Cards</h3>
                        <p style={{ color: 'var(--text-secondary)', marginBottom: '30px' }}>You haven't requested any cards yet. Use the buttons above to issue your first card.</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Cards;
