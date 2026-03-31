import React, { useState, useEffect } from 'react';
import { leaveService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Calendar, Clock, CheckCircle2, XCircle, Plus, AlertCircle, Loader2 } from 'lucide-react';
import './EmployeeDashboard.css';

const EmployeeDashboard = () => {
    const { user } = useAuth();
    const [balances, setBalances] = useState({});
    const [leaves, setLeaves] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [showApplyModal, setShowApplyModal] = useState(false);
    
    const [formData, setFormData] = useState({
        leaveType: 'ANNUAL',
        startDate: '',
        endDate: '',
        reason: ''
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [balanceRes, leavesRes] = await Promise.all([
                leaveService.getBalance(),
                leaveService.getMyLeaves()
            ]);
            setBalances(balanceRes.data);
            setLeaves(leavesRes.data);
        } catch (err) {
            console.error('Error fetching data', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleApply = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            await leaveService.applyLeave(formData);
            setShowApplyModal(false);
            setFormData({ leaveType: 'ANNUAL', startDate: '', endDate: '', reason: '' });
            fetchData();
        } catch (err) {
            alert(err.response?.data?.message || 'Failed to apply leave');
        } finally {
            setIsLoading(false);
        }
    };

    const getStatusClass = (status) => {
        switch (status) {
            case 'APPROVED': return 'status-approved';
            case 'REJECTED': return 'status-rejected';
            default: return 'status-pending';
        }
    };

    if (isLoading && leaves.length === 0) {
        return <div className="loading-screen"><Loader2 className="animate-spin" /></div>;
    }

    return (
        <div className="dashboard">
            <header className="dashboard-header">
                <div>
                    <h1>Welcome, {user?.firstName}!</h1>
                    <p>Track and manage your leave applications here.</p>
                </div>
                <button className="apply-btn" onClick={() => setShowApplyModal(true)}>
                    <Plus size={20} />
                    <span>Apply Leave</span>
                </button>
            </header>

            <section className="stats-grid">
                <div className="stat-card glass-card">
                    <div className="stat-icon annual">A</div>
                    <div className="stat-info">
                        <h3>Annual Leave</h3>
                        <p className="stat-value">{balances.ANNUAL || 0}</p>
                        <span className="stat-label">Days Available</span>
                    </div>
                </div>
                <div className="stat-card glass-card">
                    <div className="stat-icon sick">S</div>
                    <div className="stat-info">
                        <h3>Sick Leave</h3>
                        <p className="stat-value">{balances.SICK || 0}</p>
                        <span className="stat-label">Days Available</span>
                    </div>
                </div>
                <div className="stat-card glass-card">
                    <div className="stat-icon casual">C</div>
                    <div className="stat-info">
                        <h3>Casual Leave</h3>
                        <p className="stat-value">{balances.CASUAL || 0}</p>
                        <span className="stat-label">Days Available</span>
                    </div>
                </div>
                <div className="stat-card glass-card">
                    <div className="stat-icon maternity">M</div>
                    <div className="stat-info">
                        <h3>Maternity/Paternity</h3>
                        <p className="stat-value">{(balances.MATERNITY || 0) + (balances.PATERNITY || 0)}</p>
                        <span className="stat-label">Days Available</span>
                    </div>
                </div>
            </section>

            <section className="history-section glass-card">
                <div className="section-header">
                    <h2>Leave History</h2>
                    <Calendar size={20} className="text-muted" />
                </div>
                <div className="table-responsive">
                    <table className="history-table">
                        <thead>
                            <tr>
                                <th>Type</th>
                                <th>Duration</th>
                                <th>Days</th>
                                <th>Status</th>
                                <th>Reason</th>
                            </tr>
                        </thead>
                        <tbody>
                            {leaves.length === 0 ? (
                                <tr>
                                    <td colSpan="5" className="empty-state">No leave applications found.</td>
                                </tr>
                            ) : (
                                leaves.map(leave => (
                                    <tr key={leave.id}>
                                        <td className="type-cell">
                                            <strong>{leave.leaveType}</strong>
                                            <span className="applied-date">Applied on {new Date(leave.appliedAt).toLocaleDateString()}</span>
                                        </td>
                                        <td>{leave.startDate} to {leave.endDate}</td>
                                        <td>{leave.totalDays}</td>
                                        <td>
                                            <span className={`status-badge ${getStatusClass(leave.status)}`}>
                                                {leave.status === 'APPROVED' && <CheckCircle2 size={14} />}
                                                {leave.status === 'REJECTED' && <XCircle size={14} />}
                                                {leave.status === 'PENDING' && <Clock size={14} />}
                                                {leave.status}
                                            </span>
                                        </td>
                                        <td className="reason-cell" title={leave.reason}>{leave.reason}</td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </section>

            {showApplyModal && (
                <div className="modal-overlay">
                    <div className="modal-content glass-card">
                        <div className="modal-header">
                            <h2>Apply for Leave</h2>
                            <button className="close-modal" onClick={() => setShowApplyModal(false)}>×</button>
                        </div>
                        <form onSubmit={handleApply} className="modal-form">
                            <div className="form-group">
                                <label>Leave Type</label>
                                <select 
                                    value={formData.leaveType}
                                    onChange={(e) => setFormData({...formData, leaveType: e.target.value})}
                                >
                                    <option value="ANNUAL">Annual Leave</option>
                                    <option value="SICK">Sick Leave</option>
                                    <option value="CASUAL">Casual Leave</option>
                                    <option value="MATERNITY">Maternity Leave</option>
                                    <option value="PATERNITY">Paternity Leave</option>
                                    <option value="UNPAID">Unpaid Leave</option>
                                </select>
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label>Start Date</label>
                                    <input 
                                        type="date" 
                                        required 
                                        value={formData.startDate}
                                        onChange={(e) => setFormData({...formData, startDate: e.target.value})}
                                    />
                                </div>
                                <div className="form-group">
                                    <label>End Date</label>
                                    <input 
                                        type="date" 
                                        required 
                                        value={formData.endDate}
                                        onChange={(e) => setFormData({...formData, endDate: e.target.value})}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <label>Reason</label>
                                <textarea 
                                    rows="3" 
                                    placeholder="Briefly explain your reason..." 
                                    required
                                    value={formData.reason}
                                    onChange={(e) => setFormData({...formData, reason: e.target.value})}
                                ></textarea>
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="cancel-btn" onClick={() => setShowApplyModal(false)}>Cancel</button>
                                <button type="submit" className="submit-btn" disabled={isLoading}>
                                    {isLoading ? <Loader2 className="animate-spin" /> : 'Submit Application'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EmployeeDashboard;
