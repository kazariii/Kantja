-- Jalankan script ini di Supabase SQL Editor
-- Settings → SQL Editor → New Query → paste ini → Run

-- ========================================
-- Tabel profiles (data profil pengguna)
-- ========================================
CREATE TABLE IF NOT EXISTS profiles (
    id          UUID        PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    name        TEXT        NOT NULL DEFAULT '',
    avatar_res  TEXT        NOT NULL DEFAULT 'avatar_default',
    total_score INTEGER     NOT NULL DEFAULT 0,
    stories_completed INTEGER NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users manage own profile"
    ON profiles FOR ALL
    USING (auth.uid() = id)
    WITH CHECK (auth.uid() = id);

-- ========================================
-- Tabel score_records (riwayat nilai)
-- ========================================
CREATE TABLE IF NOT EXISTS score_records (
    id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID        NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    story_id     TEXT        NOT NULL,
    story_title  TEXT        NOT NULL,
    score        INTEGER     NOT NULL,
    max_score    INTEGER     NOT NULL,
    completed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE score_records ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users manage own scores"
    ON score_records FOR ALL
    USING (auth.uid() = user_id)
    WITH CHECK (auth.uid() = user_id);
