create table if not exists webhook_evento (
    id uuid primary key default uuid_generate_v4(),
    recebido_em timestamp not null default current_timestamp,
    fonte varchar(100) not null,
    invoice_slug varchar(255),
    order_nsu varchar(255),
    produto_id uuid,
    amount integer,
    paid_amount integer,
    capture_method varchar(50),
    transaction_nsu varchar(255),
    receipt_url varchar(1000),
    items_json jsonb,
    payload_json jsonb not null,
    aprovado boolean not null,
    sucesso_processamento boolean not null,
    mensagem_erro text
);

create index if not exists idx_webhook_evento_recebido_em on webhook_evento(recebido_em);
create index if not exists idx_webhook_evento_order_nsu on webhook_evento(order_nsu);
create index if not exists idx_webhook_evento_produto_id on webhook_evento(produto_id);

