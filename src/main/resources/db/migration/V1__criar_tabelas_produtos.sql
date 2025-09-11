create extension if not exists "uuid-ossp";

create table if not exists produto (
    id uuid primary key default uuid_generate_v4(),
    titulo varchar(255) not null,
    descricao varchar(2000) not null,
    valor integer not null, -- Conferir como fica com Interge
    status_reservado boolean not null default false,
    image_url varchar(1000),
    image_alt varchar(255)
);

create table if not exists produto_links (
    produto_id uuid not null references produto(id) on delete cascade, -- Conferir com UUID
    nome_loja varchar(255) not null,
    link_produto varchar(1000) not null
);

create index if not exists idx_produto_titulo on produto(titulo); -- Ver como issso funciona