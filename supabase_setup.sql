-- Simple auth setup for this app.
-- Run this in Supabase SQL Editor.
--
-- This intentionally does NOT use Supabase Auth email confirmation.
-- The app stores email + password in app_users and checks them directly.
-- This is simple for coursework/prototype use, not secure for production.

DROP TABLE IF EXISTS score_records;
DROP TABLE IF EXISTS app_users;

CREATE TABLE app_users (
    id                UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name              TEXT        NOT NULL DEFAULT '',
    email             TEXT        NOT NULL UNIQUE,
    password          TEXT        NOT NULL,
    avatar_res        TEXT        NOT NULL DEFAULT 'avatar_default',
    total_score       INTEGER     NOT NULL DEFAULT 0,
    stories_completed INTEGER     NOT NULL DEFAULT 0,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE app_users ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow simple app user access"
    ON app_users FOR ALL
    USING (true)
    WITH CHECK (true);

CREATE TABLE score_records (
    id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID        NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    story_id     TEXT        NOT NULL,
    story_title  TEXT        NOT NULL,
    score        INTEGER     NOT NULL,
    max_score    INTEGER     NOT NULL,
    completed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE score_records ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow simple score access"
    ON score_records FOR ALL
    USING (true)
    WITH CHECK (true);
