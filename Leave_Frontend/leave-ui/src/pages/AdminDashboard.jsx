import React, { useState, useEffect } from 'react';
import { adminService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Check, X, FileText, BarChart3, Users, Loader2, AlertCircle, Info } from 'lucide-react';
import './AdminDashboard.css';

const AdminDashboard = () => {
    const { user } = useAuth();
    const [pendingLeaves, setPendingLeaves] = useState([]);
    const [report, setReport] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(null);
    const [rejectId, setRejectId] = useState(null);
    const [comment, setComment] = useState('');

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [pendingRes, reportRes] = await Promise.all([
                adminService.getPendingLeaves(),
                adminService.getReportsSummary()
            ]);
            setPendingLeaves(pendingRes.data);
            setReport(reportRes.data);
        } catch (err) {
            console.error('Error fetching admin data', err);
        } finally {
            setIsLoading(false);
        }
    };

    const handleApprove = async (id) => {
        setActionLoading(id);
        try {
            await adminService.approveLeave(id);
            fetchData();
        } catch (err) {
            alert(err.response?.data?.message || 'Approval failed');
        } finally {
            setActionLoading(null);
        }
    };

    const handleReject = async (e) => {
        e.preventDefault();
        if (!comment.trim()) return;
        
        setActionLoading(rejectId);
        try {
            await adminService.rejectLeave(rejectId, { comment });
            setRejectId(null);
            setComment('');
            fetchData();
        } catch (err) {
            alert(err.response?.data?.message || 'Rejection failed');
        } finally {
            setActionLoading(null);
        }
    };

    if (isLoading && pendingLeaves.length === 0) {
        return <div className="loading-screen"><Loader2 className="animate-spin" /></div>;
    }

    return (
        <div className="admin-dashboard">
            <header className="dashboard-header">
                <div>
                    <h1>Administrative Overview</h1>
                    <p>Review pending applications and system statistics.</p>
                </div>
            </header>

            <section className="report-stats">
                <div className="report-card glass-card">
                    <div className="report-icon purple"><BarChart3 size={24} /></div>
                    <div className="report-info">
                        <h3>Total Applications</h3>
                        <p className="report-value">
                            {report ? Object.values(report.statusCounts).reduce((a, b) => a + b, 0) : 0}
                        </p>
                    </div>
                </div>
                <div className="report-card glass-card">
                    <div className="report-icon orange"><Clock size={24} /></div>
                    <div className="report-info">
                        <h3>Pending Now</h3>
                        <p className="report-value">{pendingLeaves.length}</p>
                    </div>
                </div>
                <div className="report-card glass-card">
                    <div className="report-icon green"><Check size={24} /></div>
                    <div className="report-info">
                        <h3>Approved Total</h3>
                        <p className="report-value">{report?.statusCounts?.APPROVED || 0}</p>
                    </div>
                </div>
            </section>

            <div className="admin-grid">
                <section className="pending-section glass-card">
                    <div className="section-header">
                        <h2>Pending Approvals</h2>
                        <span className="badge-count">{pendingLeaves.length}</span>
                    </div>
                    
                    <div className="pending-list">
                        {pendingLeaves.length === 0 ? (
                            <div className="empty-state">
                                <Info size={32} />
                                <p>No pending leave applications to review.</p>
                            </div>
                        ) : (
                            pendingLeaves.map(leave => (
                                <div key={leave.id} className="pending-card">
                                    <div className="applicant-info">
                                        <div className="applicant-avatar">
                                            {leave.user?.firstName?.charAt(0)}{leave.user?.lastName?.charAt(0)}
                                        </div>
                                        <div>
                                            <h4>{leave.user?.firstName} {leave.user?.lastName}</h4>
                                            <p className="applicant-dept">{leave.user?.department}</p>
                                        </div>
                                    </div>
                                    
                                    <div className="leave-details">
                                        <div className="detail-item">
                                            <span className="detail-label">Type</span>
                                            <span className="detail-value">{leave.leaveType}</span>
                                        </div>
                                        <div className="detail-item">
                                            <span className="detail-label">Dates</span>
                                            <span className="detail-value">{leave.startDate} to {leave.endDate}</span>
                                        </div>
                                        <div className="detail-item">
                                            <span className="detail-label">Duration</span>
                                            <span className="detail-value">{leave.totalDays} Days</span>
                                        </div>
                                    </div>

                                    <div className="leave-reason">
                                        <span className="detail-label">Reason</span>
                                        <p>{leave.reason}</p>
                                    </div>

                                    <div className="action-buttons">
                                        <button 
                                            className="btn-approve" 
                                            onClick={() => handleApprove(leave.id)}
                                            disabled={actionLoading === leave.id}
                                        >
                                            {actionLoading === leave.id ? <Loader2 className="animate-spin" size={16} /> : <Check size={16} />}
                                            <span>Approve</span>
                                        </button>
                                        <button 
                                            className="btn-reject" 
                                            onClick={() => setRejectId(leave.id)}
                                            disabled={actionLoading === leave.id}
                                        >
                                            <X size={16} />
                                            <span>Reject</span>
                                        </button>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </section>

                <section className="side-panels">
                    <div className="stats-panel glass-card">
                        <h3>Type Distribution</h3>
                        <div className="chart-placeholder">
                            {report && Object.entries(report.totalLeavesByType).map(([type, count]) => (
                                <div key={type} className="chart-row">
                                    <span className="chart-label">{type}</span>
                                    <div className="chart-bar-container">
                                        <div 
                                            className="chart-bar" 
                                            style={{ width: `${(count / Math.max(...Object.values(report.totalLeavesByType))) * 100}%` }}
                                        ></div>
                                    </div>
                                    <span className="chart-value">{count}</span>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="stats-panel glass-card">
                        <h3>Department Wise</h3>
                        <div className="chart-placeholder">
                            {report && Object.entries(report.departmentWiseBreakdown).map(([dept, count]) => (
                                <div key={dept} className="chart-row">
                                    <span className="chart-label">{dept}</span>
                                    <div className="chart-bar-container">
                                        <div 
                                            className="chart-bar secondary" 
                                            style={{ width: `${(count / Math.max(...Object.values(report.departmentWiseBreakdown))) * 100}%` }}
                                        ></div>
                                    </div>
                                    <span className="chart-value">{count}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                </section>
            </div>

            {rejectId && (
                <div className="modal-overlay">
                    <div className="modal-content glass-card">
                        <h2>Reject Application</h2>
                        <form onSubmit={handleReject} className="modal-form">
                            <p className="warning-text">Provide a mandatory reason for standard rejection.</p>
                            <textarea 
                                required 
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="Mandatory rejection comment..."
                                rows="4"
                            ></textarea>
                            <div className="modal-actions">
                                <button type="button" className="btn-secondary" onClick={() => setRejectId(null)}>Cancel</button>
                                <button type="submit" className="btn-danger" disabled={actionLoading}>
                                    {actionLoading ? <Loader2 className="animate-spin" /> : 'Confirm Rejection'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminDashboard;
