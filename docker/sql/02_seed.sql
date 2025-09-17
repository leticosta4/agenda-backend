INSERT INTO agenda (nome) VALUES
('Agenda Pessoal'),
('Agenda de Trabalho'),
('Agenda da Família'),
('Agenda de Estudos'),
('Agenda de Projetos'),
('Agenda de Viagens'),
('Agenda de Clientes'),
('Agenda de Fornecedores'),
('Agenda de Eventos'),
('Agenda de Saúde');

INSERT INTO contato (agenda_id, nome, telefone) VALUES
((SELECT id FROM agenda WHERE nome = 'Agenda Pessoal'), 'João Silva', '71999990001'),
((SELECT id FROM agenda WHERE nome = 'Agenda Pessoal'), 'Maria Souza', '71999990002'),
((SELECT id FROM agenda WHERE nome = 'Agenda Pessoal'), 'Pedro Santos', '71999990003'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Trabalho'), 'Carlos Pereira', '71999990004'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Trabalho'), 'Ana Lima', '71999990005'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Trabalho'), 'Rafael Costa', '71999990006'),

((SELECT id FROM agenda WHERE nome = 'Agenda da Família'), 'Tia Rosa', '71999990007'),
((SELECT id FROM agenda WHERE nome = 'Agenda da Família'), 'Primo Lucas', '71999990008'),
((SELECT id FROM agenda WHERE nome = 'Agenda da Família'), 'Vó Maria', '71999990009'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Estudos'), 'Paulo Henrique', '71999990010'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Estudos'), 'Fernanda Castro', '71999990011'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Estudos'), 'Bruno Almeida', '71999990012'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Projetos'), 'Ricardo Almeida', '71999990013'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Projetos'), 'Juliana Mendes', '71999990014'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Projetos'), 'Mariana Lopes', '71999990015'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Viagens'), 'Lucas Martins', '71999990016'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Viagens'), 'Patrícia Gomes', '71999990017'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Viagens'), 'André Souza', '71999990018'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Clientes'), 'Cláudia Nunes', '71999990019'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Clientes'), 'Marcos Vinícius', '71999990020'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Clientes'), 'Sérgio Oliveira', '71999990021'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Fornecedores'), 'Empresa ABC', '71999990022'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Fornecedores'), 'Fornecedor XYZ', '71999990023'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Fornecedores'), 'Distribuidora LM', '71999990024'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Eventos'), 'Organizador João', '71999990025'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Eventos'), 'Cerimonial Ana', '71999990026'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Eventos'), 'Buffet Sabor', '71999990027'),

((SELECT id FROM agenda WHERE nome = 'Agenda de Saúde'), 'Dr. Felipe', '71999990028'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Saúde'), 'Clínica Vida', '71999990029'),
((SELECT id FROM agenda WHERE nome = 'Agenda de Saúde'), 'Farmácia Popular', '71999990030');
