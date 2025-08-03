DELETE FROM transacao;
DELETE FROM conta;

INSERT INTO conta (numero_conta, saldo) VALUES (100, 500.00);
INSERT INTO conta (numero_conta, saldo) VALUES (101, 0.00);
INSERT INTO conta (numero_conta, saldo) VALUES (102, 2000.00);
INSERT INTO conta (numero_conta, saldo) VALUES (103, 50.00);
INSERT INTO conta (numero_conta, saldo) VALUES (104, 5000.00);
INSERT INTO conta (numero_conta, saldo) VALUES (105, 1000.00);


INSERT INTO transacao (conta_numero_conta, forma_pagamento, valor, taxa, data_transacao) VALUES
(102, 'P', 150.00, 0.00, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(102, 'D', 100.00, 3.00, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
(102, 'C', 200.00, 10.00, DATEADD('DAY', -1, CURRENT_TIMESTAMP));


INSERT INTO transacao (conta_numero_conta, forma_pagamento, valor, taxa, data_transacao) VALUES
(103, 'D', 40.00, 1.20, DATEADD('HOUR', -2, CURRENT_TIMESTAMP));


INSERT INTO transacao (conta_numero_conta, forma_pagamento, valor, taxa, data_transacao) VALUES
(104, 'P', 50.00, 0.00, DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(104, 'D', 100.00, 3.00, DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
(104, 'C', 150.00, 7.50, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(104, 'D', 200.00, 6.00, DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(104, 'C', 250.00, 12.50, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
(104, 'P', 75.00, 0.00, DATEADD('HOUR', -12, CURRENT_TIMESTAMP)),
(104, 'D', 125.00, 3.75, DATEADD('HOUR', -6, CURRENT_TIMESTAMP));


INSERT INTO transacao (conta_numero_conta, forma_pagamento, valor, taxa, data_transacao) VALUES
(105, 'C', 100.00, 5.00, DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
(105, 'D', 50.00, 1.50, DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(105, 'P', 200.00, 0.00, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(105, 'D', 30.00, 0.90, CURRENT_TIMESTAMP);
