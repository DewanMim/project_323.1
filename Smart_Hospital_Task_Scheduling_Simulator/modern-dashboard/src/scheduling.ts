export interface HospitalTask {
  id: string;
  name: string;
  arrival: number;
  duration: number;
  priority: number;
  completion?: number;
  turnaround?: number;
  waiting?: number;
  remaining?: number;
}

export interface GanttRecord {
  taskName: string;
  startTime: number;
  endTime: number;
  color: string;
}

const COLORS = [
  '#38bdf8', '#818cf8', '#fb7185', '#34d399', '#fbbf24', '#a78bfa', '#2dd4bf', '#f472b6'
];

export function runFCFS(tasks: HospitalTask[]): { tasks: HospitalTask[], gantt: GanttRecord[] } {
  const sorted = [...tasks].sort((a, b) => a.arrival - b.arrival);
  const gantt: GanttRecord[] = [];
  let currentTime = 0;

  const results = sorted.map((task, index) => {
    const start = Math.max(currentTime, task.arrival);
    const end = start + task.duration;
    
    gantt.push({
      taskName: task.name,
      startTime: start,
      endTime: end,
      color: COLORS[index % COLORS.length]
    });

    const completion = end;
    const turnaround = completion - task.arrival;
    const waiting = turnaround - task.duration;
    currentTime = end;

    return { ...task, completion, turnaround, waiting };
  });

  return { tasks: results, gantt };
}

export function runSJN(tasks: HospitalTask[]): { tasks: HospitalTask[], gantt: GanttRecord[] } {
  let currentTime = 0;
  const readyQueue: HospitalTask[] = [];
  const completedTasks: HospitalTask[] = [];
  const gantt: GanttRecord[] = [];
  let remainingTasks = [...tasks];

  while (completedTasks.length < tasks.length) {
    // Add arrived tasks to ready queue
    const arrived = remainingTasks.filter(t => t.arrival <= currentTime);
    remainingTasks = remainingTasks.filter(t => t.arrival > currentTime);
    readyQueue.push(...arrived);

    if (readyQueue.length === 0) {
      if (remainingTasks.length > 0) {
        currentTime = Math.min(...remainingTasks.map(t => t.arrival));
        continue;
      }
      break;
    }

    // Sort ready queue by duration (Shortest Job First)
    readyQueue.sort((a, b) => a.duration - b.duration);
    const task = readyQueue.shift()!;

    const start = currentTime;
    const end = start + task.duration;

    gantt.push({
      taskName: task.name,
      startTime: start,
      endTime: end,
      color: COLORS[completedTasks.length % COLORS.length]
    });

    const completion = end;
    const turnaround = completion - task.arrival;
    const waiting = turnaround - task.duration;
    currentTime = end;

    completedTasks.push({ ...task, completion, turnaround, waiting });
  }

  return { tasks: completedTasks, gantt };
}

export function runRR(tasks: HospitalTask[], quantum: number): { tasks: HospitalTask[], gantt: GanttRecord[] } {
  let currentTime = 0;
  const gantt: GanttRecord[] = [];
  const taskMap = new Map<string, HospitalTask>(tasks.map(t => [t.id, { ...t, remaining: t.duration }]));
  const queue: string[] = [];
  const completedTasks: HospitalTask[] = [];
  const processedIds = new Set<string>();

  const getArrived = (time: number) => {
    return tasks.filter(t => t.arrival <= time && !processedIds.has(t.id));
  };

  // Initial tasks
  const initial = getArrived(currentTime);
  initial.forEach(t => {
    queue.push(t.id);
    processedIds.add(t.id);
  });

  if (queue.length === 0 && tasks.length > 0) {
    currentTime = Math.min(...tasks.map(t => t.arrival));
    getArrived(currentTime).forEach(t => {
      queue.push(t.id);
      processedIds.add(t.id);
    });
  }

  while (queue.length > 0 || processedIds.size < tasks.length) {
    if (queue.length === 0) {
      currentTime = Math.min(...tasks.filter(t => !processedIds.has(t.id)).map(t => t.arrival));
      getArrived(currentTime).forEach(t => {
        queue.push(t.id);
        processedIds.add(t.id);
      });
    }

    const taskId = queue.shift()!;
    const task = taskMap.get(taskId)!;
    const executeTime = Math.min(task.remaining!, quantum);

    const start = currentTime;
    const end = start + executeTime;

    gantt.push({
      taskName: task.name,
      startTime: start,
      endTime: end,
      color: COLORS[tasks.findIndex(t => t.id === taskId) % COLORS.length]
    });

    task.remaining! -= executeTime;
    currentTime = end;

    // Add any tasks that arrived during execution
    getArrived(currentTime).forEach(t => {
      queue.push(t.id);
      processedIds.add(t.id);
    });

    if (task.remaining! > 0) {
      queue.push(taskId);
    } else {
      task.completion = currentTime;
      task.turnaround = task.completion - task.arrival;
      task.waiting = task.turnaround - task.duration;
      completedTasks.push(task);
    }
  }

  return { tasks: completedTasks, gantt };
}
