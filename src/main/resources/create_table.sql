
DROP TABLE IF EXISTS users;

-- User table
CREATE TABLE IF NOT EXISTS users (
     id BIGSERIAL PRIMARY KEY,
     user_account VARCHAR(256) NOT NULL,
     user_password VARCHAR(512) NOT NULL,
     user_name VARCHAR(256),
     user_avatar VARCHAR(1024),
     user_profile VARCHAR(512),
     user_role VARCHAR(256) DEFAULT 'user' NOT NULL,
     edit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
     is_delete SMALLINT DEFAULT 0 NOT NULL
);

-- Create constraints and indexes
ALTER TABLE users ADD CONSTRAINT uk_user_account UNIQUE (user_account);
CREATE INDEX IF NOT EXISTS idx_user_name ON users(user_name);

-- Add table comment
COMMENT ON TABLE users IS 'User table';

-- Add column comments
COMMENT ON COLUMN users.id IS 'User ID';
COMMENT ON COLUMN users.user_account IS 'Account/Username';
COMMENT ON COLUMN users.user_password IS 'Password';
COMMENT ON COLUMN users.user_name IS 'Display name';
COMMENT ON COLUMN users.user_avatar IS 'Avatar URL';
COMMENT ON COLUMN users.user_profile IS 'User profile/bio';
COMMENT ON COLUMN users.user_role IS 'User role: user/admin';
COMMENT ON COLUMN users.edit_time IS 'Edit time';
COMMENT ON COLUMN users.create_time IS 'Creation time';
COMMENT ON COLUMN users.update_time IS 'Update time';
COMMENT ON COLUMN users.is_delete IS 'Soft delete flag';


-- App table
DROP TABLE IF EXISTS app;

-- Application table
CREATE TABLE IF NOT EXISTS app (
    id BIGSERIAL PRIMARY KEY,
    app_name VARCHAR(256),
    cover VARCHAR(512),
    init_prompt TEXT,
    code_gen_type VARCHAR(64),
    deploy_key VARCHAR(64),
    deployed_time TIMESTAMP,
    priority INTEGER DEFAULT 0 NOT NULL,
    user_id BIGINT NOT NULL,
    edit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_delete SMALLINT DEFAULT 0 NOT NULL
);

-- Create constraints and indexes
ALTER TABLE app ADD CONSTRAINT uk_deploy_key UNIQUE (deploy_key);
CREATE INDEX IF NOT EXISTS idx_app_name ON app(app_name);
CREATE INDEX IF NOT EXISTS idx_user_id ON app(user_id);

-- Add table comment
COMMENT ON TABLE app IS 'Application table';

-- Add column comments
COMMENT ON COLUMN app.id IS 'Application ID';
COMMENT ON COLUMN app.app_name IS 'Application name';
COMMENT ON COLUMN app.cover IS 'Application cover image';
COMMENT ON COLUMN app.init_prompt IS 'Application initialization prompt';
COMMENT ON COLUMN app.code_gen_type IS 'Code generation type (enum)';
COMMENT ON COLUMN app.deploy_key IS 'Deployment identifier';
COMMENT ON COLUMN app.deployed_time IS 'Deployment time';
COMMENT ON COLUMN app.priority IS 'Priority level';
COMMENT ON COLUMN app.user_id IS 'Creator user ID';
COMMENT ON COLUMN app.edit_time IS 'Edit time';
COMMENT ON COLUMN app.create_time IS 'Creation time';
COMMENT ON COLUMN app.update_time IS 'Update time';
COMMENT ON COLUMN app.is_delete IS 'Soft delete flag';




DROP TABLE IF EXISTS chat_history;

-- Chat history table
CREATE TABLE IF NOT EXISTS chat_history (
    id BIGSERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    message_type VARCHAR(32) NOT NULL,
    app_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_delete SMALLINT DEFAULT 0 NOT NULL
);

-- Create indexes for query performance
CREATE INDEX IF NOT EXISTS idx_chat_app_id ON chat_history(app_id);
CREATE INDEX IF NOT EXISTS idx_chat_create_time ON chat_history(create_time);
CREATE INDEX IF NOT EXISTS idx_chat_app_id_create_time ON chat_history(app_id, create_time);
CREATE INDEX IF NOT EXISTS idx_chat_user_id ON chat_history(user_id);

-- Add table comment
COMMENT ON TABLE chat_history IS 'Chat history table';

-- Add column comments
COMMENT ON COLUMN chat_history.id IS 'Chat history ID';
COMMENT ON COLUMN chat_history.message IS 'Message content';
COMMENT ON COLUMN chat_history.message_type IS 'Message type: user/ai';
COMMENT ON COLUMN chat_history.app_id IS 'Application ID';
COMMENT ON COLUMN chat_history.user_id IS 'Creator user ID';
COMMENT ON COLUMN chat_history.create_time IS 'Creation time';
COMMENT ON COLUMN chat_history.update_time IS 'Update time';
COMMENT ON COLUMN chat_history.is_delete IS 'Soft delete flag';