-- 1. Insert authentication types into 'credential_types'
MERGE INTO credential_types (code, display_name)
    KEY(code)
    VALUES
    ('TOKEN', 'Personal Access Token'),
    ('BASIC', 'Username and Password'),
    ('SSH', 'SSH Private Key');

-- 2. Insert the GitHub provider into 'providers'
MERGE INTO providers (name)
    KEY(name)
    VALUES
    ('GitHub');

-- 3. Link GitHub with the three credential types in the join table
INSERT INTO provider_credential_types (provider_id, credential_type_id)
SELECT p.id, ct.id
FROM providers p
         JOIN credential_types ct ON ct.code IN ('TOKEN', 'BASIC', 'SSH')
WHERE p.name = 'GitHub'
  AND NOT EXISTS (
    SELECT 1
    FROM provider_credential_types pct
    WHERE pct.provider_id = p.id AND pct.credential_type_id = ct.id
);