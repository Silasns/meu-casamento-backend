create table if not exists tentativa_pagamento (
    id uuid primary key default uuid_generate_v4(),
    nome varchar(255) not null,
    telefone varchar(20) not null,
    email varchar(255) not null,
    mensagem text,
    produto_id uuid not null,
    criado_em timestamp not null default current_timestamp,
    status varchar(20) not null default 'PENDENTE' check (status in ('PENDENTE', 'PAGO', 'CANCELADO')),
    pago_em timestamp,
    order_nsu varchar(255)
);

create index if not exists idx_tentativa_pagamento_email on tentativa_pagamento(email);
create index if not exists idx_tentativa_pagamento_produto_id on tentativa_pagamento(produto_id);
create index if not exists idx_tentativa_pagamento_status on tentativa_pagamento(status);
create index if not exists idx_tentativa_pagamento_order_nsu on tentativa_pagamento(order_nsu);
