-- Criar tabela de usuários com lista de produtos reservados
create table if not exists usuario (
    id uuid primary key default uuid_generate_v4(),
    nome varchar(255) not null,
    telefone varchar(20) not null,
    email varchar(255) unique not null,
    mensagem text,
    data_cadastro timestamp not null default current_timestamp,
    meio_reserva varchar(50) not null check (meio_reserva in ('lojas', 'pagamentoDireto')),
    produtos_reservados uuid[] default '{}' -- Array de UUIDs dos produtos reservados
);

-- Remover a tabela comprador antiga
drop table if exists comprador;

-- Índices para melhorar performance
create index if not exists idx_usuario_email on usuario(email);
create index if not exists idx_usuario_meio_reserva on usuario(meio_reserva);
