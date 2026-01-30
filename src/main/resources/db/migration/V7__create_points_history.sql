-- V7: Create user points history table
CREATE TABLE IF NOT EXISTS user_points_history (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    points_earned INTEGER NOT NULL,
    reason VARCHAR(255) NOT NULL, -- "Task completed: Learn Java", "Project completed: Backend"
    task_id INTEGER REFERENCES tasks(id) ON DELETE SET NULL,
    project_id INTEGER REFERENCES projects(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add total points to users table
ALTER TABLE users ADD COLUMN total_points INTEGER DEFAULT 0;
ALTER TABLE users ADD COLUMN points_spent INTEGER DEFAULT 0;

-- Indexes
CREATE INDEX idx_points_history_user_id ON user_points_history(user_id);
CREATE INDEX idx_points_history_created_at ON user_points_history(created_at DESC);
