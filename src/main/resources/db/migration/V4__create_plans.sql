-- V4: Create daily plans table
CREATE TABLE IF NOT EXISTS daily_plans (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    plan_date DATE NOT NULL,
    day_of_week VARCHAR(10), -- MONDAY, TUESDAY, etc. (for recurring plans)
    is_template BOOLEAN DEFAULT FALSE, -- Template for specific day of week
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, plan_date)
);

-- Create plan items table (tasks in the plan with time slots)
CREATE TABLE IF NOT EXISTS plan_items (
    id SERIAL PRIMARY KEY,
    plan_id INTEGER NOT NULL REFERENCES daily_plans(id) ON DELETE CASCADE,
    task_id INTEGER REFERENCES tasks(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL, -- Can be independent of tasks
    description TEXT,
    time_from TIME NOT NULL,
    time_to TIME NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (time_to > time_from)
);

-- Indexes
CREATE INDEX idx_daily_plans_user_date ON daily_plans(user_id, plan_date);
CREATE INDEX idx_daily_plans_day_of_week ON daily_plans(user_id, day_of_week) WHERE is_template = true;
CREATE INDEX idx_plan_items_plan_id ON plan_items(plan_id);
CREATE INDEX idx_plan_items_task_id ON plan_items(task_id);
