import React, { useState, useMemo } from 'react';
import { 
  Activity, 
  Plus, 
  Trash2, 
  Play, 
  BarChart3, 
  Clock, 
  Settings2,
  Stethoscope,
  TrendingUp,
  UserRound
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  HospitalTask, 
  GanttRecord, 
  runFCFS, 
  runSJN, 
  runRR 
} from './scheduling';

const DEFAULT_TASKS: HospitalTask[] = [
  { id: '1', name: 'Emergency Surgery', arrival: 0, duration: 5, priority: 1 },
  { id: '2', name: 'Blood Analysis', arrival: 2, duration: 3, priority: 2 },
  { id: '3', name: 'MRI Scan', arrival: 4, duration: 4, priority: 2 },
  { id: '4', name: 'Patient Discharge', arrival: 6, duration: 2, priority: 3 },
];

const RELATABLE_TASK_NAMES = [
  'X-Ray Scan', 'Heart Monitor', 'Pharmacy Refill', 'ICU Checkup', 
  'Trauma Care', 'Dental Clinic', 'Optometry Exam', 'Vitals Sync'
];

export default function App() {
  const [tasks, setTasks] = useState<HospitalTask[]>(DEFAULT_TASKS);
  const [algo, setAlgo] = useState<'FCFS' | 'SJN' | 'RR'>('FCFS');
  const [quantum, setQuantum] = useState(2);
  const [results, setResults] = useState<{ tasks: HospitalTask[], gantt: GanttRecord[] } | null>(null);

  const addTask = () => {
    const nextId = (tasks.length + 1).toString();
    const name = RELATABLE_TASK_NAMES[tasks.length % RELATABLE_TASK_NAMES.length];
    setTasks([...tasks, { id: nextId, name, arrival: 0, duration: 1, priority: 1 }]);
  };

  const removeTask = (id: string) => {
    setTasks(tasks.filter(t => t.id !== id));
  };

  const updateTask = (id: string, field: keyof HospitalTask, value: any) => {
    setTasks(tasks.map(t => t.id === id ? { ...t, [field]: value } : t));
  };

  const runSimulation = () => {
    let res;
    if (algo === 'FCFS') res = runFCFS(tasks);
    else if (algo === 'SJN') res = runSJN(tasks);
    else res = runRR(tasks, quantum);
    setResults(res);
  };

  const stats = useMemo(() => {
    if (!results) return null;
    const avgTAT = results.tasks.reduce((acc, t) => acc + (t.turnaround || 0), 0) / results.tasks.length;
    const avgWT = results.tasks.reduce((acc, t) => acc + (t.waiting || 0), 0) / results.tasks.length;
    return { avgTAT, avgWT };
  }, [results]);

  const maxTime = results?.gantt[results.gantt.length - 1].endTime || 0;

  return (
    <div className="app-container">
      <motion.header 
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        <div className="title-group">
          <h1><Stethoscope style={{verticalAlign: 'bottom', marginRight: '10px'}} /> Smart Hospital Scheduler</h1>
          <p>Advanced Task Optimization & Resource Management</p>
        </div>
        <div className="header-actions">
          <button className="btn btn-primary" onClick={runSimulation}>
            <Play size={18} /> Run Simulation
          </button>
        </div>
      </motion.header>

      <div className="dashboard-grid">
        <div className="main-content">
          <motion.section 
            className="card"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.1 }}
          >
            <div className="card-title">
              <Activity size={20} color="var(--primary)" />
              Hospital Task Queue
            </div>
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Task Name</th>
                    <th>Arrival</th>
                    <th>Duration</th>
                    <th>Priority</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <AnimatePresence>
                    {tasks.map(task => (
                      <motion.tr 
                        key={task.id}
                        initial={{ opacity: 0, x: -10 }}
                        animate={{ opacity: 1, x: 0 }}
                        exit={{ opacity: 0, x: 10 }}
                      >
                        <td>
                          <input 
                            value={task.name} 
                            onChange={e => updateTask(task.id, 'name', e.target.value)}
                          />
                        </td>
                        <td>
                          <input 
                            type="number" 
                            value={task.arrival} 
                            onChange={e => updateTask(task.id, 'arrival', parseInt(e.target.value) || 0)}
                          />
                        </td>
                        <td>
                          <input 
                            type="number" 
                            value={task.duration} 
                            onChange={e => updateTask(task.id, 'duration', parseInt(e.target.value) || 0)}
                          />
                        </td>
                        <td>
                          <select 
                            value={task.priority}
                            onChange={e => updateTask(task.id, 'priority', parseInt(e.target.value))}
                          >
                            <option value={1}>1 (Urgent)</option>
                            <option value={2}>2 (Normal)</option>
                            <option value={3}>3 (Low)</option>
                          </select>
                        </td>
                        <td>
                          <button className="btn btn-outline" style={{padding: '0.4rem'}} onClick={() => removeTask(task.id)}>
                            <Trash2 size={14} color="var(--error)" />
                          </button>
                        </td>
                      </motion.tr>
                    ))}
                  </AnimatePresence>
                </tbody>
              </table>
            </div>
            <button className="btn btn-outline" style={{marginTop: '1rem', width: '100%'}} onClick={addTask}>
              <Plus size={18} /> Add New Medical Task
            </button>
          </motion.section>

          {results && (
            <motion.section 
              className="card" 
              style={{marginTop: '2rem'}}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
            >
              <div className="card-title">
                <BarChart3 size={20} color="var(--primary)" />
                Task Execution Timeline
              </div>
              <div className="gantt-chart">
                <div className="gantt-container">
                  {results.gantt.map((record, i) => (
                    <motion.div 
                      key={i}
                      className="gantt-bar"
                      style={{
                        width: `${((record.endTime - record.startTime) / maxTime) * 100}%`,
                        backgroundColor: record.color,
                        boxShadow: `0 0 15px ${record.color}44`
                      }}
                      initial={{ width: 0 }}
                      animate={{ width: `${((record.endTime - record.startTime) / maxTime) * 100}%` }}
                      transition={{ duration: 0.5, delay: i * 0.1 }}
                    >
                      {record.taskName}
                      <span className="gantt-time-label">{record.startTime}</span>
                      {i === results.gantt.length - 1 && (
                        <span className="gantt-time-label-end">{record.endTime}</span>
                      )}
                    </motion.div>
                  ))}
                </div>
              </div>

              <div className="table-container" style={{marginTop: '3rem'}}>
                <div className="card-title" style={{padding: '1rem 1rem 0'}}>
                  <TrendingUp size={20} color="var(--success)" />
                  Performance Metrics
                </div>
                <table>
                  <thead>
                    <tr>
                      <th>Task</th>
                      <th>Comp. Time</th>
                      <th>Turnaround</th>
                      <th>Waiting</th>
                    </tr>
                  </thead>
                  <tbody>
                    {results.tasks.map(task => (
                      <tr key={task.id}>
                        <td>{task.name}</td>
                        <td>{task.completion}</td>
                        <td>{task.turnaround}</td>
                        <td>{task.waiting}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </motion.section>
          )}
        </div>

        <aside>
          <motion.div 
            className="card"
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.2 }}
          >
            <div className="card-title">
              <Settings2 size={20} color="var(--primary)" />
              Simulation Config
            </div>
            <div className="input-section">
              <div>
                <label style={{display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', color: 'var(--text-muted)'}}>
                  Scheduling Algorithm
                </label>
                <select value={algo} onChange={e => setAlgo(e.target.value as any)}>
                  <option value="FCFS">First Come First Served (FCFS)</option>
                  <option value="SJN">Shortest Job Next (SJN)</option>
                  <option value="RR">Round Robin (RR)</option>
                </select>
              </div>
              {algo === 'RR' && (
                <motion.div
                  initial={{ opacity: 0, height: 0 }}
                  animate={{ opacity: 1, height: 'auto' }}
                >
                  <label style={{display: 'block', marginBottom: '0.5rem', fontSize: '0.875rem', color: 'var(--text-muted)'}}>
                    Time Quantum
                  </label>
                  <input type="number" value={quantum} onChange={e => setQuantum(parseInt(e.target.value) || 1)} />
                </motion.div>
              )}
            </div>
          </motion.div>

          {stats && (
            <motion.div 
              className="stats-grid" 
              style={{marginTop: '2rem', gridTemplateColumns: '1fr'}}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
            >
              <div className="stat-card">
                <div className="stat-label">Avg. Turnaround Time</div>
                <div className="stat-value" style={{color: 'var(--primary)'}}>{stats.avgTAT.toFixed(2)}s</div>
              </div>
              <div className="stat-card">
                <div className="stat-label">Avg. Waiting Time</div>
                <div className="stat-value" style={{color: 'var(--success)'}}>{stats.avgWT.toFixed(2)}s</div>
              </div>
              <div className="stat-card">
                <div className="stat-label">Active Medical Staff</div>
                <div className="stat-value">12</div>
                <div style={{fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '0.5rem'}}>
                  <UserRound size={12} inline /> Optimal Capacity
                </div>
              </div>
            </motion.div>
          )}
        </aside>
      </div>
    </div>
  );
}
