-- Initialize Healenium database schema
CREATE SCHEMA IF NOT EXISTS public;
ALTER ROLE healenium_user SET search_path TO public;
GRANT ALL PRIVILEGES ON SCHEMA public TO healenium_user;
