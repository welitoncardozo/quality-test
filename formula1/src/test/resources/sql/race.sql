INSERT INTO pais (id, name) VALUES (1, 'Brasil');
INSERT INTO pista (id_pista, nome_pista, tamanho_pista, pais) VALUES (1, 'Interlagos', 50000, 1);
INSERT INTO campeonato (codigo_campeonato, descricao, ano) VALUES (1, 'Campeonato nacional', YEAR(NOW()));
INSERT INTO corrida (id_corrida, data_corrida, pista, campeonato) VALUES (10, NOW(), 1, 1);