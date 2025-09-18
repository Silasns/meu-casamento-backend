alter table if exists webhook_evento
    add column if not exists installments integer;

